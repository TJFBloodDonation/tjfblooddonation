package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;

@Controller
public class LogInController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    UsersService usersService;

    @FXML
    private TextField usernameTextBox;

    @FXML
    private TextField passwordTextBox;

    @FXML
    private Button logInButton;

    @FXML
    public void logInButtonPressed(ActionEvent actionEvent) {
        String username = usernameTextBox.getText();
        String password = passwordTextBox.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        LoginInformation loginInformation = LoginInformation.builder()
                .username(username)
                .password(password)
                .build();

        alert.setTitle("Login info" + usersService.getAllDonors().size());
        alert.setHeaderText("Haha I will reveal your username and password!");
        if(usersService.getAllDonors().size() > 0)
            alert.setContentText(loginInformation.toString() + "\n" +
                    "Example of donor:" +
                    usersService.getAllDonors().get(0));
        else{
            alert.setContentText(loginInformation.toString());
        }

        alert.showAndWait();

        if(username.equals("donor")){
            try {
                loader.createNewWindow("/fxml/donor.fxml", "Donor Main Page", actionEvent);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void registerButtonClicked(ActionEvent actionEvent) {
        try {
            loader.createNewWindow("/fxml/register.fxml", "Register Page", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
