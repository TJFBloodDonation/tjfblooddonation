package ro.ubb.tjfblooddonation.controller.donor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

@Controller
public class BeforeDonatingController {

    @FXML
    Button okButton;


    @FXML
    void okClicked() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
