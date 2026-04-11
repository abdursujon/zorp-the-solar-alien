# Zorp The Solar Alien

This is JavaFX game that teaches primary school children about the solar system.

## How to run

```bash
mvn clean javafx:run
```

Or through Eclipse or any IDE run button. 

To do
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

