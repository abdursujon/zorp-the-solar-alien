package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import zorp_the_solar_alien.GameObject;

public class SolarSystem extends GameObject {
    private static SolarSystem instance = null;

    private Image[] planetImages;
    private int currentPlanet = 0;
    private int factsCollected = 0;
    private int maxFacts = 10;

    private double currentScale = 0.0;
    private double targetScale = 0.0;
    private static final double MIN_SCALE = 0.08;
    private static final double MAX_SCALE = 0.6;
    private static final double SCALE_LERP_SPEED = 0.02;

    private double rotationAngle = 0.0;
    private static final double ROTATION_SPEED = 0.15;

    private String[] planetNames = {"Sun", "Mercury", "Venus", "Earth", "Mars",
            "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};

    private SolarSystem(GraphicsContext gc, double x, double y) {
        super(gc, x, y);

        planetImages = new Image[10];
        planetImages[0] = new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm());


        for (int i = 1; i < 10; i++) {
            planetImages[i] = planetImages[0];
        }

        currentScale = MIN_SCALE;
        targetScale = MIN_SCALE;
    }

    public static SolarSystem getInstance(GraphicsContext gc, double x, double y) {
        if (instance == null) {
            instance = new SolarSystem(gc, x, y);
        }
        return instance;
    }

    public void setFactsCollected(int facts) {
        this.factsCollected = facts;
        double progress = Math.min(1.0, (double) factsCollected / maxFacts);
        targetScale = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * progress;
    }

    public void nextPlanet() {
        if (currentPlanet < planetNames.length - 1) {
            currentPlanet++;
            factsCollected = 0;
            currentScale = MIN_SCALE;
            targetScale = MIN_SCALE;
        }
    }

    public void setCurrentPlanet(int planet) {
        this.currentPlanet = Math.min(planet, planetNames.length - 1);
        factsCollected = 0;
        currentScale = MIN_SCALE;
        targetScale = MIN_SCALE;
    }

    public int getCurrentPlanet() {
        return currentPlanet;
    }

    public String getCurrentPlanetName() {
        return planetNames[currentPlanet];
    }

    public void reset() {
        currentPlanet = 0;
        factsCollected = 0;
        currentScale = MIN_SCALE;
        targetScale = MIN_SCALE;
    }

    @Override
    public void update() {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillRect(0, 0, w, h);


        currentScale += (targetScale - currentScale) * SCALE_LERP_SPEED;


        rotationAngle += ROTATION_SPEED;
        if (rotationAngle >= 360) rotationAngle -= 360;

        Image img = planetImages[currentPlanet];
        double size = Math.max(w, h) * currentScale;
        double centerX = w / 2;
        double centerY = h / 2;

        double opacity = 0.3 + 0.7 * ((currentScale - MIN_SCALE) / (MAX_SCALE - MIN_SCALE));
        gc.setGlobalAlpha(opacity);

        gc.save();
        Rotate r = new Rotate(rotationAngle, centerX, centerY);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.drawImage(img, centerX - size / 2, centerY - size / 2, size, size);
        gc.restore();

        gc.setGlobalAlpha(1.0);
    }
}