package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Jupiter extends GameObject{
    private static Jupiter instance = null;

    private Jupiter (GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image(Jupiter.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static Jupiter getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new Jupiter(gc, x, y);
        }
        return instance;
    }
}