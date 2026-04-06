package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Saturn extends GameObject{
    private static  Saturn instance = null;

    private  Saturn (GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image( Saturn.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static  Saturn getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new  Saturn(gc, x, y);
        }
        return instance;
    }
}