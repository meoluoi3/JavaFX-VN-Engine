package com.vnengine.core;

import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CharacterManager {
    private static final Logger logger = LoggerFactory.getLogger(CharacterManager.class);

    private final StackPane root;
    private final Map<String, CharacterView> characters;
    private String currentSpeaker = null;

    public CharacterManager(StackPane root) {
        this.root = root;
        this.characters = new HashMap<>();
    }

    public void changeCharacterImage(String name, String newImagePath) {
        CharacterView character = characters.get(name);
        if (character != null) {
            character.changeImage(newImagePath);
            logger.info("Character {} image changed to {}", name, newImagePath);
        } else {
            logger. warn("Character {} not found", name);
        }
    }

    public void showCharacter(String name, String imagePath, CharacterView.Position position, boolean isFlipped) {
        if (characters.containsKey(name)) {
            logger.warn("Character {} already exists.  Removing old one.", name);
            hideCharacter(name);
        }

        CharacterView character = new CharacterView(imagePath, position, isFlipped, true);
        characters.put(name, character);

        int size = root.getChildren().size();
        if (size >= 2) {
            root.getChildren().add(size - 1, character);
        } else {
            root.getChildren().add(character);
        }

        logger.info("Character {} shown at position {} (flipped: {})", name, position, isFlipped);
    }

    public void hideCharacter(String name) {
        CharacterView character = characters.get(name);
        if (character != null) {
            root.getChildren().remove(character);
            characters.remove(name);
            logger.info("Character {} hidden", name);
        } else {
            logger.warn("Character {} not found", name);
        }
    }

    public void setSpeaker(String speakerName) {
        this.currentSpeaker = speakerName;

        for (Map.Entry<String, CharacterView> entry : characters.entrySet()) {
            String name = entry.getKey();
            CharacterView character = entry. getValue();

            if (name.equals(speakerName)) {
                character.setOpacity(1.0);
            } else {
                character.setOpacity(0.5);
            }
        }

        logger.info("Speaker set to: {}", speakerName);
    }

    public void clearAll() {
        for (CharacterView character : characters.values()) {
            root.getChildren().remove(character);
        }
        characters.clear();
        currentSpeaker = null;
        logger.info("All characters cleared");
    }

    public StackPane getRoot() {
        return root;
    }

    public Map<String, CharacterView> getCharacters() {
        return characters;
    }

    public String getCurrentSpeaker() {
        return currentSpeaker;
    }

    public void setCurrentSpeaker(String currentSpeaker) {
        this.currentSpeaker = currentSpeaker;
    }
}