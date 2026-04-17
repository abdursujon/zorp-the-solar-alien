package zorp_the_solar_alien.SingletonObjects;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Implements singleton pattern which is used in playController
 * and game logic classes to play specific audio.
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
     * Constructor set to private to enforce Singleton pattern.
     * It loads all audio files from resources.
     */
    private AudioManager(){
        homeTracks = new ArrayList<>(List.of(
                loadAudio("/zorp_the_solar_alien/assets/audio/song1.mp3"),
                loadAudio("/zorp_the_solar_alien/assets/audio/song2.mp3"),
                loadAudio("/zorp_the_solar_alien/assets/audio/song3.mp3"),
                loadAudio("/zorp_the_solar_alien/assets/audio/song4.mp3"),
                loadAudio("/zorp_the_solar_alien/assets/audio/song5.mp3"),
                loadAudio("/zorp_the_solar_alien/assets/audio/song6.mp3")
        ));

        laserSound = loadAudio("/zorp_the_solar_alien/assets/audio/cartoon-laser.mp3");
        enemyDiedSound = loadAudio("/zorp_the_solar_alien/assets/audio/enemy-died.mp3");
        damageTakenSound = loadAudio("/zorp_the_solar_alien/assets/audio/damage-taken.mp3");
        gameOverSound = loadAudio("/zorp_the_solar_alien/assets/audio/gameover.mp3");
        bossBitenSound = loadAudio("/zorp_the_solar_alien/assets/audio/boss-biten.mp3");
        bossFightMusic = new Media[] {
                loadAudio("/zorp_the_solar_alien/assets/audio/boss-fight1.mp3"),
                loadAudio("/zorp_the_solar_alien/assets/audio/boss-fight2.mp3")
        };
    }

    private Media loadAudio(String resourcePath) {
        try {
            InputStream in = getClass().getResourceAsStream(resourcePath);
            File tempFile = File.createTempFile("zorp_audio_", resourcePath.substring(resourcePath.lastIndexOf('.')));
            tempFile.deleteOnExit();
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();
            return new Media(tempFile.toURI().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
     * Loops through all audio track for home and normal gameplay.
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
     * Used by homeController to check if user click on skip button to skip a song.
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
     * For short sound effect it is utilised by other methods to handle what sound they need.
     * The method uses MediaPlayer dispose method to free the memory when a specific sound not needed anymore.
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


    public void playBossBitten() {
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
