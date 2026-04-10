package zorp_the_solar_alien.SingletonObjects;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public void skipTrack() {
        if (playing) {
            currentIndex = (currentIndex + 1) % homeTracks.size();
            playTrack();
        }
    }

    public void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
        playing = false;
    }

    private boolean playing = false;

    public boolean isPlaying(){
        return playing;
    }

    private void playSfx(Media sound) {
        MediaPlayer sfx = new MediaPlayer(sound);
        sfx.setOnEndOfMedia(sfx::dispose);
        sfx.play();
    }

    public void playLaser() {
        playSfx(laserSound);
    }

    public void playEnemyDied() {
        playSfx(enemyDiedSound);
    }

    public void playDamageTaken() {
        playSfx(damageTakenSound);
    }

    public void playGameOver() {
        playSfx(gameOverSound);
    }

    public void playBossBiten() {
        playSfx(bossBitenSound);
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
