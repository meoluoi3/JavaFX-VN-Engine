module com.vnengine.vngameengine {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires javafx.media;

    opens com.vnengine.images;
    opens com.vnengine.dialogue;
    opens com.vnengine to javafx.fxml;
    exports com.vnengine;
}