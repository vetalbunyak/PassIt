package vt.passit.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vt.passit.VIews.FXMLCustomLoader;

import java.io.IOException;

public class AuthWindowController extends BaseController {
    @FXML TextField usernameField;
    @FXML TextField passwordField;
    @FXML Button loginButton;
    @FXML Hyperlink registerLink;

    @FXML public void initialize(){
        registerLink.setOnAction(this::handleRegisterLinkAction);
    }

    @FXML public void loginButtonClicked(){
        System.out.println("LOGIN BUTTON");
    }

    private void handleRegisterLinkAction(ActionEvent event) {
        try{
            FXMLCustomLoader loader = new FXMLCustomLoader("RegisterWindow-view.fxml");
            Stage newStage = loader.getStage();
            newStage.setMinHeight(400);
            newStage.setMinWidth(500);
            newStage.setTitle("Реєстрація");
            newStage.show();

            ((Stage) registerLink.getScene().getWindow()).close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
