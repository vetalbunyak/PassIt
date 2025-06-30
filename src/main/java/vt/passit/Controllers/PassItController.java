package vt.passit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PassItController extends BaseController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}