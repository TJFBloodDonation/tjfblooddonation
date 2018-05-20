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
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.repository.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CatalogConfig.class, AppLocalConfig.class,
        JPAConfig.class, CacheConfig.class})
public class RequestServiceTest {
    @Autowired
    RequestService requestService;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    InstitutionRepository institutionRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    HealthWorkerRepository healthWorkerRepository;
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    LoginInformationRepository loginInformationRepository;
    @Autowired
    BloodRepository bloodRepository;
    @Autowired
    BloodComponentRepository bloodComponentRepository;

    @Test
    public void addRequest() {

        Address adr = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1")
                .build();
        Address res = Address.builder().country("CTRY2").region("R2").city("CITY2").street("S2 NO.2")
                .build();

        IdCard idCard = IdCard.builder().cnp("1234567890000").address(res)
                .build();

        Patient patient = Patient.builder()
                .firstName("FNP1").lastName("LNP1").idCard(idCard)
                .email("p1@email.com").phoneNumber("+40712345678")
                .bloodType("AB").rH("-")
                .build();
        patientRepository.add(patient);

        Institution institution = Institution.builder()
                .name("INST1").type(Institution.types.HOSPITAL).address(adr)
                .build();
        institutionRepository.add(institution);

        HealthWorker healthWorker = HealthWorker.builder()
                .firstName("HWFN1").lastName("HWLN1")
                .email("hw1@email.com").phoneNumber("+40712345678")
                .institution(institution).type(HealthWorker.types.BLOOD_ANALYST)
                .build();
        healthWorkerRepository.add(healthWorker);

        Request request = Request.builder()
                .healthWorker(healthWorker).patient(patient)
                .thrombocytesUnits((byte) 1).plasmaUnits((byte) 1).redBloodCellsUnits((byte) 1)
                .urgency(Request.urgencyLevels.MEDIUM)
                .build();

        try {
            requestService.addRequest(request);
            assert false;
        }
        catch (RuntimeException ex)
        {
            assert true;
        }

        healthWorker.setType("doctor");
        healthWorkerRepository.update(healthWorker);
        request.setHealthWorker(healthWorker);
        requestRepository.update(request);

        requestService.addRequest(request);

        assert requestRepository.getById(request.getId()).equals(request);

        requestRepository.remove(request.getId());
        healthWorkerRepository.remove(healthWorker.getId());
        institutionRepository.remove(institution.getId());
        patientRepository.remove(patient.getId());
    }

    @Test
    public void getUnsatisfiedRequests() {

        Request request1 = Request.builder()
                .urgency(Request.urgencyLevels.HIGH)
                .build();
        request1.setIsSatisfied(true);
        Request request2 = Request.builder()
                .urgency(Request.urgencyLevels.MEDIUM)
                .build();
        Request request3 = Request.builder()
                .urgency(Request.urgencyLevels.LOW)
                .build();
        Request request4 = Request.builder()
                .urgency(Request.urgencyLevels.HIGH)
                .build();
        Request request5 = Request.builder()
                .urgency(Request.urgencyLevels.HIGH)
                .build();
        request5.setIsSatisfied(true);

        requestRepository.add(request1);
        requestRepository.add(request2);
        requestRepository.add(request3);
        requestRepository.add(request4);
        requestRepository.add(request5);

        assert requestService.getUnsatisfiedRequests().containsAll(Arrays.asList(request2, request3, request4));
        assert !requestService.getUnsatisfiedRequests().containsAll(Arrays.asList(request1, request5));

        assert requestService.getUnsatisfiedRequests().get(0).getId().equals(request4.getId());
        assert requestService.getUnsatisfiedRequests().get(1).getId().equals(request2.getId());
        assert requestService.getUnsatisfiedRequests().get(2).getId().equals(request3.getId());

        request5.setIsSatisfied(false);
        requestRepository.update(request5);

        assert requestService.getUnsatisfiedRequests().containsAll(Arrays.asList(request2, request3, request4, request5));
        assert !requestService.getUnsatisfiedRequests().contains(request1);
        assert (requestService.getUnsatisfiedRequests().get(0).getId().equals(request5.getId()) ||
                requestService.getUnsatisfiedRequests().get(1).getId().equals(request5.getId()));
        assert requestService.getUnsatisfiedRequests().get(2).getId().equals(request2.getId());
        assert requestService.getUnsatisfiedRequests().get(3).getId().equals(request3.getId());

        requestRepository.remove(request1.getId());
        requestRepository.remove(request2.getId());
        requestRepository.remove(request3.getId());
        requestRepository.remove(request4.getId());
        requestRepository.remove(request5.getId());
    }

