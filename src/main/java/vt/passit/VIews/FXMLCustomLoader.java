package vt.passit.VIews;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vt.passit.Interfaces.SceneController;
import vt.passit.PassItMain;

import java.io.IOException;
import java.util.Objects;

public class FXMLCustomLoader{
    private Stage stage;
    private Scene scene;
    private final Object controller;

    public FXMLCustomLoader(String pathToFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(PassItMain.class.getResource(pathToFXML));
        this.scene = new Scene(loader.load());
        this.controller = loader.getController();

        this.stage = new Stage();
        this.stage.setScene(scene);

        if (controller instanceof SceneController) {
            ((SceneController) controller).setScene(scene);
            ((SceneController) controller).setStage(stage);
        }

        String styleFile = "/vt/passit/CSS/styles.css";
        this.scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(styleFile)).toExternalForm());
    }

    public FXMLCustomLoader(String pathToFXML, int x, int y) throws IOException {
        FXMLLoader loader = new FXMLLoader(PassItMain.class.getResource(pathToFXML));
        this.scene = new Scene(loader.load(), x, y);
        this.controller = loader.getController();

        this.stage = new Stage();
        this.stage.setScene(scene);

        if (controller instanceof SceneController) {
            ((SceneController) controller).setScene(scene);
            ((SceneController) controller).setStage(stage);
        }

        String styleFile = "/vt/passit/CSS/styles.css";
        this.scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(styleFile)).toExternalForm());
    }

    public void show() {
        if (stage != null) {
            stage.show();
        } else {
            throw new IllegalStateException("Stage is not set!");
        }
    }

    public void showAndWait(){
        if (stage != null) {
            stage.showAndWait();
        } else {
            throw new IllegalStateException("Stage is not set!");
        }
    }

    public Stage getStage(){
        return this.stage;
    }

    public Scene getScene(){
        return this.scene;
    }

    public void setStage(Stage stage){
        this.stage = stage;
        stage.setScene(scene);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Object getController(){
        return this.controller;
    }

    public void close(){
        this.stage.close();
    }
}
