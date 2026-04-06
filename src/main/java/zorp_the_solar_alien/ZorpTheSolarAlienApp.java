package zorp_the_solar_alien;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import zorp_the_solar_alien.controller.PlayController;

import zorp_the_solar_alien.SingletonObject.MainCharacterManager;
import zorp_the_solar_alien.model.HomeModel;
import zorp_the_solar_alien.model.PlayModel;
import zorp_the_solar_alien.view.HomeView;
import zorp_the_solar_alien.controller.HomeController;
import zorp_the_solar_alien.view.PlayView;
import zorp_the_solar_alien.view.HowToPlayView;
import zorp_the_solar_alien.controller.HowToPlayController;

/**
 * 
 * The main application which launches zorp the solar alien 
 */
public class ZorpTheSolarAlienApp extends Application {
	HomeModel homeModel;
	HomeView playView;
	HomeController homeController;

	@Override
	public void start(Stage stage) {
		Pane root = new Pane();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.setTitle("Zorp The Solar Alien");
		stage.show();

		HomeModel homeModel = new HomeModel();
		PlayModel playModel = new PlayModel();

		HomeView homeView = new HomeView(root, homeModel);
		PlayView playView = new PlayView(root);
		playView.hide();
		HowToPlayView howToPlayView = new HowToPlayView(root);
		HowToPlayController howToPlayController = new HowToPlayController(howToPlayView);
		howToPlayController.hide();

		HomeController homeController = new HomeController(homeModel, homeView, playView, howToPlayController);
		PlayController playController = new PlayController(playModel, playView);
		root.getChildren().addAll(homeView.menuBar, homeView.playBtn, homeView.quitBtn);

		MainCharacterManager.getInstance(playView.gc);
		scene.setOnKeyPressed(e -> {
			MainCharacterManager.getInstance().setInput(e.getCode().toString(), true);
		});

		scene.setOnKeyReleased(e -> {
			MainCharacterManager.getInstance().setInput(e.getCode().toString(), false);
		});

		scene.setOnMousePressed(e -> {
			MainCharacterManager.getInstance().setMouseInput(e.getButton().toString(), true);
		});

		scene.setOnMouseReleased(e -> {
			MainCharacterManager.getInstance().setMouseInput(e.getButton().toString(), false);
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
