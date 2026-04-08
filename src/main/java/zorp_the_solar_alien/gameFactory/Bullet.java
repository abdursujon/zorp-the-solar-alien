package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import zorp_the_solar_alien.GameObject;

public class Bullet extends GameObject {
    private double vx, vy;
    private double speed = 8;
    private boolean active = true;
    private static final double RADIUS = 6;

    public Bullet(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        vx = speed;
        vy = 0;
    }

    public void setTarget(double targetX, double targetY) {
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 0) {
            vx = (dx / dist) * speed;
            vy = (dy / dist) * speed;
        }
    }

    @Override
    public void update() {
        x += vx;
        y += vy;
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();
        if (x < -20 || x > w + 20 || y < -20 || y > h + 20) {
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
