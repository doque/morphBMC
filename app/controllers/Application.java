package controllers;

import models.Attribute;
import models.Parameter;
import models.Problem;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import securesocial.*;
import views.html.*;

public class Application extends Controller {

	/**
	 * First step: Define problem parameters
	 * 
	 * @return
	 */
	public static Result define() {
		return ok(define.render());
	}

	public static Result fucker() {

		Problem p = new Problem();
		Parameter kosten = new Parameter().setId(1).setName("Kosten");
		Parameter distanz = new Parameter().setId(2).setName("Distanz");
		Parameter urlaubsziel = new Parameter().setId(3).setName("Urlaubsziel");

		// Kosten
		kosten.addAttribute(new Attribute("0-100 EUR").setId(10))
				.addAttribute(new Attribute("100-500 EUR").setId(11))
				.addAttribute(new Attribute("500+ EUR").setId(12));

		// Distanz
		distanz.addAttribute(new Attribute("0-200 km").setId(13))
				.addAttribute(new Attribute("200-500 km").setId(14))
				.addAttribute(new Attribute("500+ km")).setId(15);

		// Urlaubsziel
		urlaubsziel.addAttribute(new Attribute("Wanderurlaub").setId(16))
				.addAttribute(new Attribute("Städtetrip").setId(17))
				.addAttribute(new Attribute("Strandurlaub").setId(18));

		p.addParameter(kosten).addParameter(distanz).addParameter(urlaubsziel);

		return ok(index.render(p));
	}

	public static Logger.ALogger logger = Logger
			.of("application.controllers.Application");
	private RuntimeEnvironment env;

	/**
	 * A constructor needed to get a hold of the environment instance. This
	 * could be injected using a DI framework instead too.
	 * 
	 * @param env
	 */
	public Application(RuntimeEnvironment env) {
		this.env = env;
	}

	/**
	 * This action only gets called if the user is logged in.
	 * 
	 * @return
	 */

	@SecuredAction
	public Result index() {
		if (logger.isDebugEnabled()) {
			logger.debug("access granted to index");
		}
		DemoUser user = (DemoUser) ctx().args.get(SecureSocial.USER_KEY);
		return ok(index.render(user, SecureSocial.<DemoUser> env()));
	}

	@UserAwareAction
	public Result userAware() {
		DemoUser demoUser = (DemoUser) ctx().args.get(SecureSocial.USER_KEY);
		String userName;
		if (demoUser != null) {
			BasicProfile user = demoUser.main;
			if (user.firstName().isDefined()) {
				userName = user.firstName().get();
			} else if (user.fullName().isDefined()) {
				userName = user.fullName().get();
			} else {
				userName = "authenticated user";
			}
		} else {
			userName = "guest";
		}
		return ok("Hello " + userName + ", you are seeing a public page");
	}

	@SecuredAction(authorization = WithProvider.class, params = { "twitter" })
	public Result onlyTwitter() {
		return ok("You are seeing this because you logged in using Twitter");
	}

	@SecuredAction
	public Result linkResult() {
		DemoUser current = (DemoUser) ctx().args.get(SecureSocial.USER_KEY);
		return ok(linkResult.render(current, current.identities));
	}

	/**
	 * Sample use of SecureSocial.currentUser. Access the /current-user to test
	 * it
	 */
	public F.Promise<Result> currentUser() {
		return SecureSocial.currentUser(env).map(
				new F.Function<Object, Result>() {
					@Override
					public Result apply(Object maybeUser) throws Throwable {
						String id;

						if (maybeUser != null) {
							DemoUser user = (DemoUser) maybeUser;
							id = user.main.userId();
						} else {
							id = "not available. Please log in.";
						}
						return ok("your id is " + id);
					}
				});
	}

}
