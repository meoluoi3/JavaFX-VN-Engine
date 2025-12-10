package com.vnengine.states;

import javafx.scene.layout.StackPane;

public interface GameState {
    void enter(StackPane root);
    void update(double dt);
    void exit();
}
