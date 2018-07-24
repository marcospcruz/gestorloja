package br.com.marcospcruz.gestorloja;

import br.com.marcospcruz.gestorloja.view.fxui.LogIn;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppFx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage = new LogIn();
		primaryStage.show();
	}

	public static void main(String args[]) {
		launch(args);
	}

}
