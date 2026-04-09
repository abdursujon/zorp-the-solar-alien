package zorp_the_solar_alien.model;

public class PlayModel {
    private String[] planetNames = {"Sun", "Mercury", "Venus", "Earth", "Mars",
            "Jupiter", "Saturn", "Uranus", "Neptune"};

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
    private int currentWaveEnemyCount = 5;
    private static final int TOTAL_WAVES = 10;
    private boolean waveActive = false;
    private boolean objectiveUnlocked = false;

    private String[][] planetFacts = {
            {
                    "The Sun is the star at the heart of the Solar System. The Sun is so gigantic that its gravity holds the entire Solar System together!",
                    "The Sun is so big that over a million Earths could fit inside it!",
                    "99.8 percent of the total mass of the Solar System is the Sun!",
                    "Inside the Sun, there is a process called nuclear fusion which converts hydrogen to helium deep inside the Sun's core, " +
                    "which is why the Sun emits so much energy.",
                    "The energy created by the Sun in its core by nuclear fusion takes a million years to reach its surface!",
                    "Light from the Sun takes about 8 minutes and 20 seconds to reach Earth.",
                    "The Sun is made of roughly 74% hydrogen and 24% helium.",
                    "The Sun is over 4.5 billion years old!",
                    "The surface of the Sun is 5,505 degrees Celsius hot!",
                    "All the planets in the Solar System orbit the Sun.",
            },

            {
                    "Mercury is the smallest planet in the Solar System.",
                    "It is the closest planet to the Sun yet not the hottest planet! The hottest planet is Venus not Mercury, mysterious I know right?",
                    "It has no atmosphere like Earth! Therefore it has no weather, no seasons, no wind, no rain, nothing! " +
                    "It would be boring to live on Mercury don't you think?",
                    "A year on Mercury is only 88 Earth days! As you guessed correctly, because it's the smallest it orbits the fastest.",
                    "Do you like the Moon on Earth? Sadly there is no moon for Mercury :(",
                    "Mercury's surface is covered with craters (giant holes) just like the Moon!",
                    "If you were living on Mercury, did you know one of your days would be 59 Earth days long!",
                    "Mercury can get as hot as 430 degrees Celsius during the day, and -180 Celsius at night!",
                    "Mercury is a tiny planet, it's only slightly bigger than Earth's Moon.",
                    "Mercury has hidden frozen craters, even though it's closest to the Sun! Do you know why? " +
                    "Because you could be closest but if the sunlight does not reach " +
                    "a certain area it will always be frozen even if you are that close!",
            },

            {
                    "Venus is the hottest planet in the Solar System, even hotter than Mercury, even though it's the second planet by distance from the Sun.",
                    "Venus has a twin called Earth, well scientists like to call them that because they are almost the same size!",
                    "Did you know the Sun rises in the west on Venus! Sunset is in the east! " +
                            "Really odd isn't it? It's because Venus spins backwards, the opposite direction of how Earth orbits!",
                    "Now this will blow your mind, a day on Venus is longer than a year! Yeah you heard it right. " +
                            "It takes Venus only 225 days to go around the Sun but 243 Earth days to spin on its orbit!",
                    "Venus has clouds full of sulfuric acid! Do not breathe in the air if you ever go there! Otherwise bad things will happen.",
                    "Venus is the brightest planet in the Solar System, from Earth sometimes in the morning it can be seen without a telescope!",
                    "The surface of this planet is so hot (465 degrees Celsius) that it is enough to melt any metal on Earth.",
                    "This planet has the most volcanoes on its surface than any other planet in the Solar System.",
                    "Sadly Venus has no moon or rings just like Mercury.",
                    "The air pressure on the surface of this planet is so strong that it would crush any biological being into pieces."
            },

            {
                    "Earth is the only planet that is known to have life in the Solar System.",
                    "About 71% of Earth is water, that's why it looks so blue from space.",
                    "In the entire Solar System, Earth is the only planet which has liquid water on its surface.",
                    "Earth's atmosphere protects its living beings by burning up most space rocks before they hit the ground.",
                    "Earth spins at over 1,600 kilometres per hour but no one can feel it because everything else is moving at the same rate.",
                    "Earth has one Moon, which is the fifth largest moon in the entire Solar System.",
                    "The highest peak on Earth is called Mount Everest but the funny thing is that the deepest point in the ocean called the Mariana Trench is deeper than Mount Everest is tall!",
                    "Earth is not a proper circle shape, it is slightly squished at the top and bottom.",
                    "Earth's core is as hot as the surface of the Sun, which is about 5,500 degrees Celsius!",
                    "A year on Earth is 365 and a quarter days, that's why it has a leap year every 4 years to make up for it."
            },

            {
                    "Now comes the red planet, Mars! Do you know why it's called the red planet? Because its surface is covered in rusty iron dust.",
                    "Can you imagine a volcano that is nearly three times taller than Mount Everest? " +
                            "Mars has a volcano called Olympus Mons which is almost three times taller than Mount Everest!",
                    "Mars has two dwarf moons called Phobos and Deimos, which mean fear and panic in Greek!",
                    "A day on Mars is almost the same as Earth — only 37 minutes longer. Maybe that's why humans from Earth are so interested in this planet?",
                    "It's known that scientists from Earth found frozen water ice at the poles of Mars under the surface.",
                    "Mars has the biggest canyon called Valles Marineris in the entire Solar System.",
                    "Gravity on Mars is only about 38% of Earth's, if someone could jump 1 metre on Earth, they could jump 3 metres on Mars! Imagine running!",
                    "Many scientists from Earth believe Mars once had rivers and oceans of liquid water billions of years ago. Who knows, potentially life too?",
                    "Did you know Mars has dust storms so massive that they can cover the entire planet and last for months!"
            },

            {
                    "Jupiter is the biggest planet in the entire Solar System, about 1,300 Earths can fit inside it.",
                    "Jupiter has at least 95 moons! The three biggest ones are called Io, Europa, and Ganymede.",
                    "Jupiter spins faster than all other planets in the Solar System, one day is only about 10 hours!",
                    "It has no solid ground to stand on because it's like a giant gas ball.",
                    "The moon of Jupiter called Europa has a giant ocean of liquid water hidden under its surface which scientists from Earth believe could have life swimming in it!",
                    "Jupiter acts like a shield for Earth because it pulls asteroids from space towards it which otherwise would have hit Earth.",
                    "Jupiter has faint rings around it, like Saturn, but they are not easy to see because of how thin they are.",
                    "Jupiter is so heavy that it is heavier than all other planets combined together!",
                    "A year on Jupiter is about 12 Earth years long, so you would have to wait 12 years for each birthday! I know, not many presents :(",
                    "One of the moons of Jupiter called Ganymede is bigger than Mercury alone!"

            },

            {
                    "Can you see that ring on the Saturn just behind you? Yep, that is Saturn's famous beautiful rings made of billions of pieces of ice, rock, and dust!",
                    "Saturn has even more moons than Jupiter! A total of 146 moons, the most of any planet in the Solar System!",
                    "Did you know Saturn's density is lighter than water? So if you had a big enough pond, it would float on the water!" +
                            " This is because it's mostly made of hydrogen and helium gas.",
                    "Now imagine a lake with liquid methane not water, yes Saturn's largest moon Titan has lakes and rivers on its surface which have pure methane.",
                    "A year on Saturn is about 29 Earth years long, imagine you are not one year old until you are 29? Confusing I know!",
                    "The rings you see behind you stretch out for hundreds of thousands of kilometres but are only about 10 metres thick!",
                    "It is the second biggest planet in the Solar System after Jupiter and about 95 times heavier than Earth.",
                    "It spins so fast that a day is only about 10 and a half hours long!",
                    "It is also a gas giant like Jupiter, so there is no solid ground to stand on sadly, meaning we won't be able to land on its surface to explore!",
                    "Winds blowing on its surface can sometimes be about 1,800 kilometres per hour, much faster than any hurricane on Earth!",
                    "Saturn's moon called Enceladus shoots giant fountains of water ice into space from cracks in its surface, looks like fountains of Earth but only from beneath!"
            },

            {
                    "Uranus is the only planet in the entire Solar System that spins on its side, like a rolling football!",
                    "It is mostly made of water, methane, and ammonia ice.",
                    "Scientists from Earth think it has 27 moons, humans named them after characters from Shakespeare and Alexander Pope!",
                    "A year on Uranus is 84 Earth years long, imagine not having the chance to celebrate your first birthday :(",
                    "Uranus has rings too, 13 of them! But they are faint and very hard to see.",
                    "As you see behind you, it looks blue-green because of the methane gas in its atmosphere.",
                    "One day on Uranus is about 17 hours long.",
                    "Uranus is the coldest planet in the Solar System even though it's not the last planet in it, with temperatures dropping to minus 224 degrees Celsius!",
                    "It was first discovered using a telescope, by William Herschel in 1781.",
                    "Hope you know how fast light can travel? Still it takes sunlight 2 hours and 40 minutes to reach Uranus's surface!"
            },

            {
                    "Neptune is the farthest planet from the Sun in the Solar System. As you can imagine it is really really cold.",
                    "The winds on Neptune are wild, it has the strongest winds out of all the planets, blowing at over 2,000 kilometres per hour!",
                    "Have you heard of the name Triton? It is the biggest moon of Neptune out of the 16 moons it has.",
                    "Don't think about how long a year is, because it is 165 Earth years long! No biological being on Earth will ever have a birthday!",
                    "It has lots of methane on its surface, making it a beautiful deep blue colour.",
                    "As you guessed it is also an ice giant just like Uranus, made mostly of water, methane, and ammonia.",
                    "One day on Neptune is only about 16 hours long.",
                    "Huh, rings again, yeah Neptune has rings too, but you won't see them, they are way too thin.",
                    "Hope you like maths? Neptune was the first planet found using maths! Scientists from Earth predicted where it would be before anyone saw it through a telescope!",
                    "Neptune's moon Triton is the coldest object ever measured in the Solar System at minus 235 degrees Celsius, and it orbits Neptune backwards!"
            }

    };

