package zorp_the_solar_alien.SingletonObject;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class ScoreManager {
    private static ScoreManager instance = null;
    private int currentScore = 0;
    private int highScore = 0;
    private int[] levelHighScores = new int[10];
    private int highestLevelUnlocked = 0;

    private ScoreManager() {
        loadFromFile();
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void addScore(int points) { currentScore += points; }
    public int getCurrentScore() { return currentScore; }
    public void resetCurrentScore() { currentScore = 0; }

    public void saveLevelScore(int levelIndex) {
        if (currentScore > levelHighScores[levelIndex]) {
            levelHighScores[levelIndex] = currentScore;
        }
    }

    public int getLevelHighScore(int levelIndex) {
        return levelHighScores[levelIndex];
    }

    public void saveScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
        saveToFile();
    }

    public int getHighScore() { return highScore; }

    public void unlockLevel(int levelIndex) {
        if (levelIndex > highestLevelUnlocked) {
            highestLevelUnlocked = levelIndex;
            saveToFile();
        }
    }

    public int getHighestLevelUnlocked() {
        return highestLevelUnlocked;
    }

    public void resetProgress() {
        highScore = 0;
        highestLevelUnlocked = 0;
        for (int i = 0; i < 10; i++) {
            levelHighScores[i] = 0;
        }
        saveToFile();
    }

    public void saveToFile() {
        new File("cache").mkdirs();
        try (PrintWriter writer = new PrintWriter("cache/save-game.txt")) {
            writer.println(highScore);
            for (int i = 0; i < 10; i++) {
                writer.println(levelHighScores[i]);
            }
            writer.println(highestLevelUnlocked);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void loadFromFile() {
        try (Scanner scanner = new Scanner(new File("cache/save-game.txt"))) {
            if (scanner.hasNextInt()) highScore = scanner.nextInt();
            for (int i = 0; i < 10; i++) {
                if (scanner.hasNextInt()) levelHighScores[i] = scanner.nextInt();
            }
            if (scanner.hasNextInt()) highestLevelUnlocked = scanner.nextInt();
        } catch (Exception e) { }
    }
}
