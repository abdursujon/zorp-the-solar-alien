package zorp_the_solar_alien.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import zorp_the_solar_alien.model.HowToPlayModel;

public class HowToPlayView {
    Pane root;
    Canvas canvas;
    GraphicsContext gc;
    Image backgroundView;
    HowToPlayModel model;

    public HowToPlayView(Pane root, HowToPlayModel model) {
        this.root = root;
        this.model = model;

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvas.widthProperty().addListener((obs, o, n) -> updateView());
        canvas.heightProperty().addListener((obs, o, n) -> updateView());

        backgroundView = new Image(getClass().getResource("/home/solar-system.png").toExternalForm(), 1920, 1080, true, true);

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
        gc.fillText(model.getTitle(), width * 0.15, height * 0.2);
        gc.setFont(javafx.scene.text.Font.font("Arial", 18));
        double nextY = drawWrappedText(gc, model.getStory(), width * 0.15, height * 0.25, width * 0.7, 25);

        nextY += 50;
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText(model.getObjectivesTitle(), width * 0.15, nextY);
        gc.setFont(javafx.scene.text.Font.font("Arial", 18));
        nextY = drawWrappedText(gc, model.getObjectives(), width * 0.15, nextY + 30, width * 0.7, 25);

        nextY += 50;
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText(model.getControlsTitle(), width * 0.15, nextY);

        gc.setFont(javafx.scene.text.Font.font("Monospaced", 18));
        nextY += 40;
        for (String[] control : model.getControls()) {
            gc.fillText(String.format("%-13s-  %s", control[0], control[1]), width * 0.15, nextY);
            nextY += 30;
        }
    }

    public void showHowToPlayView() {
        canvas.setVisible(true);
    }

    public void hideHowToPlayView() {
        canvas.setVisible(false);
    }
}
