package zorp_the_solar_alien.controller;

import zorp_the_solar_alien.model.PlayModel;
import zorp_the_solar_alien.view.PlayView;

public class PlayController {
    private PlayModel model;
    private PlayView view;

    public PlayController(PlayModel model, PlayView view) {
        this.model = model;
        this.view = view;

        for (int i = 0; i < view.levelButtons.length; i++) {
            int levelIndex = i;
            view.levelButtons[i].setOnAction(e -> handleLevelSelect(levelIndex));
        }
    }

    private void handleLevelSelect(int levelIndex) {
        System.out.println("Selected level: " + model.getLevelNames()[levelIndex]);
    }
}
