package zorp_the_solar_alien.controller;

import javafx.animation.AnimationTimer;
import zorp_the_solar_alien.model.HomeModel;
import zorp_the_solar_alien.view.HomeView;

public class HomeController {
	private HomeModel model;
	private HomeView view;

	public HomeController(HomeModel model, HomeView view) {
		this.model = model;
		this.view = view;

		view.updateHomeView();
		
		view.homeBtn.setOnAction(e -> handleHome());
		view.settingsBtn.setOnAction(e -> handleSettings());
		view.howToPlayBtn.setOnAction(e -> handleHowToPlay());
		view.playBtn.setOnAction(e -> handlePlay());
		view.quitBtn.setOnAction(e -> handleQuit());
	}
	
	// Starts in home screen  
	private void handleHome() {
		
	}

    
	// Switch to settings mode 
	private void handleSettings() {

	}
	
	
	// Switch the screen display to how to play screen content. 
	private void handleHowToPlay() {
		
	}
	

	// Switch to play screen when play button is clicked. 
	private void handlePlay() {

	}

	// switch to quite screen
	private void handleQuit() {
		System.exit(0);
	}
}
