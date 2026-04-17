package zorp_the_solar_alien.SingletonObjects;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 * ScoreManager uses singleton pattern because the entire gameplay needs only one instance of score management system.
 * It tracks level progress, and wave progress. When player scored and complete any facts or levels it
 * stores the score to local text file so user can start from where they left off.
 */
public class ScoreManager {
  
	private static ScoreManager instance = null;
    private int currentScore = 0;
    private int highScore = 0;
    private int[] levelHighScores = new int[10];
    private int highestLevelUnlocked = 0;
    private int savedWave = 0;
    private int savedScore = 0;


    /**
     * Private constructor enforcing singleton pattern.
     * It loads any previous saved data from the cache directory text file (save-game.txt).
     */
    private ScoreManager() {
        loadFromFile();
    }


    /**
     * Returns the singleton instance of ScoreManager for the controller to used.
     * If no score manager exist yet, it creates one.
     */
    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }


    public void saveScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
        saveToFile();
    }


    public int getHighScore() {
        return highScore;
    }


    public void unlockLevel(int levelIndex) {
        if (levelIndex > highestLevelUnlocked) {
            highestLevelUnlocked = levelIndex;
            saveToFile();
        }
    }


    public int getHighestLevelUnlocked() {
        return highestLevelUnlocked;
    }


    public void saveWaveProgress(int wave, int score) {
        this.savedWave = wave;
        this.savedScore = score;
        saveToFile();
    }


    public int getSavedWave() {
        return savedWave;
    }


    public int getSavedScore() {
        return savedScore;
    }

    public void resetProgress() {
        highScore = 0;
        highestLevelUnlocked = 0;
        savedWave = 0;
        savedScore = 0;
        for (int i = 0; i < 10; i++) {
            levelHighScores[i] = 0;
        }
        saveToFile();
    }


    /**
     * Writes game state data to save-game.txt file so player can continue playing
     * where they left previously instead of having to redo all the progress again.
     */
    public void saveToFile() {
        new File("cache").mkdirs();
        try (PrintWriter writer = new PrintWriter("cache/save-game.txt")) {
            writer.println(highScore);
            for (int i = 0; i < 10; i++) {
                writer.println(levelHighScores[i]);
            }
            writer.println(highestLevelUnlocked);
            writer.println(savedWave);
            writer.println(savedScore);
        } catch (Exception e) { e.printStackTrace(); }
    }


    /**
     * Reads the saved data from cache file in a sequence of how they were written.
     * It is used by constructor to determine if any saved data exist.
     */
    public void loadFromFile() {
        try (Scanner scanner = new Scanner(new File("cache/save-game.txt"))) {
            if (scanner.hasNextInt()) highScore = scanner.nextInt();
            for (int i = 0; i < 10; i++) {
                if (scanner.hasNextInt()) levelHighScores[i] = scanner.nextInt();
            }
            if (scanner.hasNextInt()) highestLevelUnlocked = scanner.nextInt();
            if (scanner.hasNextInt()) savedWave = scanner.nextInt();
            if (scanner.hasNextInt()) savedScore = scanner.nextInt();
        } catch (Exception e) { }
    }
    
}
