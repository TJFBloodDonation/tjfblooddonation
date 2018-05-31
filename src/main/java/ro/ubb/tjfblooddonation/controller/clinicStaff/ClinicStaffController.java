package ro.ubb.tjfblooddonation.controller.clinicStaff;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.HealthWorker;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

@Controller
public class ClinicStaffController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    private HealthWorker healthWorker;

    public void setHealthWorker(HealthWorker healthWorker) {
        this.healthWorker = healthWorker;
    }

    public void completeBasicCheckFormButtonClicked(ActionEvent actionEvent) {
        try{
            FXMLLoader ld = loader.getLoader("/fxml/clinicStaff/SearchDonors.fxml");
            loader.createNewWindow((Parent) ld.load(), "Pick a donor!", null);
            ld.<SearchDonors>getController().setHealthWorker(healthWorker);
//            loader.createNewWindow("/fxml/clinicStaff/SearchDonors.fxml", "Pick a donor!", null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void manageBloodRequestsClicked(ActionEvent actionEvent) {
        try{
            loader.createNewWindow("/fxml/clinicStaff/Requests.fxml", "Fulfill requests!", null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
