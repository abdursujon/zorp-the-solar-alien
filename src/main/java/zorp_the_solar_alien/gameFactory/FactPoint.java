package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zorp_the_solar_alien.GameObject;

/**
 * This class extends GameObject base class and is designed to support the factory pattern.
 * It is designed to handle a fact objective that spawns on the play screen after enemies are cleared.
 */
public class FactPoint extends GameObject {
    private boolean active = true;
    private boolean locked = true;
    private String factText;
    private int factNumber = 1;
    private static final double RADIUS = 15;
    private double bobPhase = 0;
    private double baseY;

    /**
     * This constructor creates a fact point at the given position with a locked icon by default.
     */
    public FactPoint(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        baseY = y;
    }

    /**
     * We override the provided update method from GameObject as required according to brief.
     * It applies bobbing movement to the fact point to make it standout, draws the label text above it,
     * and renders the fact circle in grew color with a lock when enemy spawn is not clear yet
     * and gold color with exclamation mark on the fact when unlocked by player through clearing enemy wave.
     */
    @Override
    public void update() {
        bobPhase += 0.05;
        y = baseY + Math.sin(bobPhase) * 5;

        String label = "Fact " + factNumber;
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(locked ? Color.GRAY : Color.GOLD);
        double labelWidth = label.length() * 8;
        gc.fillText(label, x - labelWidth / 2, y - RADIUS - 8);

        if (locked) {
            gc.setFill(Color.GRAY);
            gc.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
            gc.setFill(Color.DARKGRAY);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            gc.fillText("🔒", x - 8, y + 5);
        } else {
            gc.setFill(Color.GOLD);
            gc.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            gc.fillText("!", x - 3, y + 5);
        }
    }

    /**
     * This method sets the educational fact text on the screen when the player collects this fact point.
     */
    public void setFactText(String fact) {
        this.factText = fact;
    }

    public void setFactNumber(int number) {
        this.factNumber = number;
    }

    public String getFactText() {
        return factText;
    }

    public boolean isLocked() {
        return locked;
    }

    /**
     * Unlock fact objective to enable player to collect the objective.
     */
    public void unlock() {
        this.locked = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return RADIUS;
    }
}
