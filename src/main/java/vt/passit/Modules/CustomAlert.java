package vt.passit.Modules;

import javafx.scene.control.Alert;

import java.util.Objects;

public class CustomAlert extends Alert {

    public CustomAlert(AlertType alertType, String message) {
        super(alertType);

        this.setContentText(message);
        this.setHeaderText(null);

        if (alertType == AlertType.ERROR) {
            this.setTitle("Помилка!");
        } else if (alertType == AlertType.WARNING) {
            this.setTitle("Увага!");
        } else if (alertType == AlertType.INFORMATION) {
            this.setTitle("Інформація");
        } else if (alertType == AlertType.CONFIRMATION) {
            this.setTitle("Підтвердження");
        } else {
            this.setTitle("Повідомлення");
        }


        this.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/vt/passit/CSS/styles.css")).toExternalForm());

        if (alertType == AlertType.ERROR) {
            this.getDialogPane().getStyleClass().add("error-alert");
        } else if (alertType == AlertType.WARNING) {
            this.getDialogPane().getStyleClass().add("warning-alert");
        } else if (alertType == AlertType.INFORMATION) {
            this.getDialogPane().getStyleClass().add("information-alert");
        }

    }
}