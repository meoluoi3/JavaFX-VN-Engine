package com.vnengine.logic.utils.audio;

import java.io.Serializable;

public class ChannelState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String currentAudioname;
    private final double position;
    private final boolean isPlaying;

    public ChannelState(String currentAudioname, double position, boolean isPlaying) {
        this.currentAudioname = currentAudioname;
        this.position = position;
        this.isPlaying = isPlaying;
    }

    public String getCurrentAudioname() {
        return currentAudioname;
    }

    public double getPosition() {
        return position;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
