package zorp_the_solar_alien.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import zorp_the_solar_alien.SingletonObject.AudioManager;
import zorp_the_solar_alien.SingletonObject.MainCharacterManager;
import zorp_the_solar_alien.SingletonObject.ScoreManager;
import zorp_the_solar_alien.gameFactory.*;
import zorp_the_solar_alien.model.PlayModel;
import zorp_the_solar_alien.view.PlayView;

/**
 * This controller handles gameplay, manages the game loop, enemy spawn, collision detection,
 * and all interaction between players. It also handles enemy, boss, bullets and different objects by creating
 * instance of different  classes. It connects playModel and playView to interact with each other
 * creating a model view controller pattern successfully.
 */
public class PlayController {
    private PlayModel model;
    private PlayView view;

    private SolarSystem solarSystem;
    private ZorpTheSolarAlienFactory factory;
    private GameInfoBar gameInfoBar;
    private FactPoint currentObjective = null;
    private Boss currentBoss = null;

    private Timeline gameLoop;
    private boolean paused = false;

    private List<Bullet> bullets = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<EnemyBullet> enemyBullets = new ArrayList<>();

    private long lastBulletTime = 0;
    private static final long BULLET_COOLDOWN = 200_000_000L;
    private long lastMeleeTime = 0;
    private static final long MELEE_COOLDOWN = 400_000_000L;
    private static final double PLAYER_SIZE = 80;

    private Image explosionImage;

    /**
     * This constructor set up the solar system game play.
     * By using classes from factory pattern such as SolarSystem, Factory, and GameInfoBar
     * it renders different objects on the play screen.
     * It also uses PlayView to create logic what happens when start a game, on restart and pause and
     * done reading screen card.
     */
    public PlayController(PlayModel model, PlayView view) {
        this.model = model;
        this.view = view;
        this.solarSystem = SolarSystem.getInstance(view.gc, 0, 0);
        this.factory = new ZorpTheSolarAlienFactory(view.gc);
        this.gameInfoBar = (GameInfoBar) factory.createProduct("gameInfoBar", 0, 0);
        this.explosionImage = new Image(getClass().getResource("/enemies/boss/explosion.gif").toExternalForm());

        view.setOnStartGame(() -> startGame());
        view.setOnDoneReading(() -> onDoneReading());
        view.setOnRestart(() -> restartGame());
        view.setOnPause(() -> togglePause());

        view.canvas.visibleProperty().addListener((obs, wasVisible, isVisible) -> {
            if (!isVisible) {
                stopGame();
            }
        });
    }

    /**
     * This method helps us load a saved game by reading data through ScoreManager class.
     * If no game is saved, and it's a new game play, it starts a fresh game play.
     */
    public void loadSavedGame(boolean isNewGame) {
        stopGame();
        ScoreManager sm = ScoreManager.getInstance();
        int savedLevel = sm.getHighestLevelUnlocked();
        if (isNewGame) {
            model.reset();
            sm.saveWaveProgress(0, 0);
        } else {
            model.resetForNextLevel();
            model.setCurrentWave(sm.getSavedWave());
            model.setFactsCollected(sm.getSavedWave());
            model.setScore(sm.getSavedScore());
        }

        model.setCurrentPlanet(savedLevel);
        solarSystem.setCurrentPlanet(savedLevel);
        MainCharacterManager.getInstance().reset();
        view.showIntroCard(
                "Level " + (model.getCurrentPlanet() + 1) + ": " + model.getCurrentPlanetName(),
                model.getCurrentPlanetDescription()
        );
    }

    /**
     * This method starts a game by first clearing all object from the scene.
     * Then through using ScoreManager it tracks if there is any saved data, if yes, use the saved data
     * to decide which level and facts to start from.
     * It handles game loop through calling required methods from different classes and starts the game.
     */
    public void startGame() {
        bullets.clear();
        enemies.clear();
        enemyBullets.clear();
        currentObjective = null;
        currentBoss = null;

        ScoreManager.getInstance().unlockLevel(model.getCurrentPlanet());

        spawnWave();

        gameLoop = new Timeline(new KeyFrame(Duration.millis(16.67), e -> {
            long now = System.nanoTime();
            double width = view.canvas.getWidth();
            double height = view.canvas.getHeight();
            view.gc.clearRect(0, 0, width, height);

            solarSystem.setFactsCollected(model.getFactsCollected());
            solarSystem.update();
            updateObjective();
            handlePlayerShooting(now);
            updateZorpBullets();
            updateEnemies(now);
            updateBoss(now);
            updateEnemyBullets();
            MainCharacterManager.getInstance().update();
            checkCollisions(now);
            gameInfoBar.setData(
                    model.getHp(), model.getMaxHp(), model.getScore(),
                    model.getCurrentPlanetName(), model.getCurrentWave(),
                    model.getTotalWaves(), model.getWaveEnemiesKilled(),
                    model.getEnemiesPerWave()
            );
            gameInfoBar.update();
            checkGameState();
        }));

        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();
    }

