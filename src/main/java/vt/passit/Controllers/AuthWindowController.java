package vt.passit.Controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vt.passit.Modules.CustomAlert;
import vt.passit.Modules.SessionManager;
import vt.passit.Modules.User;
import vt.passit.PassItMain;
import vt.passit.Views.FXMLCustomLoader;
import vt.passit.Modules.DatabaseConnector;

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
        String tempUsername = usernameField.getText();
        String tempPassword = passwordField.getText();

        if(tempUsername.isEmpty() || tempPassword.isEmpty()){
            CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Будь ласка, введіть Email та Пароль.");
            alert.showAndWait();
            return;
        }

        loginButton.setDisable(true);

        Task<User> loginTask = new Task<User>() {
            @Override
            protected User call() throws Exception {
                return DatabaseConnector.authenticateUser(tempUsername, tempPassword);
            }
        };

        loginTask.setOnSucceeded(event -> {
            loginButton.setDisable(false);

            User authenticatedUser = loginTask.getValue();

            if(authenticatedUser != null) {
                SessionManager.getInstance().login(authenticatedUser);

                CustomAlert alert = new CustomAlert(Alert.AlertType.INFORMATION, "Ласкаво просимо, " + authenticatedUser.getUsername() + "!");
                alert.show();

                closeAuthWindow();
                try {
                    showMainWindow(authenticatedUser);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                /*if(SessionManager.getInstance().isAdmin()){

                }else {

                }
                */
            }
            else{
                CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, "Невірний Email або пароль.");
                alert.showAndWait();
            }
            clearLoginFields();
        });

        loginTask.setOnFailed(event -> {
            loginButton.setDisable(false);

            Throwable e = loginTask.getException();
            System.err.println("Помилка під час входу: " + e.getMessage());
            e.printStackTrace();
            CustomAlert alert = new CustomAlert(Alert.AlertType.ERROR, "Виникла несподівана помилка. Спробуйте ще раз.");
            alert.showAndWait();
        });

        new Thread(loginTask).start();
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

    private void closeAuthWindow() {
        this.stage.close();
    }

    private void clearLoginFields() {
        usernameField.clear();
        passwordField.clear();
    }

    private void showMainWindow(User user) throws IOException {
        FXMLCustomLoader loader = new FXMLCustomLoader("PassItMain-view.fxml", 400, 300);
        Stage newStage = loader.getStage();

        if(loader.getController() instanceof PassItMainController) {
            ((PassItMainController) loader.getController()).setUser(user);
        }

        newStage.setTitle("PassIt" + " " + user.getUsername());
        newStage.show();
    }


}
