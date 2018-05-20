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
import ro.ubb.tjfblooddonation.repository.HealthWorkerRepository;
import ro.ubb.tjfblooddonation.repository.InstitutionRepository;
import ro.ubb.tjfblooddonation.repository.PatientRepository;
import ro.ubb.tjfblooddonation.repository.RequestRepository;

import java.util.Arrays;

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
        request2.setIsSatisfied(false);
        Request request3 = Request.builder()
                .urgency(Request.urgencyLevels.LOW)
                .build();
        request2.setIsSatisfied(false);
        Request request4 = Request.builder()
                .urgency(Request.urgencyLevels.HIGH)
                .build();
        request4.setIsSatisfied(false);
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
        assert requestService.getUnsatisfiedRequests().get(0).equals(request4);
        assert requestService.getUnsatisfiedRequests().get(1).equals(request2);
        assert requestService.getUnsatisfiedRequests().get(2).equals(request3);

        request5.setIsSatisfied(false);
        requestRepository.update(request5);

        assert requestService.getUnsatisfiedRequests().containsAll(Arrays.asList(request2, request3, request4, request5));
        assert !requestService.getUnsatisfiedRequests().contains(request1);
        assert (requestService.getUnsatisfiedRequests().get(0).equals(request5) ||
                requestService.getUnsatisfiedRequests().get(1).equals(request5));
        assert requestService.getUnsatisfiedRequests().get(2).equals(request2);
        assert requestService.getUnsatisfiedRequests().get(3).equals(request3);

        requestRepository.remove(request1.getId());
        requestRepository.remove(request2.getId());
        requestRepository.remove(request3.getId());
        requestRepository.remove(request4.getId());
        requestRepository.remove(request5.getId());
    }

    @Test
    public void getRequestById() {
    }

    @Test
    public void fulfillRequest() {
    }

    @Test
    public void getDoctorRequest() {
    }
}