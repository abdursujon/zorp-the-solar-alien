package zorp_the_solar_alien.gameLogic;

import java.util.List;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import zorp_the_solar_alien.SingletonObjects.AudioManager;
import zorp_the_solar_alien.SingletonObjects.MainCharacterManager;
import zorp_the_solar_alien.gameFactory.*;
import zorp_the_solar_alien.model.PlayModel;

/**
 * This class is delegated by PlayController to handle all boss related game logic.
 * It updates the boss movement and shooting, and handles the boss defeated sequence
 * including explosion animation, score reward, and unlocking the final fact objective.
 */
public class BossHandler {
    private PlayModel model;
    private Pane root;
    private ZorpTheSolarAlienFactory factory;
    private List<EnemyBullet> enemyBullets;
    private static final double PLAYER_SIZE = 80;

    public BossHandler(PlayModel model, Pane root, ZorpTheSolarAlienFactory factory, List<EnemyBullet> enemyBullets) {
        this.model = model;
        this.root = root;
        this.factory = factory;
        this.enemyBullets = enemyBullets;
    }

    /**
     * This method helps us determine if a boss is active, if not active or dead this method is skipped.
     * Otherwise, if boss is active and alive, boss chase the player. When shooting cooldown is done,
     * boss fire a new bullet aiming towards the player. The boss also fire random bullets through using
     * factory product by using createProduct method.
     */
    public void updateBoss(Boss currentBoss, long now) {
        if (currentBoss == null || !currentBoss.isActive()) {
            return;
        }

        MainCharacterManager player = MainCharacterManager.getInstance();
        double playerCX = player.getX() + PLAYER_SIZE / 2;
        double playerCY = player.getY() + PLAYER_SIZE / 2;

        currentBoss.setChaseTarget(playerCX, playerCY);
        currentBoss.update();

        if (currentBoss.canShoot(now)) {
            EnemyBullet enemyBossBullet = (EnemyBullet) factory.createProduct("enemyBullet",
                    currentBoss.getCenterX(), currentBoss.getCenterY());
            enemyBossBullet.setTarget(playerCX, playerCY);
            enemyBossBullet.setAmmoImage(currentBoss.getAmmoImage());
            enemyBullets.add(enemyBossBullet);
            currentBoss.markShot(now);
        }
    }

    /**
     * When player defeat the boss, stop the boss music by calling singleton AudioManager method,
     * then play defeated music, and after that start playing normal music.
     * When boss defeated, on the position of the boss, this method help us create the explosion effect.
     * It update the score of the player and add 1000 points, and full heal the player and register the boss killed.
     * After all animation done, it removed all relevant objects from the screen, and unlock the final facts.
     */
    public void onBossDefeated(Boss currentBoss, FactPoint currentObjective) {
        AudioManager.getInstance().stopBossMusic();
        AudioManager.getInstance().playBossBiten();
        AudioManager.getInstance().playHomeMusic();

        ImageView explosionView = new ImageView(new Image(getClass().getResource("/enemies/boss/explosion.gif").toExternalForm()));
        explosionView.setFitWidth(180);
        explosionView.setFitHeight(180);
        explosionView.setPreserveRatio(true);
        explosionView.setLayoutX(currentBoss.getCenterX() - 90);
        explosionView.setLayoutY(currentBoss.getCenterY() - 90);
        root.getChildren().add(explosionView);
        explosionView.toFront();

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> root.getChildren().remove(explosionView));
        pause.play();

        model.registerKill();
        model.addScore(1000);
        model.heal(100);
        enemyBullets.clear();

        if (currentObjective != null) {
            currentObjective.unlock();
        }
    }
}
