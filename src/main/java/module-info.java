module vt.passit.passit {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires spring.security.crypto;

    opens vt.passit to javafx.fxml;
    exports vt.passit;
    exports vt.passit.Controllers;
    opens vt.passit.Controllers to javafx.fxml;
}