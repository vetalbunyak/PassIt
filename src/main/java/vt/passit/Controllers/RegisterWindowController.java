package vt.passit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vt.passit.Modules.CustomAlert;
import vt.passit.Modules.DatabaseConnector;
import vt.passit.Modules.EmailValidator;
import vt.passit.VIews.FXMLCustomLoader;

import java.io.IOException;

public class RegisterWindowController extends BaseController{
    @FXML TextField usernameField;
    @FXML TextField passwordField;
    @FXML TextField confirmPasswordField;
    @FXML Button registerButton;
    @FXML Hyperlink loginLink;

    @FXML public void initialize(){
        loginLink.setOnAction(this::handleLoginLinkAction);
    }

    @FXML public void registerButtonClicked(){
        String tempUsername = usernameField.getText();
        String tempPassword = passwordField.getText();
        String tempRepeatPassword = confirmPasswordField.getText();

        if(!EmailValidator.isValidEmail(tempUsername)){
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "Невірний формат електронної адреси.");
            alert.showAndWait();
            return;
        }
        if(DatabaseConnector.userExistByEmail(tempUsername)){
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "Користувач з такою поштою вже зареєстрований.");
            alert.showAndWait();
            return;
        }
        if(tempPassword.length() < 5 || tempRepeatPassword.length() < 5){
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "Довжина паролю повинна бути від 5 символів.");
            alert.showAndWait();
            return;
        }
        if(!tempPassword.equals(tempRepeatPassword)){
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "Паролі не співпадають.");
            alert.showAndWait();
            return;
        }

        if(DatabaseConnector.addUser(tempUsername, tempPassword, tempUsername)) {
            CustomAlert alert = new CustomAlert(Alert.AlertType.INFORMATION, "Аккаунт успішно зареєстровано");
            alert.showAndWait();
        }
    }

    private void handleLoginLinkAction(ActionEvent event){
        try{
            FXMLCustomLoader loader = new FXMLCustomLoader("AuthWindow-view.fxml", 400, 300);
            Stage newStage = loader.getStage();

            newStage.setTitle("Авторизація");
            newStage.show();

            ((Stage) loginLink.getScene().getWindow()).close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
