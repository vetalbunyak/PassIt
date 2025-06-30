package vt.passit.Interfaces;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface SceneController {
    void setScene(Scene scene);
    Scene getScene();

    Stage getStage();
    void setStage(Stage stage);

    void show();
    void showAndWait();
    void close();
}
