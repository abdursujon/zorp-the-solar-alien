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
 * The class is designed to support model view controller (MVC) pattern.
 * It works as a controller file which establish communication between
 * home model and home view.
 */
public class HomeController {
	private HomeModel model;
	private HomeView view;
	private PlayView playView;
	private PlayController playController;
	private HowToPlayController howToPlayController;


	/**
	 * Through using different classes from the project, the constructor handle button set up, initialises the home screen with text animation and music.
	 * When player already in a play through but wants to start a fresh game, it creates an alert to ask
	 * if user is sure about their decision.
	 */
	public HomeController(HomeModel model, HomeView view, PlayView playView, PlayController playController, HowToPlayController howToPlayController) {
		this.model = model;
		this.view = view;
		this.playView = playView;
		this.playController = playController;
		this.howToPlayController = howToPlayController;

		view.setData(model.getGameTitle(), model.getGameSubtitle());
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


	private void syncMusicBtn() {
		view.musicBtn.setText(AudioManager.getInstance().isPlaying() ? "🔊" : "🔇");
	}


	private void handleHowToPlay() {
		view.stopAnimation();
		playView.hide();
		howToPlayController.show();
		view.zorpImageView.setVisible(false);
	}


	private void handleQuit() {
		System.exit(0);
	}
}
