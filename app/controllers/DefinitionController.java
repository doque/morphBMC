package controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import services.SocketServiceInterface;

import java.util.Map;

import models.Attribute;
import models.Parameter;
import models.Problem;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class DefinitionController extends Controller {

	private SocketServiceInterface socketService;

	@Inject	public DefinitionController(SocketServiceInterface socketService) {
		this.socketService = checkNotNull(socketService);
	}

	// @SecuredAction(ajaxCall = true)
	public Result addParameter(long problemId) {
		// incoming parameter
		Parameter p = Form.form(Parameter.class).bindFromRequest().get();

		p.userId = session("userId");

		p.problem = Problem.find.byId(problemId);
		p.save();

		// outgoing result contains the same object
		Map<String, Object> result = Maps.newHashMap();
		result.put("parameter", p);

		socketService.broadcast("party tonight");

		// render json to client
		return ok(Json.toJson(result));
	}

	public Result deleteParameter(long problemId, long parameterId) {
		// TODO auth problemId
		Parameter p = Parameter.find.byId(parameterId);
		if (p != null) {
			// p.deleteManyToManyAssociations("");
			p.delete();
		}
		return ok();
	}

	// @SecuredAction(ajaxCall = true)
	public Result addAttribute(long problemId, long parameterId) {

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
