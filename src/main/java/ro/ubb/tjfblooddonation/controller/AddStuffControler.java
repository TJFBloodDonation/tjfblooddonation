package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;


@Controller
public class AddStuffControler {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    UsersService usersService;

    @FXML
    private TextField hwUsernameTextBox;

    @FXML
    void addHealthWorkerClicked(ActionEvent event) {
        try {
            //I think here it should close page, return to Admin page and write message "Health worker account succesfully created!"
            //Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //alert.setContentText("Health worker account: "+ hwUsernameTextBox.getText() +" succesfully created!");
            loader.createNewWindow("/fxml/AddStuff.fxml", "Add Page", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
