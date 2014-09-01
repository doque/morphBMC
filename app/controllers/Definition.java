package controllers;

import java.util.Map;

import models.Attribute;
import models.Parameter;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import securesocial.core.java.SecureSocial.SecuredAction;

import com.google.common.collect.Maps;

public class Definition extends Controller {

	@SecuredAction(ajaxCall = true)
	public static Result addParameter() {
		// incoming parameter
		Parameter p = Form.form(Parameter.class).bindFromRequest().get();

		// add user info
		Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
		p.setUserId(user.identityId().userId());
		p.setProviderId(user.identityId().providerId());

		// send to DB
		p.save();

		// outgoing result contains the same object
		Map<String, Object> result = Maps.newHashMap();
		result.put("parameter", p);

		// render json to client
		return ok(Json.toJson(result));
	}

	@SecuredAction(ajaxCall = true)
	public static Result addAttribute(long parameterId) {
		// grab user info
		Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);

		Attribute attr = Form.form(Attribute.class).bindFromRequest().get();
		attr.setUserId(user.identityId().userId());
		attr.setProviderId(user.identityId().providerId());

		Parameter p = Parameter.find.byId(parameterId);
		p.getAttributes().add(attr);

		Map<String, Object> result = Maps.newHashMap();
		result.put("attribute", attr);
		return ok(Json.toJson(result));
	}
}
