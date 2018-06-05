package ro.ubb.tjfblooddonation.controller.bloodAnalyst;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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



public class BloodDonationController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @Autowired
    BloodService bloodService;

    @FXML
    void bloodComboBox(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        }
    private void refresh(){
        List<LoginInformation> blood = (List<LoginInformation>) bloodService.getUnanalysedBlood()
                .stream()
                .filter(d -> ((Blood) bloodService.getUnanalysedBlood()) != null );
       //         .sorted(Comparator.comparing(d1 -> (Blood) bloodService.getUnseparatedBlood()));
                        //.collect((Collectors.toList())));
        ObservableList<TitledPane> panes = FXCollections.observableArrayList();
        blood.stream();


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