    /**
     * On each fact collected, this method helps us spawn new wave on the screen.
     * It uses MainCharacter class to create and reset main player as well as enemies.
     * When main character clear all enemy, this method also enable collecting the fact to
     * progress to next fact.
     * It also checks if the enemy is a boss to play boss music or normal enemy, and does action accordingly.
     */
    private void spawnWave() {
        double width = view.canvas.getWidth();
        double height = view.canvas.getHeight();

        model.startWave();
        currentBoss = null;
        enemies.clear();
        enemyBullets.clear();
        bullets.clear();

        MainCharacterManager player = MainCharacterManager.getInstance();
        player.setX(50);
        player.setY(height / 2 - PLAYER_SIZE / 2);

        String fact = model.getWaveFact();
        double objX =  width * 0.9;
        double objY = height * 0.5;
        currentObjective = (FactPoint) factory.createProduct("factPoint", objX, objY);
        currentObjective.setFactText(fact);
        currentObjective.setFactNumber(model.getCurrentWave() + 1);

        if (model.isBossWave()) {
            double bossX =  width * 0.65;
            double bossY = height * 0.35;
            currentBoss = new Boss(view.gc, bossX, bossY, model.getCurrentPlanet());
            AudioManager.getInstance().playBossMusic();
        } else {
            for (int i = 0; i < model.getEnemiesPerWave(); i++) {
                double spawnX =  width * 0.3 + Math.random() * ( width * 0.5);
                double spawnY = 80 + Math.random() * (height - 200);
                Enemy enemy = (Enemy) factory.createProduct("enemy", spawnX, spawnY);
                enemies.add(enemy);
            }
        }
    }

    /**
     * Pauses the game loop when user click on escape or pause button, and check for the state of the pause.
     */
    public void stopGame() {
        if (gameLoop != null) {
            gameLoop.pause();
        }
        paused = false;
    }

    /**
     * Changes between pause and play state, if paused, it draws pause overlay on top of the play screen.
     */
    public void togglePause() {
        if (paused) {
            gameLoop.play();
            paused = false;
        } else {
            gameLoop.pause();
            double w = view.canvas.getWidth();
            double h = view.canvas.getHeight();
            view.gc.setFill(Color.rgb(0, 0, 0, 0.5));
            view.gc.fillRect(0, 0, w, h);
            view.gc.setFont(Font.font("Arial", FontWeight.BOLD, 56));
            view.gc.setFill(Color.WHITE);
            view.gc.fillText("PAUSED", w / 2 - 130, h / 2);
            paused = true;
        }

        view.setPauseText(paused);
    }

    /**
     * When user collects a fact and done reading the fact, this method handle logic when player click on done reading button.
     * If the level is complete and there is no more fact to collect, show the level complete card. If entire game play is complete,
     * show gameplay screen. Otherwise, it resumes gameplay for next fact.
     */
    private void onDoneReading() {
        if (model.isLevelComplete()) {
            ScoreManager.getInstance().saveScore(model.getScore());
            ScoreManager.getInstance().unlockLevel(model.getCurrentPlanet() + 1);
            ScoreManager.getInstance().saveWaveProgress(0, model.getScore());
            if (model.isLastLevel()) {
                view.showGameComplete(model.getScore());
            } else {
                view.showLevelComplete(model.getCurrentPlanetName(), model.getScore());
            }
        } else {
            gameLoop.play();
        }
    }

    /**
     * Check if the wave of enemy is cleared by main character, if yes, unlocked the fact objectives.
     */
    private void updateObjective() {
        if (currentObjective != null && currentObjective.isActive()) {
            model.checkWaveCleared();
            if (model.isObjectiveUnlocked() && currentObjective.isLocked()) {
                currentObjective.unlock();
            }
            currentObjective.update();
        }
    }

