package vt.passit.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import vt.passit.Modules.SessionManager;
import vt.passit.Modules.User;

public class ProfileController extends BaseController {
    @FXML Button backButton;
    @FXML ImageView profileImageView;
    @FXML Label firstNameLabel, lastNameLabel, emailLabel, roleLabel;

    @FXML public void initialize() {
        backButton.setOnAction(event -> backAction());

        User currentUser = SessionManager.getInstance().getCurrentUser();

        Platform.runLater(() -> {
            firstNameLabel.setText(currentUser.getName());
            lastNameLabel.setText(currentUser.getLastName());
            emailLabel.setText(currentUser.getEmail());
            roleLabel.setText(currentUser.getRole().toString());
        });
    }

    private void backAction() {
        this.stage.close();
    }
}
