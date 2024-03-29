package controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import services.SocketServiceInterface;
import util.JsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Attribute;
import models.Compatibility;
import models.Parameter;
import models.Rating;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class CompatibilityController extends Controller {

	/**
	 * DI
	 */
	private SocketServiceInterface socketService;

	@Inject
	public CompatibilityController(SocketServiceInterface socketService) {
		this.socketService = checkNotNull(socketService);
	}

	/**
	 * store new compatibility for a pair of attributes and rating
	 */
	public synchronized Result addCompatibility(long problemId) {

		Compatibility c = Form.form(Compatibility.class).bindFromRequest()
				.get();

		// see if this is an override request
		String override = Form.form().bindFromRequest().get("override");

		if (override != null) {
			// delete all other compatibilities that refer to the attributes
			// of the this overriding compatibility
			List<Compatibility> compats = getAllCompatibilities(problemId);
			for (Compatibility compatibility : compats) {
				if (compatibility.sameAttributes(c)) {
					compatibility.delete();
				}
			}
		}

		String userId = session().get("userId");
		c.userId = userId;

		// update existing
		if (c.id != 0) {
			c.update();
		} else {
			c.save();
		}
		if (override != null) {
			socketService.broadcast(JsonBuilder.conflictResolved(c));
		}
		
		// notify user how many remaining compatibilities are left to be rated
		Rating defaultRating = Rating.find.where().eq("value", 0).findUnique();
		List<Long[]> remaining = getRemainingCompatibilities(problemId, defaultRating.id);
		socketService.broadcast(JsonBuilder.remainingCapabilities(remaining));
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibilities", getUserCompatibilities(problemId, userId));
		return ok(Json.toJson(result));
	}

	/**
	 * retrieve all compatibilities for a problem that belong to the requesting
	 * client
	 * 
	 * @param problemId
	 */
	public Result getCompatibilities(long problemId) {

		String userId = session().get("userId");
		/*
		 * if this is the first time user is in Compatibility step, no
		 * compatibilities will be present yet. In this case, they will be
		 * prefilled
		 */
		Rating defaultRating = Rating.find.where().eq("value", 0).findUnique();

		// if the user has no rated compatibiltiy yet, we have to insert some
		// dummies for him
		insertInitialCompatibilities(problemId, userId, defaultRating);

		// now we're ready to read compatibilities
		List<Compatibility> compatibilities = Lists.newArrayList();

		// are ALL compatibilities requested, or only for the requesting user?
		String all = Form.form().bindFromRequest().get("all");

		// yes, serve ALL
		if (all != null) {
			compatibilities = getAllCompatibilities(problemId);
			// no, limit by userId
		} else {
			compatibilities = getUserCompatibilities(problemId, userId);
		}

		// notify user how many remaining compatibilities are left to be rated
		List<Long[]> remaining = getRemainingCompatibilities(problemId, defaultRating.id);
		socketService.broadcast(JsonBuilder.remainingCapabilities(remaining));
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibilities", compatibilities);
		return ok(Json.toJson(result));

	}

	/**
	 * returns the remaining (unrated) compatibilites' count for a problem
	 * only includes compatibilities that haven't been rated by anyone yet
	 * and only counts them once
	 * @return
	 */
	protected List<Long[]> getRemainingCompatibilities(long problemId, long ratingId) {
		List<SqlRow> rows = Ebean
				.createSqlQuery("select least(attr1_id, attr2_id) as a1, greatest(attr1_id, attr2_id) as a2" + 
						" from compatibility c" + 
						"	JOIN attribute a ON c.attr1_id = a.id" + 
						"	JOIN PARAMETER pa ON a.parameter_id = pa.id" + 
						"	JOIN problem p ON pa.problem_id = p.id" + 
						"	WHERE p.id = :problem_id" + 
						" group by least(attr1_id, attr2_id), greatest(attr1_id, attr2_id) " + 
						" having bool_and(rating_id = :rating_id)")
				.setParameter("problem_id", problemId)
				.setParameter("rating_id", ratingId).findList();
		
		List<Long[]> ids = Lists.newArrayList();
		for (SqlRow row : rows) {
			ids.add(new Long[] { row.getLong("a1"), row.getLong("a2") });
		}
		
		return ids;

	}

	/**
	 * retrieves a user's existing compatibilities for a problem
	 * 
	 * @param problemId
	 * @param userId
	 * @return a list of the user's compatibilities
	 */
	protected List<Compatibility> getUserCompatibilities(long problemId,
			String userId) {
		// filter compatibilities by user Id
		List<Compatibility> allCompatibilities = getAllCompatibilities(problemId);
		List<Compatibility> userCompatibilities = Lists.newArrayList();

		for (Compatibility c : allCompatibilities) {
			if (c.userId.equals(userId)) {
				userCompatibilities.add(c);
			}
		}

		return userCompatibilities;
	}

	/**
	 * retrieves all existing compatibilities for a problem
	 * 
	 * @param problemId
	 * @return a list of all compatibilities
	 */
	protected List<Compatibility> getAllCompatibilities(long problemId) {
		List<SqlRow> rows = Ebean
				.createSqlQuery(
						"SELECT c.id FROM compatibility c"
								+ " JOIN attribute a ON c.attr1_id = a.id"
								+ " JOIN attribute a2 ON c.attr2_id = a2.id"
								+ " JOIN PARAMETER p ON a.parameter_id = p.id"
								+ " JOIN PARAMETER p2 ON a2.parameter_id = p2.id"
								+ " JOIN rating r ON c.rating_id = r.id"
								+ " WHERE p.problem_id = :problem_id OR p2.problem_id = :problem_id")
				.setParameter("problem_id", problemId).findList();

		List<Compatibility> compatibilities = new ArrayList<Compatibility>();

		// manually build return objects, because Ebean.find() doesn't join
		// multiple tables.
		for (SqlRow row : rows) {
			compatibilities.add(Compatibility.find.byId(row.getLong("id")));
		}

		return compatibilities;
	}

	/**
	 * inserts an initial rating of 0 - "Rate" for each possible combination of
	 * attributes for a specific problem
	 * 
	 * @param problemId
	 * @param defaultRating
	 */
	private void insertInitialCompatibilities(long problemId, String userId,
			Rating defaultRating) {
		List<Parameter> params = Parameter.find.where()
				.eq("problem_id", problemId).findList();
		Set<Pair> attributeIds = new HashSet<Pair>();
		for (Parameter p : params) {
			for (Attribute a : p.attributes) {
				for (Parameter p2 : params) {
					for (Attribute a2 : p2.attributes) {
						// create pair to avoid duplicates
						long x = a.id > a2.id ? a2.id : a.id;
						long y = x == a.id ? a2.id : a.id;
						// let's check if this combination exists yet
						boolean existing = false;
						for (Pair pair : attributeIds) {
							if (pair.x == x && pair.y == y) {
								existing = true;
								break;
							}
						}
						// skip creation of this pair if it either exists
						// or if both attributes share the same parameter
						if (existing || p.id == p2.id) {
							continue;
						}
						attributeIds.add(new Pair(x, y));
					}
				}
			}
		}
		
		// now create default rating for all these pairs
		outerloop:
		for (Pair pair : attributeIds) {
			// check if this compatibility already exists, and only create it if
			// thats not the case  
			
			
			Compatibility c = new Compatibility();
			c.attr1 = Attribute.find.byId(pair.x);
			c.attr2 = Attribute.find.byId(pair.y);
			c.rating = defaultRating;
			c.userId = userId;
			
			List<Compatibility> allCompats = getAllCompatibilities(problemId);
			for (Compatibility existingCompat : allCompats) {
				// this user has already either created a compatibility for this attribute pair
				// or he has had a dummy one inserted before.
				if (existingCompat.sameAttributes(c) && c.userId.equals(existingCompat.userId)) {
					// skip creation of this attribute
					continue outerloop;
				}
			}
			c.save();
		}
	}
	
	/**
	 * Used to hold a pair of attributes
	 */
	private class Pair {
		public long x;
		public long y;

		public Pair(long x, long y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "[" + x + ", " + y + "]";
		}
	}
}