package zorp_the_solar_alien.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class PlayView {
    public Pane root;
    public Canvas canvas;
    public GraphicsContext gc;

    public PlayView(Pane root) {
        this.root = root;

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        root.getChildren().add(canvas);
    }

    public void show() {
        canvas.setVisible(true);
    }

    public void hide() {
        canvas.setVisible(false);
    }
}