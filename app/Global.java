import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

import models.Problem;

import com.avaje.ebean.Ebean;

public class Global extends GlobalSettings {
	public void onStart(Application app) {
		Logger.info("Starting morphBMC");
		InitialData.insert(app);
	}

	static class InitialData {

		public static void insert(Application app) {
			if (Ebean.find(Problem.class).findRowCount() == 0) {

				@SuppressWarnings("unchecked")
				Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
						.load("initial-data.yml");

				Ebean.save(all.get("problems"));
				// Insert users first
				/*
				 * Ebean.save(all.get("problems"));
				 * 
				 * // Insert projects Ebean.save(all.get("projects")); for
				 * (Object project : all.get("projects")) { // Insert the
				 * project/user relation
				 * Ebean.saveManyToManyAssociations(project, "members"); }
				 * 
				 * // Insert tasks Ebean.save(all.get("tasks"));
				 */

			}
		}

	}
}
