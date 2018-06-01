package ro.ubb.tjfblooddonation.controller.donor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.ubb.tjfblooddonation.model.Address;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.model.IdCard;
import ro.ubb.tjfblooddonation.model.LoginInformation;
import ro.ubb.tjfblooddonation.service.UsersService;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;


@Controller
public class DonorSettingsController {

    @Autowired
    UsersService usersService;
    @FXML
    TextField firstNameTextBox;
    @FXML
    TextField lastNameTextBox;
    @FXML
    TextField emailTextBox;
    @FXML
    TextField phoneTextBox;
    @FXML
    TextField residenceCountryTextBox;
    @FXML
    TextField residenceRegionTextBox;
    @FXML
    TextField residenceCityTextBox;
    @FXML
    TextField residenceStreetTextBox;
    @FXML
    TextField addressCountryTextBox;
    @FXML
    TextField addressRegionTextBox;
    @FXML
    TextField addressCityTextBox;
    @FXML
    TextField addressStreetTextBox;
    @FXML
    TextField CnpTextBox;
    @FXML
    ChoiceBox<Integer> yearDropdown;
    @FXML
    ChoiceBox<String> monthDropdown;
    @FXML
    ChoiceBox<Integer> dayDropdown;
    @FXML
    Button cancelButton;
    @FXML
    Button saveButton;
    private LoginInformation donorLogin;

    private Map<String, Integer> monthMap = new HashMap<>();

    private Consumer saveClickedCallback ;


    public void setInfo(String username) {
        donorLogin = usersService.getLoginInformationByUsername(username);
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 0; i < 150; i++) {
            years.add(LocalDate.now().getYear() - i);
        }
        yearDropdown.setItems(years);

        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int i = 1; i <= 31; i++) {
            days.add(i);
        }
        dayDropdown.setItems(days);

        ObservableList<String> months = FXCollections.observableArrayList();
        for (int i = 0; i < 12; i++) {
            months.add(LocalDate.now().withMonth(i + 1).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            monthMap.put(LocalDate.now().withMonth(i + 1).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), i + 1);
        }
        monthDropdown.setItems(months);

        populate();

    }

    private void populate() {
        Donor donor = usersService.getDonor(donorLogin.getUsername());

        firstNameTextBox.setText(donor.getFirstName());
        lastNameTextBox.setText(donor.getLastName());
        emailTextBox.setText(donor.getEmail());
        phoneTextBox.setText(donor.getPhoneNumber());

        yearDropdown.getSelectionModel().select(LocalDate.now().getYear() - donor.getDateOfBirth().getYear());
        monthDropdown.getSelectionModel().select(donor.getDateOfBirth().getMonth()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        dayDropdown.getSelectionModel().select(donor.getDateOfBirth().getDayOfMonth() - 1);

        residenceCountryTextBox.setText(donor.getResidence().getCountry());
        residenceRegionTextBox.setText(donor.getResidence().getRegion());
        residenceCityTextBox.setText(donor.getResidence().getCity());
        residenceStreetTextBox.setText(donor.getResidence().getStreet());

        addressCountryTextBox.setText(donor.getIdCard().getAddress().getCountry());
        addressRegionTextBox.setText(donor.getIdCard().getAddress().getRegion());
        addressCityTextBox.setText(donor.getIdCard().getAddress().getCity());
        addressStreetTextBox.setText(donor.getIdCard().getAddress().getStreet());

        CnpTextBox.setText(donor.getIdCard().getCnp());
    }

    public void setSaveClickedCallback(Consumer callback) {
        this.saveClickedCallback = callback ;
    }

    @FXML
    void cancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClicked() {
        IdCard newIdCard = IdCard.builder()
                .address(Address.builder()
                        .country(addressCountryTextBox.getText()).region(addressRegionTextBox.getText())
                        .city(addressCityTextBox.getText()).street(addressStreetTextBox.getText()).build())
                .cnp(CnpTextBox.getText()).build();
        Address newResidence = Address.builder()
                .country(residenceCountryTextBox.getText()).region(addressRegionTextBox.getText())
                .city(addressCityTextBox.getText()).street(addressStreetTextBox.getText()).build();

        LocalDate newDateOfBirth = LocalDate.of(yearDropdown.getSelectionModel().getSelectedItem()
                , monthMap.get(monthDropdown.getSelectionModel().getSelectedItem())
                , dayDropdown.getSelectionModel().getSelectedItem());

        Donor donor = (Donor) donorLogin.getPerson();
        donor.setFirstName(firstNameTextBox.getText());
        donor.setLastName(lastNameTextBox.getText());
        donor.setEmail(emailTextBox.getText());
        donor.setPhoneNumber(phoneTextBox.getText());
        donor.setDateOfBirth(newDateOfBirth);
        donor.setResidence(newResidence);
        donor.setIdCard(newIdCard);

        usersService.updateUserAccount(donorLogin.getUsername(), donor);

        saveClickedCallback.accept(null);

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

}
