package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;

public class RequestsController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @FXML
    private TextField pacientNameTxt;

    @FXML
    private TextField cnpTxt;

    @FXML
    void checkBloodAvailability(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        try {
            loader.createNewWindow("/fxml/BloodAvailability.fxml", "Blood Availability Page", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void requestComboBox(ActionEvent event) {
        String name = pacientNameTxt.getText();
        String cnp = cnpTxt.getText();

    }

}
