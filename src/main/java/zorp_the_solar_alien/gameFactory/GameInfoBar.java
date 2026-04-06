package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zorp_the_solar_alien.GameObject;
import zorp_the_solar_alien.model.PlayModel;

public class GameInfoBar extends GameObject {
    private PlayModel model;
    private String currentFactPopup = null;
    private int popupTimer = 0;
    private static final int POPUP_DURATION = 180;

    public GameInfoBar(GraphicsContext gc, double x, double y) {
        super(gc, 0, 0);
    }

    public void setModel(PlayModel model) {
        this.model = model;
    }

    public void showFactPopup(String factText) {
        currentFactPopup = factText;
        popupTimer = POPUP_DURATION;
    }

    @Override
    public void update() {
        if (model == null) return;
        double w = gc.getCanvas().getWidth();
        double topY = 70; // below the navbar

        // Top bar background
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, topY, w, 55);

        // HP bar
        gc.setFill(Color.DARKRED);
        gc.fillRect(10, topY + 10, 200, 20);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(10, topY + 10, 200 * (model.getHp() / (double) model.getMaxHp()), 20);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(10, topY + 10, 200, 20);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(Color.WHITE);
        gc.fillText("HP: " + model.getHp() + "/" + model.getMaxHp(), 10, topY + 48);

        // Score
        gc.fillText("Score: " + model.getScore(), w / 2 - 50, topY + 25);

        // Planet name
        gc.fillText(model.getCurrentPlanetName(), w / 2 - 30, topY + 45);

        // Facts collected
        gc.fillText("Facts: " + model.getFactsCollected() + "/12", w - 150, topY + 25);

        // Kills info
        gc.fillText("Kills: " + model.getKillCount(), w - 150, topY + 45);

        // Fact popup
        if (popupTimer > 0 && currentFactPopup != null) {
            double boxW = 500;
            double boxH = 60;
            double boxX = (w - boxW) / 2;
            double boxY = topY + 65;

            gc.setFill(Color.rgb(0, 0, 0, 0.8));
            gc.fillRect(boxX, boxY, boxW, boxH);
            gc.setStroke(Color.GOLD);
            gc.strokeRect(boxX, boxY, boxW, boxH);

            gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            gc.setFill(Color.GOLD);
            gc.fillText(currentFactPopup, boxX + 15, boxY + 35);

            popupTimer--;
            if (popupTimer == 0) {
                currentFactPopup = null;
            }
        }
    }
}
