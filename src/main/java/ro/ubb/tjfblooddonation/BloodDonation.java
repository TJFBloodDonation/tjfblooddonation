package ro.ubb.tjfblooddonation;

import javafx.application.Application;
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


    public static void main(String[] args) { launch(args); }

    public static void askToDonate() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");
        BloodService bloodService = context.getBean(BloodService.class);
        //bloodService.askUsersToDonate();
    }

    public static void addBlood() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");
        BloodService bloodService = context.getBean(BloodService.class);
        UsersService usersService = context.getBean(UsersService.class);

        Form form1 = Form.builder().passedDonateForm(true).passedBasicCheckForm(true)
                .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        Form form2 = Form.builder().passedDonateForm(true).passedBasicCheckForm(true)
                .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Analysis analysis1 = Analysis.builder()
                .hiv(false).hb(false).hcv(false).htlv(false).sifilis(false).imunoHematology(false).alt(false)
                .build();
        Analysis analysis2 = Analysis.builder()
                .hiv(false).hb(false).hcv(false).htlv(false).sifilis(false).imunoHematology(true).alt(false)
                .build();


        Donor donor = usersService.getDonor("donor");

        Blood blood1 = Blood.builder()
                .donor(donor)
                .recoltationDate(LocalDate.now().minusMonths(2))
                .build();
        blood1.setAnalysis(analysis2);
        Blood blood2 = Blood.builder()
                .donor(donor)
                .recoltationDate(LocalDate.now().minusMonths(4))
                .build();
        blood2.setAnalysis(analysis1);

        donor.setForm(form1);

        usersService.updateUserAccount("donor", donor);
        bloodService.donateBlood("donor", blood1);
        donor.setForm(form2);
        usersService.updateUserAccount("donor", donor);
        bloodService.donateBlood("donor", blood2);
    }

    public static void addDonor(Integer x) {
        UsersService usersService = context.getBean(UsersService.class);
        usersService.getAllDonors().forEach(System.out::println);
        System.out.println("----------------------------------");
        Address a = new Address("Country1", "Region1", "City1", "Street1");
        Address b = new Address("Country1", "Region1", "City1", "Street1");
        IdCard idCard = new IdCard(a, "1234567890123");
        Donor d = Donor.builder()
                .bloodType("0")
                .dateOfBirth(LocalDate.parse("2000-12-12"))
                .gender("male")
                .idCard(idCard)
                .residence(b)
                .rH("+")
                .build();
        d.setEmail("donor@email.com");
        d.setPhoneNumber("+40712345678");
        d.setFirstName("Donor");
        d.setLastName("Donor");
        usersService.addDonor(d);
        usersService.createUserAccount("donor2", "donor", d);
        //usersService.getAllDonors().forEach(System.out::println);
        usersService.createUserAccount("donor" + x.toString(), "donor", d);

        usersService.getAllDonors().forEach(System.out::println);
    }
}
