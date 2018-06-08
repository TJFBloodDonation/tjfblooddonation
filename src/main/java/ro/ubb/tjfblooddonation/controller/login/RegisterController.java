package ro.ubb.tjfblooddonation.controller.login;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.controller.donor.DonorController;
import ro.ubb.tjfblooddonation.exceptions.BaseException;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;
import java.time.LocalDate;

@Controller
public class RegisterController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @Autowired
    UsersService usersService;

    @FXML
    private TextField firstNameTxt;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private TextField countryTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private TextField birthdayTxt;

    @FXML
    private TextField regionTxt;

    @FXML
    private TextField cityTxt;

    @FXML
    private TextField streetTxt;

    @FXML
    private TextField emailTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private TextField usernameTxt;


    @FXML
    private Button RegisterButton;

    @FXML
    public void registerButtonPressed(ActionEvent event) {

        String firstName = firstNameTxt.getText();
        String lastName = lastNameTxt.getText();
        String country = countryTxt.getText();
        String region = regionTxt.getText();
        String city = cityTxt.getText();
        String street = streetTxt.getText();
        String birthday = birthdayTxt.getText();
        String phone = phoneTxt.getText();
        String email = emailTxt.getText();
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        LoginInformation loginInformation = LoginInformation.builder()
                .username(username)
                .password(password)
                .build();

        Address addrDonor = new Address(country, region, city, street);
        IdCard idCardDonor = new IdCard(addrDonor, "1234567890124");
        Donor newDonor = Donor.builder().firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phone)
                .residence(addrDonor)
                .dateOfBirth(LocalDate.parse(birthday))
                .idCard(idCardDonor)
                .build();

        usersService.createUserAccount(username, password, newDonor);


        alert.setTitle("Register info" + usersService.getAllDonors().size());
        alert.setHeaderText("Welcome to our application! You have been succesfully registered.");
        if (usersService.getAllDonors().size() > 0)
            alert.setContentText(loginInformation.toString() + "\n");
        else {
            alert.setContentText(loginInformation.toString());
        }

        alert.showAndWait();
        alert.close();
        try {
            FXMLLoader ld = loader.getLoader("/fxml/donor/Donor.fxml");
            Stage childStage = loader.createNewWindow((Parent) ld.load(), "Donor Main Page", event);
            ld.<DonorController>getController().setLoginInfo(username);
        } catch (BaseException e) {
            Messages.showError("Login error!", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Messages.showError("Fatal error!", e.toString());
        }


    }
}
