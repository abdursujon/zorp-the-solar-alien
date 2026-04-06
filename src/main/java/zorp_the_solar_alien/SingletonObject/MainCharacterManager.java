package zorp_the_solar_alien.SingletonObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import zorp_the_solar_alien.GameObject;

public class MainCharacterManager extends GameObject {
    private static MainCharacterManager instance;

    private Image horizontalSheet;
    private double speed = 5;
    private int frame = 0;
    private int frameCounter = 0;
    private boolean movingLeft, movingRight, movingUp, movingDown;
    private boolean shooting;
    private boolean melee;
    private boolean jumping;

    private static final int FRAMES = 8;
    private static final int H_FRAME_W = 348;
    private static final int H_FRAME_H = 209;
    private static final int DRAW_SIZE = 80;

    private MainCharacterManager(GraphicsContext gc) {
        super(gc, 200, 400);
        horizontalSheet = new Image(getClass().getResource("/assets/levels/sun/zorp-1.png").toExternalForm());
        x = 200;
        y = 400;
    }

    public static MainCharacterManager getInstance() {
        return instance;
    }

    public static MainCharacterManager getInstance(GraphicsContext gc) {
        if (instance == null) {
            instance = new MainCharacterManager(gc);
        }
        return instance;
    }

    @Override
    public void update() {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        if (movingLeft) x -= speed;
        if (movingRight) x += speed;
        if (movingUp) y -= speed;
        if (movingDown) y += speed;

        x = Math.max(0, Math.min(x, w - DRAW_SIZE));
        y = Math.max(60, Math.min(y, h - DRAW_SIZE));


        boolean isMoving = movingLeft || movingRight || movingUp || movingDown;
        gc.drawImage(horizontalSheet,
                frame * H_FRAME_W, 0, H_FRAME_W, H_FRAME_H,
                x, y, DRAW_SIZE, DRAW_SIZE);
    }


    public void setInput(String key, boolean state) {
        switch (key) {
            case "W": movingUp = state; break;
            case "S": movingDown = state; break;
            case "A": movingLeft = state; break;
            case "D": movingRight = state; break;
            case "SPACE": jumping = state; break;
        }
    }

    public void setMouseInput(String button, boolean state) {
        switch (button) {
            case "PRIMARY": shooting = state; break;
            case "SECONDARY": melee = state; break;
        }
    }

    public boolean isShooting() { return shooting; }
    public boolean isMelee() { return melee; }
    public boolean isJumping() { return jumping; }

    public void reset() {
        x = 200;
        y = 400;
        movingLeft = false;
        movingRight = false;
        movingUp = false;
        movingDown = false;
        shooting = false;
        melee = false;
        jumping = false;
        frame = 0;
        frameCounter = 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}