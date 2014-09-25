package controllers;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Attribute;
import models.Parameter;
import models.Rating;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.google.common.collect.Maps;

public class Compatibility extends Controller {

	/**
	 * store new compatibility for a pair of attributes and rating
	 * 
	 * @return
	 */
	public static Result addCompatibility(long problemId) {
		// TODO: overwrite

		models.Compatibility c = Form.form(models.Compatibility.class)
				.bindFromRequest().get();

		c.userId = session("userId");
		// prevent dupes, instead just update existing
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
	public static Result getCompatibilities(long problemId) {

		// if no compatibilities exist yet, insert a default rating for them all
		if (getAllCompatibilities(problemId).size() == 0) {
			Rating defaultRating = Rating.find.where().eq("name", "OK")
					.findUnique();
			insertInitialCompatibilities(problemId, defaultRating);
		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibilities", getAllCompatibilities(problemId));
		return ok(Json.toJson(result));

	}

	private static void insertInitialCompatibilities(long problemId,
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
		// now create default rating for all these pairs
		for (Pair pair : attributeIds) {
			models.Compatibility c = new models.Compatibility();
			c.attr1 = Attribute.find.byId(pair.x);
			c.attr2 = Attribute.find.byId(pair.y);
			c.userId = session("userId");
			c.rating = defaultRating;
			c.save();
		}

		System.out.println(attributeIds.size());
	}

	private static List<models.Compatibility> getAllCompatibilities(
			long problemId) {
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

		List<models.Compatibility> compatibilities = new ArrayList<models.Compatibility>();

		// manually build return objects, because Ebean.find() doesn't join
		// multiple tables.
		for (SqlRow row : rows) {
			compatibilities.add(models.Compatibility.find.byId(row
					.getLong("id")));
		}

		return compatibilities;
	}

	public static class Pair {
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