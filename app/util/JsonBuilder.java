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
		DEFINITION_UPDATED,
		/* Conflict Resolution */
		CONFLICT_RESOLVED,
		/* Problem Propertes */
		PROBLEM_UPDATED
	}
	
	/**
	 * compatibility conflict has been resolved
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
	 * problem definition has been updated
	 */
	public static String definitionUpdated() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("type", Types.DEFINITION_UPDATED);
		return Json.toJson(result).toString();
	}
	
	/**
	 * problem statement has been updated
	 */
	public static String problemUpdated() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("type", Types.PROBLEM_UPDATED);
		return Json.toJson(result).toString();
	}
	
	
}
