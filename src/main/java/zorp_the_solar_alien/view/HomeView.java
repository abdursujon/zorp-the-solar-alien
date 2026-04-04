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
    Image backgroundView;
    public ImageView zorpImageView;

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

        homeBtn = new Button("HOME");
        settingsBtn = new Button("SETTINGS");
        howToPlayBtn = new Button("HELP");
        playBtn = new Button("PLAY");
        quitBtn = new Button("QUIT");

        String buttonStyle =
                "-fx-background-color: linear-gradient(to bottom, #d4923a, #a0642b, #7a4a1e);" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 30 12 30;" +
                        "-fx-cursor: hand;" +
                        "-fx-shape: 'M 5,0 L 95,2 Q 100,1 100,5 L 98,95 Q 99,100 95,100 L 3,98 Q 0,99 0,95 L 2,5 Q 1,0 5,0 Z';" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 4, 0, 2, 2);";

        homeBtn.setStyle(buttonStyle);
        settingsBtn.setStyle(buttonStyle);
        howToPlayBtn.setStyle(buttonStyle);
        playBtn.setStyle(buttonStyle);
        quitBtn.setStyle(buttonStyle);
        playBtn.setStyle(buttonStyle.replace("linear-gradient(to bottom, #d4923a, #a0642b, #7a4a1e)", "#34A853")
                .replace("-fx-text-fill: black;", "-fx-text-fill: white;")
                .replace("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 4, 0, 2, 2);", ""));


        // HBox allows us to draw the menu items like a website navbar.
        menuBar = new HBox(20, homeBtn, settingsBtn, howToPlayBtn);
        menuBar.setLayoutX(10);
        menuBar.setLayoutY(10);

        playBtn.layoutXProperty().bind(root.widthProperty().divide(2).subtract(playBtn.widthProperty().divide(2)));
        playBtn.setLayoutY(10);

        quitBtn.layoutXProperty().bind(root.widthProperty().subtract(quitBtn.widthProperty()).subtract(10));
        quitBtn.setLayoutY(10);

        backgroundView = new Image(getClass().getResource("/assets/home/solar-system.png").toExternalForm(), 1920, 1080, true, true);

        zorpAnimation = new Image(getClass().getResource("/assets/home/zorp-sequence.gif").toExternalForm());
        zorpImageView = new ImageView(zorpAnimation);
        zorpImageView.setFitWidth(400);
        zorpImageView.setFitHeight(340);
        zorpImageView.setPreserveRatio(true);
        zorpImageView.setSmooth(true);

        root.getChildren().addAll(canvas, menuBar, playBtn, quitBtn, zorpImageView);
    }


    /**
     *
     */
    public void updateHomeView() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.clearRect(0, 0, width, height);
        gc.drawImage(backgroundView, 0, 0, width, height);

        zorpImageView.setLayoutX((width - zorpImageView.getFitWidth()) / 2);
        zorpImageView.setLayoutY((height - zorpImageView.getFitHeight()) / 2);
    }

}
