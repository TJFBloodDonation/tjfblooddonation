package ro.ubb.tjfblooddonation;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.repository.InstitutionRepository;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class BloodDonation extends Application {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader.createNewWindow("/fxml/clinicStaff/SearchDonors.fxml", "Blood donation", null);
//        addDonor();
        //addHealthWorker(HealthWorker.types.DOCTOR, "doctor");
    }

    private void addHealthWorker(HealthWorker.types type, String username) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");
        UsersService usersService = context.getBean(UsersService.class);
        Address a = new Address("a", "b", "c", "d");
        Address b = new Address("a", "b", "c", "d");
        IdCard idCard = new IdCard(a, "cnp");
        Institution institution = Institution.builder()
                .address(a)
                .name("Spital")
                .type(Institution.types.CLINIC)
                .build();
        HealthWorker healthWorker = HealthWorker.builder()
                .email("email")
                .firstName("health ")
                .lastName("worker")
                .institution(institution)
                .phoneNumber("048327592")
                .type(type)
                .build();
        context.getBean(InstitutionRepository.class).add(institution);

        usersService.createHealthWorkerAccont(username, username, healthWorker);
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void addDonor() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");
        UsersService usersService = context.getBean(UsersService.class);
        usersService.getAllDonors().forEach(System.out::println);
        System.out.println("----------------------------------");
        Address a = new Address("a", "b", "c", "d");
        Address b = new Address("a", "b", "c", "d");
        IdCard idCard = new IdCard(a, "cnp");
        Donor d = Donor.builder()
                .bloodType("0")
                .dateOfBirth(LocalDate.parse("2000-12-12"))
                .gender("female")
                .idCard(idCard)
                .residence(b)

                .build();
        d.setEmail("fjks@fdjlk.com");
        d.setPhoneNumber("jfdk");
        d.setFirstName("abc");
        d.setLastName("def");
        d.setForm(Form.builder()
                .passedDonateForm(true)
                .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now()))
                .passedBasicCheckForm(false)
                .build());
        usersService.addDonor(d);
        usersService.createDonorAccount("donor", "donor", d);
        usersService.getAllDonors().forEach(System.out::println);
    }
}
