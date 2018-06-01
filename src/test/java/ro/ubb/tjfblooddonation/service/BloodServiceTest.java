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

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CatalogConfig.class, AppLocalConfig.class,
        JPAConfig.class, CacheConfig.class})
public class BloodServiceTest {
    @Autowired
    BloodService bloodService;
    @Autowired
    LoginInformationRepository loginInformationRepository;
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    BloodRepository bloodRepository;
    @Autowired
    BloodComponentRepository bloodComponentRepository;
    @Autowired
    RequestRepository requestRepository;

    @Test
    public void donateBlood() {
        Address res = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard = IdCard.builder().cnp("1234567890000").address(res).build();

        Form form = Form.builder().timeCompletedDonateForm(Timestamp.from(Instant.now()))
                .passedBasicCheckForm(true).passedDonateForm(false).build();

        Donor donor = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard).residence(res)
                .build();
        donor.setForm(form);
        donorRepository.add(donor);

        LoginInformation loginInformation = LoginInformation.builder().username("D1").password("p1")
                .person(donor).build();
        loginInformationRepository.add(loginInformation);

        Blood blood = Blood.builder().recoltationDate(LocalDate.now())
                .donor(donor).build();

        try {
            bloodService.donateBlood(loginInformation.getUsername(), blood);
            assert false ;
        }
        catch (RuntimeException ex)
        {
            assert true;
        }

        donor.getForm().setPassedDonateForm(true);
        loginInformation.setPerson(donor);
        loginInformationRepository.update(loginInformation);
        bloodService.donateBlood(loginInformation.getUsername(), blood);

        assert bloodService.getBloodById(blood.getId()).getId().equals(blood.getId());

        donor = (Donor)loginInformationRepository.getById(loginInformation.getUsername()).getPerson();
        System.out.println(donor.getForm());
        assertNotEquals(donor.getForm(), form);

