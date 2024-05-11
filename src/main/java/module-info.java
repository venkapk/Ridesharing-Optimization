module org.roux.rideshare {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    exports org.roux.rideshare.model;
    opens org.roux.rideshare.model to javafx.fxml;
    exports org.roux.rideshare.utils;
    opens org.roux.rideshare.utils to javafx.fxml;
    exports org.roux.rideshare.view;
    opens org.roux.rideshare.view to javafx.fxml;
    exports org.roux.rideshare.application;
    opens org.roux.rideshare.application to javafx.fxml;
}