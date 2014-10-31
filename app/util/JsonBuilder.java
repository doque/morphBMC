package util;

import play.libs.Json;

import java.util.Map;

import models.Compatibility;

import com.google.common.collect.Maps;


/**
 * builds various JSON-formatted files to be broadcasted to clients
 * @author dbusser
 *
 */
public class JsonBuilder {

	public enum Types {
		/* Refinement Stages */
		UPDATE,
		/* Conflict Resolution */
		CONFLICT_RESOLVED
	}
	
	/**
	 * pushes out a compatibility object that replaces all others
	 * with the same attr1_id and attr2_id.
	 * @param c - the resolving compatibility
	 * @return
	 */
	public static String conflictResolved(Compatibility c) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("type", Types.CONFLICT_RESOLVED);
		result.put("data", c);
		return Json.toJson(result).toString();
	}
	
	/**
	 * pushes an update request to clients, causing them to refresh
	 * $scope by polling from REST endpoints
	 */
	public static String update() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("type", Types.UPDATE);
		return Json.toJson(result).toString();
	}
	
}
