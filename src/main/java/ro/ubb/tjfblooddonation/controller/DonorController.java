package ro.ubb.tjfblooddonation.controller;


import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.awt.event.ActionEvent;
import java.io.IOException;

@Controller
public class DonorController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @Autowired
    UsersService usersService;


    @FXML
    void accountSettingsButtonPressed(ActionEvent event) {

        try {
            loader.createNewWindow("/fxml/accountSettings.fxml", "Account settings", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void donationHistoryButtonPressed(ActionEvent event) {

        try {
            loader.createNewWindow("/fxml/donationHistory.fxml", "Donation history", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void wantToDonateButtonPressed(ActionEvent event) {


            try {
                loader.createNewWindow("/fxml/donationForm.fxml", "Donation form", null);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


