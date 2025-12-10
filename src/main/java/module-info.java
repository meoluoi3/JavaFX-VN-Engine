module com.vnengine.vngameengine {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires org.slf4j;

    opens com.vnengine to javafx.fxml;
    exports com.vnengine;
}