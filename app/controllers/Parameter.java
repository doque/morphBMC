package controllers;

import java.util.Map;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.collect.Maps;

public class Parameter extends Controller {

	public static Result addParameter() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("parameter",
				new models.Parameter().setId(5).setName("Hullabulla"));
		return ok(Json.toJson(result));
	}
}
