package ro.ubb.tjfblooddonation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.HealthWorker;
import ro.ubb.tjfblooddonation.model.Institution;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;


@Controller
public class AddStuffControler {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    UsersService usersService;

    @FXML
    private TextField hwFirstNameTextBox;
    @FXML
    private TextField hwLastNameTextBox;
    @FXML
    private TextField hwUsernameTextBox;
    @FXML
    private TextField hwPasswordTextBox;
    @FXML
    private TextField hwEmailTextBox;
    @FXML
    private TextField hwPhoneNumberTextBox;
    @FXML
    private Button addHealthWorkerButton;
    @FXML
    private ComboBox<HealthWorker.types> typesComboBox;
    @FXML
    private ComboBox<Institution> institutionComboBox;

    private LoginInformation loginInformation = null;
    public void setId(String username) {
        try {
            loginInformation = usersService.getLoginInformationByUsername(username);
            HealthWorker hw = (HealthWorker) loginInformation.getPerson();
            hwFirstNameTextBox.setText(hw.getFirstName());
            hwLastNameTextBox.setText(hw.getLastName());
            hwUsernameTextBox.setText(loginInformation.getUsername());
            hwPasswordTextBox.setText("");
            hwPhoneNumberTextBox.setText(hw.getPhoneNumber());
            hwEmailTextBox.setText(hw.getEmail());
            int index = typesComboBox.getItems().indexOf(
                    HealthWorker.stringToType(hw.getType())
            );
            typesComboBox.getSelectionModel().select(index);
            index = institutionComboBox.getItems().indexOf(
                    hw.getInstitution()
            );
            institutionComboBox.getSelectionModel().select(index);
            hwPasswordTextBox.setPromptText("New password (Optional)");
            addHealthWorkerButton.setText("Update Health Worker");
            hwUsernameTextBox.setDisable(true);
        } catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }

    public void initialize(){
        try {
            ObservableList<HealthWorker.types> types = FXCollections.observableArrayList(HealthWorker.types.values());
            typesComboBox.setItems(types);
            ObservableList<Institution> institutions = FXCollections.observableArrayList(usersService.getAllInstitutions());
            institutionComboBox.setItems(institutions);
        } catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }

    @FXML
    void addHealthWorkerClicked(ActionEvent event) {
        try {
            HealthWorker.types type = typesComboBox.getSelectionModel().getSelectedItem();
            Institution institution = institutionComboBox.getSelectionModel().getSelectedItem();
            HealthWorker healthWorker = new HealthWorker(
                    hwFirstNameTextBox.getText(),
                    hwLastNameTextBox.getText(),
                    hwEmailTextBox.getText(),
                    hwPhoneNumberTextBox.getText(),
                    type,
                    institution
            );
            if (loginInformation == null) {
                usersService.createHealthWorkerAccont(
                        hwUsernameTextBox.getText(),
                        hwPasswordTextBox.getText(),
                        healthWorker
                );
                Messages.showConfirmation("Success!",
                        "Health worker account: " + hwUsernameTextBox.getText() + " successfully created!");
            } else {
                healthWorker.setId(loginInformation.getPerson().getId());
                usersService.updateHealthWorkerAccount(
                        hwUsernameTextBox.getText(),
                        hwPasswordTextBox.getText(),
                        healthWorker
                );
                Messages.showConfirmation("Success!",
                        "Health worker account: " + hwUsernameTextBox.getText() + " successfully updated!");
            }
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }

    public void addNewInstitution(ActionEvent actionEvent) {
        try{


        } catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }
}