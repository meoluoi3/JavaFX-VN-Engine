package com.vnengine.logic;

import com.vnengine.logic.utils.AssetLoader;
import com.vnengine.ui.DialogueBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VNExecutor {
    private static final Logger logger = LoggerFactory.getLogger(VNExecutor.class);
    private List<ScriptCommand> script;
    private int instructionPointer = 0; // point at the line of script (line 0,1,2,...)
    private double waitTimer = 0;
    private boolean isRunning = false;

    private final DialogueBox dialogueBox;
    private final ImageView imageView;

    public VNExecutor(DialogueBox dialogueBox, ImageView imageView) {
        this.dialogueBox = dialogueBox;
        this.imageView = imageView;
    }

    public void loadScript(List <ScriptCommand> script) {
        this.script = script;
        this.instructionPointer = 0;
        this.waitTimer = 0;
        this.isRunning = false;
    }

    public void start() {
        if (script == null || script.isEmpty()) return;
        this.isRunning = true;
        processNextCommand();
    }

    public void update(double deltaTime) {
        if (!isRunning) return;

        if (waitTimer > 0) {
            waitTimer -= deltaTime;
            if (waitTimer <= 0) {
                logger.info("times up!");
                processNextCommand();
            }
            return;
        }

        if (dialogueBox.isTyping()) {
            dialogueBox.update(deltaTime);
        }
    }

    public void onUserAction() {
        if (!isRunning) {
            start();
            return;
        }

        if (waitTimer > 0) {
            logger.info("waitTimer is tickin' ({}) ... dont press/click", this.waitTimer);
            return;
        }

        if (dialogueBox.isTyping()) {
            dialogueBox.skipTyping();
        } else {
            processNextCommand();
        }
    }


    private void processNextCommand() {
        if (instructionPointer >= script.size()) {
            return;
        }

        ScriptCommand cmd = script.get(instructionPointer++);

        if (cmd.getType() == ScriptCommand.Type.DIALOGUE) {
            dialogueBox.show(cmd.getParam1(), cmd.getParam2());
        } else {
            handleAction(cmd);
        }
    }

    private void handleAction(ScriptCommand cmd) {
        switch (cmd.getParam1()) {
            case "background":
                logger.info("Set Background to: {}", cmd.getParam2());
                Image bgImage = AssetLoader.loadImage(cmd.getParam2());
                if (bgImage != null) {
                    if (bgImage.isError()) {
                        logger.error("BG Error: ", bgImage.getException());
                    } else {
                        logger.info("Good Image! Size: {} x {}", bgImage.getWidth(), bgImage.getHeight());
                        imageView.setImage(bgImage);
                    }
                } else {
                    logger.error("Failed to set background: {}", cmd.getParam2());
                }

                processNextCommand();
                break;
            case "wait":
                this.waitTimer = Double.parseDouble(cmd.getParam2());
                logger.info("wait time: {} second(s).", this.waitTimer);
                break;
        }
    }
}
