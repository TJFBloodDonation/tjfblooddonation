package ro.ubb.tjfblooddonation;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.repository.InstitutionRepository;
import ro.ubb.tjfblooddonation.repository.PatientRepository;
import ro.ubb.tjfblooddonation.service.BloodService;
import ro.ubb.tjfblooddonation.service.RequestService;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class BloodDonation extends Application {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();
    static AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");

    @Override
    public void start(Stage primaryStage) throws Exception{
//        addRequest();
//        separateAllUnseparatedBlood();
        loader.createNewWindow("/fxml/login/Login.fxml", "Blood donation", null);
//        for(int i = 31 ; i <= 35 ; i++)
//            addDonor(i);
        //addHealthWorker(HealthWorker.types.DOCTOR, "doctor");
    }

    private void separateAllUnseparatedBlood(){
        BloodService bloodService = context.getBean(BloodService.class);
        bloodService.getUnseparatedBlood().forEach(b -> bloodService.separateBlood(b.getId()));
    }

    private void addRequest(){
        RequestService requestService = context.getBean(RequestService.class);
        UsersService usersService = context.getBean(UsersService.class);
        IdCard idCard = IdCard.builder()
                .cnp("4982389999997")
                .build();
        Patient patient = Patient.builder()
                .bloodType("B")
                .firstName("Andrei")
                .lastName("Sebi")
                .rH("+")
                .idCard(idCard)
                .build();
        PatientRepository patientRepository = context.getBean(PatientRepository.class);
        patientRepository.add(patient);
        Request request = Request.builder()
                .requestDate(LocalDate.now())
                .healthWorker((HealthWorker)usersService.getHealthWorkersAccounts().get(1).getPerson())
                .patient(patient)
                .plasmaUnits((byte) 1)
                .thrombocytesUnits((byte) 1)
                .redBloodCellsUnits((byte) 1)
                .status("pending")
                .urgency(Request.UrgencyLevel.HIGH)
                .build();
        requestService.addRequest(request);
    }

    private void addHealthWorker(HealthWorker.types type, String username) {
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

        usersService.createUserAccount(username, username, healthWorker);
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void addDonor(Integer x) {
        UsersService usersService = context.getBean(UsersService.class);
        usersService.getAllDonors().forEach(System.out::println);
        System.out.println("----------------------------------");
        Address a = new Address("a", "b", "c", "d");
        Address b = new Address("a", "b", "c", "d");
        IdCard idCard = new IdCard(a, "cnp");
        Donor d = Donor.builder()
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
        usersService.createUserAccount("donor" + x.toString(), "donor", d);

        usersService.getAllDonors().forEach(System.out::println);
    }
}
