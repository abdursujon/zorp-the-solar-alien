package zorp_the_solar_alien.model;

public class PlayModel {
    private String[] planetNames = {"Sun", "Mercury", "Venus", "Earth", "Mars",
            "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};

    private int currentPlanet = 0;
    private int factsCollected = 0;

    private int hp = 100;
    private int maxHp = 100;
    private int score = 0;
    private int killCount = 0;
    private boolean levelComplete = false;
    private boolean gameOver = false;
    private long lastDamageTime = 0;
    private static final long INVINCIBILITY_NS = 1_000_000_000L;

    private int currentWave = 0;
    private int waveEnemiesKilled = 0;
    private static final int ENEMIES_PER_WAVE = 5;
    private static final int TOTAL_WAVES = 10;
    private boolean waveActive = false;
    private boolean objectiveUnlocked = false;

    private String[][] planetFacts = {
            {
                    "The Sun is the gigantic star located at the center of the Solar System \uD83D\uDCA5",
                    "The sun is so big that about 1.3 million earth could fit inside it! ",
                    "It is full of hot plasma. Those plasma is so hot that if you put earth on it, it will melt in ",
                    "The Sun's core temperature reaches about 15 million degrees Celsius.",
                    "Light from the Sun takes about 8 minutes and 20 seconds to reach Earth.",
                    "The Sun is composed of roughly 73% hydrogen and 25% helium.",
                    "The Sun's diameter is about 1.4 million km, 109 times that of Earth.",
                    "About one million Earths could fit inside the Sun.",
                    "The Sun rotates faster at its equator than at its poles.",
                    "Solar flares can release energy equivalent to millions of nuclear bombs.",
                    "The Sun generates energy through nuclear fusion.",
                    "The Sun will become a red giant in about 5 billion years."
            },

            {
                    "Mercury"
            },

            {
                    "Venus"
            },

            {
                    "Earth"
            },

            {
                    "Mars"
            },

            {
                    "Jupiter"
            },

            {
                    "Saturn"
            },

            {
                    "Uranus"
            },

            {
                    "Neptune"
            },

            {
                    "Pluto"
            }

    };

    private String[] planetDescriptions = {
            "AAAAWelcome to the Sun! It's a super bright star that gives us light and warmth. Without it, everything would be freezing cold and dark!",
            "Say hello to Mercury! It's the tiniest planet and zooms around the Sun faster than any other. Speedy little guy!",
            "Watch out for Venus! It's super duper hot and covered in thick cloudy goo. It's like being inside a giant oven!",
            "Home sweet home! Earth is where we all live. It's the only place we know that has yummy food, cute animals, and YOU!",
            "Welcome to Mars, the red planet! It looks like a giant rusty playground. Maybe one day humans will build a treehouse there!",
            "Whoa, Jupiter is HUGE! It's the biggest planet and has a giant swirly storm that's been spinning for hundreds of years!",
            "Ooh la la, Saturn has the prettiest rings! They're made of ice and rocks floating around like a sparkly hula hoop!",
            "Uranus is a silly planet that rolls around on its side! It's super cold and has a blueish-green colour. Brrr!",
            "Neptune is the windiest planet ever! The winds blow so fast they could whoosh you away like a leaf in a tornado!",
            "Say hi to Pluto, the tiny adventurer at the edge of our solar system! It may be small but it has a big heart shape on it!"
    };

    public String getCurrentPlanetDescription() {
        return planetDescriptions[currentPlanet];
    }

    public String[] getPlanetNames() {
        return planetNames;
    }

    public String getCurrentPlanetName() {
        return planetNames[currentPlanet];
    }

    public int getCurrentPlanet() {
        return currentPlanet;
    }

    public void setCurrentPlanet(int planet) {
        this.currentPlanet = Math.min(planet, planetNames.length - 1);
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

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean takeDamage(int amount, long now) {
        if (now - lastDamageTime < INVINCIBILITY_NS) return false;
        hp = Math.max(0, hp - amount);
        lastDamageTime = now;
        if (hp <= 0) gameOver = true;
        return true;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public int getKillCount() {
        return killCount;
    }

    public void registerKill() {
        killCount++;
        waveEnemiesKilled++;
        addScore(100);
    }

    public boolean isObjectiveUnlocked() {
        return objectiveUnlocked;
    }

    public void checkWaveCleared() {
        if (waveEnemiesKilled >= ENEMIES_PER_WAVE) {
            objectiveUnlocked = true;
        }
    }

    public boolean isWaveActive() {
        return waveActive;
    }

    public void startWave() {
        waveActive = true;
        waveEnemiesKilled = 0;
        objectiveUnlocked = false;
    }

    public void completeWave() {
        waveActive = false;
        currentWave++;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getTotalWaves() {
        return TOTAL_WAVES;
    }

    public boolean isBossWave() {
        return currentWave == TOTAL_WAVES - 1;
    }

    public int getEnemiesPerWave() {
        return isBossWave() ? 1 : ENEMIES_PER_WAVE;
    }

    public int getWaveEnemiesKilled() {
        return waveEnemiesKilled;
    }

    public String getWaveFact() {
        int planetIdx = Math.min(currentPlanet, planetFacts.length - 1);
        int factIdx = Math.min(currentWave, planetFacts[planetIdx].length - 1);
        return planetFacts[planetIdx][factIdx];
    }

    public boolean isLevelComplete() {
        return factsCollected >= TOTAL_WAVES;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void reset() {
        currentPlanet = 0;
        factsCollected = 0;
        hp = maxHp;
        score = 0;
        killCount = 0;
        currentWave = 0;
        waveEnemiesKilled = 0;
        waveActive = false;
        objectiveUnlocked = false;
        levelComplete = false;
        gameOver = false;
        lastDamageTime = 0;
    }

    public void resetForNextLevel() {
        factsCollected = 0;
        hp = maxHp;
        killCount = 0;
        currentWave = 0;
        waveEnemiesKilled = 0;
        waveActive = false;
        objectiveUnlocked = false;
        levelComplete = false;
        gameOver = false;
        lastDamageTime = 0;
    }
}
