package ro.ubb.tjfblooddonation.controller.doctor;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.service.RequestService;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;



@Controller
public class RequestFormController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @FXML
    private Button submitRequestButton;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField country;
    @FXML
    private TextField region;
    @FXML
    private TextField city;
    @FXML
    private TextField street;
    @FXML
    private TextField cnp;
    @FXML
    private TextField plasmaUnits;
    @FXML
    private TextField redBloodCellsUnits;
    @FXML
    private TextField tombocitesUnits;

    @FXML
    private ComboBox<Request.UrgencyLevel> urgencyLevel;
    @FXML
    private Checkbox bloodTypeA;
    @FXML
    private Checkbox bloodTypeB;
    @FXML
    private Checkbox bloodTypeAB;
    @FXML
    private Checkbox bloodTypeO;
    @FXML
    private Checkbox rhPositive;
    @FXML
    private Checkbox rhNegative;

    @Autowired
    UsersService usersService;

    @Autowired
    RequestService requestService;

    private LoginInformation doctorLogin;

    public void setLoginInformation(LoginInformation loginInformation) {
        this.doctorLogin = loginInformation;
    }

    //public void initialize(){  urgencyLevel.setItems(FXCollections.observableArrayList(Request.UrgencyLevel.values())); }

    @FXML
    void SubmitRequestClicked(ActionEvent event) {
        try {
            HealthWorker healthWorker = (HealthWorker) doctorLogin.getPerson();
            Institution institution = healthWorker.getInstitution();
            Address address = new Address(country.toString(), region.toString(), city.toString(), street.toString());
            IdCard idCard = new IdCard(address, cnp.toString());

            //rh
            String rH = "";
            if (rhPositive.isCursorSet())
                rH = "+";
            if (rhNegative.isCursorSet())
                rH = "-";
            if (rH == "")
                throw new IOException("There is no rH checked!");

            //blood type
            String bloodType = "";
            if (bloodTypeA.isCursorSet())
                bloodType = "A";
            if (bloodTypeB.isCursorSet())
                bloodType = "B";
            if (bloodTypeAB.isCursorSet())
                bloodType = "AB";
            if (bloodTypeO.isCursorSet())
                bloodType = "O";
            if (bloodType == "")
                throw new IOException("There is no blood type checked!");

            LocalDate requestDate = LocalDate.now();

            Patient patient = new Patient(firstName.toString(), lastName.toString(), email.toString(), phoneNumber.toString(), institution, idCard, bloodType, rH);
            Request request = new Request(healthWorker, patient, Byte.parseByte(plasmaUnits.toString()), Byte.parseByte(tombocitesUnits.toString()), Byte.parseByte(redBloodCellsUnits.toString()), requestDate, urgencyLevel.getItems().get(urgencyLevel.getSelectionModel().getSelectedIndex()));

            usersService.addPatient(patient);
            usersService.addInstitution(institution);
            requestService.addRequest(request);

            //I think it might be necesary to also add these 2 instances to the database tables, but I saw there was no repository created for them and I assumed there is a good reason for it. To be discussed :D
            //usersService.addIdCard(idCard);
            //usersService.addAdress(address);
            Messages.showConfirmation("Success!", "Request for patient " + firstName.toString() + " " + lastName.toString() + " with urgency level " + urgencyLevel.getItems().get(urgencyLevel.getSelectionModel().getSelectedIndex()).toString() + " succesfully created!");

        } catch (IOException e) {
            Messages.showError(e.getMessage());
        }
    }

}
