package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import zorp_the_solar_alien.GameObject;

public class Bullet extends GameObject {
    private double vx, vy;
    private double speed = 8;
    private boolean active = true;
    private static final double RADIUS = 6;
    private static final double DRAW_SIZE = 20;
    private static Image bulletImage = null;

    public Bullet(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        vx = speed;
        vy = 0;
        if (bulletImage == null) {
            bulletImage = new Image(getClass().getResource("/zorp/zorp-bullet.png").toExternalForm());
        }
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
        gc.drawImage(bulletImage, x - DRAW_SIZE / 2, y - DRAW_SIZE / 2, DRAW_SIZE, DRAW_SIZE);
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
