package zorp_the_solar_alien.gameFactory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import zorp_the_solar_alien.GameObject;

public class SolarSystem extends GameObject {
    private static SolarSystem instance = null;

    private Image[][] planetLayers;
    private int currentPlanet = 0;
    private double[] scrollSpeeds;
    private double[] offsets;

    private String[] planetNames = {"Sun", "Mercury", "Venus", "Earth", "Mars",
            "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};

    private SolarSystem(GraphicsContext gc, double x, double y) {
        super(gc, x, y);

        planetLayers = new Image[10][];

        // Sun
        planetLayers[0] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Mercury
        planetLayers[1] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Venus
        planetLayers[2] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Earth
        planetLayers[3] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Mars
        planetLayers[4] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Jupiter
        planetLayers[5] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Saturn
        planetLayers[6] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Uranus
        planetLayers[7] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Neptune
        planetLayers[8] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        // Pluto
        planetLayers[9] = new Image[]{
                new Image(getClass().getResource("/sun/sun-bg1.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg2.png").toExternalForm()),
                new Image(getClass().getResource("/sun/sun-bg3.png").toExternalForm()),
        };

        scrollSpeeds = new double[]{0.5, 0.7, 1.0, 0.8, 1.5, 1.2, 2.0, 1.8};
        offsets = new double[]{400, 900, 200, 700, 100, 500, 300, 800};
    }

    public static SolarSystem getInstance(GraphicsContext gc, double x, double y) {
        if (instance == null) {
            instance = new SolarSystem(gc, x, y);
        }
        return instance;
    }

    public void nextPlanet() {
        if (currentPlanet < planetNames.length - 1) {
            currentPlanet++;
        }
    }

    public int getCurrentPlanet() {
        return currentPlanet;
    }

    public String getCurrentPlanetName() {
        return planetNames[currentPlanet];
    }

    public void reset() {
        currentPlanet = 0;
    }

    @Override
    public void update() {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillRect(0, 0, w, h);

        Image[] layers = planetLayers[currentPlanet];

        double[] sizes = {100, 80, 150, 120, 200, 160, 280, 220};
        double[] opacities = {1.0, 0.8, 0.6, 0.7, 0.4, 0.5, 0.3, 0.35};
        double[] yPositions = {h * 0.2, h * 0.7, h * 0.5, h * 0.3, h * 0.6, h * 0.15, h * 0.4, h * 0.8};

        for (int i = 0; i < offsets.length; i++) {
            offsets[i] -= scrollSpeeds[i];
            if (offsets[i] <= -sizes[i]) {
                offsets[i] = w;
            }
            gc.setGlobalAlpha(opacities[i]);
            gc.drawImage(layers[i % layers.length], offsets[i], yPositions[i] - sizes[i] / 2, sizes[i], sizes[i]);
        }
        gc.setGlobalAlpha(1.0);
    }
}