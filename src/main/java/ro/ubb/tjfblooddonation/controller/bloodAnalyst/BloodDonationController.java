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
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @Autowired
    BloodService bloodService;

    @FXML
    public ComboBox bloodComboBox;


    private void refresh(){
        Set<Blood> blood = bloodService.getUnanalysedBlood();
        bloodComboBox.setItems(FXCollections.observableArrayList(blood));
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




