// FactoryApp.java
package uos.factoryapp;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FactoryApp extends Application {

    Pane root;
    Scene scene;
    Canvas canvas;
    GraphicsContext gc;
    ArrayList<GameObject> list = new ArrayList<GameObject>();
    Factory factory;

    public static void main(String[] args) {
        launch(args);
    }

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (GameObject obj : list) {
                obj.update();
                if (obj.getBoundsInParent().intersects(obj.getBoundsInParent())) {
                    collisionDetected = true;
                }
            }
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Pane();
        scene = new Scene(root, 800, 600);
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        primaryStage.setScene(scene);
        primaryStage.show();
        root.getChildren().add(canvas);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //Invader inv = new Invader(100,100,gc);
        factory = new Factory(gc);
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                list.add(factory.createProduct("invader", i * 50, j * 50));
        timer.start();
    }
}


// GameObject.java
package uos.factoryapp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class GameObject {

    protected Image img;
    protected double x, y;
    protected GraphicsContext gc;

    public GameObject(double x, double y, GraphicsContext gc) {
        super();
        this.x = x;
        this.y = y;
        this.gc = gc;
    }

    public void update() {
        if (img != null)
            gc.drawImage(img, x, y, 30, 30);
    }
}


// Invader.java
package uos.factoryapp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Invader extends GameObject {

    public Invader(double x, double y, GraphicsContext gc) {
        super(x, y, gc);
        img = new Image(Invader.class.getResource("invader.png").toExternalForm());
        update();
    }

    static double dx = 1, yOffset = 0;
    double yStart = y;

    @Override
    public void update() {
        x += dx;
        y = yStart + yOffset;
        if (x > 800 || x < 0) {
            dx = -dx;
            yOffset += 30;
        }
        super.update();
    }
}


// FactoryIF.java
package uos.factoryapp;

public interface FactoryIF {
    GameObject createProduct(String discrim, double x, double y);
}


// Factory.java
package uos.factoryapp;

import javafx.scene.canvas.GraphicsContext;

public class Factory implements FactoryIF {

    GraphicsContext gc;

    @Override
    public GameObject createProduct(String discrim, double x, double y) {
        if (discrim.equals("invader"))
            return new Invader(x, y, gc);
        else if (discrim.equals("saucer"))
            return null; // instantiate a saucer
        else if (discrim.equals("bomb"))
            return null; // instantiate a bomb
        //etc
        return null;
    }

    public Factory(GraphicsContext gc) {
        super();
        this.gc = gc;
    }
}
