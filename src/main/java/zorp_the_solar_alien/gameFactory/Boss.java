package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zorp_the_solar_alien.GameObject;

public class Boss extends GameObject {
    private boolean active = true;
    private int hp;
    private int maxHp;
    private double speed = 1.2;
    private static final int DRAW_SIZE = 120;

    private Image sprite;
    private double sinePhase;

    private long lastShotTime = 0;
    private static final long SHOOT_COOLDOWN_NS = 1_200_000_000L;

    private double targetX, targetY;

    private String bossName;

    private static final String[] BOSS_NAMES = {
            "Solaris the Scorcher",
            "Mercurio the Swift",
            "Venoma the Toxic",
            "Captain Carrot",
            "Rusty the Red",
            "Big Jupe",
            "Ringo the Ringed",
            "Frostbite",
            "Stormy the Wind King"
    };

    private static final String[] BOSS_SPRITES = {
            "/enemies/boss/boss1.png",
            "/enemies/boss/boss2.png",
            "/enemies/boss/boss3.png",
            "/enemies/boss/boss4.png",
            "/enemies/boss/boss5.png",
            "/enemies/boss/boss6.png",
            "/enemies/boss/boss8.png",
            "/enemies/boss/boss9.png",
            "/enemies/boss/boss10.png"
    };

    private static final int TOTAL_AMMO = 50;
    private static Image[] ammoImages = null;

    public Boss(GraphicsContext gc, double x, double y, int planetIndex) {
        super(gc, x, y);
        planetIndex = Math.min(planetIndex, BOSS_SPRITES.length - 1);
        bossName = BOSS_NAMES[planetIndex];
        sprite = new Image(getClass().getResource(BOSS_SPRITES[planetIndex]).toExternalForm());
        if (ammoImages == null) {
            ammoImages = new Image[TOTAL_AMMO];
            for (int i = 0; i < TOTAL_AMMO; i++) {
                ammoImages[i] = new Image(getClass().getResource("/enemies/boss/boss-bullets/ammo" + (i + 1) + ".png").toExternalForm());
            }
        }
        sinePhase = Math.random() * Math.PI * 2;
        targetX = x;
        targetY = y;
        maxHp = 200 + (planetIndex * 100);
        hp = maxHp;
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

    public Image getAmmoImage() {
        return ammoImages[(int) (Math.random() * TOTAL_AMMO)];
    }

    public boolean isActive() { return active; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return DRAW_SIZE; }
    public double getHeight() { return DRAW_SIZE; }
    public double getCenterX() { return x + DRAW_SIZE / 2.0; }
    public double getCenterY() { return y + DRAW_SIZE / 2.0; }
}
