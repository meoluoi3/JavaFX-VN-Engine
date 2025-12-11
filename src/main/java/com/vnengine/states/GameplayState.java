package com.vnengine.states;

import com.vnengine.logic.utils.ScriptFileIO;
import com.vnengine.logic.ScriptParser;
import com.vnengine.logic.VNExecutor;
import com.vnengine.ui.DialogueBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class GameplayState implements GameState{
    private VNExecutor vnExecutor;
    private DialogueBox dialogueBox;
    private ImageView backgroundView;
    private StackPane root;

    @Override
    public void enter(StackPane root) {
        this.root = root;

        backgroundView = new ImageView();
        backgroundView.setFitWidth(1280);
        backgroundView.setFitHeight(720);
        backgroundView.setPreserveRatio(false);

        dialogueBox = new DialogueBox();

        root.getChildren().addAll(backgroundView,dialogueBox);
        StackPane.setAlignment(dialogueBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueBox, new Insets(0, 0, 30, 0));

        vnExecutor = new VNExecutor(dialogueBox,backgroundView);

        root.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case SPACE:
                case ENTER:
                    vnExecutor.onUserAction();
                    break;
            }
        });

        root.setOnMouseClicked(e -> vnExecutor.onUserAction());

        String test = ScriptFileIO.read("test.txt");

        vnExecutor.loadScript(new ScriptParser().parse(test));
        dialogueBox.show(null, "Press SPACE to Start...");
    }

    @Override
    public void update(double deltaTime) {
        vnExecutor.update(deltaTime);
    }

    @Override
    public void exit() {
        root.getChildren().clear();
    }
}
