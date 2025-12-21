package com.vnengine.logic.utils;

import com.vnengine.logic.utils.audio.IAudioAssetProvider;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader implements IAudioAssetProvider {
    private static final String DEFAULT_IMAGE_PATH = "/com/vnengine/images/";
    private static final String DEFAULT_AUDIO_PATH = "/com/vnengine/audio/";
    private static final Logger logger = LoggerFactory.getLogger(AssetLoader.class);

    // Audio Cache
    private final Map<String, Media> audioCache = new HashMap<>();


    // LOADING IMAGE METHODS
    public static Image loadImage (String filepath) {
        if (filepath.startsWith("external:")) {
            String realPath = filepath.substring("external:".length());
            return loadExternalImage(realPath);
        } else {
            String fullpath = filepath.startsWith("/") ? filepath : DEFAULT_IMAGE_PATH + filepath;
            return loadInternalImage(fullpath);
        }
    }

    private static Image loadInternalImage (String filepath) {
        try {
            InputStream is = AssetLoader.class.getResourceAsStream(filepath);
            if (is == null) {
                logger.error("Internal Image not found at: {}", filepath);
                return null;
            }
            return new Image(is);
        } catch (Exception e) {
            logger.error("Error loading internal image at: {}", filepath, e);
            return null;
        }
    }

    private static Image loadExternalImage (String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            logger.error("External image file does not exist: {}", filepath);
            return null;
        }

        try (InputStream is = new FileInputStream(filepath)){
            return new Image(is);
        } catch (Exception e) {
            logger.error("Error loading external image: {}", filepath, e);
            return null;
        }

    }

    // LOADING AUDIO METHODS
    public Media getAudio(String filepath) {
        if(audioCache.containsKey(filepath)) {
            return audioCache.get(filepath);
        }

        Media media;
        if (filepath.startsWith("external:")) {
            String realPath = filepath.substring("external:".length());
            media = loadExternalAudio(realPath);
        } else {
            String fullPath = filepath.startsWith("/") ? filepath : DEFAULT_AUDIO_PATH + filepath;
            media = loadInternalAudio(fullPath);
        }

        if (media != null) {
            audioCache.put(filepath, media);
        }

        return media;
    }

    private Media loadInternalAudio(String filepath) {
        try {
            String url = getClass().getResource(filepath).toExternalForm();
            logger.info("Loading internal audio: {}", filepath);
            return new Media(url);
        } catch (Exception e) {
            logger.error("Error loading internal audio: {}", filepath, e);
            return null;
        }
    }

    private Media loadExternalAudio(String filepath) {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                logger.error("External audio file not found: {}", filepath);
                return null;
            }

            String url = file.toURI().toString();
            logger.info("Loading external audio: {}", filepath);
            return new Media(url);
        } catch (Exception e) {
            logger. error("Error loading external audio: {}", filepath, e);
            return null;
        }
    }

    public void clearAudioCache() {
        audioCache.clear();
        logger.info("Audio cache cleared");
    }
}
