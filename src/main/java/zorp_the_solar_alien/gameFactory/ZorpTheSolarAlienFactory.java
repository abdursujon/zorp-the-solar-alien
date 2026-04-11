package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.GameObject;

/**
 * This class implements the ZorpTheSolarAlienInterface to support the factory design pattern.
 * It creates different game objects based on the type string passed to createProduct method.
 * This allows the game to create bullets, enemies, fact points, the game info bar,
 * and the solar system background without knowing the exact class being created.
 */
public class ZorpTheSolarAlienFactory implements ZorpTheSolarAlienInterface {
    private GraphicsContext gc;

    /**
     * Creates the factory with a reference to the graphics context used for rendering all game objects.
     */
    public ZorpTheSolarAlienFactory(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Creates and returns a game object based on the given type string.
     * Supported types: sun, bullet, enemyBullet, bossBullet, enemy, factPoint, gameInfoBar.
     */
    @Override
    public GameObject createProduct(String type, double x, double y) {
        switch (type) {
            case "sun":
                return SolarSystem.getInstance(gc, x, y);
            case "bullet":
                return new Bullet(gc, x, y, "zorp");
            case "enemyBullet":
                return new Bullet(gc, x, y, "enemy");
            case "bossBullet":
                return new Bullet(gc, x, y, "boss");
            case "enemy":
                return new Enemy(gc, x, y);
            case "factPoint":
                return new FactPoint(gc, x, y);
            case "gameInfoBar":
                return new GameInfoBar(gc, x, y);
            default:
                return null;
        }
    }
}
