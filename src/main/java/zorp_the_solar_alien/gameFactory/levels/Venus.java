package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Venus extends GameObject{
    private static  Venus instance = null;

    private  Venus (GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image( Venus.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static  Venus getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new  Venus(gc, x, y);
        }
        return instance;
    }
}