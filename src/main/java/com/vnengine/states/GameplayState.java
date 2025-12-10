package com.vnengine.states;

import com.vnengine.logic.ScriptParser;
import com.vnengine.logic.VNExecutor;
import com.vnengine.ui.DialogueBox;
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
        dialogueBox = new DialogueBox();

        root.getChildren().addAll(backgroundView,dialogueBox);

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

        String test =
        "[background \"forest.png\"]\n" +
        "EGG: Hello World!\n" +
        "[wait 1.0]\n" +
        "BALL: This is the new engine.";

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
