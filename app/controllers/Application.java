package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

import models.Problem;
import models.Problem.Stage;
import models.Rating;

import com.google.common.collect.Maps;

public class Application extends Controller {

	public static Result getRatings() {

		List<Rating> ratings = Rating.find.all();
		Map<String, Object> result = Maps.newHashMap();
		result.put("ratings", ratings);

		return ok(Json.toJson(result));
	}

	// @SecuredAction(ajaxCall = true)
	public static Result getProblemEnvironment() {
		Long id = Long.parseLong(session("problemId"));

		// TODO auth etc.
		Problem p = Problem.find.byId(id);
		// sanity check
		if (p == null) {
			return notFound();
		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("problem", p);

		return ok(Json.toJson(result));
	}

	/**
	 * render main angular template all further templating is done from the
	 * front end
	 * 
	 * @return HTTP 200 when creating a new problem
	 */

	// TODO parameters for problem creation (name, anything else)
	// @SecureSocial.SecuredAction
	public static Result index() {

		session("userId", "Dummy User");
		session("problemId", "1");

		// Identity identity = (Identity) ctx().args.get(SecureSocial.USER_KEY);
		Long id = Long.parseLong(session("problemId"));

		// TODO auth etc.
		Problem p = Problem.find.byId(id);
		// sanity check
		if (p == null) {
			return notFound();
		}

		// render problem ID as js var
		return ok(views.html.index.render());
	}

	/**
	 * changes the Stage that a problem is currently in can be done within any
	 * stage and affects all users
	 * 
	 * @param problemId
	 * @param stage
	 * @return not found if the problem doesnt exist, otherwise HTTP 200
	 */
	public static Result changeState(long problemId, Stage stage) {
		Long id = Long.parseLong(session("problemId"));

		// TODO auth etc.
		Problem p = Problem.find.byId(id);
		// sanity check
		if (p == null) {
			return notFound();
		}
		p.currentStage = stage;
		p.save();
		return ok();
	}

}
