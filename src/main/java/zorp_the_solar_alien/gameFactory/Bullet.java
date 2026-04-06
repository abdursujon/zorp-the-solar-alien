package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import zorp_the_solar_alien.GameObject;

public class Bullet extends GameObject {
    private double speed = 8;
    private boolean active = true;
    private static final double RADIUS = 6;

    public Bullet(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
    }

    @Override
    public void update() {
        x += speed;
        if (x > gc.getCanvas().getWidth()) {
            active = false;
        }
        gc.setFill(Color.ORANGE);
        gc.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
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
