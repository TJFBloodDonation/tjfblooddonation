package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.exceptions.ControllerError;
import ro.ubb.tjfblooddonation.model.Form;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
public class DonateFormController {

    @FXML
    Button submitFormButton;
    @Autowired
    UsersService usersService;
    private LoginInformation donorLogin = null;
    private boolean checked = false;

    public void setInfo(String username) {
        donorLogin = usersService.getLoginInformationByUsername(username);
    }

    @FXML
    public void checkedBox(ActionEvent actionEvent) {
        checked = true;
    }

    @FXML
    public void submitButtonClicked(ActionEvent actionEvent) {
        try {
            if (checked)
                throw new ControllerError("Can't donate");
            usersService.completeForm(donorLogin.getUsername(), Form.builder()
                    .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now())).passedDonateForm(true).build());
            Stage thisStage = (Stage) submitFormButton.getScene().getWindow();
            thisStage.close();
        } catch (ControllerError e) {
            usersService.completeForm(donorLogin.getUsername(), Form.builder().passedDonateForm(false)
                    .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now())).build());
            Messages.showWarning("Can't donate", "We are sorry, but you are not fit to donate. You can't have had any of the diseases " +
                    "previously specified, because the blood sample would do more harm to the patient receiving " +
                    "it, than it would do good. We are grateful for the initiative, though!");
            Stage thisStage = (Stage) submitFormButton.getScene().getWindow();
            thisStage.close();
        }
    }
}
