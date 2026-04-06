package zorp_the_solar_alien.controller;

import zorp_the_solar_alien.SingletonObject.MainCharacterManager;
import zorp_the_solar_alien.gameFactory.SolarSystem;
import zorp_the_solar_alien.model.PlayModel;
import zorp_the_solar_alien.view.PlayView;
import javafx.animation.AnimationTimer;

public class PlayController {
    private PlayModel model;
    private PlayView view;
    private SolarSystem solarSystem;
    private AnimationTimer gameLoop;

    public PlayController(PlayModel model, PlayView view) {
        this.model = model;
        this.view = view;
        this.solarSystem = SolarSystem.getInstance(view.gc, 0, 0);

        view.canvas.visibleProperty().addListener((obs, wasVisible, isVisible) -> {
            if (isVisible) {
                startGame();
            } else {
                stopGame();
            }
        });
    }

    public void startGame() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double w = view.canvas.getWidth();
                double h = view.canvas.getHeight();
                view.gc.clearRect(0, 0, w, h);
                solarSystem.update();
                MainCharacterManager.getInstance().update();
            }
        };
        gameLoop.start();
    }

    public void stopGame() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
}
