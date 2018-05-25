package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;


@Controller
public class UpdateStuffControler {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    UsersService usersService;

    private Long id;

    public void setId(Long id) {
        this.id = id;
        Messages.showError("This is the id", "" + id);
    }

    @FXML
    void saveClicked(ActionEvent event) {
        try {
            loader.createNewWindow("/fxml/UpdateStuff.fxml", "Update Page", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
