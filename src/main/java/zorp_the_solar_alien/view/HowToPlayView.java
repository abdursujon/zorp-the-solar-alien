package zorp_the_solar_alien.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class HowToPlayView {
    Pane root;
    Canvas canvas;
    GraphicsContext gc;
    Image backgroundView;

    public HowToPlayView(Pane root) {
        this.root = root;

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvas.widthProperty().addListener((obs, o, n) -> updateView());
        canvas.heightProperty().addListener((obs, o, n) -> updateView());

        backgroundView = new Image(getClass().getResource("/assets/home/solar-system.png").toExternalForm(), 1920, 1080, true, true);

        root.getChildren().add(canvas);
    }

    private double drawWrappedText(GraphicsContext gc, String text, double x, double y, double maxWidth, double lineHeight) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        javafx.scene.text.Text helper = new javafx.scene.text.Text();
        helper.setFont(gc.getFont());
        for (String word : words) {
            String testLine = line + word + " ";
            helper.setText(testLine);
            if (helper.getLayoutBounds().getWidth() > maxWidth) {
                gc.fillText(line.toString(), x, y);
                y += lineHeight;
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        gc.fillText(line.toString(), x, y);
        return y + lineHeight;
    }

    public void updateView() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);
        gc.drawImage(backgroundView, 0, 0, width, height);

        gc.setFill(javafx.scene.paint.Color.web("#091413"));
        gc.fillRect(width * 0.1, height * 0.1, width * 0.8, height * 0.85);

        gc.setFill(javafx.scene.paint.Color.WHITE);

        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText("Zorp The Solar Alien", width * 0.15, height * 0.2);
        gc.setFont(javafx.scene.text.Font.font("Arial", 18));
        double nextY = drawWrappedText(gc,
                "Hello there, Zorp. You are indeed a brave alien from \"Petunsky\" " +
                        "who has managed to survive so far. You have travelled light years " +
                        "throughout the galaxy to reach the solar system. But unfortunately, " +
                        "the ship that carried you has broken down. Now, in order to get back " +
                        "to the planet you came from, you must explore the solar system, " +
                        "learn about it and find secret locations on different planets to " +
                        "gather resources to rebuild a new ship so you can go back to your " +
                        "planet. To do that, you must first learn about all solar planets " +
                        "and the sun. Beware, there will be hostile entities everywhere; " +
                        "you must fight to survive.",
                width * 0.15, height * 0.25, width * 0.7, 25);

        nextY += 50;
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText("Objectives", width * 0.15, nextY);
        gc.setFont(javafx.scene.text.Font.font("Arial", 18));
        nextY = drawWrappedText(gc, "Your mission is to explore the solar system." +
                " Gather secret location details then build a factory to launch your own rocket to " +
                "reach your home!",  width * 0.15, nextY + 30, width * 0.7, 25);

        nextY += 50;
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText("Controls", width * 0.15, nextY);

        gc.setFont(javafx.scene.text.Font.font("Monospaced", 18));
        nextY += 40;
        gc.fillText("W            -  Move Up", width * 0.15, nextY);
        nextY += 30;
        gc.fillText("S            -  Move Down", width * 0.15, nextY);
        nextY += 30;
        gc.fillText("A            -  Move Left", width * 0.15, nextY);
        nextY += 30;
        gc.fillText("D            -  Move Right", width * 0.15, nextY);
        nextY += 30;
        gc.fillText("Left Click   -  Shoot", width * 0.15, nextY);
        nextY += 30;
        gc.fillText("Right Click  -  Melee", width * 0.15, nextY);
        nextY += 30;
        gc.fillText("Space        -  Jump", width * 0.15, nextY);
    }

    public void showHowToPlayView() {
        canvas.setVisible(true);
    }

    public void hideHowToPlayView() {
        canvas.setVisible(false);
    }
}