    private String[] planetDescriptions = {
            "AAAA Welcome to the Sun! It's a super bright star that gives all other planets light and most importantly it is one of the core reason why earth has life. Without it, everything would be freezing cold and dark!",
            "Say hello to Mercury! It's the tiniest planet and spins around the Sun faster than any other. Speedy little guy eh!",
            "Watch out for Venus! It's super duper hot and covered in thick cloudy ghost. Imagine being inside a giant oven!",
            "It's a home for human being! Earth is where we all life in the solar system lives. It's the only place you can visit that has yummy food, cute animals, and and 71% ocean water!",
            "Welcome to Mars, the red planet! It looks like a giant rusty playground. Human has been dreaming to build a treehouse and playground there!",
            "Whoa, Jupiter is HUGE! It's the biggest planet and has a giant swirly storm that's been spinning for hundreds of years!",
            "Ooh la la, Saturn has the prettiest rings! They're made of ice and rocks floating around like a sparkly hula hoop!",
            "Uranus is a silly planet that rolls around on its side! It's super cold and has a blueish-green colour. Brrr!",
            "Neptune is the windiest planet ever! The winds blow so fast they could whoosh you away like a leaf in a tornado, if you don't know what tornado feels like, give earth a visit!"
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

    public void setCurrentWave(int wave) {
        this.currentWave = wave;
    }

    public void setFactsCollected(int facts) {
        this.factsCollected = facts;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isLastLevel() {
        return currentPlanet >= planetNames.length - 1;
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
        if (waveEnemiesKilled >= currentWaveEnemyCount) {
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
        if (!isBossWave()) {
            currentWaveEnemyCount = 5 + (int) (Math.random() * 4);
        } else {
            currentWaveEnemyCount = 1;
        }
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
        return currentWaveEnemyCount;
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
        currentWaveEnemyCount = 5;
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
        currentWaveEnemyCount = 5;
        waveActive = false;
        objectiveUnlocked = false;
        levelComplete = false;
        gameOver = false;
        lastDamageTime = 0;
    }

    public void resetForRetry() {
        hp = maxHp;
        killCount = 0;
        waveEnemiesKilled = 0;
        currentWaveEnemyCount = 5;
        waveActive = false;
        objectiveUnlocked = false;
        levelComplete = false;
        gameOver = false;
        lastDamageTime = 0;
    }
}
