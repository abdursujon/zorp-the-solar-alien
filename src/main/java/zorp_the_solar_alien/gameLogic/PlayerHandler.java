package zorp_the_solar_alien.gameLogic;

import java.util.Iterator;
import java.util.List;

import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.gameFactory.*;

/**
 * This class is delegated by PlayController to handle the main character's shooting logic
 * and bullet updates. It creates bullets using the factory pattern when the player shoots,
 * and removes bullets that go off screen.
 */
public class PlayerHandler {
    private ZorpTheSolarAlienFactory factory;
    private List<Bullet> bullets;
    private static final double PLAYER_SIZE = 80;

    private long lastBulletTime = 0;
    private static final long BULLET_COOLDOWN = 200_000_000L;

    public PlayerHandler(ZorpTheSolarAlienFactory factory, List<Bullet> bullets) {
        this.factory = factory;
        this.bullets = bullets;
    }

    /**
     * When user left click mouse, and shooting cooldown has passed,
     * it first calculated where should be the position of bullet spawn.
     * It also checks if main character is facing left or right.
     * It creates a bullet using factory pattern, and sets the direction of the
     * bullet depending on if the main character facing left or right.
     * It also handles the shooting audio through calling singleton object AudioManager when main character shoot.
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
     * This method loops through the bullet list to update state of bullets for the main character.
     * If a bullet is off-screen, it removes that object.
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
     * The method loop through all enemy bullets, and update their position on the screen, and draws them.
     * If any bullet is off screen, it removes the bullet from the list.
     * Java built in class Iterator helps us to iterate over objects of enemybullets which helps us delete object during mid loop.
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
