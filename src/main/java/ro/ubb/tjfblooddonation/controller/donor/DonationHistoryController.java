package ro.ubb.tjfblooddonation.controller.donor;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.Analysis;
import ro.ubb.tjfblooddonation.model.Blood;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

@Controller
public class DonationHistoryController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    @FXML
    Label nextDonationLabel;
    @FXML
    ListView<LocalDate> analysisList;
    private LoginInformation donorLogin = null;
    @Autowired
    private UsersService usersService;
    @Autowired
    private BloodService bloodService;

    public void setInfo(String username) {
        donorLogin = usersService.getLoginInformationByUsername(username);
        populate();
    }

    private void populate() {
        analysisList.setItems(FXCollections.observableArrayList());
        bloodService.getUserBlood(donorLogin.getUsername()).stream()
                .filter(blood -> blood.getAnalysis() != null)
                .forEach(blood -> analysisList.getItems().add(blood.getRecoltationDate()));

        analysisList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    FXMLLoader ld = loader.getLoader("/fxml/donor/Analysis.fxml");
                    Stage childStage = loader.createNewWindow((Parent) ld.load(), "Analysis result", null);
                    AnalysisController childController = ld.getController();
                    Optional<Blood> bloodOpt = bloodService.getUserBlood(donorLogin.getUsername()).stream()
                            .filter(blood -> blood.getRecoltationDate()
                                    .equals(analysisList.getSelectionModel().getSelectedItem()))
                            .findFirst();
                    if(bloodOpt.isPresent()) {
                        Analysis analysis = bloodOpt.get().getAnalysis();
                        childController.setInfo(analysis, bloodOpt.get().getRecoltationDate());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } //catch (Exception e) {
//                    Messages.showError(e.getMessage());
//                }
            }
        });

        LocalDate nextDonation = bloodService.getNextDonateTime(donorLogin.getUsername());
        nextDonationLabel.setText(nextDonation.getDayOfMonth() + " " +
                nextDonation.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " +
                nextDonation.getYear());
    }
}
