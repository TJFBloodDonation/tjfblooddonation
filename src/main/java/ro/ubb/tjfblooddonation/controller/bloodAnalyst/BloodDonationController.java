package ro.ubb.tjfblooddonation.controller.bloodAnalyst;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//public class BloodDonationController {
//
//    @FXML
//    private TextField bloodTypeTxt;
//
//    @FXML
//    private TextField rhTxt;
//
//    @FXML
//    private LocalDate recoltationTxt;
//
//    @FXML
//    private Institution institutionTxt;
//
//    @FXML
//    private Donor donorTxt;
//
//    @FXML
//    private Analysis analysisTxt;
//
//    @FXML
//    private TextField bloodIdTxt;


@Controller
public class BloodDonationController {
//    @FXML
//    private ComboBox<?> bloodComboBox;
    private Set<BloodComponent> bloodComponents;

    @FXML
    private RadioButton hepatitisRadioButton;

    @FXML
    private RadioButton malariaRadioButton;

    @FXML
    private RadioButton epilepsyRadioButton;

    @FXML
    private RadioButton mentalRadioButton;

    @FXML
    private RadioButton ulcerRadioButton;

    @FXML
    private RadioButton diabetRadioButton;

    @FXML
    private RadioButton heartRadioButton;

    @FXML
    private RadioButton skinRadioButton;

    @FXML
    private RadioButton cancerRadioButton;

    @FXML
    private RadioButton myopiaRadioButton;

    @FXML
    private Button analyzeButton;

    @FXML
    void analyzeButtonClicked(ActionEvent event) {

    }

    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @Autowired
    BloodService bloodService;

    @FXML
    public ComboBox bloodComboBox;


    private void refresh(){
        Set<Blood> blood = bloodService.getUnanalysedBlood();

        if(hepatitisRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("hepatitis"))
                    .collect(Collectors.toSet());
        if(malariaRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("malaria"))
                    .collect(Collectors.toSet());
        if(epilepsyRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("epilepsy"))
                    .collect(Collectors.toSet());
        if(mentalRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("mental disease"))
                    .collect(Collectors.toSet());
        if(ulcerRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("ulcer"))
                    .collect(Collectors.toSet());
        if(diabetRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("diabet"))
                    .collect(Collectors.toSet());
        if(heartRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("heart disease"))
                    .collect(Collectors.toSet());
        if(skinRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("skin disease"))
                    .collect(Collectors.toSet());
        if(cancerRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("cancer"))
                    .collect(Collectors.toSet());
        if(myopiaRadioButton.selectedProperty().get())
            blood = blood
                    .stream()
                    .filter(bc -> bc.getAnalysis().equals("myopia"))
                    .collect(Collectors.toSet());
        bloodComboBox.setItems(FXCollections.observableArrayList(blood));
        System.out.println("Number of diseases is " + blood.size());

    }
    public void initialize(){
        refresh();
    }
}




//    @FXML
//    void bloodComboBox(ActionEvent event) {
//        String bloodType = bloodTypeTxt.getText();
//        Long bloodId = Long.valueOf(bloodIdTxt.getText());
//        Analysis analysis=analysisTxt;
//        String rH = rhTxt.getText();
//        LocalDate rDate = recoltationTxt;
//        Institution institution=institutionTxt;
//        Donor donor=donorTxt;
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        Blood blood = Blood.builder()
//                .donor(donor)
//                .recoltationDate(rDate)
//                .institution(institution)
//                .build();
//
//            alert.setContentText(blood.toString() + "\n" +
//                    "Example of sample of blood:" +
//                    bloodService.getBloodById(bloodId));
//
//        }




