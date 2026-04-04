package zorp_the_solar_alien;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import zorp_the_solar_alien.model.HomeModel;
import zorp_the_solar_alien.view.HomeView;
import zorp_the_solar_alien.controller.HomeController;

/**
 * 
 * The main application which launches zorp the solar alien 
 */
public class ZorpTheSolarAlienApp extends Application {
	HomeModel model;
	HomeView view;
	HomeController controller;

	@Override
	public void start(Stage stage) {
		Pane root = new Pane();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.setTitle("Zorp The Solar Alien");
		stage.show();

		model = new HomeModel();
		view = new HomeView(root, model);
		controller = new HomeController(model, view);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
