package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

import models.Problem;
import models.Problem.Stage;

import com.google.common.collect.Maps;

public class Application extends Controller {

	// @SecuredAction(ajaxCall = true)
	public static Result getProblemEnvironment() {
		long problemId = Long.parseLong(session("problemId"));

		Problem p = Problem.find.byId(problemId);
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
		session("userId", "dummy");
		long problemId = Long.parseLong(session("problemId"));
		Problem p = Problem.find.byId(problemId);
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
		Problem p = null;
		if (session("problemId") != null) {
			p = Problem.find.byId(Long.parseLong(session("problemId")));
			if (p == null) {
				return notFound();
			}
		}
		p.currentStage = stage;
		return ok();
	}

}
