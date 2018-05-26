package ro.ubb.tjfblooddonation.controller;

import com.sun.deploy.util.FXLoader;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.Address;
import ro.ubb.tjfblooddonation.model.Institution;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;

@Controller
public class AddNewInstitutionController {

    @Autowired
    private UsersService usersService;

    @FXML
    private TextField nameTextBox;

    @FXML
    private TextField countryTextBox;

    @FXML
    private TextField regionTextBox;

    @FXML
    private TextField cityTextBox;

    @FXML
    private TextField streetTextBox;

    @FXML
    private ComboBox<Institution.types> typeComboBox;

    @FXML
    void addInstitution(ActionEvent event) {
        try{
            Address address = new Address(
                    countryTextBox.getText(),
                    regionTextBox.getText(),
                    cityTextBox.getText(),
                    streetTextBox.getText()
            );
            Institution institution = new Institution(
                    nameTextBox.getText(),
                    typeComboBox.getItems().get(typeComboBox.getSelectionModel().getSelectedIndex()),
                    address
            );
            usersService.addInstitution(institution);

            Messages.showConfirmation("Success!",
                    "Institution: " + nameTextBox.getText() + " successfully created!");
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }

    public void initialize(){
        typeComboBox.setItems(FXCollections.observableArrayList(Institution.types.values()));
    }

}