        bloodRepository.remove(blood.getId());
        loginInformationRepository.remove(loginInformation.getId());
        donorRepository.remove(donor.getId());
    }

    @Test
    public void getBloodById() {
            Address res = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

            IdCard idCard = IdCard.builder().cnp("1234567890000").address(res).build();

            Donor donor = Donor.builder().firstName("FND1").lastName("LND1")
                    .email("d1@email.com").phoneNumber("+40712345678")
                    .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                    .bloodType("AB").rH("-")
                    .idCard(idCard).residence(res)
                    .build();
            donorRepository.add(donor);

            Blood blood1 = Blood.builder().recoltationDate(LocalDate.now().minusDays(3))
                    .donor(donor).build();
            Blood blood2 = Blood.builder().recoltationDate(LocalDate.now())
                    .donor(donor).build();

            bloodRepository.add(blood1);
            bloodRepository.add(blood2);

            assert bloodService.getBloodById(blood1.getId()).equals(blood1);
            assert bloodService.getBloodById(blood2.getId()).equals(blood2);
            try {
                bloodService.getBloodById(blood1.getId() + blood2.getId());
                assert false;
            }
            catch (RuntimeException ex)
            {
                assert true;
            }

            bloodRepository.remove(blood1.getId());
            bloodRepository.remove(blood2.getId());
            donorRepository.remove(donor.getId());

    }

    @Test
    public void getUnanalysedBlood() {
        Address res = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard = IdCard.builder().cnp("1234567890000").address(res).build();

        Donor donor = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard).residence(res)
                .build();
        donorRepository.add(donor);

        Blood blood1 = Blood.builder().recoltationDate(LocalDate.now().minusDays(3))
                .donor(donor).build();
        Blood blood2 = Blood.builder().recoltationDate(LocalDate.now())
                .donor(donor).build();

        Analysis analysis1 = Analysis.builder().hiv(false).hb(false).hcv(false)
                .sifilis(false).htlv(false).alt(false).imunoHematology(false)
                .build();

        blood1.setAnalysis(analysis1);

        bloodRepository.add(blood1);
        bloodRepository.add(blood2);

        assert !bloodService.getUnanalysedBlood().contains(blood1);
        assert bloodService.getUnanalysedBlood().contains(blood2);

        Analysis analysis2 = Analysis.builder().hiv(false).hb(false).hcv(false)
                .sifilis(false).htlv(false).alt(false).imunoHematology(false)
                .build();

        blood2.setAnalysis(analysis2);
        bloodRepository.update(blood2);

        assert !bloodService.getUnanalysedBlood().contains(blood1);
        assert !bloodService.getUnanalysedBlood().contains(blood2);

        bloodRepository.remove(blood1.getId());
        bloodRepository.remove(blood2.getId());
        donorRepository.remove(donor.getId());
    }

    @Test
    public void analyseBlood() {
        Address res = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard = IdCard.builder().cnp("1234567890000").address(res).build();

        Donor donor = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard).residence(res)
                .build();
        donorRepository.add(donor);

        Blood blood = Blood.builder().recoltationDate(LocalDate.now().minusDays(3))
                .donor(donor).build();
        bloodRepository.add(blood);

        Analysis analysis = Analysis.builder().hiv(false).hb(false).hcv(false)
                .sifilis(false).htlv(false).alt(false).imunoHematology(false)
                .build();

        bloodService.analyseBlood(blood.getId(), analysis);

        try {
            bloodService.analyseBlood(blood.getId(), analysis);
            assert false;
        }
        catch (RuntimeException ex)
        {
            assert true;
        }

        bloodRepository.remove(blood.getId());

        donorRepository.remove(donor.getId());
    }

    @Test
    public void getUnseparatedBlood() {
        Address res = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard = IdCard.builder().cnp("1234567890000").address(res).build();

        Donor donor = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard).residence(res)
                .build();
        donorRepository.add(donor);

        Blood blood1 = Blood.builder().recoltationDate(LocalDate.now().minusDays(3))
                .donor(donor).build();
        Blood blood2 = Blood.builder().recoltationDate(LocalDate.now())
                .donor(donor).build();

        blood1.setSeparated(true);

        bloodRepository.add(blood1);
        bloodRepository.add(blood2);

        assert !bloodService.getUnseparatedBlood().contains(blood1);
        assert bloodService.getUnseparatedBlood().contains(blood2);

        blood2.setSeparated(true);
        bloodRepository.update(blood2);

        assert !bloodService.getUnseparatedBlood().contains(blood1);
        assert !bloodService.getUnseparatedBlood().contains(blood2);

        bloodRepository.remove(blood1.getId());
        bloodRepository.remove(blood2.getId());
        donorRepository.remove(donor.getId());
    }

    @Test
    public void separateBlood() {
        Address res = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard = IdCard.builder().cnp("1234567890000").address(res).build();

        Donor donor = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard).residence(res)
                .build();
        donorRepository.add(donor);

        Blood blood = Blood.builder().recoltationDate(LocalDate.now().minusDays(3))
                .donor(donor).build();
        bloodRepository.add(blood);

        bloodService.separateBlood(blood.getId());

        assert bloodRepository.getById(blood.getId()).isSeparated();

        try {
            bloodService.separateBlood(blood.getId());
            assert false;
        }
        catch (RuntimeException ex)
        {
            assert true;
        }

        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodRepository.remove(blood.getId());
        donorRepository.remove(donor.getId());
    }

    @Test
    public void getOkThrombocytes() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();
        Address res2 = Address.builder().country("CTRY2").region("R2").city("CITY2").street("S2 NO.2").build();
        Address res3 = Address.builder().country("CTRY3").region("R3").city("CITY3").street("S3 NO.3").build();
        Address res4 = Address.builder().country("CTRY4").region("R4").city("CITY4").street("S4 NO.4").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();
        IdCard idCard2 = IdCard.builder().cnp("1234567890001").address(res2).build();
        IdCard idCard3 = IdCard.builder().cnp("1234567890002").address(res3).build();
        IdCard idCard4 = IdCard.builder().cnp("1234567890003").address(res4).build();

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
                .email("d3@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("0").rH("-")
                .idCard(idCard3).residence(res3)
                .build();
        Donor donor4 = Donor.builder().firstName("FND4").lastName("LND4")
                .email("d4@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard4).residence(res4)
                .build();

        donorRepository.add(donor1);
        donorRepository.add(donor2);
        donorRepository.add(donor3);
        donorRepository.add(donor4);

        Blood blood1 = Blood.builder().recoltationDate(LocalDate.now())
                .donor(donor1).build();
        Blood blood2 = Blood.builder().recoltationDate(LocalDate.now().minusDays(1))
                .donor(donor2).build();
        Blood blood3 = Blood.builder().recoltationDate(LocalDate.now().minusDays(2))
                .donor(donor3).build();
        Blood blood4 = Blood.builder().recoltationDate(LocalDate.now().minusDays(6))
                .donor(donor4).build();

        bloodRepository.add(blood1);
        bloodRepository.add(blood2);
        bloodRepository.add(blood3);
        bloodRepository.add(blood4);

        BloodComponent thrombocytes1 = BloodComponent.builder()
                .blood(blood1).type(BloodComponent.types.THROMBOCYTES)
                .build();
        BloodComponent thrombocytes2 = BloodComponent.builder()
                .blood(blood2).type(BloodComponent.types.THROMBOCYTES)
                .build();
        BloodComponent thrombocytes3 = BloodComponent.builder()
                .blood(blood3).type(BloodComponent.types.THROMBOCYTES)
                .build();
        BloodComponent thrombocytes4 = BloodComponent.builder()
                .blood(blood4).type(BloodComponent.types.THROMBOCYTES)
                .build();

        bloodComponentRepository.add(thrombocytes1);
        bloodComponentRepository.add(thrombocytes2);
        bloodComponentRepository.add(thrombocytes3);
        bloodComponentRepository.add(thrombocytes4);

        Patient patient = Patient.builder()
                .firstName("FNP1").lastName("LNP1")
                .email("p1@email.com").phoneNumber("+40712345678")
                .bloodType("AB").rH("-")
                .build();

        patientRepository.add(patient);

        Request request = Request.builder()
                .patient(patient).urgency(Request.urgencyLevels.LOW)
                .build();

        requestRepository.add(request);

        assert bloodService.getOkThrombocytes(request.getId()).contains(thrombocytes1);
        assert !bloodService.getOkThrombocytes(request.getId()).contains(thrombocytes2);
        assert bloodService.getOkThrombocytes(request.getId()).contains(thrombocytes3);
        assert !bloodService.getOkThrombocytes(request.getId()).contains(thrombocytes4);

        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood1.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood2.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood3.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood4.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));

        bloodRepository.remove(blood1.getId());
        bloodRepository.remove(blood2.getId());
        bloodRepository.remove(blood3.getId());
        bloodRepository.remove(blood4.getId());

        donorRepository.remove(donor1.getId());
        donorRepository.remove(donor2.getId());
        donorRepository.remove(donor3.getId());
        donorRepository.remove(donor4.getId());

        requestRepository.remove(request.getId());

        patientRepository.remove(patient.getId());

    }

    @Test
    public void getOkRedBloodCells() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();
        Address res2 = Address.builder().country("CTRY2").region("R2").city("CITY2").street("S2 NO.2").build();
        Address res3 = Address.builder().country("CTRY3").region("R3").city("CITY3").street("S3 NO.3").build();
        Address res4 = Address.builder().country("CTRY4").region("R4").city("CITY4").street("S4 NO.4").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();
        IdCard idCard2 = IdCard.builder().cnp("1234567890001").address(res2).build();
        IdCard idCard3 = IdCard.builder().cnp("1234567890002").address(res3).build();
        IdCard idCard4 = IdCard.builder().cnp("1234567890003").address(res4).build();

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
                .email("d3@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("0").rH("-")
                .idCard(idCard3).residence(res3)
                .build();
        Donor donor4 = Donor.builder().firstName("FND4").lastName("LND4")
                .email("d4@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard4).residence(res4)
                .build();

        donorRepository.add(donor1);
        donorRepository.add(donor2);
        donorRepository.add(donor3);
        donorRepository.add(donor4);

        Blood blood1 = Blood.builder().recoltationDate(LocalDate.now())
                .donor(donor1).build();
        Blood blood2 = Blood.builder().recoltationDate(LocalDate.now().minusDays(5))
                .donor(donor2).build();
        Blood blood3 = Blood.builder().recoltationDate(LocalDate.now().minusDays(10))
                .donor(donor3).build();
        Blood blood4 = Blood.builder().recoltationDate(LocalDate.now().minusDays(43))
                .donor(donor4).build();

        bloodRepository.add(blood1);
        bloodRepository.add(blood2);
        bloodRepository.add(blood3);
        bloodRepository.add(blood4);

        BloodComponent redBloodCells1 = BloodComponent.builder()
                .blood(blood1).type(BloodComponent.types.RED_BLOOD_CELLS)
                .build();
        BloodComponent redBloodCells2 = BloodComponent.builder()
                .blood(blood2).type(BloodComponent.types.RED_BLOOD_CELLS)
                .build();
        BloodComponent redBloodCells3 = BloodComponent.builder()
                .blood(blood3).type(BloodComponent.types.RED_BLOOD_CELLS)
                .build();
        BloodComponent redBloodCells4 = BloodComponent.builder()
                .blood(blood4).type(BloodComponent.types.RED_BLOOD_CELLS)
                .build();

        bloodComponentRepository.add(redBloodCells1);
        bloodComponentRepository.add(redBloodCells2);
        bloodComponentRepository.add(redBloodCells3);
        bloodComponentRepository.add(redBloodCells4);

        Patient patient = Patient.builder()
                .firstName("FNP1").lastName("LNP1")
                .email("p1@email.com").phoneNumber("+40712345678")
                .bloodType("AB").rH("-")
                .build();

        patientRepository.add(patient);

        Request request = Request.builder()
                .patient(patient).urgency(Request.urgencyLevels.LOW)
                .build();

        requestRepository.add(request);

        assert bloodService.getOkRedBloodCells(request.getId()).contains(redBloodCells1);
        assert !bloodService.getOkRedBloodCells(request.getId()).contains(redBloodCells2);
        assert bloodService.getOkRedBloodCells(request.getId()).contains(redBloodCells3);
        assert !bloodService.getOkRedBloodCells(request.getId()).contains(redBloodCells4);

        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood1.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood2.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood3.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood4.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));

        bloodRepository.remove(blood1.getId());
        bloodRepository.remove(blood2.getId());
        bloodRepository.remove(blood3.getId());
        bloodRepository.remove(blood4.getId());

        donorRepository.remove(donor1.getId());
        donorRepository.remove(donor2.getId());
        donorRepository.remove(donor3.getId());
        donorRepository.remove(donor4.getId());

        requestRepository.remove(request.getId());

        patientRepository.remove(patient.getId());

    }

    @Test
    public void getOkPlasma() {
        Address res1 = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();
        Address res2 = Address.builder().country("CTRY2").region("R2").city("CITY2").street("S2 NO.2").build();
        Address res3 = Address.builder().country("CTRY3").region("R3").city("CITY3").street("S3 NO.3").build();
        Address res4 = Address.builder().country("CTRY4").region("R4").city("CITY4").street("S4 NO.4").build();

        IdCard idCard1 = IdCard.builder().cnp("1234567890000").address(res1).build();
        IdCard idCard2 = IdCard.builder().cnp("1234567890001").address(res2).build();
        IdCard idCard3 = IdCard.builder().cnp("1234567890002").address(res3).build();
        IdCard idCard4 = IdCard.builder().cnp("1234567890003").address(res4).build();

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
                .email("d3@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("0").rH("-")
                .idCard(idCard3).residence(res3)
                .build();
        Donor donor4 = Donor.builder().firstName("FND4").lastName("LND4")
                .email("d4@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard4).residence(res4)
                .build();

        donorRepository.add(donor1);
        donorRepository.add(donor2);
        donorRepository.add(donor3);
        donorRepository.add(donor4);

        Blood blood1 = Blood.builder().recoltationDate(LocalDate.now())
                .donor(donor1).build();
        Blood blood2 = Blood.builder().recoltationDate(LocalDate.now().minusDays(5))
                .donor(donor2).build();
        Blood blood3 = Blood.builder().recoltationDate(LocalDate.now().minusDays(10))
                .donor(donor3).build();
        Blood blood4 = Blood.builder().recoltationDate(LocalDate.now().minusMonths(6))
                .donor(donor4).build();

        bloodRepository.add(blood1);
        bloodRepository.add(blood2);
        bloodRepository.add(blood3);
        bloodRepository.add(blood4);

        BloodComponent plasma1 = BloodComponent.builder()
                .blood(blood1).type(BloodComponent.types.PLASMA)
                .build();
        BloodComponent plasma2 = BloodComponent.builder()
                .blood(blood2).type(BloodComponent.types.PLASMA)
                .build();
        BloodComponent plasma3 = BloodComponent.builder()
                .blood(blood3).type(BloodComponent.types.PLASMA)
                .build();
        BloodComponent plasma4 = BloodComponent.builder()
                .blood(blood4).type(BloodComponent.types.PLASMA)
                .build();

        bloodComponentRepository.add(plasma1);
        bloodComponentRepository.add(plasma2);
        bloodComponentRepository.add(plasma3);
        bloodComponentRepository.add(plasma4);

        Patient patient = Patient.builder()
                .firstName("FNP1").lastName("LNP1")
                .email("p1@email.com").phoneNumber("+40712345678")
                .bloodType("AB").rH("-")
                .build();

        patientRepository.add(patient);

        Request request = Request.builder()
                .patient(patient).urgency(Request.urgencyLevels.LOW)
                .build();

        requestRepository.add(request);

        assert bloodService.getOkPlasma(request.getId()).contains(plasma1);
        assert !bloodService.getOkPlasma(request.getId()).contains(plasma2);
        assert bloodService.getOkPlasma(request.getId()).contains(plasma3);
        assert !bloodService.getOkPlasma(request.getId()).contains(plasma4);

        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood1.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood2.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood3.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));
        bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getBlood().getId().equals(blood4.getId()))
                .forEach(bloodComponent -> bloodComponentRepository.remove(bloodComponent.getId()));

        bloodRepository.remove(blood1.getId());
        bloodRepository.remove(blood2.getId());
        bloodRepository.remove(blood3.getId());
        bloodRepository.remove(blood4.getId());

        donorRepository.remove(donor1.getId());
        donorRepository.remove(donor2.getId());
        donorRepository.remove(donor3.getId());
        donorRepository.remove(donor4.getId());

        requestRepository.remove(request.getId());

        patientRepository.remove(patient.getId());

    }

    @Test
    public void getUserBlood() {
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

        donorRepository.add(donor1);
        donorRepository.add(donor2);

        LoginInformation loginInformation1 = LoginInformation.builder().username("D1").password("p1")
                .person(donor1).build();
        LoginInformation loginInformation2 = LoginInformation.builder().username("D2").password("p2")
                .person(donor2).build();

        loginInformationRepository.add(loginInformation1);
        loginInformationRepository.add(loginInformation2);

        Blood blood1 = Blood.builder().recoltationDate(LocalDate.now())
                .donor(donor1).build();
        Blood blood2 = Blood.builder().recoltationDate(LocalDate.now().minusDays(5))
                .donor(donor1).build();
        Blood blood3 = Blood.builder().recoltationDate(LocalDate.now().minusDays(10))
                .donor(donor2).build();

        bloodRepository.add(blood1);
        bloodRepository.add(blood2);
        bloodRepository.add(blood3);

        assert bloodService.getUserBlood(loginInformation1.getUsername()).contains(blood1);
        assert bloodService.getUserBlood(loginInformation1.getUsername()).contains(blood2);
        assert !bloodService.getUserBlood(loginInformation1.getUsername()).contains(blood3);
        assert bloodService.getUserBlood(loginInformation1.getUsername()).get(0).equals(blood1);
        assert bloodService.getUserBlood(loginInformation1.getUsername()).get(1).equals(blood2);

        bloodRepository.remove(blood1.getId());
        bloodRepository.remove(blood2.getId());
        bloodRepository.remove(blood3.getId());

        loginInformationRepository.remove(loginInformation1.getId());
        loginInformationRepository.remove(loginInformation2.getId());

        donorRepository.remove(donor1.getId());
        donorRepository.remove(donor2.getId());
    }

    @Test
    public void getNextDonateTime() {
        Address res = Address.builder().country("CTRY1").region("R1").city("CITY1").street("S1 NO.1").build();

        IdCard idCard = IdCard.builder().cnp("1234567890000").address(res).build();

        Donor donor = Donor.builder().firstName("FND1").lastName("LND1")
                .email("d1@email.com").phoneNumber("+40712345678")
                .dateOfBirth(LocalDate.parse("1990-05-20")).gender("female")
                .bloodType("AB").rH("-")
                .idCard(idCard).residence(res)
                .build();
        donorRepository.add(donor);

        LoginInformation loginInformation = LoginInformation.builder().username("D1").password("p1")
                .person(donor).build();
        loginInformationRepository.add(loginInformation);

        assert bloodService.getNextDonateTime(loginInformation.getUsername()).equals(LocalDate.now());

        Blood blood = Blood.builder().recoltationDate(LocalDate.now().minusMonths(2))
                .donor(donor).build();
        bloodRepository.add(blood);

        assert bloodService.getNextDonateTime(loginInformation.getUsername()).equals(LocalDate.now().plusMonths(2));

        bloodRepository.remove(blood.getId());
        loginInformationRepository.remove(loginInformation.getId());
        donorRepository.remove(donor.getId());
    }

    @Test
    public void askUsersToDonate() {
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

        donorRepository.add(donor1);
        donorRepository.add(donor2);

        LoginInformation loginInformation1 = LoginInformation.builder().username("D1").password("p1")
                .person(donor1).build();
        LoginInformation loginInformation2 = LoginInformation.builder().username("D2").password("p2")
                .person(donor2).build();

        loginInformationRepository.add(loginInformation1);
        loginInformationRepository.add(loginInformation2);

        Blood blood1 = Blood.builder().recoltationDate(LocalDate.now().minusMonths(6))
                .donor(donor1).build();
        Blood blood2 = Blood.builder().recoltationDate(LocalDate.now().minusMonths(3))
                .donor(donor1).build();
        Blood blood3 = Blood.builder().recoltationDate(LocalDate.now().minusMonths(5))
                .donor(donor2).build();

        bloodRepository.add(blood1);
        bloodRepository.add(blood2);
        bloodRepository.add(blood3);


        bloodService.askUsersToDonate();

        assertNotEquals(donorRepository.getById(donor1.getId()), "It's been a while since you last donated," +
                " and there is a blood shortage. Please come donate whenever you can.");
        assert donorRepository.getById(donor2.getId()).getMessage().equals("It's been a while since you last donated," +
                " and there is a blood shortage. Please come donate whenever you can.");


        bloodRepository.remove(blood1.getId());
        bloodRepository.remove(blood2.getId());
        bloodRepository.remove(blood3.getId());

        loginInformationRepository.remove(loginInformation1.getId());
        loginInformationRepository.remove(loginInformation2.getId());

        donorRepository.remove(donor1.getId());
        donorRepository.remove(donor2.getId());

    }

    @Test
    public void areCompatible() {
        Donor donor = Donor.builder().bloodType("O").rH("-").build();
        Patient patient = Patient.builder().bloodType("O").rH("-").build();

        assert bloodService.areCompatible(donor, patient) == 0;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == 1;

        patient.setRH("+");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("O");
        assert bloodService.areCompatible(donor, patient) == 1;

        patient.setBloodType("O");
        patient.setRH("-");
        donor.setRH("+");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setRH("+");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("O");
        assert bloodService.areCompatible(donor, patient) == 0;

        patient.setBloodType("O");
        patient.setRH("-");
        donor.setRH("-");
        donor.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == 0;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == 1;

        patient.setRH("+");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("O");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setBloodType("O");
        patient.setRH("-");
        donor.setRH("+");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setRH("+");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == 0;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("O");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setBloodType("O");
        patient.setRH("-");
        donor.setRH("-");
        donor.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == 0;
        patient.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == 1;

        patient.setRH("+");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("O");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setBloodType("O");
        patient.setRH("-");
        donor.setRH("+");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setRH("+");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == 0;
        patient.setBloodType("O");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setBloodType("O");
        patient.setRH("-");
        donor.setRH("-");
        donor.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == 0;

        patient.setRH("+");
        assert bloodService.areCompatible(donor, patient) == 1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("O");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setBloodType("O");
        patient.setRH("-");
        donor.setRH("+");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("AB");
        assert bloodService.areCompatible(donor, patient) == -1;

        patient.setRH("+");
        assert bloodService.areCompatible(donor, patient) == 0;
        patient.setBloodType("A");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("B");
        assert bloodService.areCompatible(donor, patient) == -1;
        patient.setBloodType("O");
        assert bloodService.areCompatible(donor, patient) == -1;
    }
}