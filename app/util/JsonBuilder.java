package util;

import play.libs.Json;

import java.util.Map;

import models.Compatibility;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;


/**
 * builds various JSON-formatted files to be broadcasted to clients
 * @author dbusser
 *
 */
public class JsonBuilder {

	/**
	 * pushes out a compatibility object that replaces all others
	 * with the same attr1_id and attr2_id.
	 * @param c
	 * @return
	 */
	public static JsonNode conflictResolved(Compatibility c) {
			Map<String, Object> result = Maps.newHashMap();
			result.put("compatibility", c);
		return Json.toJson(result);
	}
	
}