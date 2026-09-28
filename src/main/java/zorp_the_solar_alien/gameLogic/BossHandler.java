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
 * Handles all boss related game logic for playController.
 * It updates boss movement and shooting. Also handles the sequence what happens when boss is defeated.
 */
public class BossHandler {
    private PlayModel model;
    private Pane root;
    private ZorpTheSolarAlienFactory factory;
    private List<Bullet> enemyBullets;
    private static final double PLAYER_SIZE = 80;


    /**
     * The constructor creates boss handler with reference to the play model, and root pane for explosion animation.
     * It uses factory pattern to create boss bullets on the play screen.
     */
    public BossHandler(PlayModel model, Pane root, ZorpTheSolarAlienFactory factory, List<Bullet> enemyBullets) {
        this.model = model;
        this.root = root;
        this.factory = factory;
        this.enemyBullets = enemyBullets;
    }


    /**
     * Checks if the boss is alive, if yes, the boss chase the player and draws the boss on the screen.
     * It also checks if boss shooting cooldown is finished, if yes it creates boss bullet
     * using factory pattern aiming at the player central.
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
            Bullet bossBullet = (Bullet) factory.createProduct("bossBullet",
                    currentBoss.getCenterX(), currentBoss.getCenterY());
            bossBullet.setTarget(playerCX, playerCY);
            enemyBullets.add(bossBullet);
            currentBoss.markShot(now);
        }
    }


    /**
     * When player defeat the boss, it stops the boss music audio, and plays defeated sound instead.
     * Also in the place of where boss was defeated, it creates explosion effect. Then it awards the player 1000 points,
     * regenerate full hp and register the kills. Also, it clears all enemy bullets, boss objects from the screen and unlocks
     * the final facts.
     */
    public void onBossDefeated(Boss currentBoss, FactPoint currentObjective) {
        AudioManager.getInstance().stopBossMusic();
        AudioManager.getInstance().playBossBitten();
        AudioManager.getInstance().playHomeMusic();

        ImageView explosionView = new ImageView(new Image(getClass().getResource("/zorp_the_solar_alien/assets/enemies/boss/explosion.gif").toExternalForm()));
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