    @Test
    public void getRequestById() {
        Request request1 = Request.builder()
                .urgency(Request.urgencyLevels.LOW)
                .build();
        request1.setIsSatisfied(true);
        Request request2 = Request.builder()
                .urgency(Request.urgencyLevels.LOW)
                .build();


        requestRepository.add(request1);
        requestRepository.add(request2);

        assert requestService.getRequestById(request1.getId()).equals(request1);
        assert requestService.getRequestById(request2.getId()).equals(request2);

        try {
            requestService.getRequestById(request1.getId() + request2.getId());
            assert false;
        }
        catch (RuntimeException ex)
        {
            assert true;
        }

        requestRepository.remove(request1.getId());
        requestRepository.remove(request2.getId());
    }

    @Test
    public void fulfillRequest() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();

        Donor donor1 = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard1).residence(res1)
                .build();
        donorRepository.add(donor1);

        Blood blood1 = Blood.builder().recoltationDate(LocalDate.now())
                .donor(donor1).build();
        Blood blood2 = Blood.builder().recoltationDate(LocalDate.now().minusDays(1))
                .donor(donor1).build();
        bloodRepository.add(blood1);
        bloodRepository.add(blood2);

        BloodComponent thrombocytes1 = BloodComponent.builder()
                .blood(blood1).type(BloodComponent.types.THROMBOCYTES)
                .build();
        BloodComponent thrombocytes2 = BloodComponent.builder()
                .blood(blood2).type(BloodComponent.types.THROMBOCYTES)
                .build();
        BloodComponent plasma1 = BloodComponent.builder()
                .blood(blood1).type(BloodComponent.types.PLASMA)
                .build();
        BloodComponent plasma2 = BloodComponent.builder()
                .blood(blood2).type(BloodComponent.types.PLASMA)
                .build();
        BloodComponent redBloodCells1 = BloodComponent.builder()
                .blood(blood1).type(BloodComponent.types.RED_BLOOD_CELLS)
                .build();
        BloodComponent redBloodCells2 = BloodComponent.builder()
                .blood(blood2).type(BloodComponent.types.RED_BLOOD_CELLS)
                .build();
        bloodComponentRepository.add(thrombocytes1);
        bloodComponentRepository.add(thrombocytes2);
        bloodComponentRepository.add(plasma1);
        bloodComponentRepository.add(plasma2);
        bloodComponentRepository.add(redBloodCells1);
        bloodComponentRepository.add(redBloodCells2);

        Request request = Request.builder()
                .thrombocytesUnits((byte) 2).redBloodCellsUnits((byte) 2).plasmaUnits((byte) 2)
                .urgency(Request.urgencyLevels.LOW)
                .build();
        requestRepository.add(request);

        try {
            requestService.fulfillRequest(request.getId(), Collections.singletonList(thrombocytes1.getId())
                    ,Collections.singletonList(plasma1.getId())
                    ,Collections.singletonList(redBloodCells1.getId()));
            assert false;
        }
        catch (RuntimeException ex){
            assert ( ex.getMessage().contains("thrombocytes")
                    && ex.getMessage().contains("plasma")
                    && ex.getMessage().contains("red blood cells"));
        }

        try {
            requestService.fulfillRequest(request.getId(), Arrays.asList(thrombocytes1.getId(), thrombocytes2.getId())
                    ,Collections.singletonList(plasma1.getId())
                    ,Collections.singletonList(redBloodCells1.getId()));
            assert false;
        }
        catch (RuntimeException ex){
            assert ( ex.getMessage().contains("plasma")
                    && ex.getMessage().contains("red blood cells"));
        }

        try {
            requestService.fulfillRequest(request.getId(), Arrays.asList(thrombocytes1.getId(), thrombocytes2.getId())
                    ,Arrays.asList(plasma1.getId(), plasma2.getId())
                    ,Collections.singletonList(redBloodCells1.getId()));
            assert false;
        }
        catch (RuntimeException ex){
            assert ex.getMessage().contains("red blood cells");
        }

        assert bloodComponentRepository.getAll().containsAll(Arrays.asList(thrombocytes1, thrombocytes2
                , plasma1, plasma2, redBloodCells1, redBloodCells2));

        requestService.fulfillRequest(request.getId(), Arrays.asList(thrombocytes1.getId(), thrombocytes2.getId())
                ,Arrays.asList(plasma1.getId(), plasma2.getId())
                ,Arrays.asList(redBloodCells1.getId(), redBloodCells2.getId()));

        assert !bloodComponentRepository.getAll().containsAll(Arrays.asList(thrombocytes1, thrombocytes2
                , plasma1, plasma2, redBloodCells1, redBloodCells2));

        bloodRepository.remove(blood1.getId());
        bloodRepository.remove(blood2.getId());

        donorRepository.remove(donor1.getId());

        requestRepository.remove(request.getId());
    }

    @Test
    public void getDoctorRequest() {

        HealthWorker doctor1 = HealthWorker.builder()
                .firstName("HWFN1").lastName("HWLN1")
                .email("hw1@email.com").phoneNumber("+40712345678")
                .type(HealthWorker.types.DOCTOR)
                .build();
        HealthWorker doctor2 = HealthWorker.builder()
                .firstName("HWFN2").lastName("HWLN2")
                .email("hw2@email.com").phoneNumber("+40712345679")
                .type(HealthWorker.types.DOCTOR)
                .build();
        healthWorkerRepository.add(doctor1);
        healthWorkerRepository.add(doctor2);

        LoginInformation loginInformation1 = LoginInformation.builder()
                .person(doctor1).username("d1").password("p1")
                .build();
        LoginInformation loginInformation2 = LoginInformation.builder()
                .person(doctor2).username("d2").password("p2")
                .build();
        loginInformationRepository.add(loginInformation1);
        loginInformationRepository.add(loginInformation2);

        Request request1 = Request.builder()
                .urgency(Request.urgencyLevels.LOW).healthWorker(doctor1)
                .requestDate(LocalDate.now())
                .build();
        request1.setIsSatisfied(true);
        Request request2 = Request.builder()
                .urgency(Request.urgencyLevels.LOW).healthWorker(doctor2)
                .requestDate(LocalDate.now().minusDays(20))
                .build();
        Request request3 = Request.builder()
                .urgency(Request.urgencyLevels.LOW).healthWorker(doctor1)
                .requestDate(LocalDate.now().minusMonths(1))
                .build();
        requestRepository.add(request1);
        requestRepository.add(request2);
        requestRepository.add(request3);

        assert requestService.getDoctorRequest(loginInformation1.getUsername()).containsAll(Arrays.asList(request1,request3));
        assert !requestService.getDoctorRequest(loginInformation1.getUsername()).contains(request2);
        assert requestService.getDoctorRequest(loginInformation2.getUsername()).contains(request2);
        assert !requestService.getDoctorRequest(loginInformation2.getUsername()).containsAll(Arrays.asList(request1,request3));

        assert requestService.getDoctorRequest(loginInformation1.getUsername()).get(0).getId().equals(request3.getId());
        assert requestService.getDoctorRequest(loginInformation1.getUsername()).get(1).getId().equals(request1.getId());

        requestRepository.remove(request1.getId());
        requestRepository.remove(request2.getId());
        requestRepository.remove(request3.getId());

        loginInformationRepository.remove(loginInformation1.getId());
        loginInformationRepository.remove(loginInformation2.getId());

        healthWorkerRepository.remove(doctor1.getId());
        healthWorkerRepository.remove(doctor2.getId());
    }
}