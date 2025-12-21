package com.vnengine.logic;

import com.vnengine.core.CharacterManager;
import com.vnengine.core.CharacterView;
import com.vnengine.logic.utils.AssetLoader;
import com.vnengine.ui.DialogueBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.vnengine.logic. utils.audio.AudioManager;
import com.vnengine.logic.utils.audio.AudioChannel;

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
    private final CharacterManager characterManager;
    private final AudioManager audioManager;

    public VNExecutor(DialogueBox dialogueBox, ImageView imageView,
                      CharacterManager characterManager, AudioManager audioManager) {
        this.dialogueBox = dialogueBox;
        this.imageView = imageView;
        this.characterManager = characterManager;
        this.audioManager = audioManager;
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
            String speakerName = cmd.getParam1();

            // auto-set opacity control when speaking
            if (speakerName != null && !speakerName.isEmpty()) {
                characterManager.setSpeaker(speakerName);
            }

            dialogueBox.show(cmd.getParam1(), cmd.getParam2());
        } else {
            handleAction(cmd);
        }
    }

    private void handleAction(ScriptCommand cmd) {
        switch (cmd.getParam1()) {
            case "show":
                // Format: [show CharacterName imagePath Position (Flipped)]
                String[] showParts = cmd.getParam2().split(" ");
                if (showParts.length < 3) {
                    logger.error("show command needs:  [show name imagePath position] " +
                            "or [show name imagePath position flipped]");
                    processNextCommand();
                    break;
                }

                String charName = showParts[0];
                String charImage = showParts[1];
                CharacterView.Position pos = CharacterView.Position.valueOf(
                        showParts[2].toUpperCase());

                boolean flip = false;
                if (showParts.length >= 4) {
                    flip = Boolean.parseBoolean(showParts[3]);
                }

                characterManager.showCharacter(charName, charImage, pos, flip);
                processNextCommand();
                break;
            case "hide":
                // Format: [hide CharacterName]
                characterManager.hideCharacter(cmd.getParam2());
                processNextCommand();
                break;
            case "change":
                // Format: [change CharacterName newImagePath]
                String changeParts[] = cmd.getParam2().split(" ");
                if (changeParts.length < 2) {
                    logger.error("change command needs: [change name imagePath]");
                    processNextCommand();
                    break;
                }

                String changeCharName = changeParts[0];
                String changeCharImage = changeParts[1];

                characterManager.changeCharacterImage(changeCharName,changeCharImage);
                processNextCommand();
                break;
            case "background":
                // Format: [background backgroundName]
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
                // Format: [wait x.x (seconds in double type)]
                this.waitTimer = Double.parseDouble(cmd.getParam2());
                logger.info("wait time: {} second(s).", this.waitTimer);
                break;
            case "music":
            case "bgm":
                // Format: [music theme.mp3] or [music theme.mp3 loop] or [music theme.mp3 loop 2000]
                String[] musicParts = cmd.getParam2().split(" ");
                String musicName = musicParts[0];
                boolean loop = musicParts.length > 1 && musicParts[1].equals("loop");
                int fadeIn = musicParts.length > 2 ? Integer.parseInt(musicParts[2]) : 0;

                audioManager.play(AudioChannel.BACKGROUND, musicName, loop, fadeIn);
                processNextCommand();
                break;
            case "stopMusic":
                int fadeOut = cmd.getParam2().isEmpty() ? 0 : Integer.parseInt(cmd.getParam2().trim());
                audioManager.stop(AudioChannel.BACKGROUND, fadeOut);
                processNextCommand();
                break;
            case "crossfade":
                String[] crossfadeParts = cmd.getParam2().split(" ");
                String nextMusic = crossfadeParts[0];
                int duration = crossfadeParts.length > 1 ? Integer.parseInt(crossfadeParts[1]) : 2000;

                audioManager.crossfade(nextMusic, duration);
                processNextCommand();
                break;
            case "voice":
                audioManager.play(AudioChannel.VOICE, cmd.getParam2());
                processNextCommand();
                break;
            case "ambient":
                String[] ambientParts = cmd.getParam2().split(" ");
                String ambientName = ambientParts[0];
                boolean ambientLoop = ambientParts.length > 1 && ambientParts[1].equals("loop");

                audioManager.play(AudioChannel.AMBIENT, ambientName, ambientLoop);
                processNextCommand();
                break;

            case "stopAmbient":
                int ambientFadeOut = cmd.getParam2().isEmpty() ? 0 : Integer.parseInt(cmd.getParam2().trim());
                audioManager. stop(AudioChannel.AMBIENT, ambientFadeOut);
                processNextCommand();
                break;

            case "pauseAll":
                audioManager.pauseAll();
                processNextCommand();
                break;

            case "resumeAll":
                audioManager.resumeAll();
                processNextCommand();
                break;

            case "stopAll":
                audioManager.stopAll();
                processNextCommand();
                break;
        }
    }
}
