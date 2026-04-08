package zorp_the_solar_alien.model;

public class HomeModel {
    public enum Screen {HOME, PLAY, SETTINGS, HELP}

    private Screen currentScreen = Screen.HOME;

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(Screen screen) {
        currentScreen = screen;
    }
}
