package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.GameObject;

public class ZorpTheSolarAlienFactory implements ZorpTheSolarAlienInterface {
    private GraphicsContext gc;

    public ZorpTheSolarAlienFactory(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public GameObject createProduct(String levels, double x, double y) {
        if (levels.equals("sun")) {
            return SolarSystem.getInstance(gc, x, y);
        }
        return null;
    }
}
