package zorp_the_solar_alien.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import zorp_the_solar_alien.model.HomeModel;

/**
 * This view class is designed to support home view of the game. It displays
 * different links such as how to play, play, settings etc. The class is also
 * designed to show infinite loop animation of the main character on homeScreen.
 */
public class HomeView {
	Pane root;
	HomeModel model;
	Canvas canvas;
	GraphicsContext gc;
	ImageView backgroundView;
	ImageView zorpImageView;

	public Button homeBtn;
	public Button settingsBtn;
	public Button howToPlayBtn;
	public Button playBtn;
	public Button quitBtn;
	public HBox menuBar;

	Image zorpAnimation;

	// Constructor
	public HomeView(Pane root, HomeModel model) {
		this.root = root;
		this.model = model;

		canvas = new Canvas();
		gc = canvas.getGraphicsContext2D();

		// Canvas responsive based on the window size of the screen.
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());

		// Redraw the canvas when widow resized by user.
		canvas.widthProperty().addListener((obs, oldValue, newValue) -> updateHomeView());
		canvas.heightProperty().addListener((obs, oldValue, newValue) -> updateHomeView());

		homeBtn = new Button("Home");
		settingsBtn = new Button("Settings");
		howToPlayBtn = new Button("Help");
		playBtn = new Button("Play");
		quitBtn = new Button("Quit");

		
		// HBox allows us to draw the menu items like a website navbar.
		menuBar = new HBox(20, homeBtn, settingsBtn, howToPlayBtn, playBtn, quitBtn);
		menuBar.setStyle("-fx-font-size: 24px;");
		menuBar.setLayoutX(10);
		menuBar.setLayoutY(10);

		Image bgImage = new Image(getClass().getResource("/assets/home/solar-system.gif").toExternalForm());
		backgroundView = new ImageView(bgImage);
		backgroundView.fitWidthProperty().bind(root.widthProperty());
		backgroundView.fitHeightProperty().bind(root.heightProperty());

		zorpAnimation = new Image(getClass().getResource("/assets/home/zorp-sequence.gif").toExternalForm());
		zorpImageView = new ImageView(zorpAnimation);
		zorpImageView.setFitWidth(1000);
		zorpImageView.setFitHeight(1000);

		root.getChildren().addAll(backgroundView, canvas, menuBar, zorpImageView);

	}

	/**
	 *
	 */
	public void updateHomeView() {
		double width = canvas.getWidth();
		double height = canvas.getHeight();

		gc.clearRect(0, 0, width, height);
		double zorpSize = 400;
		zorpImageView.setLayoutX((width - zorpImageView.getFitWidth()) / 2);
		zorpImageView.setLayoutY((height - zorpImageView.getFitHeight()) / 2);
	}

}
