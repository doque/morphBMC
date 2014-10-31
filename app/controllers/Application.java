package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import securesocial.core.java.SecureSocial.SecuredAction;
import models.Problem;

public class Application extends Controller {

	/**
	 * Start page for problem creation
	 * @return
	 */
	@SecuredAction
	public static Result index() {
		return ok(views.html.index.render());
	}

	/**
	 * render main angular template. all further templating is done from the
	 * front end
	 * 
	 * @return HTTP 200 when creating a new problem
	 */
	@SecuredAction
	public static Result viewProblem(long id) {

		// does the problem exist?
		Problem p = Problem.find.byId(id);
		if (p == null) {
			return notFound();
		}

		// yes this is not 100 % accurate because different provider's userIds overlap,
		// but for now we're only using twitter anyway so it works.
		// in the future, hash userId + provider.
		
		String userId = session().get("userId");
		
		// can the user see the problem? -> for now, always yes.
		if (true || p.userId.equals(userId)) {

			return ok(views.html.problem.render(p.id, userId));
		}

		return unauthorized();
	}
}
