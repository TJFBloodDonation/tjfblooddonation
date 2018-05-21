package ro.ubb.tjfblooddonation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.UsersService;

public class BloodAvailabilityController {
    @Autowired
    UsersService usersService;


    @FXML
    void availableDonorCombo(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if(usersService.getAllDonors().size() > 0)
                    usersService.getAllDonors().get(0);
        else{
            alert.setContentText("no available donors");
        }

        alert.showAndWait();


    }

}
