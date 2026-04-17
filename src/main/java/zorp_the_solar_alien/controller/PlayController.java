package zorp_the_solar_alien.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.SingletonObjects.ScoreManager;
import zorp_the_solar_alien.SingletonObjects.SolarSystem;
import zorp_the_solar_alien.gameFactory.*;
import zorp_the_solar_alien.gameLogic.BossHandler;
import zorp_the_solar_alien.gameLogic.CollisionHandler;
import zorp_the_solar_alien.gameLogic.MainCharacterHandler;
import zorp_the_solar_alien.gameLogic.WaveSpawner;
import zorp_the_solar_alien.model.PlayModel;
import zorp_the_solar_alien.view.PlayView;


/**
 * Handles gameplay, manages game logic by using classes from gameLogic package were we have helper classes.
 * Instead of handling collision, wave spawning, boss logic all in this class, we use game logic helper classes.
 * It supports the MVC pattern, and it uses playView and playModel to create connection between them.
 */
public class PlayController {
    private PlayModel model;
    private PlayView view;
    private SolarSystem solarSystem;
    private ZorpTheSolarAlienFactory factory;
    private GameInfoBar gameInfoBar;
    private Timeline gameLoop;
    private boolean paused = false;
    private List<Bullet> bullets = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Bullet> enemyBullets = new ArrayList<>();
    private CollisionHandler collisionHandler;
    private WaveSpawner waveSpawner;
    private BossHandler bossHandler;
    private MainCharacterHandler mainCharacterHandler;


    /**
     * Handles zorp the solar alien game play.
     * It uses classes from factory pattern to render different objects on
     * the screen such as enemy, boss, main character, score manager etc.
     * It also uses gameLogic classes to handle collision, enemy spawn, boss and player logic.
     * Through playView class it also render logic of what to do when game start, restart or pause
     * or when user clicks on done reading.
     */
    public PlayController(PlayModel model, PlayView view) {
        this.model = model;
        this.view = view;
        this.solarSystem = SolarSystem.getInstance(view.gc, 0, 0);
        this.factory = new ZorpTheSolarAlienFactory(view.gc);
        this.gameInfoBar = (GameInfoBar) factory.createProduct("gameInfoBar", 0, 0);

        this.collisionHandler = new CollisionHandler(model, bullets, enemies, enemyBullets);
        this.waveSpawner = new WaveSpawner(model, view.gc, factory, bullets, enemies, enemyBullets);
        this.bossHandler = new BossHandler(model, view.root, factory, enemyBullets);
        this.mainCharacterHandler = new MainCharacterHandler(factory, bullets);

        collisionHandler.setOnBossDefeated(() ->
                bossHandler.onBossDefeated(waveSpawner.getCurrentBoss(), waveSpawner.getCurrentObjective())
        );
        collisionHandler.setOnSpawnWave(() -> spawnWave());

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
     * Loads a saved game by reading data through ScoreManager class.
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
     * First clear all objects from the screen.
     * Then handles game logic by the helper classes through utilising gameLogic package
     * by using PlayHandler class for shooting, BoosHandler for updating different boss actions,
     * and CollisionHandler for all collision check between different objects.
     * Also manages frame rate problem by using JavaFX timeline, because otherwise if someone plays the
     * game anything other than 60fps monitor, movement becomes unpredictable and way to fast.
     */
    public void startGame() {
        bullets.clear();
        enemies.clear();
        enemyBullets.clear();

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
            mainCharacterHandler.handlePlayerShooting(now);
            mainCharacterHandler.updateZorpBullets();
            updateEnemies(now);
            bossHandler.updateBoss(waveSpawner.getCurrentBoss(), now);
            mainCharacterHandler.updateBullets(enemyBullets);
            MainCharacterManager.getInstance().update();

            collisionHandler.setCurrentBoss(waveSpawner.getCurrentBoss());
            collisionHandler.setCurrentObjective(waveSpawner.getCurrentObjective());
            collisionHandler.checkCollisions(now);

            MainCharacterManager player = MainCharacterManager.getInstance();
            String[] factResult = collisionHandler.checkObjectiveCollision(player.getX(), player.getY());
            if (factResult != null) {
                ScoreManager.getInstance().saveWaveProgress(model.getCurrentWave(), model.getScore());
                gameLoop.pause();
                view.showFactCard(factResult[0], Integer.parseInt(factResult[1]));
            }

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
     * Instead of handling spawning wave of enemy in this controller this method uses helper class from gameLogic to handle it.
     * It uses WaveSpawner class to call spawnWave on the play screen.
     */
    private void spawnWave() {
        double width = view.canvas.getWidth();
        double height = view.canvas.getHeight();
        waveSpawner.spawnWave(width, height);
    }


    public void stopGame() {
        if (gameLoop != null) {
            gameLoop.pause();
        }
        paused = false;
    }


    /**
     * This method declares what happens when game is on pause. If paused, it draws pause overlay text on top of play screen.
     */
    public void togglePause() {
        if (gameLoop == null) return;
        if (paused) {
            gameLoop.play();
            paused = false;
        } else {
            gameLoop.pause();
            double w = view.canvas.getWidth();
            double h = view.canvas.getHeight();
            view.gc.setFill(Color.rgb(0, 0, 0, 0.5));
            view.gc.fillRect(0, 0, w, h);
            view.gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            view.gc.setFill(Color.WHITE);
            view.gc.fillText("PAUSED", w / 2 - 130, h / 2);
            paused = true;
        }

        view.setPauseText(paused);
    }


    /**
     * When user collects a fact and done reading the fact, it handles logic when player click on done reading button.
     * If the level is complete and there is no more fact to collect, show the level complete card. If entire game play is complete,
     * shows game complete screen. Otherwise, it resumes gameplay for next fact.
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
     * Checks if all enemies in the current wave is killed by zorp.
     * If all enemies are removed, it unlocks the fact objectives
     * making it available to collect. It also draws what objective needed on the screen
     * through calling update method of FactPoint class.
     */
    private void updateObjective() {
        FactPoint objective = waveSpawner.getCurrentObjective();
        if (objective != null && objective.isActive()) {
            model.checkWaveCleared();
            if (model.isObjectiveUnlocked() && objective.isLocked()) {
                objective.unlock();
            }
            objective.update();
        }
    }


    /**
     * First this method get the player position then it loop through all enemies objects.
     * It uses logic from gameLogic classes to handle how enemy chases main character.
     * Update enemy positon and player position on the screen and draw them.
     * If any enemy is dead it removes the enemy from the screen.
     * It also checks if shooting cooldown has passed for enemy, if yes, enemy starts shooting
     * again immediately.
     */
    private void updateEnemies(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();
        double playerCX = player.getX() + 80 / 2;
        double playerCY = player.getY() + 80 / 2;

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
                Bullet enemyBullet = (Bullet) factory.createProduct("enemyBullet", enemy.getCenterX(), enemy.getCenterY());
                enemyBullet.setTarget(playerCX, playerCY);
                enemyBullets.add(enemyBullet);
                enemy.markShot(now);
            }
        }

        collisionHandler.resolveEnemySeparation();
    }


