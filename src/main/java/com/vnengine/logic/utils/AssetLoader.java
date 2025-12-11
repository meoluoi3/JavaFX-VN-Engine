package com.vnengine.logic.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AssetLoader {
    private static final String DEFAULT_IMAGE_PATH = "/com/vnengine/images/";
    private static final Logger logger = LoggerFactory.getLogger(AssetLoader.class);

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
}
