package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zorp_the_solar_alien.GameObject;

/**
 * GameInfoBar extends GameObject base class and is designed to support the factory pattern.
 * It renders the game info bar at the top of the play screen showing the player's HP bar,
 * current score, planet name, wave progress, and enemies killed count.
 */
public class GameInfoBar extends GameObject {
    private int hp, maxHp, score;
    private String planetName;
    private int currentWave, totalWaves, waveEnemiesKilled, enemiesPerWave;

    public GameInfoBar(GraphicsContext gc, double x, double y) {
        super(gc, 0, 0);
    }

    /**
     * Override the provided update method from GameObject.
     * It draws the main character health bar, total score, wave and enemies count.
     */
    @Override
    public void update() {
        double w = gc.getCanvas().getWidth();
        double topY = 80;

        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, topY, w, 55);

        gc.setFill(Color.DARKRED);
        gc.fillRect(10, topY + 10, 200, 20);
        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(10, topY + 10, 200 * (hp / (double) maxHp), 20);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(10, topY + 10, 200, 20);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(Color.WHITE);
        gc.fillText("HP: " + hp + "/" + maxHp, 10, topY + 48);

        gc.fillText("Score: " + score, w / 2 - 50, topY + 25);

        gc.fillText(planetName, w / 2 - 30, topY + 45);

        gc.fillText("Wave: " + (currentWave + 1) + "/" + totalWaves, w - 200, topY + 25);

        gc.fillText("Enemies: " + waveEnemiesKilled + "/" + enemiesPerWave, w - 200, topY + 45);

    }


    /**
     * Updates the game info bar data with the latest game state from the play model.
     */
    public void setData(int hp, int maxHp, int score, String planetName,
                        int currentWave, int totalWaves, int waveEnemiesKilled, int enemiesPerWave) {
        this.hp = hp;
        this.maxHp = maxHp;
        this.score = score;
        this.planetName = planetName;
        this.currentWave = currentWave;
        this.totalWaves = totalWaves;
        this.waveEnemiesKilled = waveEnemiesKilled;
        this.enemiesPerWave = enemiesPerWave;
    }

}
