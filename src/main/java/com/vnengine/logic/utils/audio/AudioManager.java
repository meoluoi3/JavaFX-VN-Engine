package com.vnengine.logic.utils.audio;

import javafx.scene.media.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class AudioManager {
    private static final Logger logger = LoggerFactory.getLogger(AudioManager.class);

    private final Map<AudioChannel, ChannelController> channels;
    private final IAudioAssetProvider assetProvider;


    // Settings
    private double masterVolume = 1.0;
    private double bgmVolume = 0.5;
    private double voiceVolume = 1.0;
    private double ambientVolume = 0.5;

    private boolean bgmEnabled = true;
    private boolean voiceEnabled = true;
    private boolean ambientEnabled = true;

    public AudioManager(IAudioAssetProvider assetProvider) {
        this.assetProvider = assetProvider;
        this.channels = new HashMap<>();

        channels.put(AudioChannel.BACKGROUND, new ChannelController("BGM"));
        channels.put(AudioChannel.VOICE, new ChannelController("Voice"));
        channels.put(AudioChannel.AMBIENT, new ChannelController("Ambient"));

        logger.info("AudioManager initialized with {} channels", channels.size());
    }

    // Playback Control

    public void play(AudioChannel channel, String audioName) {
        play(channel, audioName, false, 0);
    }

    public void play(AudioChannel channel, String audioName, boolean loop) {
        play(channel, audioName, loop, 0);
    }

    public void play(AudioChannel channel, String audioName, boolean loop, int fadeInMs) {
        ChannelController controller = channels.get(channel);

        if (controller == null) {
            logger.warn("Unknown channel: {}", channel);
            return;
        }

        Media media = assetProvider.getAudio(audioName);
        if (media == null) {
            logger.error("Audio not found: {}", audioName);
            return;
        }

        double targetVolume = getChannelVolume(channel);
        controller.play(media, audioName, loop, targetVolume, fadeInMs);
    }

    public void stop(AudioChannel channel) {
        stop(channel, 0);
    }

    public void stop(AudioChannel channel, int fadeOutMs) {
        ChannelController controller = channels.get(channel);
        if (controller != null) {
            controller.stop(fadeOutMs);
        }
    }

    public void crossfade(String nextAudioName, int fadeDuration) {
        ChannelController bgmController = channels.get(AudioChannel.BACKGROUND);
        if (bgmController == null) return;

        Media nextMedia = assetProvider.getAudio(nextAudioName);
        if (nextMedia == null) {
            logger.error("Cannot crossfade to:  {}", nextAudioName);
            return;
        }

        double targetVolume = getChannelVolume(AudioChannel. BACKGROUND);
        bgmController.crossfade(nextMedia, nextAudioName, targetVolume, fadeDuration);
    }

    public void pause(AudioChannel channel) {
        ChannelController controller = channels.get(channel);
        if (controller != null) {
            controller.pause();
        }
    }

    public void resume(AudioChannel channel) {
        ChannelController controller = channels.get(channel);
        if (controller != null) {
            controller.resume();
        }
    }

    public void pauseAll() {
        channels.values().forEach(ChannelController::pause);
    }

    public void resumeAll() {
        channels.values().forEach(ChannelController::resume);
    }

    public void stopAll() {
        channels.values().forEach(c -> c.stop(0));
    }

    // Volume Control

    public void setMasterVolume(double volume) {
        this.masterVolume = clamp(volume);
        updateAllVolumes();
    }

    public void setBgmVolume(double volume) {
        this.bgmVolume = clamp(volume);
        updateChannelVolume(AudioChannel.BACKGROUND);
    }

    public void setVoiceVolume(double volume) {
        this.voiceVolume = clamp(volume);
        updateChannelVolume(AudioChannel.VOICE);
    }

    public void setAmbientVolume(double volume) {
        this.ambientVolume = clamp(volume);
        updateChannelVolume(AudioChannel.AMBIENT);
    }

    public void setBgmEnabled(boolean enabled) {
        this.bgmEnabled = enabled;
        updateChannelVolume(AudioChannel.BACKGROUND);
    }

    public void setVoiceEnabled(boolean enabled) {
        this.voiceEnabled = enabled;
        updateChannelVolume(AudioChannel. VOICE);
    }

    public void setAmbientEnabled(boolean enabled) {
        this.ambientEnabled = enabled;
        updateChannelVolume(AudioChannel.AMBIENT);
    }

    public void updateAllVolumes() {
        channels.keySet().forEach(this::updateChannelVolume);
    }

    private void updateChannelVolume(AudioChannel channel) {
        ChannelController controller = channels.get(channel);
        if (controller != null) {
            controller.setVolume(getChannelVolume(channel));
        }
    }

    private double getChannelVolume(AudioChannel channel) {
        double channelVol = 1.0;
        boolean enabled = true;

        switch (channel) {
            case BACKGROUND:
                channelVol = bgmVolume;
                enabled = bgmEnabled;
                break;
            case VOICE:
                channelVol = voiceVolume;
                enabled = voiceEnabled;
                break;
            case AMBIENT:
                channelVol = ambientVolume;
                enabled = ambientEnabled;
                break;
        }
        return enabled ? (masterVolume * channelVol) : 0.0;
    }

    // State Management

    public AudioState captureState() {
        AudioState state = new AudioState();

        channels.forEach((channel, controller) -> {
            ChannelState channelState = controller.captureState();
            state.setChannelState(channel, channelState);
        });

        state.setMasterVolume(masterVolume);
        state.setBgmVolume(bgmVolume);
        state.setVoiceVolume(voiceVolume);
        state.setAmbientVolume(ambientVolume);

        return state;
    }

    public void restoreState(AudioState state) {
        if (state == null) return;

        // Restore Volume
        this.masterVolume = state.getMasterVolume();
        this.bgmVolume = state.getBgmVolume();
        this.voiceVolume = state.getVoiceVolume();
        this.ambientVolume = state.getAmbientVolume();

        // restore channel states
        state.getChannelStates().forEach((channel,channelState) -> {
            ChannelController controller = channels.get(channel);
            if (controller != null && channelState != null) {
                String audioName = channelState.getCurrentAudioname();
                if (audioName != null && !audioName.isEmpty()) {
                    Media media  =  assetProvider.getAudio(audioName);
                    if (media != null) {
                        controller.restoreState(media, channelState, getChannelVolume(channel));
                    }
                }
            }
        });
    }

    // UTILITY

    public CompletableFuture<Void> preloadAudio(String... audioNames) {
        return CompletableFuture.runAsync(() -> {
            for (String name : audioNames) {
                assetProvider.getAudio(name);
            }
            logger.info("Preloaded {} audio files", audioNames.length);
        });
    }

    public String getCurrentAudio(AudioChannel channel) {
        ChannelController controller = channels.get(channel);
        return controller != null ? controller.getCurrentAudioName() : null;
    }

    public boolean isPlaying(AudioChannel channel) {
        ChannelController controller = channels.get(channel);
        return controller != null && controller.isPlaying();
    }

    public void dispose() {
        channels.values().forEach(ChannelController::dispose);
        channels.clear();
        logger.info("AudioManager disposed");
    }

    public double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    // Getters


    public double getMasterVolume() {
        return masterVolume;
    }

    public double getBgmVolume() {
        return bgmVolume;
    }

    public double getVoiceVolume() {
        return voiceVolume;
    }

    public double getAmbientVolume() {
        return ambientVolume;
    }

    public boolean isBgmEnabled() {
        return bgmEnabled;
    }

    public boolean isVoiceEnabled() {
        return voiceEnabled;
    }

    public boolean isAmbientEnabled() {
        return ambientEnabled;
    }
}


