package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.libs.Yaml;
import play.mvc.Controller;
import play.mvc.Result;

import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import securesocial.core.java.SecureSocial.SecuredAction;

import java.util.List;
import java.util.Map;

import models.Parameter;
import models.Problem;
import models.Problem.Stage;

import com.google.common.collect.Maps;

public class ProblemController extends Controller {
	
	@SecuredAction
	public Result createProblem() {
		DynamicForm form = Form.form().bindFromRequest();
		String name = form.get("name");
		
		// grab user
		Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
		String userId = (String) (user.email().isDefined() ? user.email().get() : user.identityId().userId());
		
		Problem p = new Problem();
		p.name = name;
		// twitter does not expose email addresses.
		p.owner = p.userId = userId;
		
		// BMC problem
		if (form.get("bmc") != null) {
			// read BMC terminology
			@SuppressWarnings("unchecked")
			Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
					.load("bmc.yml");

			// insert parameters to problem for
			for (Object o : all.get("parameters")) {
				Parameter param = (Parameter) o;
				param.userId = userId;
				p.parameters.add(param);
			}

		}

		p.save();
		
		return redirect("/problem/" + p.id);
	}

	        
	/**
	 * return detailed info about a problem.
	 */
	// @SecuredAction(ajaxCall = true)
	public Result getProblemEnvironment(long id) {

		Problem p = Problem.find.byId(id);
		if (p == null) {
			return notFound();
		}

		Map<String, Object> result = Maps.newHashMap();
		
		// strip parameters here, this is handled by DefinitionController
		p.parameters = null;
		
		result.put("problem", p);

		return ok(Json.toJson(result));
	}

	/**
	 * Updates a problem statement
	 */
	public Result changeStatement(long id) {
		Problem p = Problem.find.byId(id);
		if (p == null) {
			return notFound();
		}
		String statement = Form.form().bindFromRequest().get("statement");
		if (statement != null) {
			p.statement = statement;
			p.save();
		}
		
		return ok();
	}
	
	/**
	 * changes the Stage that a problem is currently in can be done within any
	 * stage and affects all users
	 * 
	 * @param problemId
	 * @param stage
	 * @return not found if the problem doesnt exist, otherwise HTTP 200
	 */
	public Result changeStage(long id) {
		
		String _stage = Form.form().bindFromRequest().get("stage");
		
		Stage stage = Stage.getStage(_stage);
		
		if (_stage == null) {
			return badRequest();
		}
		
		Problem p = Problem.find.byId(id);
		if (p == null) {
			return notFound();
		}
		
		if (!p.userId.equals(session().get("userId"))) {
			return unauthorized();
		}
		
		p.stage = stage;
		p.save();
		
		return ok();
	}

}
