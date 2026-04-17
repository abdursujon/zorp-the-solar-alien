package zorp_the_solar_alien.SingletonObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import zorp_the_solar_alien.GameObject;


/**
 * Implements singleton design pattern as we have only one main character.
 * Manages main character movement, shooting, melee attacks and sprite animation.
 * The class also handles keyboard and mouse input to control the character on play screen.
 */
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


    /**
     * Private constructor to enforce singleton design pattern.
     * Loads main character idle and sprite images from resource directory.
     * It also sets the starting position of the character, and add it to the screen.
     */
    private MainCharacterManager(GraphicsContext gc, Pane root) {
        super(gc, 200, 400);
        this.root = root;
        idleImage = new Image(getClass().getResource("/zorp_the_solar_alien/assets/zorp/shoot.png").toExternalForm());
        shootImage = idleImage;

        meleeFrames = new Image[] {
                new Image(getClass().getResource("/zorp_the_solar_alien/assets/zorp/img.png").toExternalForm()),
                new Image(getClass().getResource("/zorp_the_solar_alien/assets/zorp/img_1.png").toExternalForm()),
                new Image(getClass().getResource("/zorp_the_solar_alien/assets/zorp/img_2.png").toExternalForm()),
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

    
    /**
     * Returns the single instance of main character which can is used by other class to
     * use create instance of main player. If the character does not exist on the screen, it creates one.
     */
    public static MainCharacterManager getInstance(GraphicsContext gc, Pane root) {
        if (instance == null) {
            instance = new MainCharacterManager(gc, root);
        }
        return instance;
    }


    public static MainCharacterManager getInstance() {
        return instance;
    }

    
    /**
     * Here we override the provided method from GameObject base class.
     * In this class it moves the player based on key actions. It handles
     * jump physics with gravity effect to make the jump look natural.
     * It also makes sure the player stays on screen and handles mele animation frames, switching the image based on
     * current key action and if facing left it flips the image.
     */
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


    /**
     * Sets the movement state depending on which key is pressed by user or released.
     */
    public void setInput(String key, boolean state) {
        switch (key) {
            case "W": movingUp = state; break;
            case "S": movingDown = state; break;
            case "A": movingLeft = state; break;
            case "D": movingRight = state; break;
            case "SPACE": jumping = state; break;
        }
    }


    /**
     * Sets the attack option based on which mouse button clicked.
     * If left click, activates shooting and if right click activates melee.
     */
    public void setMouseInput(String button, boolean state) {
        switch (button) {
            case "PRIMARY": shooting = state; break;
            case "SECONDARY": melee = state; break;
        }
    }


    public boolean isShooting() {
        return shooting;
    }


    public boolean isMelee() {
        return melee;
    }


    public boolean isFacingRight() {
        return facingRight;
    }


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


    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }


    public void setX(double x) {
        this.x = x;
    }


    public void setY(double y) {
        this.y = y;
    }


    public void setVisible(boolean visible) {
        imageView.setVisible(visible);
    }
    
}