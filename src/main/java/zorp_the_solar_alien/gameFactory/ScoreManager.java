package zorp_the_solar_alien.gameFactory;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class ScoreManager {
    private static ScoreManager instance = null;
    private int currentScore = 0;
    private int[] levelHighScores = new int[10];

    private ScoreManager() {}

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

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter("scores.txt")) {
            for (int i = 0; i < 10; i++) {
                writer.println(levelHighScores[i]);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void loadFromFile() {
        try (Scanner scanner = new Scanner(new File("scores.txt"))) {
            for (int i = 0; i < 10; i++) {
                if (scanner.hasNextInt()) levelHighScores[i] = scanner.nextInt();
            }
        } catch (Exception e) { }
    }
}
