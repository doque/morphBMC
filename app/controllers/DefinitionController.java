package controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import services.SocketServiceInterface;

import java.util.List;
import java.util.Map;

import models.Attribute;
import models.Compatibility;
import models.Parameter;
import models.Problem;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class DefinitionController extends Controller {

	/**
	 * Dependency Injection to make sure all clients get the same SocketService
	 */
	private SocketServiceInterface socketService;

	@Inject
	public DefinitionController(SocketServiceInterface socketService) {
		this.socketService = checkNotNull(socketService);
	}

	/**
	 * retrieve parameters. either ALL or just belonging to requesting user
	 * 
	 * @param problemId
	 * @return
	 */
	public Result getParameters(long problemId) {

		Problem pr = Problem.find.byId(problemId);
		if (pr == null) {
			return notFound();
		}

		// retrieve all user's parameters or only their own?
		String override = Form.form().bindFromRequest().get("all");

		// no "all", meaning only one user's parameters
		// so strip out all others.
		if (override == null) {
			String userId = session().get("userId");
			for (int i = 0; i < pr.parameters.size(); i++) {
				Parameter p = pr.parameters.get(i);
				if (!p.userId.equals(userId)) {
					pr.parameters.remove(i);
				}
			}

		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("parameters", pr.parameters);

		return ok(Json.toJson(result));
	}

	/**
	 * Add a new Parameter with no attributes yet This triggers a WebSocket
	 * event to be broadcasted to all clients.
	 * 
	 * @param problemId
	 * @return
	 */
	public Result addParameter(long problemId) {

		Problem pr = Problem.find.byId(problemId);
		if (pr == null) {
			return notFound();
		}
		Parameter p = Form.form(Parameter.class).bindFromRequest().get();

		String userId = session().get("userId");
		p.userId = userId;
		p.problem = pr;
		p.save();

		Map<String, Object> result = Maps.newHashMap();
		result.put("parameter", p);

		// and broadcast to others
		socketService.broadcastExcept(userId, "updated");

		return ok(Json.toJson(result));
	}

	/**
	 * Delete a parameter, including all its attributes and all compatibilities
	 * that refer to one of its attributes
	 * 
	 * @param problemId
	 * @param parameterId
	 * @return
	 */

	public Result deleteParameter(long problemId, long parameterId) {
		Parameter p = Parameter.find.byId(parameterId);
		String userId = session().get("userId");
		if (p != null) {
			/*
			 * Unfortunately, the mapping of Attributes to Parameter is not as
			 * perfect as it should be so we first need to remove all attributes
			 * of the parameter and then all compatibilities.
			 */
			for (Attribute attr : p.attributes) {

				// if compatibilities are already present, delete them as well.
				List<Compatibility> compats = Compatibility.find.all();
				for (Compatibility c : compats) {
					if (c.attr1.id == attr.id || c.attr2.id == attr.id) {
						c.delete();
					}
				}

			}
			// deleting the parameter will propagate to the attributes
			p.delete();
		}

		socketService.broadcastExcept(userId, "updated");
		return ok();
	}

	/**
	 * Adds a single Attribute to a parameter
	 * 
	 * @param problemId
	 * @param parameterId
	 * @return
	 */
	public Result addAttribute(long problemId, long parameterId) {

		Parameter p = Parameter.find.byId(parameterId);
		String userId = session().get("userId");
		if (p == null) {
			return notFound();
		}

		Attribute attr = Form.form(Attribute.class).bindFromRequest().get();
		// attribute already exists, in this case it was dragdropped into
		// another parameter
		if (attr.id != 0) {
			p.attributes.add(attr);
			p.update();
			attr.parameter = p;
			attr.update();
		} else {
			// create new attribute
			attr.userId = userId;
			p.attributes.add(attr);
			p.update();
			attr.parameter = p;
			attr.save();
		}

		socketService.broadcastExcept(userId, "updated");

		Map<String, Object> result = Maps.newHashMap();
		result.put("attribute", attr);
		return ok(Json.toJson(result));
	}

	/**
	 * Delete an attribute
	 * 
	 * @param problemId
	 * @param attributeId
	 * @return
	 */
	public Result deleteAttribute(long problemId, long attributeId) {
		Attribute a = Attribute.find.byId(attributeId);
		String userId = session().get("userId");
		if (a == null) {
			return notFound();
		}
		List<Compatibility> compats = Compatibility.find.all();
		for (Compatibility c : compats) {
			if (c.attr1.id == a.id || c.attr2.id == a.id) {
				c.delete();
			}
		}
		a.delete();

		socketService.broadcastExcept(userId, "updated");
		return ok();
	}
}
