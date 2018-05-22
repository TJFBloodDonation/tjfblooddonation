package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.HealthWorker;
import ro.ubb.tjfblooddonation.model.Institution;
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
    private ComboBox<HealthWorker.types> typesComboBox;
    @FXML
    private ComboBox<Institution> institutionComboBox;

    @FXML
    void addHealthWorkerClicked(ActionEvent event) {
       // try {
            //I think here it should close page, return to Admin page and write message "Health worker account succesfully created!"

            //loader.createNewWindow("/fxml/AddStuff.fxml", "Add Page", null);
            HealthWorker.types type = typesComboBox.getSelectionModel().getSelectedItem();
            Institution institution = institutionComboBox.getSelectionModel().getSelectedItem();
            HealthWorker healthWorker = new HealthWorker(hwFirstNameTextBox.getText(), hwLastNameTextBox.getText(),hwEmailTextBox.getText(), hwPhoneNumberTextBox.getText(), type ,institution);
            usersService.createHealthWorkerAccont(hwUsernameTextBox.getText(), hwPasswordTextBox.getText(), healthWorker);
            Messages.showConfirmation("","Health worker account: " + hwUsernameTextBox.getText() + " succesfully created!");
        //}
        //catch (IOException e) {
           // e.printStackTrace();
//        }

    }
}