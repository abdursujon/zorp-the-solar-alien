package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import zorp_the_solar_alien.GameObject;

public class Enemy extends GameObject {
    private double speed;
    private boolean active = true;
    private int hp = 2;
    private static final int DRAW_SIZE = 50;

    private static Image[] sharedSprites = null;
    private static final int TOTAL_TYPES = 5;
    private Image sprite;

    private long lastShotTime = 0;
    private static final long SHOOT_COOLDOWN_NS = 2_000_000_000L;

    private double sinePhase;
    private double targetX, targetY;

    public Enemy(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        if (sharedSprites == null) {
            sharedSprites = new Image[TOTAL_TYPES];
            for (int i = 0; i < TOTAL_TYPES; i++) {
                sharedSprites[i] = new Image(getClass().getResource("/enemies/enemy_" + i + ".png").toExternalForm());
            }
        }
        speed = 1.5 + Math.random() * 1.5;
        sinePhase = Math.random() * Math.PI * 2;
        sprite = sharedSprites[(int) (Math.random() * TOTAL_TYPES)];
        targetX = x;
        targetY = y;
    }

    public void setChaseTarget(double tx, double ty) {
        this.targetX = tx;
        this.targetY = ty;
    }

    @Override
    public void update() {
        double dx = targetX - getCenterX();
        double dy = targetY - getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 5) {
            x += (dx / dist) * speed;
            y += (dy / dist) * speed;
        }

        y += Math.sin(sinePhase) * 0.5;
        sinePhase += 0.03;

        gc.drawImage(sprite, x, y, DRAW_SIZE, DRAW_SIZE);
    }

    public boolean canShoot(long now) {
        return now - lastShotTime > SHOOT_COOLDOWN_NS;
    }

    public void markShot(long now) {
        lastShotTime = now;
    }

    public void takeDamage() {
        hp--;
        if (hp <= 0) active = false;
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

    public double getWidth() {
        return DRAW_SIZE;
    }

    public double getHeight() {
        return DRAW_SIZE;
    }

    public double getCenterX() {
        return x + DRAW_SIZE / 2.0;
    }

    public double getCenterY() {
        return y + DRAW_SIZE / 2.0;
    }
}
