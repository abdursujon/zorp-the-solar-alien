# Zorp The Solar Alien

A 2D educational action game built with Java and JavaFX that teaches primary school children about the Solar System. The player controls Zorp, an alien from the planet "Petunsky" whose ship has broken down. To get home, Zorp travels from the Sun out to Neptune, fighting hostile creatures and collecting facts about each world.

---- 

## Download and Play

1. Install Java 17 or newer. Check with:

   ```bash
   java -version
   ```

   If Java is missing, download it from [Adoptium](https://adoptium.net/).

2. Download `zorp-the-solar-alien.jar` from the [Releases](../../releases) page.

3. Open a terminal in the folder containing the jar and run:

   ```bash
   java -jar zorp-the-solar-alien.jar
   ```

   On Windows and macOS the jar can usually also be started by double-clicking it.

Progress is saved in a `cache` folder created next to wherever the game is launched from, so keep launching it from the same folder to continue a saved game.

---- 

## Gameplay

The game has 9 levels, one for each body in the Solar System: Sun, Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus and Neptune.

Each level has 10 waves:

1. Waves 1–9 spawn 5–8 regular enemies. Some shoot at Zorp, others carry swords and fight up close.
2. Wave 10 is a boss fight. Each planet has its own named boss (for example *Solaris The Scorcher* on the Sun and *Stormy King* on Neptune). Bosses get 100 HP stronger on each level.
3. Clearing a wave unlocks a **Fact Point**. Walking into it shows a fact card about the current planet.
4. Collecting all 10 facts completes the level and unlocks the next planet.

The planet in the background grows larger as more facts are collected.

---- 

### Scoring

| Action            | Points |
|-------------------|--------|
| Enemy defeated    | 100    |
| Fact collected    | 500 (+30 HP) |
| Boss defeated     | 1000 (full HP restored) |

Zorp starts each level with 100 HP. When HP reaches 0 the game is over, and the player can retry from the fact they were on.

---- 

## Controls

| Input        | Action     |
|--------------|------------|
| `W` `A` `S` `D` | Move       |
| Left click   | Shoot      |
| Right click  | Melee attack |
| `Space`      | Jump       |
| `Esc`        | Pause / resume |

The home screen also has buttons to continue, start a new game, open the How To Play screen, toggle music and skip the current track.

## Building From Source

### Requirements

- JDK 17 or newer
- Maven 3.6+

JavaFX 22 (`javafx-controls`, `javafx-media`) is downloaded by Maven, so no separate JavaFX install is needed.

### Running

```bash
mvn clean javafx:run
```

The project can also be imported into Eclipse (the `.project` and `.classpath` files are included) and run with `ZorpTheSolarAlienApp` as the main class.

### Building the Jar

```bash
mvn clean package
```

The runnable jar is written to `target/zorp-the-solar-alien.jar`.

---- 
## Save Data

Progress is saved automatically to `cache/save-game.txt`, relative to the directory the game is launched from. It stores the high score, the highest unlocked level, the current wave and the current score. Choosing **Start New Game** from the home screen resets it.

---- 
## Design Patterns

- **Model–View–Controller**: each screen (Home, Play, How To Play) has its own model, view and controller.
- **Factory**: `ZorpTheSolarAlienFactory.createProduct(type, x, y)` builds game objects by name (`bullet`, `enemyBullet`, `bossBullet`, `enemy`, `factPoint`, `gameInfoBar`, `sun`).
- **Singleton**: `AudioManager`, `ScoreManager`, `MainCharacterManager` and `SolarSystem` each have a single shared instance.

The game loop in `PlayController` is a JavaFX `Timeline` fixed at roughly 60 updates per second, so movement speed is the same regardless of monitor refresh rate.
