package ro.ubb.tjfblooddonation.controller.doctor;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.HealthWorker;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.model.Request;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;


@Controller
public class DoctorController {
    private static final SpringFxmlLoader Loader = new SpringFxmlLoader();

    @Autowired
    BloodService bloodService;

    private LoginInformation loginInformation;

    public void setLoginInformation(LoginInformation loginInformation){
        this.loginInformation = loginInformation;
    }

    @FXML
    public void CheckRequestsClicked(ActionEvent event) {
        try {
            FXMLLoader ld = Loader.getLoader("/fxml/doctor/MyRequests.fxml");
            Loader.createNewWindow((Parent) ld.load(), "My Requests", null);
            ld.<MyRequestsController>getController().setLoginInformation(loginInformation);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void RequestBloodClicked(ActionEvent event) {
        try {
            FXMLLoader ld = Loader.getLoader("/fxml/doctor/RequestForm.fxml");
            Loader.createNewWindow((Parent) ld.load(), "Request Blood Form", null);
            ld.<RequestFormController>getController().setLoginInformation(loginInformation);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

