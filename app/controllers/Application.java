package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

import models.Problem;
import models.Problem.Stage;

import com.google.common.collect.Maps;

public class Application extends Controller {

	static {
		session("userId", "Dummy User");
		session("problemId", "1");
	}

	// @SecuredAction(ajaxCall = true)
	public static Result getProblemEnvironment() {

		Problem p = Problem.find.byId((long) 1);
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
		// Identity identity = (Identity) ctx().args.get(SecureSocial.USER_KEY);
		Problem p = Problem.find.byId((long) 1);
		// sanity check
		if (p == null) {
			return notFound();
		}

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
		Problem p = Problem.find.byId((long) 1);
		// sanity check
		if (p == null) {
			return notFound();
		}
		p.currentStage = stage;
		p.save();
		return ok();
	}

}
