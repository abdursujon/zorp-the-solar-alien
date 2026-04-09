package zorp_the_solar_alien.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zorp_the_solar_alien.model.HomeModel;

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
    public Button musicBtn;
    public Button skipBtn;
    public Button playBtn;
    public Button newGameBtn;
    public Button quitBtn;
    public HBox menuBar;
    Image zorpAnimation;

    private AnimationTimer homeLoop;
    private double bgScrollX = 0;
    private double zorpFloat = 0;
    private double titleGlow = 0;
    private static final int STAR_COUNT = 100;
    private double[] starX, starY, starSize, starPhase, starSpeed, starBrightness;

    public HomeView(Pane root, HomeModel model) {
        this.root = root;
        this.model = model;

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        homeBtn = new Button("HOME");
        howToPlayBtn = new Button("HOW TO PLAY");
        musicBtn = new Button("\uD83D\uDD0A");
        skipBtn = new Button("\u23ED");
        playBtn = new Button("PLAY");
        newGameBtn = new Button("START NEW GAME");
        quitBtn = new Button("QUIT");

        String buttonStyle =
                "-fx-background-color: linear-gradient(to bottom, #d4923a, #a0642b, #7a4a1e);" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10; " +
                        "-fx-cursor: hand;" +
                        "-fx-shape: 'M 10,0 L 90,5 Q 100,2 100,10 L 95,90 Q 98,100 90,100 L 8,95 Q 0,98 0,90 L 5,10 Q 2,0 10,0 Z';" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 4, 0, 2, 2);";

        homeBtn.setStyle(buttonStyle);
        howToPlayBtn.setStyle(buttonStyle);
        musicBtn.setStyle(buttonStyle);
        skipBtn.setStyle(buttonStyle);
        playBtn.setStyle(buttonStyle);
        newGameBtn.setStyle(buttonStyle);
        quitBtn.setStyle(buttonStyle);
        playBtn.setStyle(buttonStyle.replace("linear-gradient(to bottom, #d4923a, #a0642b, #7a4a1e)", "#34A853")
                .replace("-fx-text-fill: black;", "-fx-text-fill: white;")
                .replace("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 4, 0, 2, 2);", ""));
        newGameBtn.setStyle(buttonStyle.replace("linear-gradient(to bottom, #d4923a, #a0642b, #7a4a1e)", "#DC2626")
                .replace("-fx-text-fill: black;", "-fx-text-fill: white;")
                .replace("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 4, 0, 2, 2);", ""));

        menuBar = new HBox(20, homeBtn, howToPlayBtn, musicBtn, skipBtn);
        menuBar.setLayoutX(10);
        menuBar.setLayoutY(10);

        playBtn.layoutXProperty().bind(root.widthProperty().divide(2).subtract(playBtn.widthProperty()).subtract(10));
        playBtn.setLayoutY(10);

        newGameBtn.layoutXProperty().bind(root.widthProperty().divide(2).add(10));
        newGameBtn.setLayoutY(10);

        quitBtn.layoutXProperty().bind(root.widthProperty().subtract(quitBtn.widthProperty()).subtract(10));
        quitBtn.setLayoutY(10);

        backgroundView = new Image(getClass().getResource("/home/home.png").toExternalForm(), 1920, 1080, true, true);

        zorpAnimation = new Image(getClass().getResource("/home/zorp-sequence.gif").toExternalForm());
        zorpImageView = new ImageView(zorpAnimation);
        zorpImageView.setPreserveRatio(true);
        zorpImageView.setSmooth(true);

        starX = new double[STAR_COUNT];
        starY = new double[STAR_COUNT];
        starSize = new double[STAR_COUNT];
        starPhase = new double[STAR_COUNT];
        starSpeed = new double[STAR_COUNT];
        starBrightness = new double[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = Math.random();
            starY[i] = Math.random();
            starSize[i] = 1 + Math.random() * 3;
            starPhase[i] = Math.random() * Math.PI * 2;
            starSpeed[i] = 0.02 + Math.random() * 0.06;
            starBrightness[i] = 0.5 + Math.random() * 0.5;
        }

        root.getChildren().addAll(canvas, zorpImageView);

        homeLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                renderHome();
            }
        };
    }

    public void startAnimation() {
        homeLoop.start();
    }

    public void stopAnimation() {
        homeLoop.stop();
    }

    private void renderHome() {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        if (w <= 0 || h <= 0) return;

        gc.setFill(Color.rgb(5, 5, 20));
        gc.fillRect(0, 0, w, h);

        for (int i = 0; i < STAR_COUNT; i++) {
            starPhase[i] += starSpeed[i];
            double twinkle = starBrightness[i] * (0.5 + 0.5 * Math.sin(starPhase[i]));
            gc.setFill(Color.color(1, 1, 1, twinkle));
            gc.fillOval(starX[i] * w, starY[i] * h, starSize[i], starSize[i]);
        }

        bgScrollX += 0.15;
        double imgW = backgroundView.getWidth();
        double imgH = backgroundView.getHeight();
        double scaleX = w / imgW;
        double scaleY = h / imgH;
        double scale = Math.max(scaleX, scaleY);
        double bgW = imgW * scale;
        double bgH = imgH * scale;
        double offsetX = (w - bgW) / 2;
        double offsetY = (h - bgH) / 2;
        gc.setGlobalAlpha(0.85);
        gc.drawImage(backgroundView, offsetX, offsetY, bgW, bgH);
        gc.setGlobalAlpha(1.0);

        titleGlow += 0.03;
        double glow = 0.6 + 0.4 * Math.sin(titleGlow);
        double titleY = h * 0.82;

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 52));
        gc.setGlobalAlpha(glow * 0.4);
        gc.setFill(Color.web("#FF6B00"));
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                gc.fillText("ZORP THE SOLAR ALIEN", w / 2 - 310 + i, titleY + j);
            }
        }
        gc.setGlobalAlpha(1.0);
        gc.setFill(Color.WHITE);
        gc.fillText("ZORP THE SOLAR ALIEN", w / 2 - 310, titleY);

        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        gc.setFill(Color.color(1, 1, 1, 0.5 + 0.3 * Math.sin(titleGlow * 2)));
        gc.fillText("Explore the Solar System!", w / 2 - 100, titleY + 35);

        zorpFloat += 0.04;
        double floatOffset = Math.sin(zorpFloat) * 10;
        if (zorpImageView != null) {
            zorpImageView.setLayoutX((w - zorpImageView.getBoundsInLocal().getWidth()) / 2);
            zorpImageView.setLayoutY(h * 0.4 + floatOffset);
        }
    }

    public void updateHomeView() {
        renderHome();
    }
}
