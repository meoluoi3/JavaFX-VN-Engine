package com.vnengine.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DialogueBox extends StackPane {
    // UI Components
    private final Label nameLabel;
    private final Label contentLabel;
    private final VBox container;

    // Typewriter logic
    private String targetText = "";
    private int visibleCharIndex = 0;
    private double timeAccumulator = 0;
    private boolean isTyping = false;

    // Configuration
    private static final double CHARS_PER_SECOND = 30.0;
    private static final double SECONDS_PER_CHAR = 1.0 / CHARS_PER_SECOND;

    public DialogueBox() {

        this.setMaxSize(1200, 200);
        // setup Background (the kinda transparent box)
        Rectangle bg = new Rectangle(1200,200);
        bg.setFill(Color.rgb(0, 0, 0, 0.7));
        bg.setArcWidth(20);
        bg.setArcHeight(20);

        // setup Speaker Name Label
        nameLabel = new Label("Speaker");
        nameLabel.setTextFill(Color.GOLD);
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        nameLabel.setPadding(new Insets(0,0,10,0));

        // setup Dialogue Content Label
        contentLabel = new Label("Content");
        contentLabel.setTextFill(Color.WHITE);
        contentLabel.setFont(Font.font("Verdana", 20));
        contentLabel.setWrapText(true);
        // Fix width to ensure wrapping happens inside the box
        contentLabel.setPrefWidth(1150);

        // Organize in Vbox
        container = new VBox(nameLabel, contentLabel);
        container.setAlignment(Pos.TOP_LEFT);
        container.setPadding(new Insets(20));
        container.setMaxSize(1200, 200);

        // add this class to StackPane
        this.getChildren().addAll(bg, container);

        // Position the box at the bottom (changed to GameplayState)
    }

    public void show(String speakerName, String text) {
        if (speakerName == null || speakerName.trim().isEmpty()) {
            this.nameLabel.setText("");
            this.nameLabel.setVisible(false);
            this.nameLabel.setManaged(false);
        } else {
            this.nameLabel.setText(speakerName);
            this.nameLabel.setVisible(true);
            this.nameLabel.setManaged(true);
        }

        // Handle Null
        this.nameLabel.setText(speakerName != null ? speakerName: "");
        this.targetText = text != null ? text : "[]";

        // Reset Typewriter
        this.contentLabel.setText("");
        this.visibleCharIndex = 0;
        this.timeAccumulator = 0;
        this.isTyping = true;
    }

    public void update(double deltaTime) {
        if (!isTyping) return;

        timeAccumulator += deltaTime;

        while (timeAccumulator >= SECONDS_PER_CHAR) {
            timeAccumulator -= SECONDS_PER_CHAR;

            if (visibleCharIndex < targetText.length()) {
                visibleCharIndex++;
                contentLabel.setText(targetText.substring(0,visibleCharIndex));
            } else {
                isTyping = false;
                break;
            }
        }
    }

    public void skipTyping() {
        if (!isTyping) return;

        visibleCharIndex = targetText.length();
        contentLabel.setText(targetText);
        isTyping = false;
    }

    public boolean isTyping() {
        return isTyping;
    }
}
