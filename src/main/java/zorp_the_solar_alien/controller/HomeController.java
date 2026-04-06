package zorp_the_solar_alien.controller;

import zorp_the_solar_alien.gameFactory.AudioManager;
import zorp_the_solar_alien.model.HomeModel;
import zorp_the_solar_alien.view.HomeView;
import zorp_the_solar_alien.view.PlayView;
import zorp_the_solar_alien.view.HowToPlayView;

public class HomeController {
	private HomeModel model;
	private HomeView view;
	private PlayView playView;
	private HowToPlayView howToPlayView;

	public HomeController(HomeModel model, HomeView view, PlayView playView, HowToPlayView howToPlayView) {
		this.model = model;
		this.view = view;
		this.playView = playView;
		this.howToPlayView = howToPlayView;

		view.updateHomeView();
		AudioManager.getInstance().playHomeMusic();

		view.homeBtn.setOnAction(e -> {
			playView.hide();
			howToPlayView.hideHowToPlayView();
			view.zorpImageView.setVisible(true);
			view.updateHomeView();
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

		view.playBtn.setOnAction(e -> {
			playView.show();
			howToPlayView.hideHowToPlayView();
			view.zorpImageView.setVisible(false);
			syncMusicBtn();
		});

		view.quitBtn.setOnAction(e -> handleQuit());
	}
	
	// Starts in home screen  
	private void handleHome() {
		
	}

	private void syncMusicBtn() {
		view.musicBtn.setText(AudioManager.getInstance().isPlaying() ? "🔊" : "🔇");
	}
	
	
	// Switch the screen display to how to play screen content. 
	private void handleHowToPlay() {
		playView.hide();
		howToPlayView.showHowToPlayView();
		view.zorpImageView.setVisible(false);
	}
	

	// Switch to play screen when play button is clicked. 
	private void handlePlay() {

	}

	// Exit the game
	private void handleQuit() {
		System.exit(0);
	}
}