    /**
     * Tracts if player hp is zero. If yes, it handles game over logic, pauses the
     * game loop and stop current music to play game over sound. Then it saves the score to cache save txt file.
     * It also renders game over screen on play scene.
     */
    private void checkGameState() {
        if (model.isGameOver()) {
            gameLoop.pause();
            AudioManager.getInstance().stop();
            AudioManager.getInstance().playGameOver();
            ScoreManager.getInstance().saveScore(model.getScore());
            view.showGameOver(model.getScore(), ScoreManager.getInstance().getHighScore());
        }
    }


    /**
     * If player finished the level, this method handle next level button.
     * If entire gameplay is finished, it handles restart the game from level 1 again.
     * If player finish a level, restarts the game to set the level to next level.
     * And in case the player dies before the complete the level, restarts from the current fact
     * where use died not from the beginning through reading saved data.
     */
    private void restartGame() {
        AudioManager.getInstance().playHomeMusic();
        if (model.isLevelComplete() && model.isLastLevel()) {
            ScoreManager.getInstance().resetProgress();
            model.reset();
            solarSystem.reset();
            MainCharacterManager.getInstance().reset();
            view.showIntroCard("Level 1: " + model.getCurrentPlanetName(), model.getCurrentPlanetDescription());
        } else if (model.isLevelComplete()) {
            solarSystem.nextPlanet();
            model.nextPlanet();
            model.resetForNextLevel();
            MainCharacterManager.getInstance().reset();
            view.showIntroCard("Level " + (model.getCurrentPlanet() + 1) + ": " + model.getCurrentPlanetName(), model.getCurrentPlanetDescription());
        } else {
            model.resetForRetry();
            MainCharacterManager.getInstance().reset();
            startGame();
        }
    }
    
}
