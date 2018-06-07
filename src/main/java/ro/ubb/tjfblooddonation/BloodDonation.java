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
//        addHealthWorker(HealthWorker.types.ADMIN, "admin");
    }

    private void separateAllUnseparatedBlood(){
        BloodService bloodService = context.getBean(BloodService.class);
        bloodService.getUnseparatedBlood().forEach(b -> bloodService.separateBlood(b.getId()));
    }

    private void addRequest(){
        RequestService requestService = context.getBean(RequestService.class);
        UsersService usersService = context.getBean(UsersService.class);
        IdCard idCard = IdCard.builder()
                .cnp("1982329489252")
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
                .plasmaUnits((byte) 5)
                .thrombocytesUnits((byte) 5)
                .redBloodCellsUnits((byte) 5)
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

    public static void addPatients() {

        UsersService usersService = context.getBean(UsersService.class);

        Address addr1 = new Address("Country1", "Region1", "City1", "Street1");
        Address addr2 = new Address("Country1", "Region1", "City1", "Street2");
        Address addr3 = new Address("Country1", "Region1", "City1", "Street3");

        IdCard idCard1 = new IdCard(addr1, "1234567890123");
        IdCard idCard2 = new IdCard(addr2, "1234567890124");

        Institution institution = Institution.builder()
                .type(Institution.types.HOSPITAL).address(addr3).name("Hospital1")
                .build();
        usersService.addInstitution(institution);

        Patient patient1 = Patient.builder()
                .firstName("FNPatient1").lastName("LNPatient1")
                .email("pat1@email.com").idCard(idCard1).institution(institution)
                .bloodType("A").rH("+")
                .build();
        Patient patient2 = Patient.builder()
                .firstName("FNPatient2").lastName("LNPatient2")
                .email("pat2@email.com").idCard(idCard2).institution(institution)
                .bloodType("AB").rH("-")
                .build();

        usersService.addPatient(patient1);
        usersService.addPatient(patient2);
    }

    public static void askToDonate() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");
        BloodService bloodService = context.getBean(BloodService.class);
        InstitutionRepository institutionRepository = context.getBean(InstitutionRepository.class);
        bloodService.askUsersToDonate(institutionRepository.getAll().get(0));
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

//        Analysis analysis1 = Analysis.builder()
//                .hiv(false).hb(false).hcv(false).htlv(false).sifilis(false).imunoHematology(false).alt(false)
//                .build();
//        Analysis analysis2 = Analysis.builder()
//                .hiv(false).hb(false).hcv(false).htlv(false).sifilis(false).imunoHematology(true).alt(false)
//                .build();


        Donor donor = usersService.getDonor("donor");

        Blood blood1 = Blood.builder()
                .donor(donor)
                .recoltationDate(LocalDate.now().minusMonths(2))
                .build();
//        blood1.setAnalysis(analysis2);
        Blood blood2 = Blood.builder()
                .donor(donor)
                .recoltationDate(LocalDate.now().minusMonths(4))
                .build();
//        blood2.setAnalysis(analysis1);

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
                .gender("female")
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
