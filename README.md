# Zorp The Solar Alien

A JavaFX game that teaches primary school kids about the solar system.

Players control Zorp, an alien exploring the solar system. Each level covers a planet (plus the Sun). Kill enemies, collect facts, and beat the boss to move on. 9 levels, 10 facts per level, 1 boss per level.

## How to run

```bash
mvn clean javafx:run
```

## Controls

- **WASD** — Move
- **Space** — Jump
- **Left click** — Shoot (direction follows where Zorp faces)
- **Right click** — Melee attack

## Game flow

1. Start a new game or continue from where you left off (auto-saved)
2. Read the intro card for the level
3. Kill 5 enemies per wave to unlock the fact objective
4. Collect the objective and read the fact
5. Wave 10 is a boss fight — beat the boss to complete the level
6. 9 levels total: Sun, Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune

## Design patterns used

- **MVC** — Model holds game state, View handles all UI rendering, Controller connects them
- **Factory** — ZorpTheSolarAlienFactory creates game objects (enemies, bullets, etc.)
- **Singleton** — MainCharacterManager, AudioManager, ScoreManager, SolarSystem

## Built with

- Java
- JavaFX
- Maven


To do
1. Finish each level facts x
2. Final review of each line of code twice and comment all method 
3. Create UML and write report 
4. Final review of everything in the project and delete all extra file 
5. Build Jar 
6. Build Zip 
7. Record video
8. Double check everything 
8. Submit 

