package zorp_the_solar_alien.controller;

import zorp_the_solar_alien.view.HowToPlayView;

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
