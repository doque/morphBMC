package controllers;

import models.Attribute;
import models.Parameter;
import models.Problem;
import views.html.*;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	/**
	 * First step: Define problem parameters
	 * 
	 * @return
	 */

	public static Result index() {
		return ok(define.render());
	}

	public static Result define() {
		return ok(define.render());
	}

	public static Result compatiblity() {

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

		return ok(compatibility.render(p));
	}
}
