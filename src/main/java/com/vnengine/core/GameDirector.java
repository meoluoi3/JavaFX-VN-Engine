package com.vnengine.core;

import com.vnengine.logic.utils.AssetLoader;
import com.vnengine.logic.utils.audio.AudioManager;
import com.vnengine.states.GameState;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameDirector {
    private static GameDirector instance;
    private Stage stage;
    private StackPane rootLayout;
    private GameState currentState;
    private long lastTime;

    private AssetLoader assetLoader;
    private AudioManager audioManager;

    private GameDirector() {}

    public synchronized static GameDirector getInstance() {
        if (instance == null) {
            instance = new GameDirector();
            return instance;
        }
        else return instance;
    }

    public void init(Stage stage) {
        this.stage = stage;
        this.rootLayout = new StackPane();
        Scene scene = new Scene(rootLayout, 1280,720);
        stage.setScene(scene);
        stage.setTitle("A Visual Novel Engine");

        this.assetLoader = new AssetLoader();
        this.audioManager = new AudioManager(assetLoader);
        stage.show();
        startLoop();
    }

    private void startLoop() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                update(deltaTime);
            }
        };

        animationTimer.start();
    }

    private void update( double deltaTime) {
        if (currentState != null) {
            currentState.update(deltaTime);
        }
    }

    public void changeState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter(rootLayout);
    }

    public void shutdown() {
        if (audioManager != null) {
            audioManager.dispose();
        }
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public Stage getStage() {
        return stage;
    }

    public StackPane getRootLayout() {
        return rootLayout;
    }
}
