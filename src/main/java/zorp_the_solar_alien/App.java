package zorp_the_solar_alien;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
 * The main application which launches zorp the solar alien 
 */
public class App extends Application {
	@Override
	public void start(Stage stage) {
		var root = new StackPane();
		var scene = new Scene(root);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
