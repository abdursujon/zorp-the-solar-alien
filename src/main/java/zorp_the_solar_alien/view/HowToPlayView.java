package zorp_the_solar_alien.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;


/**
 * Support the MVC pattern by separating how to play view from model and controller.
 * It renders the helping screen guiding user how to play the game and
 * what are key movements.
 */
public class HowToPlayView {
   
	Pane root;
    Canvas canvas;
    GraphicsContext gc;
    Image backgroundView;
    private String title;
    private String story;
    private String objectivesTitle;
    private String objectives;
    private String controlsTitle;
    private String[][] controls;


    /**
     * Handles how to play view with a canvas bound to the window size of the screen.
     * It renders the background image and redraws the view when user resize the window.
     */
    public HowToPlayView(Pane root) {
        this.root = root;

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvas.widthProperty().addListener((obs, o, n) -> updateView());
        canvas.heightProperty().addListener((obs, o, n) -> updateView());

        backgroundView = new Image(getClass().getResource("/zorp_the_solar_alien/assets/home/solar-system.png").toExternalForm(), 1920, 1080, true, true);

        root.getChildren().add(canvas);
    }


    /**
     * Sets all the text data needed to render how to play view screen.
     */
    public void setData(String title, String story, String objectivesTitle, String objectives, String controlsTitle, String[][] controls) {
        this.title = title;
        this.story = story;
        this.objectivesTitle = objectivesTitle;
        this.objectives = objectives;
        this.controlsTitle = controlsTitle;
        this.controls = controls;
    }

    /**
     * Draws the how to play view with the background image, a dark overlay box for text content and
     * renders the story, objectives and controls section from playModel data.
     */
    public void updateView() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);
        gc.drawImage(backgroundView, 0, 0, width, height);

        gc.setFill(javafx.scene.paint.Color.web("#091413"));
        gc.fillRect(width * 0.1, height * 0.1, width * 0.8, height * 0.85);

        gc.setFill(javafx.scene.paint.Color.WHITE);

        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText(title, width * 0.15, height * 0.2);
        gc.setFont(javafx.scene.text.Font.font("Arial", 18));
        double nextY = drawWrappedText(gc, story, width * 0.15, height * 0.25, width * 0.7, 25);

        nextY += 50;
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText(objectivesTitle, width * 0.15, nextY);
        gc.setFont(javafx.scene.text.Font.font("Arial", 18));
        nextY = drawWrappedText(gc, objectives, width * 0.15, nextY + 30, width * 0.7, 25);

        nextY += 50;
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 28));
        gc.fillText(controlsTitle, width * 0.15, nextY);

        gc.setFont(javafx.scene.text.Font.font("Monospaced", 18));
        nextY += 40;

        for (String[] control : controls) {
            gc.fillText(String.format("%-13s-  %s", control[0], control[1]), width * 0.15, nextY);
            nextY += 30;
        }
    }


    /**
     * Since JavaFx has no wrapping utility which wrap text automatically,
     * we use this method to wrap text properly instead of rendering all text in one line.
     * If the line of text exceeds the width limit, if so it wraps the line and
     * pushes it to next line. It returns the y position after last line so the next section
     * knows where to start.
     */
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


    public void showHowToPlayView() {
        canvas.setVisible(true);
        updateView();
    }


    public void hideHowToPlayView() {
        canvas.setVisible(false);
    }
    
}
