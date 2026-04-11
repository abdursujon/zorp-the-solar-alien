package zorp_the_solar_alien.model;

/**
 * HowToPlayModel class handles data needed by HowToPlayController to create MVC pattern.
 * The class provides different method which can be used to retrieve data by controller
 * so it can use them to show information on view.
 */
public class HowToPlayModel {

    public String getTitle() {
        return "Zorp The Solar Alien";
    }


    public String getStory() {
        return "Hello there, Zorp. You are indeed a brave alien from \"Petunsky\" " +
                "who has managed to survive so far. You have travelled light years " +
                "throughout the galaxy to reach the solar system. But unfortunately, " +
                "the ship that carried you has broken down. Now, in order to get back " +
                "to the planet you came from, you must explore the solar system, " +
                "learn about it and find secret locations on different planets to " +
                "gather resources to rebuild a new ship so you can go back to your " +
                "planet. To do that, you must first learn about all solar planets " +
                "and the sun. Beware, there will be hostile entities everywhere; " +
                "you must fight to survive.";
    }


    public String getObjectivesTitle() {
        return "Objectives";
    }


    public String getObjectives() {
        return "Your mission is to explore all the planets in the solar system." +
                " Gather knowledge about them, and so you are aware of their atmosphere before you can gather materials to build and launch your own rocket! " +
                "Hope you reach your home! Have fun exploring :) ";
    }


    public String getControlsTitle() {
        return "Controls";
    }


    public String[][] getControls() {
        return new String[][] {
                {"W", "Move Up"},
                {"S", "Move Down"},
                {"A", "Move Left"},
                {"D", "Move Right"},
                {"Left Click", "Shoot"},
                {"Right Click", "Melee"},
                {"Space", "Jump"},
                {"Escape", "Pause"}
        };
    }
}
