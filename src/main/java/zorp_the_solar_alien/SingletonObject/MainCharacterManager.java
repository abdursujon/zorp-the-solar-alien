package zorp_the_solar_alien.SingletonObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import zorp_the_solar_alien.GameObject;

public class MainCharacterManager extends GameObject {
    private static MainCharacterManager instance;

    private Image idleImage;
    private Image shootImage;
    private ImageView imageView;
    private Pane root;

    private double speed = 5;
    private boolean movingLeft, movingRight, movingUp, movingDown;
    private boolean shooting;
    private boolean melee;
    private boolean jumping;
    private boolean facingRight = true;

    private boolean jumpPlaying = false;
    private double jumpVelocity = 0;
    private double groundY;
    private static final double GRAVITY = 0.5;
    private static final double JUMP_FORCE = -12;
    private static final int DRAW_SIZE = 80;

    private static final int MELEE_FRAMES = 3;
    private Image[] meleeFrames;
    private int meleeFrame = 0;
    private int meleeCounter = 0;
    private boolean meleePlaying = false;

    private MainCharacterManager(GraphicsContext gc, Pane root) {
        super(gc, 200, 400);
        this.root = root;
        idleImage = new Image(getClass().getResource("/zorp/shoot.png").toExternalForm());
        shootImage = new Image(getClass().getResource("/zorp/shoot.png").toExternalForm());

        meleeFrames = new Image[] {
                new Image(getClass().getResource("/zorp/img.png").toExternalForm()),
                new Image(getClass().getResource("/zorp/img_1.png").toExternalForm()),
                new Image(getClass().getResource("/zorp/img_2.png").toExternalForm()),
        };

        x = 200;
        y = 400;
        groundY = y;

        imageView = new ImageView(idleImage);
        imageView.setFitWidth(DRAW_SIZE);
        imageView.setFitHeight(DRAW_SIZE);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        root.getChildren().add(imageView);
    }

    public static MainCharacterManager getInstance(GraphicsContext gc, Pane root) {
        if (instance == null) {
            instance = new MainCharacterManager(gc, root);
        }
        return instance;
    }

    public static MainCharacterManager getInstance() {
        return instance;
    }

    @Override
    public void update() {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        if (movingLeft) x -= speed;
        if (movingRight) x += speed;

        if (jumping && !jumpPlaying) {
            jumpPlaying = true;
            jumpVelocity = JUMP_FORCE;
            groundY = y;
        }

        if (jumpPlaying) {
            y += jumpVelocity;
            jumpVelocity += GRAVITY;
            if (y >= groundY) {
                y = groundY;
                jumpPlaying = false;
                jumpVelocity = 0;
            }
        } else {
            if (movingUp) y -= speed;
            if (movingDown) y += speed;
        }

        x = Math.max(0, Math.min(x, w - DRAW_SIZE));
        y = Math.max(60, Math.min(y, h - DRAW_SIZE));

        if (melee && !meleePlaying) {
            meleePlaying = true;
            meleeFrame = 0;
            meleeCounter = 0;
        }

        if (meleePlaying) {
            meleeCounter++;
            if (meleeCounter % 5 == 0) {
                meleeFrame++;
                if (meleeFrame >= MELEE_FRAMES) {
                    meleePlaying = false;
                    meleeFrame = 0;
                }
            }
            if (meleePlaying) {
                imageView.setImage(meleeFrames[meleeFrame]);
            } else {
                imageView.setImage(idleImage);
            }
        } else if (shooting) {
            if (imageView.getImage() != shootImage) imageView.setImage(shootImage);
        } else {
            if (imageView.getImage() != idleImage) imageView.setImage(idleImage);
        }

        if (movingLeft) {
            facingRight = false;
            imageView.setScaleX(-1);
        } else if (movingRight) {
            facingRight = true;
            imageView.setScaleX(1);
        }

        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
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
    public boolean isFacingRight() { return facingRight; }

    public void reset() {
        x = 200;
        y = 400;
        groundY = y;
        movingLeft = false;
        movingRight = false;
        movingUp = false;
        movingDown = false;
        shooting = false;
        melee = false;
        jumping = false;
        jumpPlaying = false;
        meleePlaying = false;
        meleeFrame = 0;
        meleeCounter = 0;
        jumpVelocity = 0;
        imageView.setImage(idleImage);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setVisible(boolean visible) { imageView.setVisible(visible); }
}