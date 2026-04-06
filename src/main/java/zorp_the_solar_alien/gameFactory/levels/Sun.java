package zorp_the_solar_alien.gameFactory.levels;

import javafx.scene.canvas.GraphicsContext;
import zorp_the_solar_alien.gameFactory.GameObject;
import javafx.scene.image.Image;

public class Sun extends GameObject{
    private static Sun instance = null;

    private Sun(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        img = new Image(Sun.class.getResource("/assets/levels/sun/sun-bg.jpg").toExternalForm());
    }

    public static Sun getInstance(GraphicsContext gc, double x, double y){
        if(instance == null){
            instance = new Sun(gc, x, y);
        }
        return instance;
    }

    @Override
    public void update() {
        gc.drawImage(img, 0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }
}