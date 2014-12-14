package controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.libs.Yaml;
import play.mvc.Controller;
import play.mvc.Result;

import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import securesocial.core.java.SecureSocial.SecuredAction;
import services.SocketServiceInterface;
import util.JsonBuilder;

import java.util.List;
import java.util.Map;

import models.Parameter;
import models.Problem;
import models.Problem.Stage;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class ProblemController extends Controller {
	
	/**
	 * Dependency Injection to make sure all clients get the same SocketService
	 */
	private SocketServiceInterface socketService;

	@Inject
	public ProblemController(SocketServiceInterface socketService) {
		this.socketService = checkNotNull(socketService);
	}
	/**
	 * Start page for problem creation
	 * @return
	 */
	@SecuredAction
	public Result index() {
		return ok(views.html.index.render());
	}

	/**
	 * render main angular template. all further templating is done from the
	 * front end
	 * 
	 * @return HTTP 200 when creating a new problem
	 */
	@SecuredAction
	public Result viewProblem(long id) {
		
		// yes this is not 100 % accurate because different provider's userIds overlap,
		// but for now we're only using twitter anyway so it works.
		// in the future, hash userId + provider.
		String userId = session().get("userId");
		if (userId == null) {
			Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
			userId = (String) (user.email().isDefined() ? user.email().get() : user.identityId().userId());
			session().put("userId", userId);
		}
		
		userId = session().get("userId");
		

		// does the problem exist?
		Problem p = Problem.find.byId(id);
		if (p == null) {
			return notFound();
		}

		// can the user see the problem? -> for now, always yes.
		if (true || p.userId.equals(userId)) {

			return ok(views.html.problem.render(p.id, userId));
		}

		return unauthorized();
	}
	
	@SecuredAction
	public Result createProblem() {
		DynamicForm form = Form.form().bindFromRequest();
		String name = form.get("name");
		
		// grab user
		Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
		String userId = (String) (user.email().isDefined()
							? user.email().get() : user.identityId().userId());
		
		String owner = user.fullName().isEmpty() ? user.identityId().userId() : user.fullName();
		
		Problem p = new Problem();
		p.name = name;
		// twitter does not expose email addresses.
		p.owner = owner; 
		p.userId = userId;
		
		// BMC problem
		if (form.get("bmc") != null) {
			// read BMC terminology
			@SuppressWarnings("unchecked")
			Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
					.load("bmc.yml");

			// insert parameters to problem for
			int count = 0;
			for (Object o : all.get("parameters")) {
				Parameter param = (Parameter) o;
				param.userId = userId;
				// since the ID is only guaranteed to be unique but not smaller/larger than any other ids, use this
				param.created = System.currentTimeMillis() + (count++);
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
		socketService.broadcast(JsonBuilder.problemUpdated());
		
		return ok();
	}
	
	/**
	 * Updates a problem statement
	 */
	public Result changeName(long id) {
		Problem p = Problem.find.byId(id);
		if (p == null) {
			return notFound();
		}
		String name = Form.form().bindFromRequest().get("name");
		if (name != null) {
			p.name = name;
			p.save();
		}
		socketService.broadcast(JsonBuilder.problemUpdated());
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
