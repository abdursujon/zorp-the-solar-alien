# Zorp The Solar Alien

A 2D educational action game built with Java and JavaFX that teaches primary school children about the Solar System. The player controls Zorp, an alien from the planet "Petunsky" whose ship has broken down. To get home, Zorp travels from the Sun out to Neptune, fighting hostile creatures and collecting facts about each world.

---- 

## Download and Play

No Java installation is needed; each download includes everything required to run the game.

1. Go to the [latest release](https://github.com/abdursujon/zorp-the-solar-alien/releases/latest).
2. Download the zip for your system:
   - **Windows:** `ZorpTheSolarAlien-Windows.zip`
   - **macOS:** `ZorpTheSolarAlien-macOS.zip`
   - **Linux:** `ZorpTheSolarAlien-Linux.zip`
3. Unzip it and start the game:
   - **Windows:** open the `ZorpTheSolarAlien` folder and double-click `ZorpTheSolarAlien.exe`. If Windows SmartScreen appears, click **More info**, then **Run anyway**.
   - **macOS:** double-click `ZorpTheSolarAlien.app`. If macOS blocks it because the developer cannot be verified, open **System Settings**, go to **Privacy & Security**, and click **Open Anyway**. On Apple Silicon Macs, install Rosetta if prompted.
   - **Linux:** run `./ZorpTheSolarAlien/bin/ZorpTheSolarAlien` from a terminal.

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

### Publishing a Release

Pushing a version tag starts the GitHub Actions workflow in `.github/workflows/release.yml`. It builds the Windows, macOS and Linux downloads, each with a bundled Java runtime, and attaches them to a GitHub release for that tag.

```bash
git tag v1.1
git push origin v1.1
```

---- 
## Save Data

Progress is saved automatically to `~/.zorp-the-solar-alien/save-game.txt` (on Windows, `C:\Users\<name>\.zorp-the-solar-alien\save-game.txt`). It stores the high score, the highest unlocked level, the current wave and the current score. Choosing **Start New Game** from the home screen resets it.

---- 
## Design Patterns

- **Model–View–Controller**: each screen (Home, Play, How To Play) has its own model, view and controller.
- **Factory**: `ZorpTheSolarAlienFactory.createProduct(type, x, y)` builds game objects by name (`bullet`, `enemyBullet`, `bossBullet`, `enemy`, `factPoint`, `gameInfoBar`, `sun`).
- **Singleton**: `AudioManager`, `ScoreManager`, `MainCharacterManager` and `SolarSystem` each have a single shared instance.

The game loop in `PlayController` is a JavaFX `Timeline` fixed at roughly 60 updates per second, so movement speed is the same regardless of monitor refresh rate.
