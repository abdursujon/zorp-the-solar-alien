package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Mars extends GameObject{
    private static  Mars instance = null;

    private  Mars (GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image( Mars.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static  Mars getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new  Mars(gc, x, y);
        }
        return instance;
    }
}