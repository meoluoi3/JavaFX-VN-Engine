package com.vnengine.logic.utils.audio;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AudioState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<AudioChannel, ChannelState> channelStates = new HashMap<>();
    private double masterVolume;
    private double bgmVolume;
    private double voiceVolume;
    private double ambientVolume;

    // getters & setters

    public Map<AudioChannel, ChannelState> getChannelStates() { return channelStates; }
    public void setChannelState(AudioChannel channel, ChannelState state) {
        channelStates.put(channel, state);
    }

    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double masterVolume) {
        this.masterVolume = masterVolume;
    }

    public double getBgmVolume() {
        return bgmVolume;
    }

    public void setBgmVolume(double bgmVolume) {
        this.bgmVolume = bgmVolume;
    }

    public double getVoiceVolume() {
        return voiceVolume;
    }

    public void setVoiceVolume(double voiceVolume) {
        this.voiceVolume = voiceVolume;
    }

    public double getAmbientVolume() {
        return ambientVolume;
    }

    public void setAmbientVolume(double ambientVolume) {
        this.ambientVolume = ambientVolume;
    }
}
