package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

import models.Rating;

import com.google.common.collect.Maps;

public class RatingController extends Controller {
	public Result getRatings() {

		List<Rating> ratings = Rating.find.all();
		Map<String, Object> result = Maps.newHashMap();
		result.put("ratings", ratings);

		return ok(Json.toJson(result));
	}
}
