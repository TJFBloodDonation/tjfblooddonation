package ro.ubb.tjfblooddonation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.exceptions.ControllerError;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
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
    private ListView<LoginInformation> staffListView;

    public void initialize(){
        try {
            searchBox.textProperty().addListener((observable, oldValue, newValue) -> refresh());
            staffListView.setCellFactory(c -> new ListCell<LoginInformation>(){
                @Override
                protected void updateItem(LoginInformation loginInformation, boolean empty){
                    super.updateItem(loginInformation, empty);
                    if(empty || loginInformation == null)
                        setText("");
                    else
                        setText(loginInformation.getUsername());
                }
            });
            refresh();
        }catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }
    private void refresh() {
        staffListView.setItems(FXCollections.observableArrayList());
        usersService.getHealthWorkersAccounts()
                .stream()
                .filter(
                        hw -> hw.getUsername()
                        .toLowerCase()
                        .contains(searchBox
                                .getText()
                                .toLowerCase())
                )
                .forEach(hw -> staffListView.getItems().add(hw));
    }

    @FXML
    void AddClicked(ActionEvent event) {
        try {
            Stage childStage = loader.createNewWindow("/fxml/AddStuff.fxml", "Add Health Worker Page", null);
            childStage.setOnHidden((p) -> refresh());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DeleteClicked(ActionEvent event) {
        try {
            ObservableList<LoginInformation> list = staffListView.getSelectionModel().getSelectedItems();
            if (list.size() == 0) {
                throw new ControllerError("No user selected!");
            }
            usersService.deleteHealthWorkerAccount(
                    list.get(0).getUsername()
            );
            refresh();
        }
        catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }


    @FXML
    void UpdateClicked(ActionEvent event) {
        try{
            ObservableList<LoginInformation> list = staffListView.getSelectionModel().getSelectedItems();
            if(list.size() == 0){
                throw new ControllerError("No user selected!");
            }
            FXMLLoader ld = loader.getLoader("/fxml/AddStuff.fxml");
            Stage childStage = loader.createNewWindow((Parent) ld.load(), "User Info Page", null);
            ld.<AddStuffControler>getController().setId(list.get(0).getUsername());
            childStage.setOnHidden((p) -> refresh());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }
}
