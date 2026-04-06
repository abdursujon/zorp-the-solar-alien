package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Pluto extends GameObject{
    private static  Pluto instance = null;

    private  Pluto (GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image( Pluto.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static  Pluto getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new  Pluto(gc, x, y);
        }
        return instance;
    }
}