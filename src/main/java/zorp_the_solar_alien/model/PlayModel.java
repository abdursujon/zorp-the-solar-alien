package zorp_the_solar_alien.model;

public class PlayModel {
    private String[] planetNames = {"Sun", "Mercury", "Venus", "Earth", "Mars",
            "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};

    private int currentPlanet = 0;
    private int factsCollected = 0;

    public String[] getPlanetNames() {
        return planetNames;
    }

    public String getCurrentPlanetName() {
        return planetNames[currentPlanet];
    }

    public int getCurrentPlanet() {
        return currentPlanet;
    }

    public void nextPlanet() {
        if (currentPlanet < planetNames.length - 1) {
            currentPlanet++;
            factsCollected = 0;
        }
    }

    public void collectFact() {
        factsCollected++;
    }

    public int getFactsCollected() {
        return factsCollected;
    }

    public void reset() {
        currentPlanet = 0;
        factsCollected = 0;
    }
}