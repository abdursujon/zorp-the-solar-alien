package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zorp_the_solar_alien.GameObject;

/**
 * Boss extends GameObject base class and implements logic to handle how boss behave in the game play.
 * Designed to support the factory pattern.
 * It provides unique boss with their name and health bar for each level.
 */
public class Boss extends GameObject {

    private boolean active = true;
    private int hp;
    private int maxHp;
    private double speed = 1.2;
    private static final int DRAW_SIZE = 120;
    private Image sprite;
    private double sinePhase;
    private long lastShotTime = 0;
    private static final long SHOOT_COOLDOWN_NS = 500_000_000L;
    private double targetX, targetY;
    private String bossName;

    private static final String[] BOSS_NAMES = {
            "Solaris The Scorcher",
            "Mercurio The Swift",
            "Venoma The Toxic",
            "Captain Carrot",
            "Rusty Mask",
            "Demon The Jupe",
            "Ringo",
            "Frostbiter",
            "Stormy King"
    };

    private static final String[] BOSS_SPRITES = {
            "/zorp_the_solar_alien/assets/enemies/boss/boss1.png",
            "/zorp_the_solar_alien/assets/enemies/boss/boss2.png",
            "/zorp_the_solar_alien/assets/enemies/boss/boss3.png",
            "/zorp_the_solar_alien/assets/enemies/boss/boss4.png",
            "/zorp_the_solar_alien/assets/enemies/boss/boss5.png",
            "/zorp_the_solar_alien/assets/enemies/boss/boss6.png",
            "/zorp_the_solar_alien/assets/enemies/boss/boss8.png",
            "/zorp_the_solar_alien/assets/enemies/boss/boss9.png",
            "/zorp_the_solar_alien/assets/enemies/boss/boss10.png"
    };


    /**
     * The constructor creates a boss for the given planet index.
     * It loads unique sprite for the boss, name.
     * Each boss spawn in a random position on the screen.
     * It initialises first boss with 200 hp then increase each boss health by 100hp for each level.
     */
    public Boss(GraphicsContext gc, double x, double y, int planetIndex) {
        super(gc, x, y);
        planetIndex = Math.min(planetIndex, BOSS_SPRITES.length - 1);
        bossName = BOSS_NAMES[planetIndex];
        sprite = new Image(getClass().getResource(BOSS_SPRITES[planetIndex]).toExternalForm());

        sinePhase = Math.random() * Math.PI * 2;
        targetX = x;
        targetY = y;
        maxHp = 200 + (planetIndex * 100);
        hp = maxHp;
    }


    /**
     * Overrides the provided update method from GameObject.
     * In this instance, it helps us move the boss toward the main character.
     * It also applies sine wave for floating movement of the boss, draws the sprite on canvas,
     * and renders the health bar on top of the boss with each boss name through using defined array BOSS_NAMES .
     */
    @Override
    public void update() {

        double dx = targetX - getCenterX();
        double dy = targetY - getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 5) {
            x += (dx / dist) * speed;
            y += (dy / dist) * speed;
        }

        y += Math.sin(sinePhase) * 0.8;
        sinePhase += 0.02;

        gc.drawImage(sprite, x, y, DRAW_SIZE, DRAW_SIZE);

        double barWidth = DRAW_SIZE;
        double barHeight = 8;
        double barX = x;
        double barY = y - 15;
        gc.setFill(Color.DARKRED);
        gc.fillRect(barX, barY, barWidth, barHeight);
        gc.setFill(Color.RED);
        gc.fillRect(barX, barY, barWidth * ((double) hp / maxHp), barHeight);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(barX, barY, barWidth, barHeight);


        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(Color.RED);
        double textWidth = bossName.length() * 7.5;
        gc.fillText(bossName, x + DRAW_SIZE / 2.0 - textWidth / 2, barY - 4);
    }


    public void setChaseTarget(double tx, double ty) {
        this.targetX = tx;
        this.targetY = ty;
    }


    public boolean canShoot(long now) {
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
