package zorp_the_solar_alien.SingletonObjects;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class implements singleton pattern which is used in playController
 * and game logic classes to play audio based on relevance.
 */
public class AudioManager {
    private static AudioManager instance;
    private List<Media> homeTracks;
    private MediaPlayer currentPlayer;
    private int currentIndex = 0;
    private Media laserSound;
    private Media enemyDiedSound;
    private Media damageTakenSound;
    private Media gameOverSound;
    private Media bossBitenSound;
    private Media[] bossFightMusic;
    private boolean playing = false;


    /**
     * This constructor set to private to enforce Singleton pattern.
     * It loads all audio files from resources.
     */
    private AudioManager(){
        homeTracks = new ArrayList<>(List.of(
                new Media(getClass().getResource("/audio/song1.mp3").toExternalForm()),
                new Media(getClass().getResource("/audio/song2.mp3").toExternalForm()),
                new Media(getClass().getResource("/audio/song3.mp3").toExternalForm()),
                new Media(getClass().getResource("/audio/song4.mp3").toExternalForm()),
                new Media(getClass().getResource("/audio/song5.mp3").toExternalForm()),
                new Media(getClass().getResource("/audio/song6.mp3").toExternalForm())
        ));

        laserSound = new Media(getClass().getResource("/audio/cartoon-laser.mp3").toExternalForm());
        enemyDiedSound = new Media(getClass().getResource("/audio/enemy-died.mp3").toExternalForm());
        damageTakenSound = new Media(getClass().getResource("/audio/damage-taken.mp3").toExternalForm());
        gameOverSound = new Media(getClass().getResource("/audio/gameover.mp3").toExternalForm());
        bossBitenSound = new Media(getClass().getResource("/audio/boss-biten.mp3").toExternalForm());
        bossFightMusic = new Media[] {
                new Media(getClass().getResource("/audio/boss-fight1.mp3").toExternalForm()),
                new Media(getClass().getResource("/audio/boss-fight2.mp3").toExternalForm())
        };
    }


    /**
     * Returns the single instance of AudioManager.
     * If the instance does not exist yet, it creates one.
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }


    public void playHomeMusic() {
        Collections.shuffle(homeTracks);
        currentIndex = 0;
        playTrack();
    }


    /**
     * This method loops through all audio track for home and normal gameplay.
     */
    private void playTrack() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
        currentPlayer = new MediaPlayer(homeTracks.get(currentIndex));
        currentPlayer.setOnEndOfMedia(() -> {
            currentIndex = (currentIndex + 1) % homeTracks.size();
            playTrack();
        });
        currentPlayer.play();
        playing = true;
    }


    /**
     * This method is used by homeController to check if user click on skip button to skip a song.
     */
    public void skipTrack() {
        if (playing) {
            currentIndex = (currentIndex + 1) % homeTracks.size();
            playTrack();
        }
    }


    /**
     * Stops the audio track.
     */
    public void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
        playing = false;
    }


    public boolean isPlaying(){
        return playing;
    }


    /**
     * For short sound effect this method is used by other methods to handle what sound they need.
     * The method uses MediaPlayer dispose free the memory when a specific sound not needed anymore.
     */
    private void playSoundEffect(Media sound) {
        MediaPlayer sfx = new MediaPlayer(sound);
        sfx.setOnEndOfMedia(sfx::dispose);
        sfx.play();
    }


    public void playLaser() {
        playSoundEffect(laserSound);
    }


    public void playEnemyDied() {
        playSoundEffect(enemyDiedSound);
    }


    public void playDamageTaken() {
        playSoundEffect(damageTakenSound);
    }


    public void playGameOver() {
        playSoundEffect(gameOverSound);
    }


    public void playBossBiten() {
        playSoundEffect(bossBitenSound);
    }


    public void playBossMusic() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
        }

        Media chosen = bossFightMusic[(int)(Math.random() * 2)];
        currentPlayer = new MediaPlayer(chosen);
        currentPlayer.setVolume(1.0);
        currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        currentPlayer.play();
        playing = true;
    }


    public void stopBossMusic() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
    }
}
