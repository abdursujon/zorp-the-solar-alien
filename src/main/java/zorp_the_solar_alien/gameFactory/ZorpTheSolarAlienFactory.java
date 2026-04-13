package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.GameObject;
import zorp_the_solar_alien.SingletonObjects.SolarSystem;


/**
 * Implements the ZorpTheSolarAlienInterface createProduct method which is core to factory design pattern.
 */
public class ZorpTheSolarAlienFactory implements ZorpTheSolarAlienInterface {
    private GraphicsContext gc;


    public ZorpTheSolarAlienFactory(GraphicsContext gc) {
        this.gc = gc;
    }


    /**
     * Creates and returns a game object based on the given type string by createProduct from the main interface.
     * It supports type of objects which are sun, bullet, enemyBullet, bossBullet, enemy, factPoint, gameInfoBar.
     * Depending on what is called on game play, this method helps us switch between objects.
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
