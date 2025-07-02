package vt.passit;

import javafx.application.Application;
import javafx.stage.Stage;
import vt.passit.Views.FXMLCustomLoader;

import java.io.IOException;

public class PassItMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLCustomLoader fxmlLoader = new FXMLCustomLoader("AuthWindow-view.fxml", 320, 240);
        fxmlLoader.getStage().setTitle("PassIt");
        fxmlLoader.getStage().setMinHeight(300);
        fxmlLoader.getStage().setMinWidth(300);
        fxmlLoader.show();
    }

    public static void main(String[] args) {
        launch();
    }
}