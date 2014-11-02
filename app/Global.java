import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import services.SocketService;
import services.SocketServiceInterface;

import java.util.List;
import java.util.Map;

import models.Rating;

import com.avaje.ebean.Ebean;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Global extends GlobalSettings {

	private Injector injector;

	public void onStart(Application app) {

		injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(SocketServiceInterface.class).to(SocketService.class);
			}
		});

		insertInitialData();

	}

	@Override
	public <T> T getControllerInstance(Class<T> aClass) throws Exception {
		return injector.getInstance(aClass);
	}

	private static void insertInitialData() {

		@SuppressWarnings("unchecked")
		Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
				.load("initial-data.yml");

		// insert ratings
		if (Rating.find.findRowCount() == 0) {
			Ebean.save(all.get("ratings"));
		}

		// insert problems
		//if (Problem.find.findRowCount() == 0) {
			//Ebean.save(all.get("problems"));
		//}

		// save associations, top down
		/*
		 * for (Object problem : all.get("problems")) {
		 * Ebean.saveManyToManyAssociations(problem, "parameters"); } for
		 * (Object parameter : all.get("parameters")) {
		 * Ebean.saveManyToManyAssociations(parameter, "attributes"); }
		 */

		Logger.info("database populated");

	}

}
