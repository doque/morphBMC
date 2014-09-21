package controllers;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

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

		if (c.id != 0) {
			c.update();
		} else {
			c.save();
		}

		// FUCK JA
		List<SqlRow> rows = Ebean
				.createSqlQuery(
						"SELECT r.name, r.value, p.name as param1, a.name as attr1, p2.name as param2, a2.name as attr2 FROM compatibility c JOIN attribute a ON c.attr1_id = a.id JOIN attribute a2 ON c.attr2_id = a2.id JOIN PARAMETER p ON a.parameter_id = p.id JOIN PARAMETER p2 ON a2.parameter_id = p2.id JOIN rating r ON c.rating_id = r.id ")
				.findList();

		for (SqlRow row : rows) {
			String f = row.getString("param2");
			String g = f + "lolol";
		}

		/*
		 * List<models.Compatibility> cs =
		 * Ebean.find(models.Compatibility.class)
		 * .fetch("attribute").fetch("parameter").fetch("problem").where()
		 * .eq("problem.id", problemId).findList(); /*
		 * models.Compatibility.find.where() .eq("problem_id",
		 * problemId).findList();
		 */

		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibilities", rows);
		return ok(Json.toJson(result));
	}

	/**
	 * retrieve all existing compatibilities and serve them to client
	 * 
	 * @param problemId
	 * @return
	 */
	public static Result getCompatibilities(long problemId) {
		Map<String, Object> result = Maps.newHashMap();

		// TODO filter by problemId !!!
		List<models.Compatibility> compats = Ebean.find(
				models.Compatibility.class).findList();

		result.put("compatibilities", compats);

		return ok(Json.toJson(result));

	}
}