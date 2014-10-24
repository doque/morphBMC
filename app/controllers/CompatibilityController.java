package controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import services.SocketServiceInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.PersistenceException;

import models.Attribute;
import models.Compatibility;
import models.Parameter;
import models.Rating;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class CompatibilityController extends Controller {


	private SocketServiceInterface socketService;
	
	@Inject	public CompatibilityController(SocketServiceInterface socketService) {
		System.out.println("not injecting anything");
		this.socketService = checkNotNull(socketService);
	}
	/**
	 * store new compatibility for a pair of attributes and rating
	 * 
	 * @return
	 */
	public Result addCompatibility(long problemId) {

		Compatibility c = Form.form(Compatibility.class).bindFromRequest()
				.get();

		c.userId = session("userId");
		// update existing
		if (c.id != 0) {
			c.update();
		} else {
			c.save();
		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibilities", getAllCompatibilities(problemId));
		return ok(Json.toJson(result));
	}

	/**
	 * retrieve all existing compatibilities and serve them to client
	 * 
	 * @param problemId
	 * @return
	 */
	public Result getCompatibilities(long problemId) {

		// for each attribute, if attribute has no rating, insert default
		// rating for it
		Rating defaultRating = Rating.find.where().eq("name", "Rate")
				.findUnique();
		// add user id
		insertInitialCompatibilities(problemId, defaultRating);

		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibilities", getAllCompatibilities(problemId));
		return ok(Json.toJson(result));

	}

	private void insertInitialCompatibilities(long problemId,
			Rating defaultRating) {
		List<Parameter> params = Parameter.find.where()
				.eq("problem_id", problemId).findList();
		Set<Pair> attributeIds = new HashSet<Pair>();
		for (Parameter p : params) {
			for (Attribute a : p.attributes) {
				for (Parameter p2 : params) {
					for (Attribute a2 : p2.attributes) {
						// create nifty pair to avoid duplicates
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
						// or if both attribtues share the same parameter
						if (existing || p.id == p2.id) {
							continue;
						}
						attributeIds.add(new Pair(x, y));
					}
				}
			}
		}
		int count = 0;
		// now create default rating for all these pairs
		for (Pair pair : attributeIds) {
			Compatibility c = new Compatibility();
			c.attr1 = Attribute.find.byId(pair.x);
			c.attr2 = Attribute.find.byId(pair.y);
			c.rating = defaultRating;
			c.userId = session().get("userId");
			try {
				c.save();
				++count;
			} catch (PersistenceException e) {
				Logger.info("ignoring duplicate value");
			}
		}
		Logger.info("Total number of configurations: " + count);
	}

	private List<Compatibility> getAllCompatibilities(long problemId) {
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

	public class Pair {
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