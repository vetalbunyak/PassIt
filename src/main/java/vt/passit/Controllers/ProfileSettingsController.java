package vt.passit.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import vt.passit.Modules.*;

public class ProfileSettingsController extends BaseController{
    @FXML Button backButton, changePhotoButton, saveButton;
    @FXML ImageView profileImageView;
    @FXML TextField firstNameField, lastNameField, emailField, passwordField, confirmPasswordField;
    @FXML Label roleLabel;

    @FXML public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();

        Platform.runLater(() -> {
            firstNameField.setText(user.getName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            passwordField.setText("");
            roleLabel.setText(user.getRole().toString());
        });

        backButton.setOnAction(event -> backAction());
        saveButton.setOnAction(event -> saveAction());
    }

    private void backAction() {
        this.stage.close();
    }

    private void saveAction() {
        String newMail = emailField.getText().trim();
        String newFirstName = firstNameField.getText().trim();
        String newLastName = lastNameField.getText().trim();
        String newPass = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (newMail.isEmpty() || !EmailValidator.isValidEmail(newMail)) {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Пошта невірно введена.");
            alert.showAndWait();
            return;
        }

        if (!newMail.equals(currentUser.getEmail()) && DatabaseConnector.userExistByEmail(newMail)) {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Користувач з такою поштою вже існує.");
            alert.showAndWait();
            return;
        }

        if (newFirstName.length() < 3) {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Ім'я повинне містити від 3 символів.");
            alert.showAndWait();
            return;
        }

        if (newLastName.length() < 3) {
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Прізвище повинне містити від 3 символів.");
            alert.showAndWait();
            return;
        }

        if (!newPass.isEmpty()) {
            if (!newPass.equals(confirmPass)) {
                CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Новий пароль та підтвердження не співпадають.");
                alert.showAndWait();
                return;
            }
            if (newPass.length() < 6) {
                CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Пароль повинен містити принаймні 6 символів.");
                alert.showAndWait();
                return;
            }

            String hashedNewPassword = HashData.hashPassword(newPass);
            currentUser.setPasswordHash(hashedNewPassword);
            System.out.println("Пароль змінено та хешовано.");
        }

        currentUser.setName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setEmail(newMail);

        try {
            DatabaseConnector.updateUser(currentUser);
            CustomAlert alert = new CustomAlert(Alert.AlertType.INFORMATION, "Зміни успішно збережено!");
            alert.showAndWait();
            backAction();
        } catch (Exception e) {
            System.err.println("Помилка при збереженні змін у базі даних: " + e.getMessage());
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "Помилка при збереженні змін: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
