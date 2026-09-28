package zorp_the_solar_alien.gameLogic;

import java.util.Iterator;
import java.util.List;
import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.gameFactory.*;
import zorp_the_solar_alien.model.PlayModel;


/**
 * Supports collision detection logic in gameplay.
 */
public class CollisionHandler {
    private PlayModel model;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<Bullet> enemyBullets;
    private long lastMeleeTime = 0;
    private static final long MELEE_COOLDOWN = 400_000_000L;
    private static final double PLAYER_SIZE = 80;
    private Boss currentBoss;
    private FactPoint currentObjective;
    private Runnable onBossDefeated;
    private Runnable onSpawnWave;

    /**
     * The constructor creates collision handler with reference to required class that needs be used to handle collision logic.
     */
    public CollisionHandler(PlayModel model, List<Bullet> bullets, List<Enemy> enemies, List<Bullet> enemyBullets) {
        this.model = model;
        this.bullets = bullets;
        this.enemies = enemies;
        this.enemyBullets = enemyBullets;
    }


    /**
     * Check if the player has touched the unlocked fact after beating the boss.
     * If the fact is collected, it awards enemy scores, heals, and return what facts to display on the screen.
     * If level is not on last fact, it re-swan enemy wave.
     */
    public String[] checkObjectiveCollision(double px, double py) {
        if (currentObjective == null || !currentObjective.isActive() || currentObjective.isLocked()) return null;

        if (checkIfBulletOverlapsAnyCharacter(currentObjective.getX(), currentObjective.getY(), currentObjective.getRadius(),
                px, py, PLAYER_SIZE, PLAYER_SIZE)) {
            currentObjective.setActive(false);
            model.collectFact();
            model.addScore(500);
            model.heal(30);
            int factNum = model.getCurrentWave() + 1;
            String factText = currentObjective.getFactText();
            model.completeWave();

            if (!model.isLevelComplete() && onSpawnWave != null) {
                onSpawnWave.run();
            }

            return new String[]{factText, String.valueOf(factNum)};
        }
        return null;
    }


