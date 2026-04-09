package zorp_the_solar_alien.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import zorp_the_solar_alien.SingletonObject.AudioManager;
import zorp_the_solar_alien.SingletonObject.MainCharacterManager;
import zorp_the_solar_alien.SingletonObject.ScoreManager;
import zorp_the_solar_alien.gameFactory.*;
import zorp_the_solar_alien.model.PlayModel;
import zorp_the_solar_alien.view.PlayView;

public class PlayController {
    private PlayModel model;
    private PlayView view;
    private SolarSystem solarSystem;
    private AnimationTimer gameLoop;
    private ZorpTheSolarAlienFactory factory;
    private GameInfoBar gameInfoBar;

    private List<Bullet> bullets = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<EnemyBullet> enemyBullets = new ArrayList<>();
    private FactPoint currentObjective = null;
    private Boss currentBoss = null;

    private long lastBulletTime = 0;
    private static final long BULLET_COOLDOWN = 200_000_000L;

    private static final double PLAYER_SIZE = 80;

    private Image explosionImage;

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

        view.canvas.visibleProperty().addListener((obs, wasVisible, isVisible) -> {
            if (!isVisible) {
                stopGame();
            }
        });
    }

    public void loadSavedGame(boolean isNewGame) {
        stopGame();
        int savedLevel = ScoreManager.getInstance().getHighestLevelUnlocked();
        if (isNewGame) {
            model.reset();
        } else {
            model.resetForNextLevel();
        }
        model.setCurrentPlanet(savedLevel);
        solarSystem.setCurrentPlanet(savedLevel);
        MainCharacterManager.getInstance().reset();
        view.showIntroCard(
                "Level " + (model.getCurrentPlanet() + 1) + ": " + model.getCurrentPlanetName(),
                model.getCurrentPlanetDescription()
        );
    }

    public void startGame() {
        bullets.clear();
        enemies.clear();
        enemyBullets.clear();
        currentObjective = null;
        currentBoss = null;

        ScoreManager.getInstance().unlockLevel(model.getCurrentPlanet());

        spawnWave();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double w = view.canvas.getWidth();
                double h = view.canvas.getHeight();
                view.gc.clearRect(0, 0, w, h);

                solarSystem.setFactsCollected(model.getFactsCollected());
                solarSystem.update();
                updateObjective();
                handlePlayerShooting(now);
                updateBullets();
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
            }
        };
        gameLoop.start();
    }

    private void spawnWave() {
        double w = view.canvas.getWidth();
        double h = view.canvas.getHeight();

        model.startWave();
        currentBoss = null;
        enemies.clear();
        enemyBullets.clear();
        bullets.clear();

        MainCharacterManager player = MainCharacterManager.getInstance();
        player.setX(50);
        player.setY(h / 2 - PLAYER_SIZE / 2);

        String fact = model.getWaveFact();
        double objX = w * 0.9;
        double objY = h * 0.5;
        currentObjective = (FactPoint) factory.createProduct("factPoint", objX, objY);
        currentObjective.setFactText(fact);
        currentObjective.setFactNumber(model.getCurrentWave() + 1);

        if (model.isBossWave()) {
            double bossX = w * 0.65;
            double bossY = h * 0.35;
            currentBoss = new Boss(view.gc, bossX, bossY, model.getCurrentPlanet());
            AudioManager.getInstance().playBossMusic();
        } else {
            for (int i = 0; i < model.getEnemiesPerWave(); i++) {
                double spawnX = w * 0.3 + Math.random() * (w * 0.5);
                double spawnY = 80 + Math.random() * (h - 200);
                Enemy e = (Enemy) factory.createProduct("enemy", spawnX, spawnY);
                enemies.add(e);
            }
        }
    }

    public void stopGame() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    private void onDoneReading() {
        if (model.isLevelComplete()) {
            ScoreManager.getInstance().saveScore(model.getScore());
            ScoreManager.getInstance().unlockLevel(model.getCurrentPlanet() + 1);
            if (model.isLastLevel()) {
                view.showGameComplete(model.getScore());
            } else {
                view.showLevelComplete(model.getCurrentPlanetName(), model.getScore());
            }
        } else {
            gameLoop.start();
        }
    }

    private void updateObjective() {
        if (currentObjective != null && currentObjective.isActive()) {
            model.checkWaveCleared();
            if (model.isObjectiveUnlocked() && currentObjective.isLocked()) {
                currentObjective.unlock();
            }
            currentObjective.update();
        }
    }

    private void handlePlayerShooting(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();
        if (player.isShooting() && now - lastBulletTime > BULLET_COOLDOWN) {
            double cx = player.getX() + PLAYER_SIZE / 2;
            double cy = player.getY() + PLAYER_SIZE / 2;
            Bullet b = (Bullet) factory.createProduct("bullet", cx, cy);

            double dx = player.isFacingRight() ? 1 : -1;
            b.setTarget(cx + dx * 500, cy);

            bullets.add(b);
            lastBulletTime = now;
            AudioManager.getInstance().playLaser();
        }
    }

    private void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (!b.isActive()) it.remove();
        }
    }

    private void updateEnemies(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();
        double playerCX = player.getX() + PLAYER_SIZE / 2;
        double playerCY = player.getY() + PLAYER_SIZE / 2;

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.setChaseTarget(playerCX, playerCY);
            e.update();
            if (!e.isActive()) {
                it.remove();
                continue;
            }
            if (e.canShoot(now)) {
                EnemyBullet eb = (EnemyBullet) factory.createProduct("enemyBullet", e.getCenterX(), e.getCenterY());
                eb.setTarget(playerCX, playerCY);
                enemyBullets.add(eb);
                e.markShot(now);
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy a = enemies.get(i);
                Enemy b = enemies.get(j);
                double dx = a.getCenterX() - b.getCenterX();
                double dy = a.getCenterY() - b.getCenterY();
                double dist = Math.sqrt(dx * dx + dy * dy);
                double minDist = (a.getWidth() + b.getWidth()) / 2.0;
                if (dist < minDist && dist > 0) {
                    double overlap = (minDist - dist) / 2.0;
                    double nx = dx / dist;
                    double ny = dy / dist;
                    a.setX(a.getX() + nx * overlap);
                    a.setY(a.getY() + ny * overlap);
                    b.setX(b.getX() - nx * overlap);
                    b.setY(b.getY() - ny * overlap);
                }
            }
        }
    }

    private void updateBoss(long now) {
        if (currentBoss == null || !currentBoss.isActive()) return;

        MainCharacterManager player = MainCharacterManager.getInstance();
        double playerCX = player.getX() + PLAYER_SIZE / 2;
        double playerCY = player.getY() + PLAYER_SIZE / 2;

        currentBoss.setChaseTarget(playerCX, playerCY);
        currentBoss.update();

        if (currentBoss.canShoot(now)) {
            EnemyBullet eb = (EnemyBullet) factory.createProduct("enemyBullet",
                    currentBoss.getCenterX(), currentBoss.getCenterY());
            eb.setTarget(playerCX, playerCY);
            eb.setAmmoImage(currentBoss.getAmmoImage());
            enemyBullets.add(eb);
            currentBoss.markShot(now);
        }
    }

    private void onBossDefeated() {
        AudioManager.getInstance().stopBossMusic();
        AudioManager.getInstance().playBossBiten();

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

    private void updateEnemyBullets() {
        Iterator<EnemyBullet> it = enemyBullets.iterator();
        while (it.hasNext()) {
            EnemyBullet eb = it.next();
            eb.update();
            if (!eb.isActive()) it.remove();
        }
    }

    private void checkCollisions(long now) {
        MainCharacterManager player = MainCharacterManager.getInstance();
        double px = player.getX();
        double py = player.getY();

        Iterator<Bullet> bit = bullets.iterator();
        while (bit.hasNext()) {
            Bullet b = bit.next();
            for (Enemy e : enemies) {
                if (!e.isActive()) continue;
                if (circleIntersectsRect(b.getX(), b.getY(), b.getRadius(),
                        e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
                    b.setActive(false);
                    e.takeDamage();
                    if (!e.isActive()) {
                        model.registerKill();
                        AudioManager.getInstance().playEnemyDied();
                    }
                    break;
                }
            }
        }
        bullets.removeIf(b -> !b.isActive());

        if (player.isMelee()) {
            double meleeX = px + PLAYER_SIZE;
            double meleeY = py;
            double meleeW = 40;
            double meleeH = PLAYER_SIZE;
            for (Enemy e : enemies) {
                if (!e.isActive()) continue;
                if (rectsOverlap(meleeX, meleeY, meleeW, meleeH,
                        e.getX(), e.getY(), e.getWidth(), e.getHeight())) {
                    e.takeDamage();
                    e.takeDamage();
                    if (!e.isActive()) {
                        model.registerKill();
                        AudioManager.getInstance().playEnemyDied();
                    }
                }
            }
        }

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

        if (currentBoss != null && currentBoss.isActive()) {
            Iterator<Bullet> bbit = bullets.iterator();
            while (bbit.hasNext()) {
                Bullet b = bbit.next();
                if (circleIntersectsRect(b.getX(), b.getY(), b.getRadius(),
                        currentBoss.getX(), currentBoss.getY(), currentBoss.getWidth(), currentBoss.getHeight())) {
                    b.setActive(false);
                    currentBoss.takeDamage();
                    if (!currentBoss.isActive()) {
                        onBossDefeated();
                        break;
                    }
                }
            }
            bullets.removeIf(b -> !b.isActive());

            if (currentBoss != null && currentBoss.isActive() && player.isMelee()) {
                double meleeX = px + PLAYER_SIZE;
                double meleeY = py;
                double meleeW = 40;
                double meleeH = PLAYER_SIZE;
                if (rectsOverlap(meleeX, meleeY, meleeW, meleeH,
                        currentBoss.getX(), currentBoss.getY(), currentBoss.getWidth(), currentBoss.getHeight())) {
                    currentBoss.takeDamage();
                    currentBoss.takeDamage();
                    if (!currentBoss.isActive()) {
                        onBossDefeated();
                    }
                }
            }
        }

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

        if (currentObjective != null && currentObjective.isActive() && !currentObjective.isLocked()) {
            if (circleIntersectsRect(currentObjective.getX(), currentObjective.getY(), currentObjective.getRadius(),
                    px, py, PLAYER_SIZE, PLAYER_SIZE)) {
                currentObjective.setActive(false);
                model.collectFact();
                model.addScore(500);
                model.heal(30);
                int factNum = model.getCurrentWave() + 1;
                String factText = currentObjective.getFactText();
                model.completeWave();

                if (!model.isLevelComplete()) {
                    spawnWave();
                }

                gameLoop.stop();
                view.showFactCard(factText, factNum);
            }
        }
    }

    private void checkGameState() {
        if (model.isGameOver()) {
            gameLoop.stop();
            AudioManager.getInstance().stop();
            AudioManager.getInstance().playGameOver();
            ScoreManager.getInstance().saveScore(model.getScore());
            view.showGameOver(model.getScore(), ScoreManager.getInstance().getHighScore());
        }
    }

    private void restartGame() {
        if (model.isLevelComplete() && model.isLastLevel()) {
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
