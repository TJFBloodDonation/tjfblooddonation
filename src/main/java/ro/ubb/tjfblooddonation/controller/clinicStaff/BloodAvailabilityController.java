package ro.ubb.tjfblooddonation.controller.clinicStaff;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.BloodComponent;
import ro.ubb.tjfblooddonation.model.Request;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BloodAvailabilityController {

    @Autowired
    UsersService usersService;

    @Autowired
    BloodService bloodService;

    private Request request;

    private Set<BloodComponent> bloodComponents;

    private Set<BloodComponent> chosenBloodComponents;

    @FXML
    private RadioButton thrombocytesRadioButton;

    @FXML
    private ToggleGroup group;

    @FXML
    private RadioButton redBloodCellsRadioButton;

    @FXML
    private RadioButton plasmaRadioButton;

    @FXML
    private ListView<BloodComponent> componentsListView;

    @FXML
    private ListView<BloodComponent> selectedList;

    @FXML
    private Button manageRequestsButton;

    @FXML
    void manageRequestsButtonClicked(ActionEvent event) {

    }
    private void refreshBloodList() {
        Set<BloodComponent> components = new HashSet<>();
        if(request == null)
            return;
        if(thrombocytesRadioButton.selectedProperty().get())
            components = bloodComponents
                    .stream()
                    .filter(bc -> bc.getType().equals("thrombocytes"))
                    .collect(Collectors.toSet());
        if(plasmaRadioButton.selectedProperty().get())
            components = bloodComponents
                    .stream()
                    .filter(bc -> bc.getType().equals("plasma"))
                    .collect(Collectors.toSet());
        if(redBloodCellsRadioButton.selectedProperty().get())
            components = bloodComponents
                    .stream()
                    .filter(bc -> bc.getType().equals("red blood cells"))
                    .collect(Collectors.toSet());

        componentsListView.setItems(FXCollections.observableArrayList(components));
        selectedList.setItems(FXCollections.observableArrayList(chosenBloodComponents));
        System.out.println("Number of printed components is " + components.size());
        System.out.println("Number of total components is " + bloodComponents.size());
        System.out.println("Number of selected components " + chosenBloodComponents.size());
    }


    public void setRequest(Request request) {
        this.request = request;
        try{
            chosenBloodComponents = new HashSet<>();
            bloodComponents = bloodService.getOkRedBloodCells(request.getId());
            bloodComponents.addAll(bloodService.getOkPlasma(request.getId()));
            bloodComponents.addAll(bloodService.getOkThrombocytes(request.getId()));
            refreshBloodList();

            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> refreshBloodList());
            componentsListView.getSelectionModel().selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> {
                if(componentsListView.getSelectionModel().getSelectedItems().size() == 0)
                    return;
                BloodComponent bloodComponent = componentsListView.getSelectionModel().getSelectedItem();
                bloodComponents.remove(bloodComponent);
                chosenBloodComponents.add(bloodComponent);
                refreshBloodList();
                        System.out.println("Hereeee");
            });

        } catch (Exception e) {
            e.printStackTrace();
            Messages.showError(e.getMessage());
        }
    }
}
