package ro.ubb.tjfblooddonation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.exceptions.UiException;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class SearchDonors {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    UsersService usersService;

    @FXML
    public Accordion accordion;
    @FXML
    public TextField searchTextField;

    public void refresh(){
        List<Donor> donors = usersService.getAllDonors()
                .stream()
                .filter(d -> d.getForm() != null && d.getForm().getPassedDonateForm())
                .sorted(Comparator.comparing(d1 -> d1.getForm().getTimeCompletedDonateForm()))
                .collect(Collectors.toList());
        ObservableList<TitledPane> panes = FXCollections.observableArrayList();
        donors.stream()
                .filter(d -> (d.getFirstName() + " " + d.getLastName()).contains(searchTextField.getText()))
                .forEach(d -> panes.add(getTitledPane(d)));
        accordion.getPanes().setAll(panes);
    }
    public void initialize(){
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> refresh());
        refresh();
    }

    private void addInGrid(GridPane gridPane, int row, String label, TextField textField, Boolean disabled, String text){
        gridPane.add(new Label(label), 1, row);
        if(text == null){
            text = "";
        }
        textField.setText(text);
        textField.setDisable(disabled);
        gridPane.add(textField, 2, row);
    }

    private TitledPane getTitledPane(Donor d) {
        GridPane gridPane = new GridPane();
        TextField firstNameTextField = new TextField();
        TextField lastNameTextField = new TextField();
        TextField genderTextField = new TextField();
        TextField emailTextField = new TextField();
        TextField phoneNumberTextField = new TextField();
        TextField cnpTextField = new TextField();
        TextField bloodTypeTextField = new TextField();
        TextField rhTextField = new TextField();
        TextField weightTextField = new TextField();
        TextField bloodPressureTextField = new TextField();
        TextField pulseTextField = new TextField();
        int row = 0;
        addInGrid(gridPane, ++row, "First name", firstNameTextField, true, d.getFirstName());
        addInGrid(gridPane, ++row, "Last name", lastNameTextField, true, d.getLastName());
        addInGrid(gridPane, ++row, "Gender", genderTextField, true, d.getGender());
        addInGrid(gridPane, ++row, "Email", emailTextField, true, d.getEmail());
        addInGrid(gridPane, ++row, "Phone number", phoneNumberTextField, true, d.getPhoneNumber());
        addInGrid(gridPane, ++row, "CNP", cnpTextField, true, d.getIdCard().getCnp());
        boolean hasBloodType = d.getBloodType() != null;
        boolean hasRh = d.getRH() != null;
        addInGrid(gridPane, ++row, "Blood type", bloodTypeTextField, hasBloodType, d.getBloodType());
        addInGrid(gridPane, ++row, "RH", rhTextField, hasRh, d.getRH());
        addInGrid(gridPane, ++row, "Weight", weightTextField, false, "");
        addInGrid(gridPane, ++row, "Pulse", pulseTextField, false, "");
        addInGrid(gridPane, ++row, "Systolic blood pressure", bloodPressureTextField, false, "");
        Button button = new Button("Submit!");
        button.setOnAction(e -> {
            try{
                int age = LocalDate.now().getYear() - d.getDateOfBirth().getYear();
                if(!(age >= 18 && age < 60))
                    throw new UiException("Age is " + age + "; must be between 18 and 60 years");
                int weight = Integer.parseInt(weightTextField.getText());
                if(!(weight >= 50))
                    throw new UiException("Weight must be grater or equal than 50 kg");
                int pulse = Integer.parseInt(pulseTextField.getText());
                if(!(pulse >= 60 && pulse <= 100))
                    throw new UiException("The pulse must be between 60 and 100");
                int systolicBloodPressure = Integer.parseInt(bloodPressureTextField.getText());
                if(!(systolicBloodPressure >= 100 && systolicBloodPressure <= 180))
                    throw new UiException("The blood pressure must be between 100 and 180");
                d.getForm().setPassedDonateForm(true);
                usersService.updateDonor(d);
                Messages.showConfirmation("All went well!", "All is good");
                refresh();
            } catch (Exception ex){
                Messages.showError(ex.getMessage());
            }
        });
        gridPane.add(button, 2, ++row);
        String title = d.getFirstName() + " " + d.getLastName();
        if(d.getForm().getPassedBasicCheckForm())
            title = title + " - ready for donation!";
        return new TitledPane(title, gridPane);
    }
}
