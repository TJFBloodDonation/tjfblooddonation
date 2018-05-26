package ro.ubb.tjfblooddonation.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.ubb.tjfblooddonation.config.AppLocalConfig;
import ro.ubb.tjfblooddonation.config.CacheConfig;
import ro.ubb.tjfblooddonation.config.CatalogConfig;
import ro.ubb.tjfblooddonation.config.JPAConfig;
import ro.ubb.tjfblooddonation.exceptions.LogInException;
import ro.ubb.tjfblooddonation.exceptions.ServiceError;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.repository.DonorRepository;
import ro.ubb.tjfblooddonation.repository.HealthWorkerRepository;
import ro.ubb.tjfblooddonation.repository.InstitutionRepository;
import ro.ubb.tjfblooddonation.repository.LoginInformationRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CatalogConfig.class, AppLocalConfig.class,
        JPAConfig.class, CacheConfig.class})
public class UsersServiceTest {
    @Autowired
    UsersService usersService;
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    HealthWorkerRepository healthWorkerRepository;
    @Autowired
    LoginInformationRepository loginInformationRepository;
    @Autowired
    InstitutionRepository institutionRepository;

    @Test
    public void getAllDonors() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();
        Address res2 = Address.builder().country("CTRY2").region("R2").city("CITY2").street("S2 NO.2").build();
        Address res3 = Address.builder().country("CTRY3").region("R3").city("CITY3").street("S3 NO.3").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();
        IdCard idCard2 = IdCard.builder().cnp("1234567890001").address(res2).build();
        IdCard idCard3 = IdCard.builder().cnp("1234567890002").address(res3).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        Donor donor2 = Donor.builder().firstName("FND2").lastName("LND2")
                .email("d2@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("male")
                .bloodType("A").rH("+")
                .idCard(idCard2).residence(res2)
                .build();
        Donor donor3 = Donor.builder().firstName("FND3").lastName("LND3")
                .email("d3@email.com").phoneNumber("+40712345679")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("B").rH("+")
                .idCard(idCard3).residence(res3)
                .build();


        donorRepository.add(donor1);
        donorRepository.add(donor2);


        assert usersService.getAllDonors().containsAll(Arrays.asList(donor1, donor2));

        donorRepository.add(donor3);

        assert usersService.getAllDonors().containsAll(Arrays.asList(donor1, donor2, donor3));


        donorRepository.remove(donor1.getId());
        donorRepository.remove(donor2.getId());
        donorRepository.remove(donor3.getId());
    }

    @Test
    public void addDonor() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();
        Address res2 = Address.builder().country("CTRY2").region("R2").city("CITY2").street("S2 NO.2").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();
        IdCard idCard2 = IdCard.builder().cnp("1234567890001").address(res2).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        Donor donor2 = Donor.builder().firstName("FND2").lastName("LND2")
                .email("d2@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("male")
                .bloodType("A").rH("+")
                .idCard(idCard2).residence(res2)
                .build();

        usersService.addDonor(donor1);
        assert usersService.getAllDonors().contains(donor1);

        usersService.addDonor(donor2);
        assert usersService.getAllDonors().containsAll(Arrays.asList(donor1, donor2));


        donorRepository.remove(donor1.getId());
        donorRepository.remove(donor2.getId());
    }

    @Test
    public void createUserAccount() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();

        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();


        usersService.createUserAccount("don1", "p1", donor1);
        assert donorRepository.getAll().contains(donor1);
        assert donorRepository.getById(donor1.getId()).getFirstName().equals("FND1");
        assertNotNull(loginInformationRepository.getById("don1"));

        usersService.createUserAccount("hw1", "p2", healthWorker1);
        assert healthWorkerRepository.getAll().contains(healthWorker1);
        assert healthWorkerRepository.getById(healthWorker1.getId()).getFirstName().equals("FNHW1");
        assertNotNull(loginInformationRepository.getById("hw1"));

        try {
            usersService.createUserAccount("don1", "pass", healthWorker1);
            assert false;
        }
        catch (ServiceError ex) {
            assert ex.getMessage().contains("is already taken");
        }


        loginInformationRepository.remove("don1");
        loginInformationRepository.remove("hw1");

