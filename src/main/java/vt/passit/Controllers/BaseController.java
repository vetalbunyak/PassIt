package vt.passit.Controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import vt.passit.Interfaces.SceneController;

public class BaseController implements SceneController {
    protected Stage stage;
    protected Scene scene;

    @Override public void setScene(Scene scene){
        this.scene = scene;
    }

    @Override public void setStage(Stage stage){
        this.stage = stage;
    }

    @Override public Scene getScene(){
        return this.scene;
    }

    @Override public Stage getStage() {
        return this.stage;
    }

    @Override public void show() {
        if (stage != null && !stage.isShowing()) {
            this.stage.show();
        }
    }

    @Override public void close() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override public void showAndWait(){
        if(stage != null) {
            stage.showAndWait();
        }
    }
}
