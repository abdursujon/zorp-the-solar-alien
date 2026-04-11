package zorp_the_solar_alien.gameLogic;

import java.util.Iterator;
import java.util.List;
import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.gameFactory.*;

/**
 * This class handles game logic for the main character shooting and bullet updates.
 * It uses the factory pattern to create bullets, manages the shooting cooldown, and update
 * bullets status depending on if the bullet is on or off-screen.
 */
public class MainCharacterHandler {
    private ZorpTheSolarAlienFactory factory;
    private List<Bullet> bullets;
    private static final double PLAYER_SIZE = 80;
    private long lastBulletTime = 0;
    private static final long BULLET_COOLDOWN = 200_000_000L;


    public MainCharacterHandler(ZorpTheSolarAlienFactory factory, List<Bullet> bullets) {
        this.factory = factory;
        this.bullets = bullets;
    }


    /**
     * When the player left click to shoot after cooldown, it creates a bullet at the player position using the factory objects.
     * Depending on player position, bullet is shot either left or right.
     * When player shoots, it also handles the shooting laser sound.
     */
    public void handlePlayerShooting(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();

        if (player.isShooting() && now - lastBulletTime > BULLET_COOLDOWN) {
            double bulletStartX = player.isFacingRight() ? player.getX() + PLAYER_SIZE - 5 : player.getX() + 5;
            double bulletStartY = player.getY() + PLAYER_SIZE * 0.35;
            Bullet bullet = (Bullet) factory.createProduct("bullet", bulletStartX, bulletStartY);

            double bulletDirection = player.isFacingRight() ? 1 : -1;
            bullet.setTarget(bulletStartX + bulletDirection * 500, bulletStartY);

            bullets.add(bullet);
            lastBulletTime = now;
            AudioManager.getInstance().playLaser();
        }
    }


    /**
     * Updates the position of each of the player bullets and remove bullets that are off screen.
     */
    public void updateZorpBullets() {
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.update();
            if (!b.isActive()) bulletIterator.remove();
        }
    }


    /**
     * First it updates the position of the enemy and boss bullet. Then removes bullet that are not on screen.
     */
    public void updateBullets(List<Bullet> enemyBullets) {
        Iterator<Bullet> enemyBulletIterator = enemyBullets.iterator();

        while (enemyBulletIterator.hasNext()) {
            Bullet eb = enemyBulletIterator.next();
            eb.update();
            if (!eb.isActive()) enemyBulletIterator.remove();
        }
    }
}