        healthWorkerRepository.remove(healthWorker1.getId());
        donorRepository.remove(donor1.getId());
    }

    @Test
    public void updateUserAccount() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        donorRepository.add(donor1);

        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        healthWorkerRepository.add(healthWorker1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("don1").password("p1")
                .person(donor1)
                .build();
        LoginInformation loginInformation2 = LoginInformation.builder()
                .username("hw1").password("p2")
                .person(healthWorker1)
                .build();
        loginInformationRepository.add(loginInformation1);
        loginInformationRepository.add(loginInformation2);


        assert loginInformationRepository.getById(loginInformation1.getUsername())
                .getPerson().getFirstName().equals("FND1");

        donor1.setFirstName("UPDATED");
        usersService.updateUserAccount(loginInformation1.getUsername(), donor1);

        assert loginInformationRepository.getById(loginInformation1.getUsername())
                .getPerson().getFirstName().equals("UPDATED");
        assert donorRepository.getById(donor1.getId()).getFirstName().equals("UPDATED");

        assert loginInformationRepository.getById(loginInformation2.getUsername())
                .getPerson().getFirstName().equals("FNHW1");

        healthWorker1.setFirstName("UPDATED");
        usersService.updateUserAccount(loginInformation2.getUsername(), healthWorker1);

        assert loginInformationRepository.getById(loginInformation2.getUsername())
                .getPerson().getFirstName().equals("UPDATED");
        assert healthWorkerRepository.getById(healthWorker1.getId()).getFirstName().equals("UPDATED");


        loginInformationRepository.remove(loginInformation1.getId());
        loginInformationRepository.remove(loginInformation2.getId());

        healthWorkerRepository.remove(healthWorker1.getId());

        donorRepository.remove(donor1.getId());
    }

    @Test
    public void deleteUserAccount() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        donorRepository.add(donor1);
        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        healthWorkerRepository.add(healthWorker1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("don1").password("p1")
                .person(donor1)
                .build();
        LoginInformation loginInformation2 = LoginInformation.builder()
                .username("hw1").password("p2")
                .person(healthWorker1)
                .build();
        loginInformationRepository.add(loginInformation1);
        loginInformationRepository.add(loginInformation2);


        assert loginInformationRepository.getAll().contains(loginInformation1);
        assert donorRepository.getAll().contains(donor1);
        assert loginInformationRepository.getAll().contains(loginInformation2);
        assert healthWorkerRepository.getAll().contains(healthWorker1);

        usersService.deleteUserAccount(loginInformation1.getUsername());
        usersService.deleteUserAccount(loginInformation2.getUsername());

        assert !loginInformationRepository.getAll().contains(loginInformation1);
        assert !donorRepository.getAll().contains(donor1);
        assert !loginInformationRepository.getAll().contains(loginInformation2);
        assert !healthWorkerRepository.getAll().contains(healthWorker1);
    }

    @Test
    public void getDonor() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        donorRepository.add(donor1);

        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        healthWorkerRepository.add(healthWorker1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("don1").password("p1")
                .person(donor1)
                .build();
        LoginInformation loginInformation2 = LoginInformation.builder()
                .username("hw1").password("p2")
                .person(healthWorker1)
                .build();
        loginInformationRepository.add(loginInformation1);
        loginInformationRepository.add(loginInformation2);


        assert usersService.getDonor(loginInformation1.getUsername()).getId().equals(donor1.getId());

        try{
            usersService.getDonor(loginInformation2.getUsername());
            assert false;
        }
        catch (ServiceError ex) {
            assert true;
        }


        loginInformationRepository.remove(loginInformation1.getId());
        loginInformationRepository.remove(loginInformation2.getId());

        healthWorkerRepository.remove(healthWorker1.getId());

        donorRepository.remove(donor1.getId());
    }

    @Test
    public void completeForm() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();

        Form form1 = new Form();
        Form form2 = Form.builder()
                .passedDonateForm(true).passedBasicCheckForm(false)
                .timeCompletedDonateForm(Timestamp.from(Instant.now()))
                .build();

        donor1.setForm(form1);
        donorRepository.add(donor1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("don1").password("p1")
                .person(donor1)
                .build();
        loginInformationRepository.add(loginInformation1);


        assert ((Donor)loginInformationRepository.getById(loginInformation1.getUsername()).getPerson())
                .getForm().equals(form1);
        usersService.completeForm(loginInformation1.getUsername(), form2);
        assert ((Donor)loginInformationRepository.getById(loginInformation1.getUsername()).getPerson())
                .getForm().getTimeCompletedDonateForm().equals(form2.getTimeCompletedDonateForm());
        try {
            usersService.completeForm("noUsernameLikeThisExistsHopefullyInTheDatabase6245", form2);
            assert false;
        }
        catch (ServiceError ex){
            assert ex.getMessage().contains("Invalid donor username.");
        }

        loginInformationRepository.remove(loginInformation1.getId());
        donorRepository.remove(donor1.getId());
    }

    @Test
    public void updateDonorInfo() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1995-03-14")).gender("male")
                .bloodType("nope").rH("nope")
                .idCard(idCard1).residence(res1)
                .build();
        donorRepository.add(donor1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("don1").password("p1")
                .person(donor1)
                .build();
        loginInformationRepository.add(loginInformation1);


        assert ((Donor)loginInformationRepository.getById(loginInformation1.getUsername())
                .getPerson()).getRH().equals("nope");
        assert ((Donor)loginInformationRepository.getById(loginInformation1.getUsername())
                .getPerson())
                .getBloodType().equals("nope");

        usersService.updateDonorInfo(loginInformation1.getUsername(), "B", "-");

        assert ((Donor)loginInformationRepository.getById(loginInformation1.getUsername())
                .getPerson()).getRH().equals("-");
        assert ((Donor)loginInformationRepository.getById(loginInformation1.getUsername())
                .getPerson()).getBloodType().equals("B");

        try {
            usersService.updateDonorInfo("noUsernameLikeThisExistsHopefullyInTheDatabase6245",
                    "AB", "+");
            assert false;
        }
        catch (ServiceError ex) {
            assert ex.getMessage().contains("Invalid donor username.");
        }


        loginInformationRepository.remove(loginInformation1.getId());
        donorRepository.remove(donor1.getId());
    }

    @Test
    public void checkIfCanDonate() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1995-03-14")).gender("male")
                .bloodType("nope").rH("nope")
                .idCard(idCard1).residence(res1)
                .build();
        Form form1 = Form.builder()
                .passedDonateForm(false).passedBasicCheckForm(false)
                .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now().minusWeeks(2)))
                .build();
        donor1.setForm(form1);
        donorRepository.add(donor1);

        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        healthWorkerRepository.add(healthWorker1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("don1").password("p1")
                .person(donor1)
                .build();
        LoginInformation loginInformation2 = LoginInformation.builder()
                .username("hw1").password("p1")
                .person(healthWorker1)
                .build();
        loginInformationRepository.add(loginInformation1);
        loginInformationRepository.add(loginInformation2);


        try {
            usersService.checkIfCanDonate(loginInformation1.getUsername());
            assert false;
        }
        catch (ServiceError ex) {
            assert ex.getMessage().contains("did not pass the basic check form");
            assert ex.getMessage().contains("did not pass the check form they completed prior to donation");
            assert ex.getMessage().contains("check form was completed more than a week prior to donation");
        }

        try {
            usersService.checkIfCanDonate("noUsernameLikeThisExistsHopefullyInTheDatabase6245");
            assert false;
        }
        catch (ServiceError ex) {
            assert ex.getMessage().contains("Invalid donor username.");
        }

        donor1.getForm().setTimeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now().minusDays(3)));
        donor1.getForm().setPassedBasicCheckForm(true);
        donor1.getForm().setPassedDonateForm(true);
        donorRepository.update(donor1);
        loginInformation1.setPerson(donor1);
        loginInformationRepository.update(loginInformation1);

        assert usersService.checkIfCanDonate(loginInformation1.getUsername());
        assert !usersService.checkIfCanDonate(loginInformation2.getUsername());


        loginInformationRepository.remove(loginInformation1.getId());
        loginInformationRepository.remove(loginInformation2.getId());

        healthWorkerRepository.remove(healthWorker1.getId());
        donorRepository.remove(donor1.getId());
    }

    @Test
    public void getDonorsReadyForDonating() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();
        Address res2 = Address.builder().country("CTRY2").region("R2").city("CITY2").street("S2 NO.2").build();
        Address res3 = Address.builder().country("CTRY3").region("R3").city("CITY3").street("S3 NO.3").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();
        IdCard idCard2 = IdCard.builder().cnp("1234567890001").address(res2).build();
        IdCard idCard3 = IdCard.builder().cnp("1234567890002").address(res3).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        Donor donor2 = Donor.builder().firstName("FND2").lastName("LND2")
                .email("d2@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("male")
                .bloodType("A").rH("+")
                .idCard(idCard2).residence(res2)
                .build();
        Donor donor3 = Donor.builder().firstName("FND3").lastName("LND3")
                .email("d3@email.com").phoneNumber("+40712345679")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("B").rH("+")
                .idCard(idCard3).residence(res3)
                .build();
        Form form1 = Form.builder()
                .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now()))
                .passedBasicCheckForm(true).passedDonateForm(true)
                .build();
        Form form2 = Form.builder()
                .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now()))
                .passedBasicCheckForm(false).passedDonateForm(true)
                .build();
        Form form3 = Form.builder()
                .timeCompletedDonateForm(Timestamp.valueOf(LocalDateTime.now().minusDays(8)))
                .passedBasicCheckForm(true).passedDonateForm(true)
                .build();

        donor1.setForm(form1);
        donor2.setForm(form2);
        donor3.setForm(form3);

        donorRepository.add(donor1);
        donorRepository.add(donor2);
        donorRepository.add(donor3);

        System.out.println(donor1);
        System.out.println(donor2);
        System.out.println(donor3);


        assert usersService.getDonorsReadyForDonating().contains(donor1);
        assert !usersService.getDonorsReadyForDonating().contains(donor2);
        assert !usersService.getDonorsReadyForDonating().contains(donor3);


        donorRepository.remove(donor1.getId());
        donorRepository.remove(donor2.getId());
        donorRepository.remove(donor3.getId());
    }

    @Test
    public void getPerson() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        donorRepository.add(donor1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("don1").password("p1")
                .person(donor1)
                .build();
        loginInformationRepository.add(loginInformation1);


        try {
            usersService.getPerson(loginInformation1.getUsername(), "notCorrectPassword");
            assert false;
        }
        catch (LogInException ex) {
            assert ex.getMessage().contains("Wrong password");
        }

        assert usersService.getPerson(loginInformation1.getUsername(), "p1").equals(donor1);


        loginInformationRepository.remove(loginInformation1.getId());
        donorRepository.remove(donor1.getId());
    }

    @Test
    public void getAllHealthWorkers() {
        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        HealthWorker healthWorker2 = HealthWorker.builder()
                .firstName("FNHW2").lastName("LNHW2")
                .email("hw2@email.com").phoneNumber("+040712345679")
                .type(HealthWorker.types.DOCTOR)
                .build();
        HealthWorker healthWorker3 = HealthWorker.builder()
                .firstName("FNHW3").lastName("LNHW3")
                .email("hw3@email.com").phoneNumber("+040712345680")
                .type(HealthWorker.types.CLINIC_STAFF)
                .build();

        healthWorkerRepository.add(healthWorker1);
        healthWorkerRepository.add(healthWorker2);


        assert usersService.getAllHealthWorkers().containsAll(Arrays.asList(healthWorker1, healthWorker2));
        assert !usersService.getAllHealthWorkers().contains(healthWorker3);

        healthWorkerRepository.add(healthWorker3);

        assert usersService.getAllHealthWorkers().containsAll(Arrays.asList(healthWorker1, healthWorker2, healthWorker3));


        healthWorkerRepository.remove(healthWorker1.getId());
        healthWorkerRepository.remove(healthWorker2.getId());
        healthWorkerRepository.remove(healthWorker3.getId());
    }

    @Test
    public void getHealthWorker() {
        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        healthWorkerRepository.add(healthWorker1);


        assert usersService.getHealthWorker(healthWorker1.getId()).getFirstName().equals("FNHW1");


        healthWorkerRepository.remove(healthWorker1.getId());
    }

    @Test
    public void getHealthWorkersAccounts() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        donorRepository.add(donor1);

        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        healthWorkerRepository.add(healthWorker1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("don1").password("p1")
                .person(donor1)
                .build();
        LoginInformation loginInformation2 = LoginInformation.builder()
                .username("hw1").password("p2")
                .person(healthWorker1)
                .build();
        loginInformationRepository.add(loginInformation1);
        loginInformationRepository.add(loginInformation2);


        assert !usersService.getHealthWorkersAccounts().contains(loginInformation1);
        assert usersService.getHealthWorkersAccounts().contains(loginInformation2);


        loginInformationRepository.remove(loginInformation1.getId());
        loginInformationRepository.remove(loginInformation2.getId());

        healthWorkerRepository.remove(healthWorker1.getId());

        donorRepository.remove(donor1.getId());
    }

    @Test
    public void getLoginInformationByUsername() {
        HealthWorker healthWorker1 = HealthWorker.builder()
                .firstName("FNHW1").lastName("LNHW1")
                .email("hw1@email.com").phoneNumber("+040712345678")
                .type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        healthWorkerRepository.add(healthWorker1);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .username("hw1").password("p1")
                .person(healthWorker1)
                .build();
        loginInformationRepository.add(loginInformation1);


        assert usersService.getLoginInformationByUsername("hw1").equals(loginInformation1);


        loginInformationRepository.remove(loginInformation1.getId());
        healthWorkerRepository.remove(healthWorker1.getId());
    }

    @Test
    public void getAllInstitutions() {
        Address adr1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();
        Address adr2 = Address.builder().country("CTRY2").region("R2").city("CITY2").street("S2 NO.2").build();

        Institution institution1 = Institution.builder()
                .address(adr1).name("I1")
                .type(Institution.types.CLINIC)
                .build();
        Institution institution2 = Institution.builder()
                .address(adr2).name("I2")
                .type(Institution.types.HOSPITAL)
                .build();

        institutionRepository.add(institution1);
        institutionRepository.add(institution2);


        assert usersService.getAllInstitutions().containsAll(Arrays.asList(institution1, institution2));


        institutionRepository.remove(institution1.getId());
        institutionRepository.remove(institution2.getId());
    }
}