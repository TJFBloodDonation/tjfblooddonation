package ro.ubb.tjfblooddonation.controller.clinicStaff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.w3c.dom.events.UIEvent;
import ro.ubb.tjfblooddonation.exceptions.UiException;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.service.BloodService;
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
    @Autowired
    BloodService bloodService;

    @FXML
    public Accordion accordion;
    @FXML
    public TextField searchTextField;

    private HealthWorker healthWorker;

    public void setHealthWorker(HealthWorker healthWorker) {
        this.healthWorker = healthWorker;
    }

    private void refresh(){
        List<LoginInformation> donors = usersService.getDonorsAccounts()
                .stream()
                .filter(d -> ((Donor)d.getPerson()).getForm() != null && ((Donor)d.getPerson()).getForm().getPassedDonateForm())
                .sorted(Comparator.comparing(d1 -> ((Donor)d1.getPerson()).getForm().getTimeCompletedDonateForm()))
                .collect(Collectors.toList());
        ObservableList<TitledPane> panes = FXCollections.observableArrayList();
        donors.stream()
                .filter(d -> (d.getUsername()).contains(searchTextField.getText()))
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

    private TitledPane getTitledPane(LoginInformation donorLoginInfo) {
        Donor d = (Donor)donorLoginInfo.getPerson();
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        TextField firstNameTextField = new TextField();
        TextField lastNameTextField = new TextField();
        TextField genderTextField = new TextField();
        TextField emailTextField = new TextField();
        TextField phoneNumberTextField = new TextField();
        TextField cnpTextField = new TextField();
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


        ++row;
        gridPane.add(new Label("Blood type"), 1, row);
        ToggleGroup bloodTypeGroup = new ToggleGroup();
        ArrayList<RadioButton> b1 = new ArrayList<>();
        b1.add(new RadioButton("0"));
        b1.add(new RadioButton("A"));
        b1.add(new RadioButton("B"));
        b1.add(new RadioButton("AB"));
        b1.forEach(b -> b.setToggleGroup(bloodTypeGroup));
        if(hasBloodType){
            b1.forEach(b -> {
                if(b.getText().equals(d.getBloodType()))
                    b.setSelected(true);
                b.setDisable(true);
            });
        }
        HBox hBox1 = new HBox();
        hBox1.setSpacing(5);
        hBox1.getChildren().setAll(b1);

        gridPane.add(hBox1, 2, row);
        ++row;
        gridPane.add(new Label("RH"), 1, row);
        ToggleGroup rhGroup = new ToggleGroup();
        ArrayList<RadioButton> b2 = new ArrayList<>();
        b2.add(new RadioButton("+"));
        b2.add(new RadioButton("-"));
        b2.forEach(b -> b.setToggleGroup(rhGroup));
        if(hasRh){
            b2.forEach(b -> {
                if(b.getText().equals(d.getRH()))
                    b.setSelected(true);
                b.setDisable(true);
            });
        }
        HBox hBox2 = new HBox();
        hBox2.setSpacing(5);
        hBox2.getChildren().setAll(b2);
        gridPane.add(hBox2, 2, row);
        if(!d.getForm().getPassedBasicCheckForm()) {
            addInGrid(gridPane, ++row, "Weight", weightTextField, false, "");
            addInGrid(gridPane, ++row, "Pulse", pulseTextField, false, "");
            addInGrid(gridPane, ++row, "Systolic blood pressure", bloodPressureTextField, false, "");
            Button button = new Button("Submit!");
            button.setOnAction(e -> {
                try {
                    int age = LocalDate.now().getYear() - d.getDateOfBirth().getYear();
                    if (!(age >= 18 && age < 60))
                        throw new UiException("Age is " + age + "; must be between 18 and 60 years");
                    int weight = Integer.parseInt(weightTextField.getText());
                    if (!(weight >= 50))
                        throw new UiException("Weight must be grater or equal than 50 kg");
                    int pulse = Integer.parseInt(pulseTextField.getText());
                    if (!(pulse >= 60 && pulse <= 100))
                        throw new UiException("The pulse must be between 60 and 100");
                    int systolicBloodPressure = Integer.parseInt(bloodPressureTextField.getText());
                    if (!(systolicBloodPressure >= 100 && systolicBloodPressure <= 180))
                        throw new UiException("The blood pressure must be between 100 and 180");
                    Form f = d.getForm();
                    f.setPassedBasicCheckForm(true);
                    String bloodType = b1.stream()
                            .filter(b -> b.selectedProperty().get())
                            .map(Labeled::getText)
                            .reduce("", (a, s) -> a + s);
                    String rh = b2.stream()
                            .filter(b -> b.selectedProperty().get())
                            .map(Labeled::getText)
                            .reduce("", (a, s) -> a + s);
                    if (bloodType.equals(""))
                        throw new UiException("Please select a blood type!");
                    if (rh.equals(""))
                        throw new UiException("Please select a rh!");
                    usersService.updateDonorInfo(donorLoginInfo.getUsername(), bloodType, rh);
                    usersService.completeForm(donorLoginInfo.getUsername(), f);


                    Messages.showConfirmation("All went well!", "Basic check form completed for this donor!");
                    refresh();
                } catch (Exception ex) {
                    Messages.showError(ex.getMessage());
                }
            });
            gridPane.add(button, 2, ++row);
        } else{
            Button button = new Button("Extract blood!");
            button.setOnAction(p -> {
                try {
                    Blood blood = Blood.builder()
                            .donor(d)
                            .recoltationDate(LocalDate.now())
                            .institution(healthWorker.getInstitution())
                            .build();
                    bloodService.donateBlood(donorLoginInfo.getUsername(), blood);
                    refresh();
                    Messages.showConfirmation("Blood successfully introduced into the DB!");
                    bloodService.separateBlood(blood.getId());
                    Messages.showConfirmation("Blood successfully separated!");
                } catch (Exception e){
                    Messages.showError(e.getMessage());
                }

            });
            gridPane.add(button, 2, ++row);
        }
        String title = donorLoginInfo.getUsername();
        if(d.getForm().getPassedBasicCheckForm())
            title = title + " - ready for donation!";
        return new TitledPane(title, gridPane);
    }
}
