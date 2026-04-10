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
File to comment: 
1. ZorpTheSolarAlienApp - Main x
2. GameObject - Abstract x
3. HomeModel - MVC (Model) 
4. PlayModel - MVC (Model)
5. HowToPlayModel - MVC (Model)
6. HomeView - MVC (View)
7. PlayView - MVC (View)
8. HowToPlayView - MVC (View)
9. HomeController - MVC (Controller)
10. PlayController - MVC (Controller)
11. HowToPlayController - MVC (Controller)
12. MainCharacterManager - Singleton
13. AudioManager - Singleton
14. ScoreManager - Singleton
15. SolarSystem - Singleton
16. Enemy - Factory Product
17. Boss - Factory Product
18. Bullet - Factory Product
19. EnemyBullet - Factory Product
20. FactPoint - Factory Product
21. GameInfoBar - Factory Product
22. ZorpTheSolarAlienFactory - Factory
23. ZorpTheSolarAlienInterface - Factory Interface

3. Create UML and write report 
---                                                                                                                                                                                                                               
Title Page (not counted in 6 pages)
- Roll number (e.g. @123456)
- "Zorp The Solar Alien — A JavaFX Educational Game"
- List of contents/subheadings

  ---                                                                                                                                                                                                                               
Page 1-2: Design & Responsibility-Driven Design
- Why you chose MVC, Factory, and Singleton patterns
- How responsibilities are divided (Model holds state, View renders UI, Controller connects them)
- Use case diagram showing player interactions (start game, shoot, collect facts, etc.)
- Sequence diagram showing a key flow (e.g. collecting a fact: player → controller → model → view)

Page 3: Class Diagram
- Hand-drawn or manually created class diagram (NOT auto-generated — they'll dock marks)
- Show GameObject as the abstract parent, factory products extending it, MVC relationships, singletons
- Show key interfaces (ZorpTheSolarAlienInterface)

Page 4: Key Code Explanation
- Short code snippets (not screenshots) of your most important patterns:
    - Factory pattern: ZorpTheSolarAlienFactory.createProduct()
    - Singleton: MainCharacterManager.getInstance()
    - MVC: How PlayController connects PlayModel and PlayView
- Explain why you made these choices, not just what the code does

Page 5: Critique & Evaluation
- What works well in your design
- What you'd improve (e.g. PlayController is 500+ lines — could split further)
- How patterns helped (Factory made spawning enemies easy, Singleton ensured one player instance)
- Limitations honestly discussed

Page 6: User Experience & Gameplay
- How the game teaches science to primary school kids (facts per wave, read timer)
- UI decisions (colourful sprites, boss fights, sound effects, pause functionality)
- How difficulty progresses across 9 levels

4. Final review of everything in the project and delete all extra file 
5. Build Jar 
6. Build Zip 
7. Record video
8. Double check everything 
8. Submit 

