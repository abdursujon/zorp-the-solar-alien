package zorp_the_solar_alien.controller;

import zorp_the_solar_alien.model.HowToPlayModel;
import zorp_the_solar_alien.view.HowToPlayView;


/**
 * Connects how to play view and model.
 * When pressed the click on screen for how to play view it shows the view.
 * When user switches screen it hides the view again.
 */
public class HowToPlayController {
   
	private HowToPlayView view;
    private HowToPlayModel model;

    public HowToPlayController(HowToPlayModel model, HowToPlayView view) {
        this.model = model;
        this.view = view;
        view.setData(model.getTitle(), model.getStory(), model.getObjectivesTitle(),
                model.getObjectives(), model.getControlsTitle(), model.getControls());
    }

    public void show() {
        view.showHowToPlayView();
    }

    public void hide() {
        view.hideHowToPlayView();
    }
    
}
