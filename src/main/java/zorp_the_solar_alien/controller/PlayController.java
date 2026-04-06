package zorp_the_solar_alien.controller;

import zorp_the_solar_alien.model.PlayModel;
import zorp_the_solar_alien.view.PlayView;
import javafx.animation.AnimationTimer;
import zorp_the_solar_alien.gameFactory.GameObject;
import zorp_the_solar_alien.gameFactory.ZorpTheSolarAlienFactory;

public class PlayController {
    private PlayModel model;
    private PlayView view;
    private ZorpTheSolarAlienFactory factory;
    private GameObject currentLevel;
    private AnimationTimer gameLoop;

    public PlayController(PlayModel model, PlayView view) {
        this.model = model;
        this.view = view;
        this.factory = new ZorpTheSolarAlienFactory(view.gc);

        for (int i = 0; i < view.levelButtons.length; i++) {
            int levelIndex = i;
            view.levelButtons[i].setOnAction(e -> handleLevelSelect(levelIndex));
        }
    }

    private void handleLevelSelect(int levelIndex) {
        String levelName = model.getLevelNames()[levelIndex].toLowerCase();
        currentLevel = factory.createProduct(levelName, 0, 0);

        view.levelGrid.setVisible(false);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double w = view.canvas.getWidth();
                double h = view.canvas.getHeight();
                view.gc.clearRect(0, 0, w, h);
                currentLevel.update();
            }
        };
        gameLoop.start();
    }
}
