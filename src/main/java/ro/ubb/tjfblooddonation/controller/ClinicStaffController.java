package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

@Controller
public class ClinicStaffController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    public void completeBasicCheckFormButtonClicked(ActionEvent actionEvent) {
        try{
            loader.createNewWindow("/fxml/clinicStaff/SearchDonors.fxml", "Pick a donor!", null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void manageBloodRequestsClicked(ActionEvent actionEvent) {
        try{
            loader.createNewWindow("/fxml/clinicStaff/Requests.fxml", "Pick a donor!", null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
