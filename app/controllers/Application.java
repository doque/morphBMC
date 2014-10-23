package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import models.Problem;

public class Application extends Controller {

	public static Result index() {
		return ok(views.html.index.render());
	}

	/**
	 * render main angular template. all further templating is done from the
	 * front end
	 * 
	 * @return HTTP 200 when creating a new problem
	 */

	@SecureSocial.SecuredAction
	public static Result viewProblem(long id) {

		// does the problem exist?
		Problem p = Problem.find.byId(id);
		if (p == null) {
			return notFound();
		}

		Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
		String userId = user.identityId().userId();

		// can the user see the problem? -> for now, always yes.
		if (true || p.userId.equals(userId)) {
			return ok(views.html.problem.render(p));
		}

		return unauthorized();
	}

}
