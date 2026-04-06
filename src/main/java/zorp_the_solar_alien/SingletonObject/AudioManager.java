package zorp_the_solar_alien.SingletonObject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.List;

public class AudioManager {
    private static AudioManager instance;
    private List<Media> homeTracks;
    private MediaPlayer currentPlayer;
    private int currentIndex = 0;

    private AudioManager(){
        homeTracks = List.of(
                new Media(getClass().getResource("/assets/audio/dragon-studio-alien-song-323613.mp3").toExternalForm()),
                new Media(getClass().getResource("/assets/audio/alien_i_trust-hypnotic-rominimal-line-by-alien-i-trust-125_bpm-275023.mp3").toExternalForm()),
                new Media(getClass().getResource("/assets/audio/fnx_sound-alien-underworld-sound-287342.mp3").toExternalForm())
        );
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playHomeMusic() {
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
}
