package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

import models.Problem;

import com.google.common.collect.Maps;

public class ProblemController extends Controller {

	/**
	 * create a new problem in the database
	 * 
	 * @param userId
	 *            - the creating user's userId. Name will be stored in the owner
	 *            field
	 * @return the new problem
	 * 
	 *         public static Problem createProblem(String userId) {
	 * 
	 *         Problem p = new models.Problem(); p.userId = userId;
	 * 
	 *         // get the user identities InMemoryUserService service =
	 *         (InMemoryUserService) Play.application()
	 *         .plugin(BaseUserService.class);
	 * 
	 *         p.save();
	 * 
	 *         return p; } create a new problem based on BMC terminology
	 * 
	 * @return
	 * 
	 *         public static Problem createBMC(String userId) { Problem p = new
	 *         models.Problem(); p.userId = userId;
	 * 
	 *         // read BMC terminology
	 * @SuppressWarnings("unchecked") Map<String, List<Object>> all =
	 *                                (Map<String, List<Object>>) Yaml
	 *                                .load("bmc.yml");
	 * 
	 *                                // insert parameters to problem for
	 *                                (Object o : all.get("parameters")) {
	 *                                Parameter param = (Parameter) o;
	 *                                p.parameters.add(param); }
	 * 
	 *                                p.save(); return p; }
	 */

	/**
	 * return detailed info about a problem.
	 * 
	 * @return
	 */
	// @SecuredAction(ajaxCall = true)
	public static Result getProblemEnvironment(long id) {

		Problem p = Problem.find.byId(id);
		if (p == null) {
			return notFound();
		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("problem", p);

		return ok(Json.toJson(result));
	}

	/**
	 * changes the Stage that a problem is currently in can be done within any
	 * stage and affects all users
	 * 
	 * @param problemId
	 * @param stage
	 * @return not found if the problem doesnt exist, otherwise HTTP 200
	 * 
	 *         public static Result changeState(long problemId, Stage stage) {
	 *         Long id = Long.parseLong(session("problemId"));
	 * 
	 *         // TODO auth etc. Problem p = Problem.find.byId(id); // sanity
	 *         check if (p == null) { return notFound(); } p.currentStage =
	 *         stage; p.save(); return ok(); }
	 */
}
