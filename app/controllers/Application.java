package controllers;

import models.Attribute;
import models.Parameter;
import models.Problem;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class Application extends Controller {

	public static Result index() {

		Problem p = new Problem();
		Parameter kosten = new Parameter().setName("Kosten");
		Parameter distanz = new Parameter().setName("Distanz");
		Parameter urlaubsziel = new Parameter().setName("Urlaubsziel");

		// Kosten
		kosten.addAttribute(new Attribute("0-100 EUR"))
				.addAttribute(new Attribute("100-500 EUR"))
				.addAttribute(new Attribute("500+ EUR"));

		// Distanz
		distanz.addAttribute(new Attribute("0-200 km"))
				.addAttribute(new Attribute("200-500 km"))
				.addAttribute(new Attribute("500+ km"));

		// Urlaubsziel
		urlaubsziel.addAttribute(new Attribute("Wanderurlaub"))
				.addAttribute(new Attribute("Städtetrip"))
				.addAttribute(new Attribute("Strandurlaub"));

		p.addParameter(kosten).addParameter(distanz).addParameter(urlaubsziel);

		return ok(index.render(p));
	}
}
