package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import zorp_the_solar_alien.GameObject;

public class EnemyBullet extends GameObject {
    private double vx, vy;
    private double speed = 4;
    private boolean active = true;
    private static final double RADIUS = 5;
    private Image ammoImage = null;
    private static final int TOTAL_BULLETS = 15;
    private static Image[] enemyBulletImages = null;

    public EnemyBullet(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        if (enemyBulletImages == null) {
            enemyBulletImages = new Image[TOTAL_BULLETS];
            for (int i = 0; i < TOTAL_BULLETS; i++) {
                enemyBulletImages[i] = new Image(getClass().getResource("/enemies/normal-enemy/normal-enemy-bullets/bullet" + (i + 1) + ".png").toExternalForm());
            }
        }
        ammoImage = enemyBulletImages[(int)(Math.random() * TOTAL_BULLETS)];
    }

    public void setAmmoImage(Image image) {
        this.ammoImage = image;
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
        if (ammoImage != null) {
            double size = RADIUS * 8;
            gc.drawImage(ammoImage, x - size / 2, y - size / 2, size, size);
        } else {
            gc.setFill(Color.RED);
            gc.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
        }
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
