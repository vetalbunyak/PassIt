package vt.passit.Modules;

import javafx.scene.control.Alert;

public class CustomAlert extends Alert{
    AlertType type;
    String text;

    public CustomAlert(AlertType alertType, String text) {
        super(alertType, text);

        this.type = alertType;
        this.text = text;
        this.setHeaderText(null);
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }
}
