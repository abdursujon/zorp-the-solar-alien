package zorp_the_solar_alien.view;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import zorp_the_solar_alien.model.PlayModel;


public class PlayView {
    Pane root;
    PlayModel model;
    public Canvas canvas;
    public GraphicsContext gc;
    Image backgroundView;
    public GridPane levelGrid;
    public Button[] levelButtons;

    public PlayView(Pane root, PlayModel model) {
        this.root = root;
        this.model = model;

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvas.widthProperty().addListener((obs, o, n) -> drawBackground());
        canvas.heightProperty().addListener((obs, o, n) -> drawBackground());

        backgroundView = new Image(getClass().getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm(), 1920, 1080, true, true);

        levelGrid = new GridPane();
        levelGrid.setHgap(20);
        levelGrid.setVgap(20);
        levelGrid.setAlignment(Pos.CENTER);

        String[] names = model.getLevelNames();
        levelButtons = new Button[10];

        for (int i = 0; i < 10; i++) {
            int num = i + 1;
            String lockText = model.isLevelUnlocked(i) ? "" : "[LOCKED]";
            Label numberLabel = new Label(lockText + num);
            numberLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
            Label nameLabel = new Label(names[i]);
            nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

            StackPane card = new StackPane();
            card.setPrefSize(300, 200);
            card.setStyle(
                    "-fx-background-color: rgba(0,0,0,0.5);" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;"
            );

            javafx.scene.layout.VBox cardContent = new javafx.scene.layout.VBox(8, numberLabel, nameLabel);
            cardContent.setAlignment(Pos.CENTER);
            card.getChildren().add(cardContent);

            levelButtons[i] = new Button();
            levelButtons[i].setGraphic(card);
            levelButtons[i].setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            levelButtons[i].setDisable(!model.isLevelUnlocked(i));

            levelGrid.add(levelButtons[i], i % 5, i / 5);
        }

        // Center the grid on screen
        levelGrid.layoutXProperty().bind(root.widthProperty().divide(2).subtract(levelGrid.widthProperty().divide(2)));
        levelGrid.layoutYProperty().bind(root.heightProperty().divide(2).subtract(levelGrid.heightProperty().divide(2)));

        root.getChildren().addAll(canvas, levelGrid);
    }

    private void drawBackground() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);
        gc.drawImage(backgroundView, 0, 0, width, height);
    }

    public void show() {
        canvas.setVisible(true);
        levelGrid.setVisible(true);
    }

    public void hide() {
        canvas.setVisible(false);
        levelGrid.setVisible(false);
    }
}

