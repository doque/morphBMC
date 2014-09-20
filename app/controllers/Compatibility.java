package controllers;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.google.common.collect.Maps;

public class Compatibility extends Controller {

	/**
	 * store new compatibility for a pair of attributes and rating
	 * 
	 * @return
	 */
	public static Result addCompatibility(long problemId) {
		models.Compatibility c = Form.form(models.Compatibility.class)
				.bindFromRequest().get();
		c.userId = session("userId");
		c.save();

		Map<String, Object> result = Maps.newHashMap();
		result.put("compatibility", c);
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

		List<models.Compatibility> compats = Ebean.find(
				models.Compatibility.class).findList();

		result.put("compatibilities", compats);

		return ok(Json.toJson(result));

	}
}