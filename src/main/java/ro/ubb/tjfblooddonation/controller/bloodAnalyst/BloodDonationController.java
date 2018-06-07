package ro.ubb.tjfblooddonation.controller.bloodAnalyst;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.Analysis;
import ro.ubb.tjfblooddonation.model.Blood;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.util.Set;

@Controller
public class BloodDonationController {

    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    BloodService bloodService;

    @FXML
    private RadioButton hivRadioButton;
    @FXML
    private RadioButton hepatitisBRadioButton;
    @FXML
    private RadioButton hepatitisCRadioButton;
    @FXML
    private RadioButton immunoRadioButton;
    @FXML
    private RadioButton syphilisRadioButton;
    @FXML
    private RadioButton htlvRadioButton;
    @FXML
    private RadioButton altRadioButton;
    @FXML
    private Button analyzeButton;
    @FXML
    public ComboBox<Blood> bloodComboBox;


    @FXML
    void analyzeButtonClicked(ActionEvent event) {
        if(bloodComboBox.getSelectionModel().isEmpty())
            Messages.showError("Unselected Sample", "You must select a blood sample from the provided list first!");
        else {
            Blood selectedBlood = bloodComboBox.getSelectionModel().getSelectedItem();
            Analysis analysis = Analysis.builder()
                    .hiv(hivRadioButton.isSelected())
                    .hb(hepatitisBRadioButton.isSelected())
                    .hcv(hepatitisCRadioButton.isSelected())
                    .imunoHematology(immunoRadioButton.isSelected())
                    .sifilis(syphilisRadioButton.isSelected())
                    .alt(altRadioButton.isSelected())
                    .htlv(htlvRadioButton.isSelected())
                    .build();
            bloodService.analyseBlood(selectedBlood.getId(), analysis);
            refresh();
        }
    }

    private void refresh(){
        Set<Blood> blood = bloodService.getUnanalysedBlood();
        bloodComboBox.setCellFactory(lv -> new ListCell<Blood>() {
            @Override
            public void updateItem(Blood item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.shortToString());
                }
            }
        });
        bloodComboBox.setItems(FXCollections.observableArrayList(blood));

        hivRadioButton.setSelected(false);
        hepatitisBRadioButton.setSelected(false);
        hepatitisCRadioButton.setSelected(false);
        immunoRadioButton.setSelected(false);
        syphilisRadioButton.setSelected(false);
        altRadioButton.setSelected(false);
        htlvRadioButton.setSelected(false);
    }

    public void initialize(){
        refresh();
    }
}





