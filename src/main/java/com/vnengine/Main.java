package com.vnengine;

import com.vnengine.core.GameDirector;
import com.vnengine.states.GameplayState;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        GameDirector.getInstance().init(stage);
        GameDirector.getInstance().changeState(new GameplayState());
    }

    public static void main(String[] args) {
        launch();
    }
}
