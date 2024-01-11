/*
	Aplikasi SeRuput Teh
	Reynard Hans Prayoga
*/
package main;

import javafx.application.Application;
import javafx.stage.Stage;
import pages.LoginPage;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("SeRuput Teh");
		LoginPage loginPage = new LoginPage(primaryStage);
		loginPage.setLoginPage(1000, 700);
		primaryStage.show();
	}

}
