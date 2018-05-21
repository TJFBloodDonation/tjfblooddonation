package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.tjfblooddonation.model.Analysis;
import ro.ubb.tjfblooddonation.model.Blood;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.model.Institution;
import ro.ubb.tjfblooddonation.service.BloodService;

import java.util.Date;

public class BloodDonationController {

    @FXML
    private TextField bloodTypeTxt;

    @FXML
    private TextField rhTxt;

    @FXML
    private Date recoltationTxt;

    @FXML
    private Institution institutionTxt;

    @FXML
    private Donor donorTxt;

    @FXML
    private Analysis analysisTxt;

    @FXML
    private TextField bloodIdTxt;
    @Autowired
    BloodService bloodService;
    @FXML
    void bloodComboBox(ActionEvent event) {
        String bloodType = bloodTypeTxt.getText();
        Long bloodId = Long.valueOf(bloodIdTxt.getText());
        Analysis analysis=analysisTxt;
        String rH = rhTxt.getText();
        Date rDate=recoltationTxt;
        Institution institution=institutionTxt;
        Donor donor=donorTxt;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Blood blood = Blood.builder()
                .donor(donor)
                .recoltationDate((java.sql.Date) rDate)
                .institution(institution)
                .build();

            alert.setContentText(blood.toString() + "\n" +
                    "Example of sample of blood:" +
                    bloodService.getBlood(bloodId));

        }

    }


