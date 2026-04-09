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
    private static Image swordImage = null;
    private static final int TOTAL_TYPES = 11;
    private Image sprite;
    private boolean hasSword = false;

    private long lastShotTime = 0;
    private static final long SHOOT_COOLDOWN_NS = 2_000_000_000L;

    private double sinePhase;
    private double targetX, targetY;

    private double swingAngle = 0;
    private boolean swinging = false;
    private long lastContactDamageTime = 0;
    private static final long CONTACT_DAMAGE_COOLDOWN_NS = 800_000_000L;

    public Enemy(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        if (sharedSprites == null) {
            sharedSprites = new Image[TOTAL_TYPES];
            for (int i = 0; i < 5; i++) {
                sharedSprites[i] = new Image(getClass().getResource("/enemies/enemy_" + i + ".png").toExternalForm());
            }
            for (int i = 5; i < TOTAL_TYPES; i++) {
                sharedSprites[i] = new Image(getClass().getResource("/enemies/enemy" + i + ".png").toExternalForm());
            }
            swordImage = new Image(getClass().getResource("/enemies/enemysord.png").toExternalForm());
        }
        speed = 1.5 + Math.random() * 1.5;
        sinePhase = Math.random() * Math.PI * 2;
        int typeIndex = (int) (Math.random() * TOTAL_TYPES);
        sprite = sharedSprites[typeIndex];
        hasSword = typeIndex >= 5;
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
        double stopDist = hasSword ? 45 : 70;
        if (dist > stopDist) {
            x += (dx / dist) * speed;
            y += (dy / dist) * speed;
        }

        y += Math.sin(sinePhase) * 0.5;
        sinePhase += 0.03;

        gc.drawImage(sprite, x, y, DRAW_SIZE, DRAW_SIZE);

        double distToTarget = Math.sqrt(Math.pow(targetX - getCenterX(), 2) + Math.pow(targetY - getCenterY(), 2));
        swinging = distToTarget < 80;

        if (hasSword) {
            double swordSize = 28;
            boolean facingRight = targetX > getCenterX();
            double pivotX = facingRight ? x + DRAW_SIZE - 2 : x + 2;
            double pivotY = y + DRAW_SIZE / 2.0;

            gc.save();
            gc.translate(pivotX, pivotY);
            if (!facingRight) gc.scale(-1, 1);

            if (swinging) {
                swingAngle += 0.15;
                double angle = Math.sin(swingAngle * 6) * 60;
                gc.rotate(angle);
            } else {
                swingAngle = 0;
                gc.rotate(-30);
            }

            gc.drawImage(swordImage, -4, -swordSize / 2, swordSize, swordSize);
            gc.restore();
        }
    }

    public boolean canShoot(long now) {
        if (hasSword) return false;
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

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

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

    public boolean hasSword() {
        return hasSword;
    }

    public boolean canContactDamage(long now) {
        return now - lastContactDamageTime > CONTACT_DAMAGE_COOLDOWN_NS;
    }

    public void markContactDamage(long now) {
        lastContactDamageTime = now;
    }

    public int getContactDamage() {
        return hasSword ? 15 : 5;
    }
}
