package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Uranus extends GameObject{
    private static  Uranus instance = null;

    private  Uranus (GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image( Uranus.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static  Uranus getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new  Uranus(gc, x, y);
        }
        return instance;
    }
}