package vt.passit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
        System.out.println("REGISTER BUTTON");
    }

    private void handleLoginLinkAction(ActionEvent event){
        try{
            FXMLCustomLoader loader = new FXMLCustomLoader("AuthWindow-view.fxml", 400, 300); // Можете вказати розміри або використовувати конструктор без них, якщо стилі вже вбудовані
            Stage newStage = loader.getStage();

            newStage.setTitle("Авторизація");
            newStage.show();

            ((Stage) loginLink.getScene().getWindow()).close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
