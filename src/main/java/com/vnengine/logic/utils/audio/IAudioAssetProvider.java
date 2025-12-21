package com.vnengine.logic.utils.audio;

import javafx.scene.media.Media;

public interface IAudioAssetProvider {
    Media getAudio(String name);
}
