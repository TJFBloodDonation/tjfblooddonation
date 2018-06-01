package ro.ubb.tjfblooddonation.controller.donor;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.Analysis;
import ro.ubb.tjfblooddonation.service.BloodService;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;


@Controller
public class AnalysisController {

    private Analysis analysis;
    @Autowired
    BloodService bloodService;

    @FXML
    Label recoltationDateLabel;

    @FXML
    CheckBox hivCheckBox;
    @FXML
    CheckBox hbCheckBox;
    @FXML
    CheckBox hcCheckBox;
    @FXML
    CheckBox syphilisCheckBox;
    @FXML
    CheckBox htlvCheckBox;
    @FXML
    CheckBox altCheckBox;
    @FXML
    CheckBox immunoCheckBox;

    public void setInfo(Analysis analysis, LocalDate date) {
        this.analysis = analysis;
        recoltationDateLabel.setText(date.getDayOfMonth() + " " +
                date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " +
                date.getYear());
        populate();
    }

    private void populate() {
        hivCheckBox.setStyle("-fx-opacity: 1");
        hbCheckBox.setStyle("-fx-opacity: 1");
        hcCheckBox.setStyle("-fx-opacity: 1");
        htlvCheckBox.setStyle("-fx-opacity: 1");
        syphilisCheckBox.setStyle("-fx-opacity: 1");
        altCheckBox.setStyle("-fx-opacity: 1");
        immunoCheckBox.setStyle("-fx-opacity: 1");

        if(analysis.getHiv()) {
            hivCheckBox.setSelected(true);
            hivCheckBox.setTextFill(Color.web("#ff0000"));
        }
        if(analysis.getHb()) {
            hbCheckBox.setSelected(true);
            hbCheckBox.setTextFill(Color.web("#ff0000"));
        }
        if(analysis.getHcv()) {
            hcCheckBox.setSelected(true);
            hcCheckBox.setTextFill(Color.web("#ff0000"));
        }
        if(analysis.getHtlv()) {
            htlvCheckBox.setSelected(true);
            htlvCheckBox.setTextFill(Color.web("#ff0000"));
        }
        if(analysis.getSifilis()) {
            syphilisCheckBox.setSelected(true);
            syphilisCheckBox.setTextFill(Color.web("#ff0000"));
        }
        if(analysis.getAlt()) {
            altCheckBox.setSelected(true);
            altCheckBox.setTextFill(Color.web("#ff0000"));
        }
        if(analysis.getImunoHematology()) {
            immunoCheckBox.setSelected(true);
            immunoCheckBox.setTextFill(Color.web("#ff0000"));
        }
    }
}
