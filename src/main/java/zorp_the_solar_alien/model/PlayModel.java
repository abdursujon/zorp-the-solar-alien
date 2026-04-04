package zorp_the_solar_alien.model;

public class PlayModel {
    private String[] levelNames = {"Sun", "Mercury", "Venus", "Earth", "Mars",
            "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};

    private boolean[] unlockedLevels = new boolean[10];

    public PlayModel(){
        unlockedLevels[0] = true;
    }

    public String[] getLevelNames() {
            return levelNames;
    }

    public void unlockedLevel(int index){
        unlockedLevels[index] = true;
    }

    public boolean isLevelUnlocked(int index) {
        return unlockedLevels[index];
    }
}
