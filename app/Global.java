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
				Logger.info("no data found yet, populating database");
				@SuppressWarnings("unchecked")
				Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
						.load("initial-data.yml");

				// we only have one list
				Ebean.save(all.get("problems"));

				// save associations, top down
				for (Object problem : all.get("problems")) {
					Ebean.saveManyToManyAssociations(problem, "parameters");
				}
				for (Object parameter : all.get("parameters")) {
					Ebean.saveManyToManyAssociations(parameter, "attributes");
				}

				Logger.info("database populated");

			}
		}

	}
}
