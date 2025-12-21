package com.vnengine.logic.utils.audio;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ChannelController {
    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    private final String channelName;
    private MediaPlayer currentPlayer;
    private String currentAudioName;
    private Timeline fadeTimeline;

    ChannelController(String channelName) {
        this.channelName = channelName;
    }

    public void play(Media media, String audioName, boolean loop, double targetVolume, int fadeInMs) {
        if (fadeTimeline != null) {
            fadeTimeline.stop();
        }

        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
        }

        currentPlayer = new MediaPlayer(media);
        currentAudioName = audioName;

        if (loop) {
            currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }

        currentPlayer.setOnError(() -> {
            logger.error("[{}] MediaPlayer error: {}", channelName, currentPlayer.getError());
        });

        // Fade in
        if (fadeInMs > 0) {
            currentPlayer.setVolume(0.0);
            currentPlayer.play();

            fadeTimeline = new Timeline(
                    new KeyFrame(Duration.millis(fadeInMs),
                    new KeyValue(currentPlayer.volumeProperty(), targetVolume))
            );
            fadeTimeline.play();
        } else {
            currentPlayer.setVolume(targetVolume);
            currentPlayer.play();
        }

        logger.info("[{}] Playing: {} (loop={}, fadeIn={}ms)", channelName, audioName, loop, fadeInMs);
    }

    public void stop(int fadeOutMs) {
        if (currentPlayer == null) return;

        if (fadeTimeline != null) {
            fadeTimeline.stop();
        }

        if (fadeOutMs > 0) {
            fadeTimeline = new Timeline(
                    new KeyFrame(Duration.millis(fadeOutMs),
                    new KeyValue(currentPlayer.volumeProperty(), 0.0))
            );
            fadeTimeline.setOnFinished(e -> disposePlayer());
            fadeTimeline.play();
        } else {
            disposePlayer();
        }

        logger.info("[{}] Stopped:  {}", channelName, currentAudioName);
    }

    public void crossfade(Media nextMedia, String nextAudioName, double targetVolume, int fadeDuration) {
        MediaPlayer oldPlayer = currentPlayer;

        // Create new player
        currentPlayer = new MediaPlayer(nextMedia);
        currentAudioName = nextAudioName;
        currentPlayer.setVolume(0.0);
        currentPlayer.play();

        if (fadeTimeline != null) fadeTimeline.stop();

        Timeline newFadein = new Timeline(
                new KeyFrame(Duration.millis(fadeDuration),
                new KeyValue(currentPlayer.volumeProperty(), targetVolume))
        );
        newFadein.play();

        // Fade out oldplayer
        if (oldPlayer != null) {
            Timeline oldFadeOut = new Timeline(
                    new KeyFrame(Duration. millis(fadeDuration),
                            new KeyValue(oldPlayer.volumeProperty(), 0.0))
            );
            oldFadeOut.setOnFinished(e ->{
                oldPlayer.stop();
                oldPlayer.dispose();
            });
            oldFadeOut.play();
        }

        logger.info("[{}] Crossfading to: {} ({}ms)", channelName, nextAudioName, fadeDuration);
    }

    public void pause() {
        if (currentPlayer != null && currentPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            currentPlayer.pause();
            logger.debug("[{}] Paused", channelName);
        }
    }

    public void resume() {
        if (currentPlayer != null && currentPlayer.getStatus() == MediaPlayer.Status. PAUSED) {
            currentPlayer.play();
            logger. debug("[{}] Resumed", channelName);
        }
    }

    public void setVolume(double volume) {
        if (currentPlayer != null) {
            currentPlayer.setVolume(volume);
        }
    }

    public ChannelState captureState() {
        if (currentPlayer == null || currentAudioName == null) {
            return new ChannelState(null, 0, false);
        }

        double position = currentPlayer.getCurrentTime().toMillis();
        boolean isPlaying = currentPlayer.getStatus() == MediaPlayer.Status.PLAYING;

        return new ChannelState(currentAudioName, position, isPlaying);
    }

    public void restoreState(Media media, ChannelState state, double targetVolume) {
        disposePlayer();

        currentPlayer = new MediaPlayer(media);
        currentAudioName = state.getCurrentAudioname();
        currentPlayer.setVolume(targetVolume);

        currentPlayer.setOnReady(() -> {
            if (state.getPosition() > 0) {
                currentPlayer.seek(Duration.millis(state.getPosition()));
            }

            if (state.isPlaying()) {
                currentPlayer.play();
            }
        });

        logger.info("[{}] Restored:  {} at {}ms", channelName, currentAudioName, state.getPosition());
    }

    public void dispose() {
        if (fadeTimeline != null) {
            fadeTimeline.stop();
        }
        disposePlayer();
    }

    private void disposePlayer() {
        if (currentPlayer != null) {
            currentPlayer. stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
        currentAudioName = null;
    }

    public String getCurrentAudioName() {
        return currentAudioName;
    }

    public boolean isPlaying() {
        return currentPlayer != null && currentPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
