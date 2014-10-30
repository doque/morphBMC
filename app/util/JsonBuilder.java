package util;

import play.libs.Json;


/**
 * builds various JSON-formatted files to be broadcasted to clients
 * @author dbusser
 *
 */
public class JsonBuilder {

	public enum Types {
		/* Refinement Stages */
		PARAMETER_ADDED,
		ATTRIBUTE_ADDED,
		PARAMETER_DELETED,
		ATTRIBUTE_DELETED,
		ATTRIBUTE_REASSIGNED,
		
		/* Conflict Resolution */
		CONFLICT_RESOLVED
	}
	
	/**
	 * pushes out a compatibility object that replaces all others
	 * with the same attr1_id and attr2_id.
	 * @param c
	 * @return
	 */
	public static String conflictResolved() {
			//Map<String, Object> result = Maps.newHashMap();
			//result.put("compatibility", c);
		return Json.toJson(Types.CONFLICT_RESOLVED).toString();
	}
	
	
	
}
