package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zorp_the_solar_alien.GameObject;

public class FactPoint extends GameObject {
    private boolean active = true;
    private String factText;
    private static final double RADIUS = 15;
    private double bobPhase = 0;
    private double baseY;

    public FactPoint(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        baseY = y;
    }

    public void setFactText(String fact) {
        this.factText = fact;
    }

    public String getFactText() {
        return factText;
    }

    @Override
    public void update() {
        bobPhase += 0.05;
        y = baseY + Math.sin(bobPhase) * 5;

        gc.setFill(Color.GOLD);
        gc.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText("!", x - 3, y + 5);
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
