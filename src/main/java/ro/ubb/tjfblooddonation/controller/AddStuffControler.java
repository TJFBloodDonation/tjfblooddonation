package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    void addHealthWorkerClicked(ActionEvent event) {
        try {
            loader.createNewWindow("/fxml/AddStuff.fxml", "Add Page", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
