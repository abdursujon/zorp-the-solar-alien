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
import zorp_the_solar_alien.model.HowToPlayModel;
import zorp_the_solar_alien.controller.HowToPlayController;

/**
 * The main application class which launches zorp the solar alien game.
 */
public class ZorpTheSolarAlienApp extends Application {
	HomeModel homeModel;
	HomeView playView;
	HomeController homeController;

	/**
	 * This method enable us to set up the stage, through creating different components of different class from MVC pattern.
	 * In the beginning of the application, home screen is set to show, and rest of the view is set to hide.
	 * This method also places different nav link into the scene to enable user interaction for play, home etc.
	 * It also creates the instance of the main character and register different mouse events from the user.
	 */
	@Override
	public void start(Stage stage) {
		Pane root = new Pane();
		root.setStyle("-fx-background-color: black;");
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
		HowToPlayModel howToPlayModel = new HowToPlayModel();
		HowToPlayView howToPlayView = new HowToPlayView(root, howToPlayModel);
		HowToPlayController howToPlayController = new HowToPlayController(howToPlayView);
		howToPlayController.hide();

		PlayController playController = new PlayController(playModel, playView);
		HomeController homeController = new HomeController(homeModel, homeView, playView, playController, howToPlayController);
		homeView.menuBar.getChildren().add(playView.pauseBtn);
		root.getChildren().addAll(homeView.menuBar, homeView.playBtn, homeView.newGameBtn, homeView.quitBtn);

		MainCharacterManager.getInstance(playView.gc, root);
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, e -> {
			MainCharacterManager.getInstance().setInput(e.getCode().toString(), true);
			if (e.getCode() == javafx.scene.input.KeyCode.SPACE) {
				e.consume();
			}
			if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE && playView.canvas.isVisible()) {
				playController.togglePause();
			}
		});
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, e -> {
			MainCharacterManager.getInstance().setInput(e.getCode().toString(), false);
			if (e.getCode() == javafx.scene.input.KeyCode.SPACE) {
				e.consume();
			}
		});
		root.setOnMouseClicked(e -> root.requestFocus());
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
