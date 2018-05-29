package ro.ubb.tjfblooddonation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;

@Controller
public class BloodAnalysisController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @FXML
    void donationsButtonPressed(ActionEvent event) {
        try {
            loader.createNewWindow("/fxml/bloodAnalyst/BloodDonations.fxml", "Blood Donations Page", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void requestsButtonPressed(ActionEvent event) {
        try {
            loader.createNewWindow("/fxml/clinicStaff/Requests.fxml", "Requests Page", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

}