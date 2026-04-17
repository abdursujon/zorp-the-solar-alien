package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import zorp_the_solar_alien.GameObject;

/**
 * As required, Enemy class also extends GameObject base class to support the factory design pattern.
 * It handles normal enemy actions on gameplay.
 * Each enemy is spawned randomly and uses random sprite images from resource.
 * Enemy with index 5 or more has sword mechanics and rest of the enemies uses shooting mechanics.
 */
public class Enemy extends GameObject {

    private double speed;
    private boolean active = true;
    private int hp = 30;
    private static final int DRAW_SIZE = 50;
    private static Image[] sharedSprites = null;
    private static Image swordImage = null;
    private static final int TOTAL_TYPES = 10;
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

    /**
     * Creates an enemy at the given position and loads shared sprite images on the screen.
     * Each enemy gets a random sprite from 10 types, random speed between 1.5 and 3 to make the game look dynamic and fun,
     * and a random sine phase for floating movement so all enemy does not look the same.
     * Enemies with type index 5 or above are given a sword and less than index 5 enemy uses shooting mechanics.
     */
    public Enemy(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        if (sharedSprites == null) {
            sharedSprites = new Image[TOTAL_TYPES];
            for (int i = 0; i < TOTAL_TYPES; i++) {
                sharedSprites[i] = new Image(getClass().getResource("/zorp_the_solar_alien/assets/enemies/normal-enemy/enemy" + (i + 1) + ".png").toExternalForm());
            }
            swordImage = new Image(getClass().getResource("/zorp_the_solar_alien/assets/enemies/normal-enemy/enemysord.png").toExternalForm());
        }

        speed = 1.5 + Math.random() * 1.5;
        sinePhase = Math.random() * Math.PI * 2;
        int typeIndex = (int) (Math.random() * TOTAL_TYPES);
        sprite = sharedSprites[typeIndex];
        hasSword = typeIndex >= 5;
        targetX = x;
        targetY = y;
    }


    /**
     * Overrides the provided update method from GameObject to update logic form enemy.
     * Handle updating and moves the enemy toward the player, applies sine wave
     * for floating movement instead of static straight line movement,
     * and draws the enemy sprite on the canvas.
     * If enemy has sword, they stop closer to the player and swing their sword when in range.
     * Rest of the enemies uses shooting mechanics.
     */
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


    public void setChaseTarget(double tx, double ty) {
        this.targetX = tx;
        this.targetY = ty;
    }


    public boolean canShoot(long now) {
        if (hasSword) return false;
        return now - lastShotTime > SHOOT_COOLDOWN_NS;
    }


    public void markShot(long now) {
        lastShotTime = now;
    }


    public void takeDamage(int damage) {
        hp -= damage;
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


    public void setX(double x) {
        this.x = x;
    }


    public void setY(double y) {
        this.y = y;
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


    public boolean hasSword() {
        return hasSword;
    }


    /**
     * After enough time passed, it returns true when enemy can deal contact damage to the player.
     */
    public boolean canContactDamage(long now) {
        return now - lastContactDamageTime > CONTACT_DAMAGE_COOLDOWN_NS;
    }


    public void markContactDamage(long now) {
        lastContactDamageTime = now;
    }


    /**
     * Returns the contact damage amount. Sword enemies deal 15 damage, ranged enemies deal 5.
     */
    public int getContactDamage() {
        return hasSword ? 15 : 5;
    }
    
}
