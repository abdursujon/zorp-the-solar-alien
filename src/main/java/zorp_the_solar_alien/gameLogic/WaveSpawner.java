package zorp_the_solar_alien.gameLogic;

import java.util.List;

import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.gameFactory.*;
import zorp_the_solar_alien.model.PlayModel;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class is delegated by PlayController to handle spawning waves of enemies and bosses.
 * It resets player position, creates the fact objective, and spawns either normal enemies
 * or a boss depending on the current wave.
 */
public class WaveSpawner {
    private PlayModel model;
    private GraphicsContext gc;
    private ZorpTheSolarAlienFactory factory;
    private List<Enemy> enemies;
    private List<EnemyBullet> enemyBullets;
    private List<Bullet> bullets;
    private static final double PLAYER_SIZE = 80;

    private Boss currentBoss;
    private FactPoint currentObjective;

    public WaveSpawner(PlayModel model, GraphicsContext gc, ZorpTheSolarAlienFactory factory,
                       List<Bullet> bullets, List<Enemy> enemies, List<EnemyBullet> enemyBullets) {
        this.model = model;
        this.gc = gc;
        this.factory = factory;
        this.bullets = bullets;
        this.enemies = enemies;
        this.enemyBullets = enemyBullets;
    }

    public Boss getCurrentBoss() { return currentBoss; }
    public FactPoint getCurrentObjective() { return currentObjective; }

    /**
     * On each fact collected, this method helps us spawn new wave on the screen.
     * It uses MainCharacter class to create and reset main player as well as enemies.
     * When main character clear all enemy, this method also enable collecting the fact to
     * progress to next fact.
     * It also checks if the enemy is a boss to play boss music or normal enemy, and does action accordingly.
     */
    public void spawnWave(double canvasWidth, double canvasHeight) {
        model.startWave();
        currentBoss = null;
        enemies.clear();
        enemyBullets.clear();
        bullets.clear();

        MainCharacterManager player = MainCharacterManager.getInstance();
        player.setX(50);
        player.setY(canvasHeight / 2 - PLAYER_SIZE / 2);

        String fact = model.getWaveFact();
        double objX = canvasWidth * 0.9;
        double objY = canvasHeight * 0.5;
        currentObjective = (FactPoint) factory.createProduct("factPoint", objX, objY);
        currentObjective.setFactText(fact);
        currentObjective.setFactNumber(model.getCurrentWave() + 1);

        if (model.isBossWave()) {
            double bossX = canvasWidth * 0.65;
            double bossY = canvasHeight * 0.35;
            currentBoss = new Boss(gc, bossX, bossY, model.getCurrentPlanet());
            AudioManager.getInstance().playBossMusic();
        } else {
            for (int i = 0; i < model.getEnemiesPerWave(); i++) {
                double spawnX = canvasWidth * 0.3 + Math.random() * (canvasWidth * 0.5);
                double spawnY = 80 + Math.random() * (canvasHeight - 200);
                Enemy enemy = (Enemy) factory.createProduct("enemy", spawnX, spawnY);
                enemies.add(enemy);
            }
        }
    }
}
