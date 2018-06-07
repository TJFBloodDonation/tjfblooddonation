package ro.ubb.tjfblooddonation.controller.donor;


import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

@Controller
public class DonorController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    UsersService usersService;

    public void requestComboBox(ActionEvent actionEvent) {
    }

    public void checkBloodAvailability(ActionEvent actionEvent) {
    }

    public void bloodComboBox(ActionEvent actionEvent) {
    }
}
