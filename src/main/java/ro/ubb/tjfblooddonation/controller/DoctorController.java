package ro.ubb.tjfblooddonation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;


@Controller
public class DoctorController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    UsersService usersService;

    @FXML
    public void ChechRequestClicked(ActionEvent event) {
        try {
            loader.createNewWindow("/fxml/clinicStaff/Requests.fxml", "Check Requests", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void RequestBloodClicked(ActionEvent event) {
        try {
            loader.createNewWindow("/fxml/doctor/RequestForm.fxml", "Request Form", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

