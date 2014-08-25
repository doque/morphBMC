package controllers;

import java.util.Map;

import models.Attribute;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

public class Parameter extends Controller {

	
	
	public static Result addParameter() {
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("parameter",
				new models.Parameter().setId(5).setName("Hullabulla"));
		return ok(Json.toJson(result));
	}
	
	public static Result addAttribute(long id) {
		
		Form<Attribute> attributeForm = Form.form(Attribute.class);
		
	 	Attribute attr = attributeForm.bindFromRequest().get();
	
		Map<String, Object> result = Maps.newHashMap();
		result.put("attribute", new models.Attribute(attr.name).setId(5000));
		return ok(Json.toJson(result));
	}
}
