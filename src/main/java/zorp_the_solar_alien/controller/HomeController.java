package zorp_the_solar_alien.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.SingletonObjects.ScoreManager;
import zorp_the_solar_alien.model.HomeModel;
import zorp_the_solar_alien.view.HomeView;
import zorp_the_solar_alien.view.PlayView;


/**
 * This controller class is designed to support MVC pattern.
 * This is a controller for the home scree, it handles navigation between different
 * screen such as how to play, play and home.
 * It also manages some actions such as starting the game, starting a new game, continue from saved game
 * and quiting the game by clicking on the quit button.
 */
public class HomeController {
	private HomeModel model;
	private HomeView view;
	private PlayView playView;
	private PlayController playController;
	private HowToPlayController howToPlayController;


	/**
	 * Through using different classes from the project, this constructor handle button set up.
	 * It also initialises the home screen with text animation and music.
	 * When player already in a play through but wants to start a fresh game, this controller creates an alert to ask for
	 * if user is sure about their decision.
	 */
	public HomeController(HomeModel model, HomeView view, PlayView playView, PlayController playController, HowToPlayController howToPlayController) {
		this.model = model;
		this.view = view;
		this.playView = playView;
		this.playController = playController;
		this.howToPlayController = howToPlayController;

		view.updateHomeView();
		view.startAnimation();
		updatePlayButtonText();
		AudioManager.getInstance().playHomeMusic();

		view.homeBtn.setOnAction(e -> {
			playView.hide();
			howToPlayController.hide();
			MainCharacterManager.getInstance().setVisible(false);
			view.canvas.setVisible(true);
			view.zorpImageView.setVisible(true);
			view.startAnimation();
			updatePlayButtonText();
			syncMusicBtn();
		});

		view.howToPlayBtn.setOnAction(e -> handleHowToPlay());

		view.musicBtn.setOnAction(e -> {
			AudioManager audio = AudioManager.getInstance();
			if (audio.isPlaying()) {
				audio.stop();
			} else {
				audio.playHomeMusic();
			}
			syncMusicBtn();
		});

		view.skipBtn.setOnAction(e -> {
			AudioManager.getInstance().skipTrack();
		});

		view.playBtn.setOnAction(e -> {
			view.stopAnimation();
			view.canvas.setVisible(false);
			playController.stopGame();
			playView.show();
			playController.loadSavedGame(false);
			howToPlayController.hide();
			view.zorpImageView.setVisible(false);
			MainCharacterManager.getInstance().setVisible(true);
			syncMusicBtn();
		});

		view.newGameBtn.setOnAction(e -> {
			boolean hasProgress = ScoreManager.getInstance().getHighestLevelUnlocked() > 0
					|| ScoreManager.getInstance().getHighScore() > 0
					|| playView.canvas.isVisible();
			if (hasProgress) {
				Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
				confirm.setTitle("Start New Game");
				confirm.setHeaderText("Are you sure?");
				confirm.setContentText("All your progress and scores will be lost!");
				confirm.showAndWait().ifPresent(response -> {
					if (response == ButtonType.OK) {
						startNewGame();
					}
				});
			} else {
				startNewGame();
			}
		});

		view.quitBtn.setOnAction(e -> handleQuit());
	}


	/**
	 * Reset all progress and start the game from level 1.
	 */
	private void startNewGame() {
		view.stopAnimation();
		view.canvas.setVisible(false);
		ScoreManager.getInstance().resetProgress();
		playController.stopGame();
		playView.show();
		playController.loadSavedGame(true);
		howToPlayController.hide();
		view.zorpImageView.setVisible(false);
		MainCharacterManager.getInstance().setVisible(true);
		syncMusicBtn();
	}


	/**
	 * This method help us update play button.
	 * If the user already started a game, we show continue button
	 * which user can click to continue where they left of.
	 */
	private void updatePlayButtonText() {
		boolean hasSave = ScoreManager.getInstance().getHighestLevelUnlocked() > 0
				|| ScoreManager.getInstance().getSavedWave() > 0
				|| ScoreManager.getInstance().getSavedScore() > 0;
		if (hasSave) {
			view.playBtn.setText("CONTINUE");
			view.playBtn.setVisible(true);
			view.newGameBtn.setText("START NEW GAME");
		} else {
			view.playBtn.setVisible(false);
			view.newGameBtn.setText("START NEW GAME");
		}
	}


	/**
	 * This helps us to change icon for the music on and off button.
	 */
	private void syncMusicBtn() {
		view.musicBtn.setText(AudioManager.getInstance().isPlaying() ? "🔊" : "🔇");
	}


	/**
	 * When user is on home, or play screen, if they click on how to play button, this method
	 * switches to how to play view by calling how to play controller and hide other screens.
	 */
	private void handleHowToPlay() {
		view.stopAnimation();
		playView.hide();
		howToPlayController.show();
		view.zorpImageView.setVisible(false);
	}


	/**
	 * This method help us exit the application.
	 */
	private void handleQuit() {
		System.exit(0);
	}
}
