package ro.ubb.tjfblooddonation.utils;

import javafx.scene.control.Alert;

public class Messages {
    public static void showError(String message){
        showError("Error!", message);
    }
    public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Oups.... An error occured!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showConfirmation(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Success!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
