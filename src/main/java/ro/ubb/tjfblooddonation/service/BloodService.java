package ro.ubb.tjfblooddonation.service;

import com.nimbusds.oauth2.sdk.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.tjfblooddonation.exceptions.ServiceError;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.repository.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BloodService {
    @Autowired
    LoginInformationRepository loginInformationRepository;
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    HealthWorkerRepository healthWorkerRepository;
    @Autowired
    BloodRepository bloodRepository;
    @Autowired
    BloodComponentRepository bloodComponentRepository;

    public void donateBlood(String donorUsername, Blood blood) {
        Person person = loginInformationRepository.getById(donorUsername).getPerson();
        Donor donor = new Donor();
        if( person instanceof Donor)
            donor = (Donor) person;

        if( donor.getForm().getPassedBasicCheckForm() && donor.getForm().getPassedDonateForm())
            bloodRepository.add(blood);

        donor.setForm(null);
    }

    public Blood getBlood(Long bloodId) {
        return bloodRepository.getById(bloodId);
    }

    public Set<Blood> getUnanalysedBlood() {
        return bloodRepository.getAll().stream()
                .filter(blood -> blood.getAnalysis() == null ).collect(Collectors.toSet());
    }

    public void analyseBlood(Long bloodId, Analysis analysis) {
        Blood blood = bloodRepository.getById(bloodId);
        if(blood.getAnalysis() == null )
            blood.setAnalysis(analysis);
        else
            throw new ServiceError("Blood with id " + bloodId + " is already analysed!");
    }

    public Set<Blood> getUnseparatedBlood() {
        return bloodRepository.getAll().stream()
                .filter(blood -> blood.isSeparated()).collect(Collectors.toSet());
    }

    public void separateBlood(Long bloodId) {
        Blood blood = bloodRepository.getById(bloodId);
        if(!blood.isSeparated()) {
            blood.setSeparated(true);

            bloodComponentRepository.add(
                    BloodComponent.builder()
                            .blood(blood)
                            .type(BloodComponent.types.THROMBOCYTES)
                            .build()
            );

            bloodComponentRepository.add(
                    BloodComponent.builder()
                            .blood(blood)
                            .type(BloodComponent.types.PLASMA)
                            .build()
            );

            bloodComponentRepository.add(
                    BloodComponent.builder()
                            .blood(blood)
                            .type(BloodComponent.types.RED_BLOOD_CELLS)
                            .build()
            );
        }
        else
            throw new ServiceError("Blood with id " + bloodId + " is already separated!");
    }

    public List<Blood> getUserBlood(String donorUsername) {
        Person person = loginInformationRepository.getById(donorUsername).getPerson();

        return bloodRepository.getAll().stream()
                .filter(blood -> blood.getDonor().getId() == person.getId())
                .sorted((b1,b2) -> -b1.getRecoltationDate().compareTo(b2.getRecoltationDate()))
                .collect(Collectors.toList());
    }

    public Date getNextDonateTime(String donorUsername) {
        Optional<Blood> bloodOpt = getUserBlood(donorUsername).stream()
                .max(Comparator.comparing(Blood::getRecoltationDate));

        if(bloodOpt.isPresent()) {
            Date lastDonated = bloodOpt.get().getRecoltationDate();
            lastDonated.setMonth(lastDonated.getMonth() + 2);
            return lastDonated;
        }

        return Date.from(Instant.now());
    }

}
