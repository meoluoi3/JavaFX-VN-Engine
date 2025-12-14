package com.vnengine.core;

import com.vnengine.logic.utils.AssetLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterView extends StackPane {
    private static final Logger logger = LoggerFactory.getLogger(Character.class);
    private static final double CHARACTER_HEIGHT = 450;
    private ImageView imageView;
    public enum Position {LEFT, RIGHT, CENTER};
    private Position position = Position.LEFT;
    private boolean isFlipped = false;
    private boolean isVisible = true;


    public CharacterView(String imagepath) {
        this(imagepath, Position.LEFT, false, true);
    }

    public CharacterView(String imagepath, Position position, boolean isFlipped, boolean isVisible) {
        this.imageView = new ImageView();
        Image charImage = AssetLoader.loadImage(imagepath);
        if (charImage == null) {
            logger.error("Could not load Character Image:  {}", imagepath);
            return;
        }
        logger. info("Successfully load Character Image: {}", imagepath);

        this.imageView. setImage(charImage);
        this.imageView.setFitHeight(CHARACTER_HEIGHT);
        this.imageView.setPreserveRatio(true);

        this.position = position;
        this.isFlipped = isFlipped;  // Set directly, no auto-logic
        this.isVisible = isVisible;

        this.getChildren().add(imageView);

        applyPosition();
        applyFlip();
    }

    public void changeImage(String newImagePath) {
        Image newImage = AssetLoader.loadImage(newImagePath);
        if (newImage == null) {
            logger.error("Could not load new image: {}", newImagePath);
            return;
        }

        this.imageView.setFitHeight(CHARACTER_HEIGHT);
        this.imageView.setPreserveRatio(true);

        logger.info("Changing character image to: {}", newImagePath);
        this.imageView.setImage(newImage);
    }

    private void checkFlippedIntegrity(Position position) {
        if (position.equals(Position.RIGHT)) {
            this.isFlipped = true;
        }
        else this.isFlipped = false;
    }

    private void applyPosition() {
        if (this.position == Position. LEFT) {
            this.setTranslateX(-500);
            this.setTranslateY(150);
        } else if (this.position == Position.RIGHT) {
            this.setTranslateX(500);
            this.setTranslateY(150);
        } else if (this.position == Position.CENTER) {
            this.setTranslateX(0);
            this.setTranslateY(150);
        }
    }

    private void applyFlip() {
         if (isFlipped == true) {
            imageView.setScaleX(-1);
        } else {
             imageView.setScaleX(1);
         }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        applyPosition();
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
        applyFlip();
    }


}
