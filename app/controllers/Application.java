package controllers;

import models.Problem;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	/**
	 * render main angular template
	 * 
	 * @return
	 */
	// @SecureSocial.SecuredAction
	public static Result index() {

		// Mock Problem
		// Identity identity = (Identity) ctx().args.get(SecureSocial.USER_KEY);
		session("userId", "dummy");
		Problem p = null;
		if (session("problemId") != null) {
			p = Problem.find.byId(Long.parseLong(session("problemId")));
		}
		if (p == null) {
			p = new Problem();
			p.name = "Wicked Problem";
			p.userId = session("userId");
			p.save();
		}
		session("problemId", Long.toString(p.id));

		return ok(views.html.index.render(p));
	}
}
