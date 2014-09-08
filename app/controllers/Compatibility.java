package controllers;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class Compatibility extends Controller {

	/**
	 * store new compatibility for a pair of attributes and rating
	 * 
	 * @return
	 */
	public static Result addCompatibility() {
		models.Compatibility compat = Form.form(models.Compatibility.class)
				.bindFromRequest().get();

		if (compat != null) {
			compat.userId = session("userId");
			compat.save();
		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibility", compat);
		return ok(Json.toJson(result));
	}

	/**
	 * retrieve all existing compatibilities and serve them to client
	 * 
	 * @param problemId
	 * @return
	 */
	public static Result getCompatibilities(long problemId) {
		List<models.Compatibility> compats = models.Compatibility.find.where()
				.eq("problem_id", problemId).findList();
		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibilities", compats);
		return ok(Json.toJson(result));
	}
}