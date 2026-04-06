package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Earth extends GameObject{
    private static Earth instance = null;

    private Earth (GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image(Earth.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static Earth getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new Earth(gc, x, y);
        }
        return instance;
    }
}