package ro.ubb.tjfblooddonation.controller.login;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

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
        private TextField adressTxt;

        @FXML
        private TextField phoneTxt;

        @FXML
        private TextField CNPTxt;

        @FXML
        private TextField confPassTxt;

        @FXML
        private TextField passTxt;

        @FXML
        private TextField usernameTxt;



        @FXML
        private Button RegisterButton;

        @FXML
        public void registerButtonPressed(ActionEvent event){

            String firstName= firstNameTxt.getText();
            String lastName= lastNameTxt.getText();
            String address = adressTxt.getText();
            String CNP = CNPTxt.getText();
            String phone= phoneTxt.getText();
            String username = usernameTxt.getText();
            String password = passTxt.getText();
            String confpass= confPassTxt.getText();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            LoginInformation loginInformation = LoginInformation.builder()
                    .username(username)
                    .password(password)
                    .build();

            alert.setTitle("Register info" + usersService.getAllDonors().size());
            alert.setHeaderText("Welcome to our application! You have been succesfully registered.");
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
                    loader.createNewWindow("/fxml/donor.fxml", "Donor Main Page", null);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }



}
