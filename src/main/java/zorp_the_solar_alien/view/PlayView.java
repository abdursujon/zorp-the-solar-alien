package zorp_the_solar_alien.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class PlayView {
    public Pane root;
    public Canvas canvas;
    public GraphicsContext gc;
    private VBox introCard;
    private VBox factCard;
    private Button restartBtn;
    public Button pauseBtn;
    private Runnable onStartGame;
    private Runnable onDoneReading;
    private Runnable onRestart;
    private Runnable onPause;

    public PlayView(Pane root) {
        this.root = root;

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        root.getChildren().add(canvas);

        buildIntroCard();
        buildFactCard();
        buildRestartBtn();
        buildPauseBtn();
    }


    public void setOnStartGame(Runnable callback) {
        this.onStartGame = callback;
    }


    public void setOnDoneReading(Runnable callback) {
        this.onDoneReading = callback;
    }


    public void setOnRestart(Runnable callback) {
        this.onRestart = callback;
    }


    public void setOnPause(Runnable callback) {
        this.onPause = callback;
    }


    public void show() {
        canvas.setVisible(true);
        pauseBtn.setVisible(true);
        pauseBtn.toFront();
    }


    public void hide() {
        canvas.setVisible(false);
        pauseBtn.setVisible(false);
    }


    private void buildIntroCard() {
        introCard = new VBox(20);
        introCard.setAlignment(Pos.CENTER);
        introCard.setPadding(new Insets(40));
        introCard.setMaxWidth(500);
        introCard.setMaxHeight(300);
        introCard.setStyle(
                "-fx-background-color: #DC2626;" +
                "-fx-background-radius: 15;");

        Label titleLabel = new Label();
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setWrapText(true);

        Label descLabel = new Label();
        descLabel.setFont(Font.font("Arial", 18));
        descLabel.setTextFill(Color.WHITE);
        descLabel.setTextAlignment(TextAlignment.CENTER);
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(420);

        Button startBtn = new Button("START");
        startBtn.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: #DC2626;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 40;" +
                "-fx-cursor: hand;" +
                "-fx-background-radius: 8;");
        startBtn.setOnAction(e -> {
            introCard.setVisible(false);
            if (onStartGame != null) onStartGame.run();
        });

        introCard.getChildren().addAll(titleLabel, descLabel, startBtn);
        introCard.setVisible(false);
        root.getChildren().add(introCard);
    }


    public void showIntroCard(String title, String description) {
        Label titleLabel = (Label) introCard.getChildren().get(0);
        Label descLabel = (Label) introCard.getChildren().get(1);
        titleLabel.setText(title);
        descLabel.setText(description);

        introCard.setVisible(true);
        introCard.toFront();
        introCard.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            double w = root.getWidth();
            double h = root.getHeight();
            introCard.setLayoutX((w - newB.getWidth()) / 2);
            introCard.setLayoutY((h - newB.getHeight()) / 2);
        });
        javafx.application.Platform.runLater(() -> {
            double w = root.getWidth();
            double h = root.getHeight();
            introCard.setLayoutX((w - introCard.getBoundsInLocal().getWidth()) / 2);
            introCard.setLayoutY((h - introCard.getBoundsInLocal().getHeight()) / 2);
        });
    }


    private void buildFactCard() {
        factCard = new VBox(15);
        factCard.setAlignment(Pos.CENTER);
        factCard.setPadding(new Insets(30));
        factCard.setMaxWidth(550);
        factCard.setMaxHeight(350);
        factCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #DC2626;" +
                "-fx-border-width: 6;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;");

        Label factTitle = new Label();
        factTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        factTitle.setTextFill(Color.web("#DC2626"));
        factTitle.setTextAlignment(TextAlignment.CENTER);

        Label factContent = new Label();
        factContent.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        factContent.setTextFill(Color.web("#1E40AF"));
        factContent.setTextAlignment(TextAlignment.CENTER);
        factContent.setWrapText(true);
        factContent.setMaxWidth(480);

        Label separator = new Label("─────────────────────────────────");
        separator.setTextFill(Color.web("#333333"));

        Button doneBtn = new Button("Done Reading");
        doneBtn.setStyle(
                "-fx-background-color: #888888;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 30;" +
                "-fx-background-radius: 8;");
        doneBtn.setDisable(true);
        doneBtn.setOpacity(0.3);
        doneBtn.setOnAction(e -> {
            factCard.setVisible(false);
            if (onDoneReading != null) onDoneReading.run();
        });

        factCard.getChildren().addAll(factTitle, factContent, separator, doneBtn);
        factCard.setVisible(false);
        root.getChildren().add(factCard);
    }


    public void showFactCard(String factText, int factNumber) {
        Label factTitle = (Label) factCard.getChildren().get(0);
        Label factContent = (Label) factCard.getChildren().get(1);
        factTitle.setText("Fact " + factNumber);
        factContent.setText(factText);

        Button doneBtn = (Button) factCard.getChildren().get(3);
        doneBtn.setDisable(true);
        doneBtn.setOpacity(1.0);

        String baseStyle =
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 30;" +
                "-fx-background-radius: 8;";
        doneBtn.setStyle("-fx-background-color: #888888;" + baseStyle);

        int totalFrames = 300;
        Timeline fillTimer = new Timeline();

        for (int i = 0; i <= totalFrames; i++) {
            int pct = (int) ((i / (double) totalFrames) * 100);
            KeyFrame kf = new KeyFrame(javafx.util.Duration.millis(i * (10000.0 / totalFrames)), e -> {
                doneBtn.setStyle("-fx-background-color: linear-gradient(to right, #34A853 " + pct + "%, #888888 " + pct + "%);" + baseStyle);
            });
            fillTimer.getKeyFrames().add(kf);
        }

        fillTimer.setOnFinished(e -> {
            doneBtn.setDisable(false);
            doneBtn.setStyle("-fx-background-color: #34A853;" + baseStyle + "-fx-cursor: hand;");
        });
        fillTimer.play();

        factCard.setVisible(true);
        factCard.toFront();
        javafx.application.Platform.runLater(() -> {
            double w = root.getWidth();
            double h = root.getHeight();
            factCard.setLayoutX((w - factCard.getBoundsInLocal().getWidth()) / 2);
            factCard.setLayoutY((h - factCard.getBoundsInLocal().getHeight()) / 2);
        });
    }


    private void buildPauseBtn() {
        pauseBtn = new Button("PAUSE");
        pauseBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #d4923a, #a0642b, #7a4a1e);" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10;" +
                "-fx-cursor: hand;" +
                "-fx-shape: 'M 10,0 L 90,5 Q 100,2 100,10 L 95,90 Q 98,100 90,100 L 8,95 Q 0,98 0,90 L 5,10 Q 2,0 10,0 Z';" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 4, 0, 2, 2);");
        pauseBtn.setVisible(false);
        pauseBtn.setOnAction(e -> {
            if (onPause != null) onPause.run();
        });
    }


    public void setPauseText(boolean paused) {
        pauseBtn.setText(paused ? "UNPAUSE" : "PAUSE");
    }


    private void buildRestartBtn() {
        restartBtn = new Button("RESTART");
        restartBtn.setStyle(
                "-fx-background-color: #34A853;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 30;" +
                "-fx-cursor: hand;");
        restartBtn.setVisible(false);
        root.getChildren().add(restartBtn);
        restartBtn.setOnAction(e -> {
            restartBtn.setVisible(false);
            restartBtn.setText("RESTART");
            if (onRestart != null) onRestart.run();
        });
    }


    public void showGameOver(int score, int highScore) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, w, h);
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.fillText("GAME OVER", w / 2 - 150, h / 2);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Score: " + score, w / 2 - 50, h / 2 + 40);
        gc.fillText("High Score: " + highScore, w / 2 - 60, h / 2 + 70);

        restartBtn.setText("RESTART");
        restartBtn.setLayoutX(w / 2 - 80);
        restartBtn.setLayoutY(h / 2 + 90);
        restartBtn.setVisible(true);
        restartBtn.toFront();
    }


    public void showLevelComplete(String planetName, int score) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, w, h);
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.fillText("CONGRATULATIONS!", w / 2 - 230, h / 2 - 20);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 24));
        gc.fillText("You completed " + planetName + "!", w / 2 - 120, h / 2 + 30);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Score: " + score, w / 2 - 50, h / 2 + 65);

        restartBtn.setText("NEXT LEVEL");
        restartBtn.setLayoutX(w / 2 - 80);
        restartBtn.setLayoutY(h / 2 + 90);
        restartBtn.setVisible(true);
        restartBtn.toFront();
    }


    public void showGameComplete(int score) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.setFill(Color.rgb(0, 0, 0, 0.85));
        gc.fillRect(0, 0, w, h);

        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 56));
        gc.fillText("YOU BEAT THE GAME!", w / 2 - 280, h / 2 - 80);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gc.fillText("Congratulations, Zorp has explored", w / 2 - 230, h / 2 - 20);
        gc.fillText("the entire Solar System!", w / 2 - 160, h / 2 + 20);

        gc.setFill(Color.CYAN);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gc.fillText("Final Score: " + score, w / 2 - 90, h / 2 + 70);

        restartBtn.setText("PLAY AGAIN");
        restartBtn.setLayoutX(w / 2 - 80);
        restartBtn.setLayoutY(h / 2 + 100);
        restartBtn.setVisible(true);
        restartBtn.toFront();
    }
}
