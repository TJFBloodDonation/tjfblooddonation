package ro.ubb.tjfblooddonation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.exceptions.BaseException;
import ro.ubb.tjfblooddonation.exceptions.LogInException;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.model.HealthWorker;
import ro.ubb.tjfblooddonation.model.Person;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.io.IOException;

@Controller
public class LogInController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Autowired
    UsersService usersService;

    @FXML
    private TextField usernameTextBox;

    @FXML
    private PasswordField passwordTextBox;

    @FXML
    private Button logInButton;

    @FXML
    public void logInButtonPressed(ActionEvent actionEvent) {
        String username = usernameTextBox.getText();
        String password = passwordTextBox.getText();
        try {
            Person p = usersService.getPerson(username, password);
            if(p instanceof Donor) {
                FXMLLoader ld = loader.getLoader("/fxml/donor/Donor.fxml");
                Stage childStage = loader.createNewWindow((Parent) ld.load(), "Donor Main Page", actionEvent);
                ld.<DonorController>getController().setLoginInfo(username);
            }
            if(p instanceof HealthWorker) {
                HealthWorker healthWorker = (HealthWorker) p;

                switch (healthWorker.getType()){
                    case "admin":
                        loader.createNewWindow("/fxml/admin/Admin.fxml", "Admin Main Page", actionEvent);
                        break;
                    case "clinicStaff":
                        loader.createNewWindow("/fxml/ClinicStuff.fxml", "Clinic Stuff Main Page", actionEvent);
                        break;
                    case "doctor":
                        loader.createNewWindow("/fxml/doctor/Doctor.fxml", "Doctor Main Page", actionEvent);
                        break;
                    case "bloodAnalyst":
                        throw new LogInException("Not yet implemented!");
                        //loader.createNewWindow("/fxml/.fxml", "Blood Analyst Main Page", actionEvent);
//                        break;
                    default:
                        throw new LogInException("The health worker does not match any of the known types!");
                }
            }
        }catch (BaseException e) {
            Messages.showError("Login error!", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Messages.showError("Fatal error!", e.toString());
        }

    }

    public void registerButtonClicked(ActionEvent actionEvent) {
        try {
            loader.createNewWindow("/fxml/login/Register.fxml", "Register Page", null);
        }
        catch (IOException e) {
            e.printStackTrace();
            Messages.showError("Fatal error!", e.toString());
        }
    }
}