    /**
     * When user left click mouse, and shooting cooldown has passed,
     * it first calculated where should be the position of bullet spawn.
     * It also checks if main character is facing left or right.
     * It creates a bullet using factory pattern, and sets the direction of the
     * bullet depending on if the main character facing left or right.
     * It also handles the shooting audio through calling singleton object AudioManager when main character shoot.
     */
    private void handlePlayerShooting(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();

        if (player.isShooting() && now - lastBulletTime > BULLET_COOLDOWN) {
            double bulletStartX = player.isFacingRight() ? player.getX() + PLAYER_SIZE - 5 : player.getX() + 5;
            double bulletStartY = player.getY() + PLAYER_SIZE * 0.35;
            Bullet bullet = (Bullet) factory.createProduct("bullet", bulletStartX,  bulletStartY);

            double bulletDirection = player.isFacingRight() ? 1 : -1;
            bullet.setTarget(bulletStartX + bulletDirection * 500,  bulletStartY);

            bullets.add(bullet);
            lastBulletTime = now;
            AudioManager.getInstance().playLaser();
        }
    }

    /**
     * This method loops through the bullet list to update state of bullets for the main character.
     * If a bullet is off-screen, it removes that object.
     */
    private void updateZorpBullets() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.update();
            if (!b.isActive()) bulletIterator.remove();
        }
    }

    /**
     * The method loop through all enemy bullets, and update their positon on the screen, and draws them.
     * If any bullet is off screen, it removes the bullet from the list.
     * Java built in class Iterator helps us to iterate over objects of enemybullets which helps us delete object during mid loop.
     */
    private void updateEnemyBullets() {
        Iterator<EnemyBullet> enemyBulletIterator = enemyBullets.iterator();
        while (enemyBulletIterator.hasNext()) {
            EnemyBullet eb = enemyBulletIterator.next();
            eb.update();
            if (!eb.isActive()) enemyBulletIterator.remove();
        }
    }

    /**
     * Get the players positions, and loop through all enemy.
     * Handle logic so enemy chase the main character.
     * Update enemey position on the screen and draw them, and if
     * any enemy is dead remove them from screen. If shooting cooldown has passed for enemy,
     * the enemy automatically shoot bullets aiming on main character.
     */
    private void updateEnemies(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();
        double playerCX = player.getX() + PLAYER_SIZE / 2;
        double playerCY = player.getY() + PLAYER_SIZE / 2;

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.setChaseTarget(playerCX, playerCY);
            enemy.update();
            if (!enemy.isActive()) {
                enemyIterator.remove();
                continue;
            }
            if (enemy.canShoot(now)) {
                EnemyBullet enemyBullet = (EnemyBullet) factory.createProduct("enemyBullet", enemy.getCenterX(), enemy.getCenterY());
                enemyBullet.setTarget(playerCX, playerCY);
                enemyBullets.add(enemyBullet);
                enemy.markShot(now);
            }
        }

        resolveEnemySeparation();
    }

    /**
     * This method helps us creat logic so that enemy does not overlap each other.
     * It create true object collsion and keeps the enemy seperated from each other.
     * Without this method, all enemy could potentially go on top of each other
     * making the game look has no physics elements.
     * It calculate the mergin of overlap by comparing enemy in pair, then pushed enemy equally in opposite direction.
     */
    private void resolveEnemySeparation() {
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
                    double normalisedX = distanceX/ dist;
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
     * This method helps us determind if a boss it active, if not active or dead this method is skipped.
     * Otherwise, if boss is active and alive, boss chase the player. When shooting cooldown is done,
     * boss fire a new bullet aiming towards the player. The boss also fire random bullets through using
     * factory product by using createProduct method.
     */
    private void updateBoss(long now) {
        if (currentBoss == null || !currentBoss.isActive()) return;

        MainCharacterManager player = MainCharacterManager.getInstance();
        double playerCX = player.getX() + PLAYER_SIZE / 2;
        double playerCY = player.getY() + PLAYER_SIZE / 2;

        currentBoss.setChaseTarget(playerCX, playerCY);
        currentBoss.update();

        if (currentBoss.canShoot(now)) {
            EnemyBullet enemeyBossBullet = (EnemyBullet) factory.createProduct("enemyBullet",
                    currentBoss.getCenterX(), currentBoss.getCenterY());
            enemeyBossBullet.setTarget(playerCX, playerCY);
            enemeyBossBullet.setAmmoImage(currentBoss.getAmmoImage());
            enemyBullets.add(enemeyBossBullet);
            currentBoss.markShot(now);
        }
    }

    /**
     * When player defeat the boss, stop the boss music by calling singleton AudioManager method,
     * then play defeated music, and after that start playing normal music.
     * When boss defeated, on the position of the boss, this method help us create the explosion effect.
     * It update the score of the player and add 1000 points, and full heal the player and register the boss killed.
     * After all anmiation done, it removed all relevant objects from the screen, and unlock the final facts.
     */
    private void onBossDefeated() {
        AudioManager.getInstance().stopBossMusic();
        AudioManager.getInstance().playBossBiten();
        AudioManager.getInstance().playHomeMusic();

        ImageView explosionView = new ImageView(new Image(getClass().getResource("/enemies/boss/explosion.gif").toExternalForm()));
        explosionView.setFitWidth(180);
        explosionView.setFitHeight(180);
        explosionView.setPreserveRatio(true);
        explosionView.setLayoutX(currentBoss.getCenterX() - 90);
        explosionView.setLayoutY(currentBoss.getCenterY() - 90);
        view.root.getChildren().add(explosionView);
        explosionView.toFront();

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> view.root.getChildren().remove(explosionView));
        pause.play();

        model.registerKill();
        model.addScore(1000);
        model.heal(100);
        enemyBullets.clear();
        currentBoss = null;

        if (currentObjective != null) {
            currentObjective.unlock();
        }
    }

    /**
     * First we get the player position then we run all collion check method to handle game logic
     * such as what happends when bullet hits enemy, or if enemy bullet hits the main player etc.
     * It also handle runt he logic when player touch the objective after clearing enemy wave.
     */
    private void checkCollisions(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();
        double playerXPosition = player.getX();
        double playerYPosition = player.getY();

        checkBulletEnemyCollisions();
        checkMeleeEnemyCollisions(player, playerXPosition,playerYPosition, now);
        checkEnemyContactCollisions(player, playerXPosition, playerYPosition, now);
        checkBulletBossCollisions();
        checkMeleeBossCollisions(player, playerXPosition, playerYPosition, now);
        checkEnemyBulletPlayerCollisions(playerXPosition, playerYPosition, now);
        checkObjectiveCollision(playerXPosition, playerYPosition);
    }

    private void checkBulletEnemyCollisions() {
        Iterator<Bullet> bit = bullets.iterator();
        while (bit.hasNext()) {
            Bullet b = bit.next();
            for (Enemy e : enemies) {
                if (!e.isActive()) continue;
                if (circleIntersectsRect(b.getX(), b.getY(), b.getRadius(),
                        e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
                    b.setActive(false);
                    e.takeDamage(10);
                    if (!e.isActive()) {
                        model.registerKill();
                        AudioManager.getInstance().playEnemyDied();
                    }
                    break;
                }
            }
        }
        bullets.removeIf(b -> !b.isActive());
    }

    private void checkMeleeEnemyCollisions(MainCharacterManager player, double px, double py, long now) {
        if (!player.isMelee() || now - lastMeleeTime < MELEE_COOLDOWN) return;
        double meleeX = px + PLAYER_SIZE;
        double meleeY = py;
        double meleeW = 40;
        double meleeH = PLAYER_SIZE;
        boolean hit = false;
        for (Enemy e : enemies) {
            if (!e.isActive()) continue;
            if (rectsOverlap(meleeX, meleeY, meleeW, meleeH,
                    e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
                e.takeDamage(15);
                hit = true;
                if (!e.isActive()) {
                    model.registerKill();
                    AudioManager.getInstance().playEnemyDied();
                }
            }
        }
        if (hit) lastMeleeTime = now;
    }

    private void checkEnemyContactCollisions(MainCharacterManager player, double px, double py, long now) {
        for (Enemy e : enemies) {
            if (!e.isActive()) continue;
            if (rectsOverlap(px, py, PLAYER_SIZE, PLAYER_SIZE,
                    e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
                if (e.canContactDamage(now)) {
                    if (model.takeDamage(e.getContactDamage(), now)) {
                        AudioManager.getInstance().playDamageTaken();
                    }
                    e.markContactDamage(now);
                }
                double pcx = px + PLAYER_SIZE / 2;
                double pcy = py + PLAYER_SIZE / 2;
                double ecx = e.getCenterX();
                double ecy = e.getCenterY();
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
        Iterator<Bullet> bbit = bullets.iterator();
        while (bbit.hasNext()) {
            Bullet b = bbit.next();
            if (circleIntersectsRect(b.getX(), b.getY(), b.getRadius(),
                    currentBoss.getX(), currentBoss.getY(), currentBoss.getWidth(), currentBoss.getHeight())) {
                b.setActive(false);
                currentBoss.takeDamage(10);
                if (!currentBoss.isActive()) {
                    onBossDefeated();
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
        if (rectsOverlap(meleeX, meleeY, meleeW, meleeH,
                currentBoss.getX(), currentBoss.getY(), currentBoss.getWidth(), currentBoss.getHeight())) {
            currentBoss.takeDamage(15);
            lastMeleeTime = now;
            if (!currentBoss.isActive()) {
                onBossDefeated();
            }
        }
    }

    private void checkEnemyBulletPlayerCollisions(double px, double py, long now) {
        Iterator<EnemyBullet> ebit = enemyBullets.iterator();
        while (ebit.hasNext()) {
            EnemyBullet eb = ebit.next();
            if (circleIntersectsRect(eb.getX(), eb.getY(), eb.getRadius(),
                    px, py, PLAYER_SIZE, PLAYER_SIZE)) {
                eb.setActive(false);
                if (model.takeDamage(10, now)) {
                    AudioManager.getInstance().playDamageTaken();
                }
            }
        }
        enemyBullets.removeIf(eb -> !eb.isActive());
    }

    private void checkObjectiveCollision(double px, double py) {
        if (currentObjective == null || !currentObjective.isActive() || currentObjective.isLocked()) return;
        if (circleIntersectsRect(currentObjective.getX(), currentObjective.getY(), currentObjective.getRadius(),
                px, py, PLAYER_SIZE, PLAYER_SIZE)) {
            currentObjective.setActive(false);
            model.collectFact();
            model.addScore(500);
            model.heal(30);
            int factNum = model.getCurrentWave() + 1;
            String factText = currentObjective.getFactText();
            model.completeWave();
            ScoreManager.getInstance().saveWaveProgress(model.getCurrentWave(), model.getScore());

            if (!model.isLevelComplete()) {
                spawnWave();
            }

            gameLoop.pause();
            view.showFactCard(factText, factNum);
        }
    }

    private void checkGameState() {
        if (model.isGameOver()) {
            gameLoop.pause();
            AudioManager.getInstance().stop();
            AudioManager.getInstance().playGameOver();
            ScoreManager.getInstance().saveScore(model.getScore());
            view.showGameOver(model.getScore(), ScoreManager.getInstance().getHighScore());
        }
    }

    private void restartGame() {
        AudioManager.getInstance().playHomeMusic();
        if (model.isLevelComplete() && model.isLastLevel()) {
            ScoreManager.getInstance().resetProgress();
            model.reset();
            solarSystem.reset();
            MainCharacterManager.getInstance().reset();
            view.showIntroCard(
                    "Level 1: " + model.getCurrentPlanetName(),
                    model.getCurrentPlanetDescription()
            );
        } else if (model.isLevelComplete()) {
            solarSystem.nextPlanet();
            model.nextPlanet();
            model.resetForNextLevel();
            MainCharacterManager.getInstance().reset();
            view.showIntroCard(
                    "Level " + (model.getCurrentPlanet() + 1) + ": " + model.getCurrentPlanetName(),
                    model.getCurrentPlanetDescription()
            );
        } else {
            model.resetForRetry();
            MainCharacterManager.getInstance().reset();
            startGame();
        }
    }

    private boolean circleIntersectsRect(double cx, double cy, double cr,
                                          double rx, double ry, double rw, double rh) {
        double closestX = Math.max(rx, Math.min(cx, rx + rw));
        double closestY = Math.max(ry, Math.min(cy, ry + rh));
        double dx = cx - closestX;
        double dy = cy - closestY;
        return (dx * dx + dy * dy) <= (cr * cr);
    }

    private boolean rectsOverlap(double x1, double y1, double w1, double h1,
                                  double x2, double y2, double w2, double h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }
}
