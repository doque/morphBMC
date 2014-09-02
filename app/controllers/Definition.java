package controllers;

import java.util.List;
import java.util.Map;

import models.Attribute;
import models.Parameter;
import models.Problem;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.common.collect.Maps;

public class Definition extends Controller {

	// @SecuredAction(ajaxCall = true)
	public static Result getProblemEnvironment(long problemId) {
		List<Parameter> params = Parameter.find.where()
				.eq("problem_id", problemId).findList();

		Map<String, Object> result = Maps.newHashMap();
		result.put("parameters", params);

		return ok(Json.toJson(result));
	}

	// @SecuredAction(ajaxCall = true)
	public static Result addParameter() {
		// incoming parameter
		Parameter p = Form.form(Parameter.class).bindFromRequest().get();

		p.userId = session("userId");

		p.problem = Problem.find.byId(Long.parseLong(session("problemId")));
		p.save();

		// outgoing result contains the same object
		Map<String, Object> result = Maps.newHashMap();
		result.put("parameter", p);

		// render json to client
		return ok(Json.toJson(result));
	}

	// @SecuredAction(ajaxCall = true)
	public static Result addAttribute(long parameterId) {

		Attribute attr = Form.form(Attribute.class).bindFromRequest().get();
		attr.userId = session("userId");
		Parameter p = Parameter.find.byId(parameterId);
		p.attributes.add(attr);
		p.save();
		attr.parameter = p;
		attr.save();

		Map<String, Object> result = Maps.newHashMap();
		result.put("attribute", attr);
		return ok(Json.toJson(result));
	}
}
