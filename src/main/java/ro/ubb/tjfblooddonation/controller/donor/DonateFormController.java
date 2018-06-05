package ro.ubb.tjfblooddonation.controller.donor;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.exceptions.ControllerError;
import ro.ubb.tjfblooddonation.model.Form;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.model.Patient;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
public class DonateFormController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @FXML
    Button submitFormButton;
    @Autowired
    UsersService usersService;
    @FXML
    CheckBox pregnantCheckBox;
    @FXML
    ListView<Patient> patientList;
    private LoginInformation donorLogin = null;
    private boolean checkedDisease = false;
    private boolean checkedMinor = false;

    public void setInfo(String username) {
        donorLogin = usersService.getLoginInformationByUsername(username);
        if (usersService.getDonor(donorLogin.getUsername()).getGender().equals("male"))
            pregnantCheckBox.setDisable(true);
        patientList.setCellFactory(lv -> new ListCell<Patient>() {
            @Override
            public void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.shortToString());
                }
            }
        });
        patientList.setItems(FXCollections.observableArrayList());
        usersService.getAllPatients().forEach(patient -> patientList.getItems().add(patient));
    }

    @FXML
    public void checkedDiseaseBox(ActionEvent actionEvent) {
        checkedDisease = true;
    }


    @FXML
    public void checkedMinorBox(ActionEvent actionEvent) {
        checkedMinor = true;
    }

    @FXML
    public void submitButtonClicked(ActionEvent actionEvent) {
        try {
            if (checkedDisease || checkedMinor)
                throw new ControllerError("Can't donate");

            Form form = Form.builder()
                    .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now()))
                    .passedDonateForm(true)
                    .build();

            if (!patientList.getSelectionModel().isEmpty())
                form.setPatient(patientList.getSelectionModel().getSelectedItems().get(0));

            usersService.completeForm(donorLogin.getUsername(), form);

            Stage thisStage = (Stage) submitFormButton.getScene().getWindow();
            loader.createNewWindow("/fxml/donor/BeforeDonating.fxml", "Before donating", null);
            thisStage.close();
        } catch (ControllerError e) {
            if (checkedDisease) {
                usersService.completeForm(donorLogin.getUsername(), Form.builder().passedDonateForm(false)
                        .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now())).build());
                Messages.showWarning("Can't donate", "We are sorry, but you are not fit to donate. You " +
                        "can't have had any of the diseases previously specified, because the blood sample would do " +
                        "more harm to the patient receiving it, than it would do good. We are grateful for the " +
                        "initiative, though!");
                Stage thisStage = (Stage) submitFormButton.getScene().getWindow();
                thisStage.close();
                checkedDisease = false;
                checkedMinor = false;
            } else if (checkedMinor) {
                usersService.completeForm(donorLogin.getUsername(), Form.builder().passedDonateForm(false)
                        .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now())).build());
                Messages.showWarning("Can't donate", "We are sorry, but you are not fit to donate. " +
                        "Try again when none of the statements you selected apply any longer to you.");
                Stage thisStage = (Stage) submitFormButton.getScene().getWindow();
                thisStage.close();
                checkedMinor = false;
            }
        } catch (IOException ex) {
            Messages.showError(ex.getMessage());
        }
    }
}
