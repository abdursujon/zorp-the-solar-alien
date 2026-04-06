package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Mercury extends GameObject{
    private static Mercury instance = null;

    private Mercury(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image(Mercury.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static Mercury getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new Mercury(gc, x, y);
        }
        return instance;
    }
}