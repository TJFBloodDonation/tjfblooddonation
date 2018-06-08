package ro.ubb.tjfblooddonation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;

public class DonationFormController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @Autowired
    UsersService usersService;

    @FXML
    void pressedSubmitButton(ActionEvent event) {
        try {
            loader.createNewWindow("/fxml/donationForm.fxml", "Donation form", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}

