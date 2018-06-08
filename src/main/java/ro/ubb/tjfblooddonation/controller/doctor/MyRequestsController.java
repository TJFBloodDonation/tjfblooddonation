package ro.ubb.tjfblooddonation.controller.doctor;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.model.Request;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.service.RequestService;
import ro.ubb.tjfblooddonation.utils.Messages;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.util.stream.Collectors;

@Controller
public class MyRequestsController {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();


    @Autowired
    RequestService requestService;

    @Autowired
    BloodService bloodService;

    @FXML
    private ListView<Request> requestsListView;

    @FXML
    private TextField searchRequestsTextField;

    private LoginInformation loginInformation;

    public void setLoginInformation(LoginInformation loginInformation) {

        this.loginInformation = loginInformation;

        try{
            refreshRequestsList();
            searchRequestsTextField.textProperty().addListener((observable, oldValue, newValue) -> refreshRequestsList());
            requestsListView.setCellFactory(c -> new ListCell<Request>(){
                @Override
                protected void updateItem(Request request, boolean empty){
                    super.updateItem(request, empty);
                    if(empty || request == null) {
                        setText("");
                        setGraphic(null);
                    }
                    else{
                        setText(makeString(request));
                        Color c = null;
                        switch (request.getUrgency()){
                            case LOW: c = Color.GREEN; break;
                            case MEDIUM: c = Color.YELLOW; break;
                            case HIGH: c = Color.RED; break;
                        }
                        Circle circle = new Circle(1.0, 1.0, 5.0);
                        circle.setFill(c);
                        setGraphic(circle);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            Messages.showError(e.toString());
        }
    }

    private String makeString(Request request){
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(request.getPatient().getFirstName());
        stringBuffer.append(" ");
        stringBuffer.append(request.getPatient().getLastName());
        if(request.getPatient().getIdCard() != null) {
            stringBuffer.append("    ");
            stringBuffer.append(request.getPatient().getIdCard().getCnp());
        }
        stringBuffer.append(" ");
        stringBuffer.append(request.getPatient().getBloodType());
        stringBuffer.append(" ");
        stringBuffer.append(request.getPatient().getRH());
//        stringBuffer.append("\n(");
//        stringBuffer.append(bloodService.getOkThrombocytes(request.getId()).size());
//        stringBuffer.append("/");
//        stringBuffer.append(request.getThrombocytesUnits());
//        stringBuffer.append(", ");
//        stringBuffer.append(bloodService.getOkRedBloodCells(request.getId()).size());
//        stringBuffer.append("/");
//        stringBuffer.append(request.getRedBloodCellsUnits());
//        stringBuffer.append(", ");
//        stringBuffer.append(bloodService.getOkPlasma(request.getId()).size());
//        stringBuffer.append("/");
//        stringBuffer.append(request.getPlasmaUnits());
//        stringBuffer.append(")");
//        stringBuffer.append("  No. People who donated for this patient: ");
//        stringBuffer.append(bloodService.getNoOfPeopleWhoDonatedForPatient(request.getPatient()));
        stringBuffer.append("\nIs satisfied: ");
        stringBuffer.append(request.getIsSatisfied());

        return stringBuffer.toString();
    }

    private void refreshRequestsList(){
        requestsListView.getItems().setAll(
                requestService
                        .getDoctorRequest(loginInformation.getUsername())
                        .stream()
                        .filter(r -> makeString(r).contains(searchRequestsTextField.getText()))
                        .collect(Collectors.toList())
        );
    }

    public void initialize(){


    }
//    @FXML
//    void ViewStatusClicked(ActionEvent event) {
//        try {
//            loader.createNewWindow("/fxml/doctor/MyRequests.fxml", "Requests Page", null);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
