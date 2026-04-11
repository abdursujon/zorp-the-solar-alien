package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import zorp_the_solar_alien.GameObject;

/**
 * This Bullet class is designed to support factory pattern.
 * It handles all bullet types in the game: zorp, enemy, and boss.
 * The bullet travels in a straight line towards the target and when off-screen it gets deactivated.
 * Depending on the type, the bullet loads a different image and speed.
 */
public class Bullet extends GameObject {

    private double vx, vy;
    private double speed;
    private boolean active = true;
    private static final double RADIUS = 6;
    private Image ammoImage;
    private double drawSize;
    private String type;
    private static Image zorpBulletImage = null;
    private static final int TOTAL_ENEMY_BULLETS = 15;
    private static Image[] enemyBulletImages = null;
    private static final int TOTAL_BOSS_BULLETS = 50;
    private static Image[] bossBulletImages = null;

    /**
     * Bullet constructor helps us create bullet at the given position with a type such as for zorp, boss or normal enemy
     * that determines it's image and speed.
     * Main character bullet is fast and use a single image on the other hand normal enemy uses
     * random bullets from normal enemy bullets directory.
     * Boos bullets are the most random one which selects random bullets from 50 image from the boss-bullet directory..
     */
    public Bullet(GraphicsContext gc, double x, double y, String type) {
        super(gc, x, y);
        this.type = type;

        switch (type) {
            case "zorp":
                speed = 8;
                drawSize = 20;

                if (zorpBulletImage == null) {
                    zorpBulletImage = new Image(getClass().getResource("/zorp/zorp-bullet.png").toExternalForm());
                }
                ammoImage = zorpBulletImage;
                break;

            case "enemy":
                speed = 4;
                drawSize = 40;

                if (enemyBulletImages == null) {
                    enemyBulletImages = new Image[TOTAL_ENEMY_BULLETS];
                    for (int i = 0; i < TOTAL_ENEMY_BULLETS; i++) {
                        enemyBulletImages[i] = new Image(getClass().getResource("/enemies/normal-enemy/normal-enemy-bullets/bullet" + (i + 1) + ".png").toExternalForm());
                    }
                }
                ammoImage = enemyBulletImages[(int)(Math.random() * TOTAL_ENEMY_BULLETS)];
                break;

            case "boss":
                speed = 4;
                drawSize = 40;

                if (bossBulletImages == null) {
                    bossBulletImages = new Image[TOTAL_BOSS_BULLETS];
                    for (int i = 0; i < TOTAL_BOSS_BULLETS; i++) {
                        bossBulletImages[i] = new Image(getClass().getResource("/enemies/boss/boss-bullets/ammo" + (i + 1) + ".png").toExternalForm());
                    }
                }
                ammoImage = bossBulletImages[(int)(Math.random() * TOTAL_BOSS_BULLETS)];
                break;
        }

        vx = speed;
        vy = 0;
    }


    /**
     * This method override the method provided by GameObject class.
     * It moves the bullet by its velocity, when off-screen it deactivates the bullet.
     * It also handles drawing bullet image.
     */
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
            gc.drawImage(ammoImage, x - drawSize / 2, y - drawSize / 2, drawSize, drawSize);
        } else {
            gc.setFill(Color.RED);
            gc.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
        }
    }


    /**
     * Based on the target position, this method calculate bullet's velocity direction.
     */
    public void setTarget(double targetX, double targetY) {
        double distanceX = targetX - x;
        double distanceY = targetY - y;
        double dist = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (dist > 0) {
            vx = (distanceX / dist) * speed;
            vy = (distanceY / dist) * speed;
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
