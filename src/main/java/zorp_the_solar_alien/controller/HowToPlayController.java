package zorp_the_solar_alien.controller;

import zorp_the_solar_alien.view.HowToPlayView;

/**
 * This controller handles how to play view, when needed it shows the view.
 * When user switches screen it hides how to play view.
 */
public class HowToPlayController {
    private HowToPlayView view;

    public HowToPlayController(HowToPlayView view) {
        this.view = view;
    }

    public void show() {
        view.showHowToPlayView();
    }

    public void hide() {
        view.hideHowToPlayView();
    }
}
