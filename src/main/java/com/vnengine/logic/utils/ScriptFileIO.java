package com.vnengine.logic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ScriptFileIO {

    private static final Logger logger = LoggerFactory.getLogger(ScriptFileIO.class);
    private static final String DEFAULT_RESOURCE_PATH = "/com/vnengine/dialogue/";
    public static String read(String pathfile) {
        if (pathfile.startsWith("external:")) {
            // use external to determine ResourcePath and AbsolutePath
            String realPath = pathfile.substring("external:".length());
            return readExternal(realPath);
        } else {
            String fullPath = pathfile.startsWith("/") ? pathfile : DEFAULT_RESOURCE_PATH + pathfile;
            return readInternal(fullPath);
        }
    }

    private static String readInternal(String pathfile) {
        StringBuilder content = new StringBuilder();
        logger.info("Reading internal resource: {}", pathfile);

        try (InputStream is = ScriptFileIO.class.getResourceAsStream(pathfile)) {
            if (is == null) {
                logger.error("File not found in resources: {}", pathfile);
                return null;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            logger.error("Error reading resource: {}", pathfile, e);
            return null;
        }
        return content.toString();
    }

    private static String readExternal(String filepath) {
        StringBuilder content = new StringBuilder();
        logger.info("Reading external file: {}", filepath);

        File file = new File(filepath);
        if (!file.exists()) {
            logger.error("External file does not exist: {}", filepath);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error("Error reading external file: {}", filepath, e);
            return null;
        }

        return content.toString();
    }



    public static boolean write(String filepath, String content) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filepath, StandardCharsets.UTF_8))) {
            writer.write(content);
            return true;

        } catch (IOException e) {
            logger.error("Error writing script file: {}", filepath);
            e.printStackTrace();
            return false;
        }
    }
}
