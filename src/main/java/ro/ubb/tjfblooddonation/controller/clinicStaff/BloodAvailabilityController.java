package ro.ubb.tjfblooddonation.controller.clinicStaff;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.Blood;
import ro.ubb.tjfblooddonation.model.BloodComponent;
import ro.ubb.tjfblooddonation.model.Request;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.service.RequestService;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;

import javax.swing.event.ChangeListener;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BloodAvailabilityController {

    @Autowired
    UsersService usersService;

    @Autowired
    BloodService bloodService;

    @Autowired
    RequestService requestService;

    private Request request;

    private Set<BloodComponent> bloodComponents;

    private Set<BloodComponent> chosenBloodComponents;

    @FXML
    public Label label;

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
        try{
            requestService.fulfillRequest(
                    request.getId(),
                    chosenBloodComponents
                            .stream()
                            .filter(bc -> bc.getType().equals("thrombocytes"))
                            .map(BloodComponent::getId)
                            .collect(Collectors.toList()),
                    chosenBloodComponents
                            .stream()
                            .filter(bc -> bc.getType().equals("plasma"))
                            .map(BloodComponent::getId)
                            .collect(Collectors.toList()),
                    chosenBloodComponents
                            .stream()
                            .filter(bc -> bc.getType().equals("red blood cells"))
                            .map(BloodComponent::getId)
                            .collect(Collectors.toList())
            );
            Messages.showConfirmation("Request satisfied!");
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (Exception e){
            Messages.showError(e.getMessage());
        }
    }
    private void refreshBloodList() {
        Set<BloodComponent> components = new HashSet<>();
        if(request == null)
            return;
        if(thrombocytesRadioButton.selectedProperty().get() &&
                getNumber(chosenBloodComponents, "thrombocytes") != request.getThrombocytesUnits()) {
            components = bloodComponents
                    .stream()
                    .filter(bc -> bc.getType().equals("thrombocytes"))
                    .collect(Collectors.toSet());
        }
        if(plasmaRadioButton.selectedProperty().get() &&
            getNumber(chosenBloodComponents, "plasma") != request.getPlasmaUnits()) {
            components = bloodComponents
                    .stream()
                    .filter(bc -> bc.getType().equals("plasma"))
                    .collect(Collectors.toSet());
        }
        if(redBloodCellsRadioButton.selectedProperty().get() &&
            getNumber(chosenBloodComponents, "red blood cells") != request.getRedBloodCellsUnits()) {
            components = bloodComponents
                    .stream()
                    .filter(bc -> bc.getType().equals("red blood cells"))
                    .collect(Collectors.toSet());
        }

        componentsListView.setItems(FXCollections.observableArrayList(components));
        selectedList.setItems(FXCollections.observableArrayList(chosenBloodComponents));
        updateLabel();
    }

    private long getNumber(Set<BloodComponent> set, String type){
        return set.stream().filter(bc -> bc.getType().equals(type)).count();
    }

    private void updateLabel() {
        long chosenT = getNumber(chosenBloodComponents, "thrombocytes");
        long chosenR = getNumber(chosenBloodComponents, "red blood cells");
        long chosenP = getNumber(chosenBloodComponents, "plasma");
        long allT = request.getThrombocytesUnits();
        long allR = request.getRedBloodCellsUnits();
        long allP = request.getPlasmaUnits();
        label.setText( "Thrombocites: " + chosenT + "/" + allT + "\n" +
                    "Red blood cells: " + chosenR + "/" + allR + "\n" +
                             "Plasma: " + chosenP + "/" + allP + "\n");
    }

    private ListCell<BloodComponent> getListCell(){
        return new ListCell<BloodComponent>(){
            @Override
            protected void updateItem(BloodComponent bloodComponent, boolean empty){
                super.updateItem(bloodComponent, empty);
                if(empty || bloodComponent == null)
                    setText("");
                else {
                        setText(bloodComponent.getType() + " " + bloodComponent.getBlood().getDonor().getBloodType() +
                                " " + bloodComponent.getBlood().getDonor().getRH());
                }
            }
        };
    }

    public void setRequest(Request request) {
        this.request = request;
        try{

            componentsListView.setCellFactory(c -> getListCell());
            selectedList.setCellFactory(c -> getListCell());
            chosenBloodComponents = new HashSet<>();
            bloodComponents = bloodService.getOkRedBloodCells(request.getId());
            bloodComponents.addAll(bloodService.getOkPlasma(request.getId()));
            bloodComponents.addAll(bloodService.getOkThrombocytes(request.getId()));
            refreshBloodList();

            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> refreshBloodList());
            componentsListView.getSelectionModel().selectedItemProperty()
                    .addListener((observable, oldValue, newValue) ->
                            transfer(bloodComponents,
                                    chosenBloodComponents,
                                    componentsListView.getSelectionModel().getSelectedItem())
                    );

            selectedList.getSelectionModel().selectedItemProperty()
                    .addListener((observable, oldValue, newValue) ->
                            transfer(chosenBloodComponents,
                                    bloodComponents,
                                    selectedList.getSelectionModel().getSelectedItem())
                    );

        } catch (Exception e) {
            e.printStackTrace();
            Messages.showError(e.getMessage());
        }
    }

    private void transfer(Set<BloodComponent> bloodComponents, Set<BloodComponent> chosenBloodComponents,
                          BloodComponent bloodComponent) {
        if(bloodComponent == null)
            return;
        Platform.runLater(() -> {
            bloodComponents.remove(bloodComponent);
            chosenBloodComponents.add(bloodComponent);
            refreshBloodList();
        });
    }
}
