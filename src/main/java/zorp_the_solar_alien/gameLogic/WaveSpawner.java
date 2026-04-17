package zorp_the_solar_alien.gameLogic;

import java.util.List;
import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.gameFactory.*;
import zorp_the_solar_alien.model.PlayModel;
import javafx.scene.canvas.GraphicsContext;


/**
 * Spawns enemy wave after each fact collected.
 * It used by playController to spawn random number of enemies.
 */
public class WaveSpawner {
   
	private PlayModel model;
    private GraphicsContext gc;
    private ZorpTheSolarAlienFactory factory;
    private List<Enemy> enemies;
    private List<Bullet> enemyBullets;
    private List<Bullet> bullets;
    private static final double PLAYER_SIZE = 80;
    private Boss currentBoss;
    private FactPoint currentObjective;

    
    /**
     * The constructor create wave spawner with reference to required classes to handle enemy wave.
     */
    public WaveSpawner(PlayModel model, GraphicsContext gc, ZorpTheSolarAlienFactory factory,
                       List<Bullet> bullets, List<Enemy> enemies, List<Bullet> enemyBullets) {
        this.model = model;
        this.gc = gc;
        this.factory = factory;
        this.bullets = bullets;
        this.enemies = enemies;
        this.enemyBullets = enemyBullets;
    }


    public Boss getCurrentBoss() {
        return currentBoss;
    }


    public FactPoint getCurrentObjective() {
        return currentObjective;
    }


    /**
     * Manages enemy spawn logic by clearing all existing objects from the screen when all enemy dies.
     * It resets the player and enemy position using the factory pattern. Creates the fact objects
     * and if boss level required it spawn boss instead of normal enemy. Also handle
     * music depending on enemy type.
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
