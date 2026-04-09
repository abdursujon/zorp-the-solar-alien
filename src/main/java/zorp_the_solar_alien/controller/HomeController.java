package zorp_the_solar_alien.controller;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import zorp_the_solar_alien.SingletonObject.AudioManager;
import zorp_the_solar_alien.SingletonObject.MainCharacterManager;
import zorp_the_solar_alien.SingletonObject.ScoreManager;
import zorp_the_solar_alien.model.HomeModel;
import zorp_the_solar_alien.view.HomeView;
import zorp_the_solar_alien.view.PlayView;

public class HomeController {
	private HomeModel model;
	private HomeView view;
	private PlayView playView;
	private PlayController playController;
	private HowToPlayController howToPlayController;

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
			playController.stopGame();
			playController.loadSavedGame(false);
			playView.show();
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

	private void startNewGame() {
		view.stopAnimation();
		ScoreManager.getInstance().resetProgress();
		playController.stopGame();
		playController.loadSavedGame(true);
		playView.show();
		howToPlayController.hide();
		view.zorpImageView.setVisible(false);
		MainCharacterManager.getInstance().setVisible(true);
		syncMusicBtn();
	}

	private void updatePlayButtonText() {
		boolean hasSave = ScoreManager.getInstance().getHighestLevelUnlocked() > 0;
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