    /**
     * Prevents enemy from overlapping each other by comparing them in pairs.
     * When two enemy are too close to each other, it calculates the overlap and pushes the
     * enemy from each other in opposite direction.
     */
    public void resolveEnemySeparation() {
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy a = enemies.get(i);
                Enemy b = enemies.get(j);

                double distanceX = a.getCenterX() - b.getCenterX();
                double distanceY = a.getCenterY() - b.getCenterY();
                double dist = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                double minDist = (a.getWidth() + b.getWidth()) / 2.0;

                if (dist < minDist && dist > 0) {
                    double overlap = (minDist - dist) / 2.0;
                    double normalisedX = distanceX / dist;
                    double normalisedY = distanceY / dist;

                    a.setX(a.getX() + normalisedX * overlap);
                    a.setY(a.getY() + normalisedY * overlap);
                    b.setX(b.getX() - normalisedX * overlap);
                    b.setY(b.getY() - normalisedY * overlap);
                }
            }
        }
    }


    /**
     * Loops through main character bullets to check if any bullets hit an enemy.
     * If bullet hits enemy, it registers 10 damage, and deactivate the bullet.
     * If enemy dies, the method save the kill count and play death sound.
     */
    private void checkZorpBulletEnemyCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            for (Enemy enemy : enemies) {
                if (!enemy.isActive()) continue;

                if (checkIfBulletOverlapsAnyCharacter(bullet.getX(), bullet.getY(), bullet.getRadius(),
                        enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight())) {
                    bullet.setActive(false);
                    enemy.takeDamage(10);
                    if (!enemy.isActive()) {
                        model.registerKill();
                        AudioManager.getInstance().playEnemyDied();
                    }
                    break;
                }
            }
        }
        bullets.removeIf(b -> !b.isActive());
    }

    /**
     * Checks melee attact range by measuring if two rectenagles overlap each other by comparing their edges from each other.
     */
    private boolean rectangleOverlapToCheckMeleeDamageAndEnemyContactDamage(double attackerX, double attackerY,
                                                                            double attackerWidth, double attackerHeight,
                                                                            double targetX, double targetY,
                                                                            double targetWidth, double targetHeight) {
        return attackerX < targetX + targetWidth && attackerX + attackerWidth > targetX && attackerY < targetY +
                targetHeight && attackerY + attackerHeight > targetY;
    }


    /**
     * Handle logics for main character melee attact.
     * When player uses melee attact after cooldown has passed, it creates invisible
     * melee range box in front of the player. If any enemy is in the range of player melee attact
     * and player is using melee key, it does 15 damage to the enemy. When any enemy dies,
     * the method registers the kill and plays the death sound.
     */
    private void checkZorpMeleeAttackAndEnemyCollisions(MainCharacterManager player, double px, double py, long now) {
        if (!player.isMelee() || now - lastMeleeTime < MELEE_COOLDOWN) {
            return;
        }

        double meleeX = px + PLAYER_SIZE;
        double meleeY = py;
        double meleeW = 40;
        double meleeH = PLAYER_SIZE;
        boolean hit = false;

        for (Enemy enemy : enemies) {
            if (!enemy.isActive()) {
                continue;
            }

            if (rectangleOverlapToCheckMeleeDamageAndEnemyContactDamage(meleeX, meleeY, meleeW, meleeH,
                    enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight())) {
                enemy.takeDamage(15);
                hit = true;
                if (!enemy.isActive()) {
                    model.registerKill();
                    AudioManager.getInstance().playEnemyDied();
                }
            }
        }

        if (hit) {
            lastMeleeTime = now;
        }
    }


    /**
     * Checks if any enemy touching the main character. If enemy makes any contact with
     * the player, it registers some damage and play the damage sound.
     * It also pushes the player away from the enemy to prevent all enemy going on top of main character.
     */
    private void checkEnemyContactCollisions(MainCharacterManager player, double px, double py, long now) {
        for (Enemy enemy : enemies) {
            if (!enemy.isActive()) continue;

            if (rectangleOverlapToCheckMeleeDamageAndEnemyContactDamage(px, py, PLAYER_SIZE, PLAYER_SIZE,
                    enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight())) {

                if (enemy.canContactDamage(now)) {
                    if (model.takeDamage(enemy.getContactDamage(), now)) {
                        AudioManager.getInstance().playDamageTaken();
                    }
                    enemy.markContactDamage(now);
                }

                double pcx = px + PLAYER_SIZE / 2;
                double pcy = py + PLAYER_SIZE / 2;
                double ecx = enemy.getCenterX();
                double ecy = enemy.getCenterY();
                double pushDx = pcx - ecx;
                double pushDy = pcy - ecy;
                double pushDist = Math.sqrt(pushDx * pushDx + pushDy * pushDy);

                if (pushDist > 0) {
                    double pushStrength = 6;
                    player.setX(px + (pushDx / pushDist) * pushStrength);
                    player.setY(py + (pushDy / pushDist) * pushStrength);
                    px = player.getX();
                    py = player.getY();
                }

            }
        }
    }


    /**
     * Checks if bullets fired by zorp hit the boss. Each hit does 10 damage to the boss.
     * When boss dies, it triggers the boss defeated sequence such as fact unlocked, show fact card etc.
     */
    private void checkBulletBossCollisions() {
        if (currentBoss == null || !currentBoss.isActive()) return;

        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            if (checkIfBulletOverlapsAnyCharacter(b.getX(), b.getY(), b.getRadius(),
                    currentBoss.getX(), currentBoss.getY(), currentBoss.getWidth(), currentBoss.getHeight())) {
                b.setActive(false);
                currentBoss.takeDamage(10);
                if (!currentBoss.isActive()) {
                    if (onBossDefeated != null) onBossDefeated.run();
                    break;
                }
            }
        }
        bullets.removeIf(b -> !b.isActive());
    }


    /**
     * Checks if the main character melee attack hits the boss and handles logics to manage damage.
     */
    private void checkMeleeBossCollisions(MainCharacterManager player, double px, double py, long now) {
        if (currentBoss == null || !currentBoss.isActive() || !player.isMelee()) return;

        if (now - lastMeleeTime < MELEE_COOLDOWN) return;

        double meleeX = px + PLAYER_SIZE;
        double meleeY = py;
        double meleeW = 40;
        double meleeH = PLAYER_SIZE;

        if (rectangleOverlapToCheckMeleeDamageAndEnemyContactDamage(meleeX, meleeY, meleeW, meleeH,
                currentBoss.getX(), currentBoss.getY(), currentBoss.getWidth(), currentBoss.getHeight())) {
            currentBoss.takeDamage(15);
            lastMeleeTime = now;
            if (!currentBoss.isActive()) {
                if (onBossDefeated != null) onBossDefeated.run();
            }
        }
    }


    /**
     * Checks if an enemy or boss bullets hits the player. For each hit, the
     * player takes 10 damage, and plays the damage sound. It also removed the hit bullets from the screen.
     */
    private void checkEnemyBulletPlayerCollisions(double px, double py, long now) {
        Iterator<Bullet> enemyBulletIterator = enemyBullets.iterator();

        while (enemyBulletIterator.hasNext()) {
            Bullet eb = enemyBulletIterator.next();
            if (checkIfBulletOverlapsAnyCharacter(eb.getX(), eb.getY(), eb.getRadius(),
                    px, py, PLAYER_SIZE, PLAYER_SIZE)) {
                eb.setActive(false);
                if (model.takeDamage(10, now)) {
                    AudioManager.getInstance().playDamageTaken();
                }
            }
        }
        enemyBullets.removeIf(eb -> !eb.isActive());
    }

    /**
     * Checks if bullet overlaps any rectangular target by finding the closest point on the target.
     */
    private boolean checkIfBulletOverlapsAnyCharacter(double bulletX, double bulletY, double bulletRadius,
                                                      double targetX, double targetY, double targetWidth, double targetHeight) {

        double closestX = Math.max(targetX, Math.min(bulletX, targetX + targetWidth));
        double closestY = Math.max(targetY, Math.min(bulletY, targetY + targetHeight));
        double distanceX = bulletX - closestX;
        double distanceY = bulletY - closestY;

        return (distanceX * distanceX + distanceY * distanceY) <= (bulletRadius * bulletRadius);
    }



    /**
     * Gets the player position then runs all the collision check through using different collision methods.
     */
    public void checkCollisions(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();
        double playerXPosition = player.getX();
        double playerYPosition = player.getY();

        checkZorpBulletEnemyCollisions();
        checkZorpMeleeAttackAndEnemyCollisions(player, playerXPosition, playerYPosition, now);
        checkEnemyContactCollisions(player, playerXPosition, playerYPosition, now);
        checkBulletBossCollisions();
        checkMeleeBossCollisions(player, playerXPosition, playerYPosition, now);
        checkEnemyBulletPlayerCollisions(playerXPosition, playerYPosition, now);
    }


    public void setCurrentBoss(Boss boss) {
        this.currentBoss = boss;
    }


    public void setCurrentObjective(FactPoint objective) {
        this.currentObjective = objective;
    }


    public void setOnBossDefeated(Runnable callback) {
        this.onBossDefeated = callback;
    }


    public void setOnSpawnWave(Runnable callback) {
        this.onSpawnWave = callback;
    }
}
