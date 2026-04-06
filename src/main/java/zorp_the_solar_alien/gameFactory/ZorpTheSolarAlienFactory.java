package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.levels.*;

public class ZorpTheSolarAlienFactory implements ZorpTheSolarAlienInterface {
    private GraphicsContext gc;

    public ZorpTheSolarAlienFactory(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public GameObject createProduct(String levels, double x, double y) {
        if (levels.equals("sun")) {
            return Sun.getInstance(gc, x, y);
        } else if (levels.equals("mercury")) {
            return Mercury.getInstance(gc, x, y);
        } else if (levels.equals("venus")) {
            return Venus.getInstance(gc, x, y);
        } else if (levels.equals("earth")) {
            return Earth.getInstance(gc, x, y);
        } else if (levels.equals("mars")) {
            return Mars.getInstance(gc, x, y);
        } else if (levels.equals("jupiter")) {
            return Jupiter.getInstance(gc, x, y);
        } else if (levels.equals("saturn")) {
            return Saturn.getInstance(gc, x, y);
        } else if (levels.equals("uranus")) {
            return Uranus.getInstance(gc, x, y);
        } else if (levels.equals("neptune")) {
            return Neptune.getInstance(gc, x, y);
        } else if (levels.equals("pluto")) {
            return Pluto.getInstance(gc, x, y);
        }
        return null;
    }

}
