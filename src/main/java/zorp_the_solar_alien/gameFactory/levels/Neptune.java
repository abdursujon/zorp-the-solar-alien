package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Neptune extends GameObject{
    private static  Neptune instance = null;

    private  Neptune (GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image( Neptune.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static  Neptune getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new  Neptune(gc, x, y);
        }
        return instance;
    }
}