package zorp_the_solar_alien.gameLogic;

import java.util.Iterator;
import java.util.List;

import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.gameFactory.*;
import zorp_the_solar_alien.model.PlayModel;

/**
 * This class is delegated by PlayController to handle all collision detection logic in the game.
 * It checks collisions between Zorp's bullets and enemies, melee attacks, enemy contact damage,
 * boss collisions, enemy bullets hitting the player, and objective collection.
 */
public class CollisionHandler {
    private PlayModel model;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<EnemyBullet> enemyBullets;

    private long lastMeleeTime = 0;
    private static final long MELEE_COOLDOWN = 400_000_000L;
    private static final double PLAYER_SIZE = 80;

    private Boss currentBoss;
    private FactPoint currentObjective;
    private Runnable onBossDefeated;
    private Runnable onSpawnWave;

    public CollisionHandler(PlayModel model, List<Bullet> bullets, List<Enemy> enemies, List<EnemyBullet> enemyBullets) {
        this.model = model;
        this.bullets = bullets;
        this.enemies = enemies;
        this.enemyBullets = enemyBullets;
    }

    public void setCurrentBoss(Boss boss) { this.currentBoss = boss; }
    public void setCurrentObjective(FactPoint objective) { this.currentObjective = objective; }
    public void setOnBossDefeated(Runnable callback) { this.onBossDefeated = callback; }
    public void setOnSpawnWave(Runnable callback) { this.onSpawnWave = callback; }

    /**
     * First we get the player position then we run all collision check method to handle game collision logic
     * such as what happens when bullet hits enemy, or if enemy bullet hits the main player etc.
     * It also handle logic what happens when player touch the objective after clearing enemy wave.
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

    /**
     * Checks if player has touched the unlocked fact objective.
     * If yes, collects the fact, awards score, heals, saves progress,
     * spawns next wave if level not complete, and returns fact data for display.
     * @return String array with [factText, factNum] if collected, null otherwise
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
     * This method helps us create logic so that enemy does not overlap each other.
     * It create true object collision and keeps the enemy separated from each other.
     * Without this method, all enemy could potentially go on top of each other
     * making the game look has no physics elements.
     * It calculate the margin of overlap by comparing enemy in pair, then pushed enemy equally in opposite direction.
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
     * Loop through shot bullets from the main character.
     * If a bullet hits an enemy, deactivate the bullet from the screen.
     * When bullet is hit on an enemy, they take 10 damage.
     * If enemy dies, we register the kill and play relevant sound through AudioManager.
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
     * When player use melee attack, and cooldown for melee attack has passed,
     * this method creates invisible melee range box 40px by 80px.
     * Then it checks if any living enemy overlap with the melee range.
     * If they are in the melee range, it does 15 damage for each hit.
     * If any enemy dies, it registers the kill and plays the death sound.
     * Cooldown resets when enemy is hit.
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

            if (rectangleOverlapToCheckMeleDamageAndEnemyContactDamage(meleeX, meleeY, meleeW, meleeH,
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

    private void checkEnemyContactCollisions(MainCharacterManager player, double px, double py, long now) {
        for (Enemy enemy : enemies) {
            if (!enemy.isActive()) continue;
            if (rectangleOverlapToCheckMeleDamageAndEnemyContactDamage(px, py, PLAYER_SIZE, PLAYER_SIZE,
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

    private void checkMeleeBossCollisions(MainCharacterManager player, double px, double py, long now) {
        if (currentBoss == null || !currentBoss.isActive() || !player.isMelee()) return;
        if (now - lastMeleeTime < MELEE_COOLDOWN) return;
        double meleeX = px + PLAYER_SIZE;
        double meleeY = py;
        double meleeW = 40;
        double meleeH = PLAYER_SIZE;
        if (rectangleOverlapToCheckMeleDamageAndEnemyContactDamage(meleeX, meleeY, meleeW, meleeH,
                currentBoss.getX(), currentBoss.getY(), currentBoss.getWidth(), currentBoss.getHeight())) {
            currentBoss.takeDamage(15);
            lastMeleeTime = now;
            if (!currentBoss.isActive()) {
                if (onBossDefeated != null) onBossDefeated.run();
            }
        }
    }

    private void checkEnemyBulletPlayerCollisions(double px, double py, long now) {
        Iterator<EnemyBullet> enemyBulletIterator = enemyBullets.iterator();
        while (enemyBulletIterator.hasNext()) {
            EnemyBullet eb = enemyBulletIterator.next();
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
     * Bullet are measured in a circular way, check if any bullet circle overlaps any rectangle object (target)
     * by finding closest edge point on the target. Then compare the distance against the bullet radius.
     * @return true if any bullets overlap any target
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
     * Check if two object in play hit boxes are overlapping through comparing where their edges are.
     * Attacker is the main character and target is the enemy.
     * The method also check if zorp is contacting any enemy which does additional damage through other methods.
     * @return true if two object boxes overlap each other.
     */
    private boolean rectangleOverlapToCheckMeleDamageAndEnemyContactDamage(double attackerX, double attackerY,
                                                                           double attackerWidth, double attackerHeight,
                                                                           double targetX, double targetY,
                                                                           double targetWidth, double targetHeight) {
        return attackerX < targetX + targetWidth && attackerX + attackerWidth > targetX
                && attackerY < targetY + targetHeight && attackerY + attackerHeight > targetY;
    }
}
