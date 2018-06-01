package ro.ubb.tjfblooddonation.controller.donor;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;


@Controller
public class DonorController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @Autowired
    UsersService usersService;
    @Autowired
    BloodService bloodService;
    private LoginInformation donorLogin = null;
    @FXML
    private Label userLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label residenceLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Label bloodLabel;

    @FXML
    private Button settingsButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button donateButton;


    public void setLoginInfo(String username) {
        donorLogin = usersService.getLoginInformationByUsername(username);
        populate();
    }

    private void populate() {
        userLabel.setText(donorLogin.getUsername());
        Donor donor = usersService.getDonor(donorLogin.getUsername());
        nameLabel.setText(donor.getFirstName() + " " + donor.getLastName());
        emailLabel.setText(donor.getEmail());
        phoneLabel.setText(donor.getPhoneNumber());
        residenceLabel.setText(donor.getResidence().getCountry() + " " + donor.getResidence().getRegion() + " " +
                donor.getResidence().getCity() + " " + donor.getResidence().getStreet());
        birthdayLabel.setText(donor.getDateOfBirth().toString());
        if (donor.getRH() != null && donor.getBloodType() != null)
            bloodLabel.setText(donor.getBloodType() + donor.getRH());
        else
            bloodLabel.setText("unknown");

        if(donor.getMessage() != null) {
            Messages.showSomething("Maybe come donate", donor.getMessage());
            donor.setMessage(null);
            usersService.updateUserAccount(donorLogin.getUsername(), donor);
        }
    }

    @FXML
    void donateClicked() {
        try {
            FXMLLoader ld = loader.getLoader("/fxml/donor/DonateForm.fxml");
            Stage childStage = loader.createNewWindow((Parent) ld.load(), "Donate Form", null);
            ld.<DonateFormController>getController().setInfo(donorLogin.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Messages.showError(e.getMessage());
        }
    }

    @FXML
    void settingsClicked() {
        try {
            FXMLLoader ld = loader.getLoader("/fxml/donor/DonorSettings.fxml");
            Stage childStage = loader.createNewWindow((Parent) ld.load(), "Account Settings", null);
            DonorSettingsController childController = ld.getController();
            childController.setInfo(donorLogin.getUsername());
            childController.setSaveClickedCallback( nothing -> this.populate());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Messages.showError(e.getMessage());
        }
    }

    @FXML
    void historyClicked() {
        try {
            FXMLLoader ld = loader.getLoader("/fxml/donor/DonationHistory.fxml");
            Stage childStage = loader.createNewWindow((Parent) ld.load(), "Donation History", null);
            DonationHistoryController childController = ld.getController();
            childController.setInfo(donorLogin.getUsername());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Messages.showError(e.getMessage());
        }
    }

    @FXML
    void logoutClicked(ActionEvent actionEvent) {
        try {
        loader.createNewWindow("/fxml/login/Login.fxml", "Blood donation", actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Messages.showError(e.getMessage());
        }
    }
}
