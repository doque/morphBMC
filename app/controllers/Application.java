package controllers;

import models.Problem;
import play.mvc.Controller;
import play.mvc.Result;
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;

public class Application extends Controller {

	/**
	 * render main angular template
	 * 
	 * @return
	 */
	@SecureSocial.SecuredAction
	public static Result index() {

		// Mock Problem
		Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);

		Problem p = new Problem();
		p.setName("Wicked Problem");
		p.setUserId(user.identityId().userId());
		p.setProviderId(user.identityId().providerId());
		p.save();

		// this sucks, set it to client-session only and maybe enable multiple
		// sessions
		ctx().args.put("problemId", p.getId());

		return ok(views.html.index.render(p));
	}
}
