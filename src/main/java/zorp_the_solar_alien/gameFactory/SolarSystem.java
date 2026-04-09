package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import zorp_the_solar_alien.GameObject;

public class SolarSystem extends GameObject {
    private static SolarSystem instance = null;

    private Image[] planetImages;
    private int currentPlanet = 0;
    private int factsCollected = 0;
    private int maxFacts = 10;

    private static final int STAR_COUNT = 150;
    private double[] starX, starY, starSize, starPhase, starSpeed;
    private double[] starBrightness;

    private double currentScale = 0.0;
    private double targetScale = 0.0;
    private static final double MIN_SCALE = 0.08;
    private static final double MAX_SCALE = 0.6;
    private static final double SCALE_LERP_SPEED = 0.02;

    private double rotationAngle = 0.0;
    private static final double ROTATION_SPEED = 0.15;

    private String[] planetNames = {"Sun", "Mercury", "Venus", "Earth", "Mars",
            "Jupiter", "Saturn", "Uranus", "Neptune"};

    private SolarSystem(GraphicsContext gc, double x, double y) {
        super(gc, x, y);

        planetImages = new Image[9];
        planetImages[0] = new Image(getClass().getResource("/sun/sun.png").toExternalForm());
        planetImages[1] = new Image(getClass().getResource("/planets/mercury.png").toExternalForm());
        planetImages[2] = new Image(getClass().getResource("/planets/venus.png").toExternalForm());
        planetImages[3] = new Image(getClass().getResource("/planets/earth.png").toExternalForm());
        planetImages[4] = new Image(getClass().getResource("/planets/mars.png").toExternalForm());
        planetImages[5] = new Image(getClass().getResource("/planets/jupiter.png").toExternalForm());
        planetImages[6] = new Image(getClass().getResource("/planets/saturn.jpg").toExternalForm());
        planetImages[7] = new Image(getClass().getResource("/planets/uranus.png").toExternalForm());
        planetImages[8] = new Image(getClass().getResource("/planets/neptune.png").toExternalForm());

        currentScale = MIN_SCALE;
        targetScale = MIN_SCALE;

        starX = new double[STAR_COUNT];
        starY = new double[STAR_COUNT];
        starSize = new double[STAR_COUNT];
        starPhase = new double[STAR_COUNT];
        starSpeed = new double[STAR_COUNT];
        starBrightness = new double[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starX[i] = Math.random();
            starY[i] = Math.random();
            starSize[i] = 1 + Math.random() * 3;
            starPhase[i] = Math.random() * Math.PI * 2;
            starSpeed[i] = 0.02 + Math.random() * 0.05;
            starBrightness[i] = 0.5 + Math.random() * 0.5;
        }
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

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        for (int i = 0; i < STAR_COUNT; i++) {
            starPhase[i] += starSpeed[i];
            double twinkle = starBrightness[i] * (0.6 + 0.4 * Math.sin(starPhase[i]));
            gc.setFill(Color.color(1, 1, 1, twinkle));
            gc.fillOval(starX[i] * w, starY[i] * h, starSize[i], starSize[i]);
        }

        currentScale += (targetScale - currentScale) * SCALE_LERP_SPEED;


        rotationAngle += ROTATION_SPEED;
        if (rotationAngle >= 360) rotationAngle -= 360;

        Image img = planetImages[currentPlanet];
        double baseSize = Math.max(w, h) * currentScale;
        double imgRatio = img.getWidth() / img.getHeight();
        double drawW, drawH;
        if (imgRatio >= 1) {
            drawW = baseSize;
            drawH = baseSize / imgRatio;
        } else {
            drawH = baseSize;
            drawW = baseSize * imgRatio;
        }
        double centerX = w / 2;
        double centerY = h / 2;

        double opacity = 0.3 + 0.7 * ((currentScale - MIN_SCALE) / (MAX_SCALE - MIN_SCALE));
        gc.setGlobalAlpha(opacity);

        gc.save();
        Rotate r = new Rotate(rotationAngle, centerX, centerY);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.drawImage(img, centerX - drawW / 2, centerY - drawH / 2, drawW, drawH);
        gc.restore();

        gc.setGlobalAlpha(1.0);
    }
}