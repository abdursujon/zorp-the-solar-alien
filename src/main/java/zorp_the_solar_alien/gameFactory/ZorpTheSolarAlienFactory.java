package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.GameObject;

public class ZorpTheSolarAlienFactory implements ZorpTheSolarAlienInterface {
    private GraphicsContext gc;

    public ZorpTheSolarAlienFactory(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public GameObject createProduct(String type, double x, double y) {
        switch (type) {
            case "sun":
                return SolarSystem.getInstance(gc, x, y);
            case "bullet":
                return new Bullet(gc, x, y);
            case "enemyBullet":
                return new EnemyBullet(gc, x, y);
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
