package br.com.marcospcruz.gestorloja.view.fxui;

import javafx.stage.Stage;

public class StageFactory {

	public static Stage createStage(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class clazz = Class.forName(className);

		return (Stage) clazz.newInstance();
	}

}
