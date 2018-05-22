package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;

@Controller
public class AdminController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();


    @Autowired
    UsersService usersService;

    @FXML
    private TextField searchBox;

    @FXML
    private ListView<String> staffListView;

    public void initialize(){
        System.out.println("Users service: " + usersService);
        System.out.println("Staff list view: " + staffListView);
        usersService.getAllDonors().forEach(d -> staffListView.getItems().add(d.toString()));
    }

    @FXML
    void AddClicked(ActionEvent event) {
        try {
            loader.createNewWindow("/fxml/AddStuff.fxml", "Add Health Worker Page", null);
            loader.createNewWindow("/fxml/AddStuff.fxml", "Admin Page", null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DeleteClicked(ActionEvent event) {
            try {
                //usersService.deleteHealthWorkerAccount(somehow determine selected health worker);
                loader.createNewWindow("/fxml/Admin.fxml", "Admin Page", null);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
    }

    @FXML
    void UpdateClicked(ActionEvent event) {
        try{
            loader.createNewWindow("/fxml/UpdateStuff", "User Info Page", null);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
