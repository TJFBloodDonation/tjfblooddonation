package ro.ubb.tjfblooddonation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.tjfblooddonation.exceptions.ServiceError;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.repository.*;
import ro.ubb.tjfblooddonation.utils.InfoCheck;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BloodService {
    @Autowired
    private LoginInformationRepository loginInformationRepository;
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    private BloodRepository bloodRepository;
    @Autowired
    private BloodComponentRepository bloodComponentRepository;
    @Autowired
    private RequestRepository requestRepository;

    /**
     * Function to donate blood. It checks for the donor by username, and in case the donor
     * passed both the basic check form, completed by the clinic staff the day of donation
     * and the donate form completed by the donor nt more than a week before when registering,
     * it will allow for the blood collected from the donor to be added into the system;
     * Afterwards it resets the form
     *
     * @param donorUsername - the username of the donor
     * @param blood         - the Blood sample collected from the donor
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the LoginInformation with the specified
     * donorUsername as ID is not in the Repository
     * @throws ServiceError if the Donor did not pass both Forms
     */
    public void donateBlood(String donorUsername, Blood blood) {

        LoginInformation loginInformation = loginInformationRepository.getById(donorUsername);
        Person person = loginInformation.getPerson();
        Donor donor = new Donor();
        if (person instanceof Donor)
            donor = (Donor) person;

        String err = InfoCheck.canDonate(donor);
        if(err.equals(""))
            bloodRepository.add(blood);
        else
            throw new ServiceError(err);

        donor.getForm().setPassedDonateForm(false);
        donor.getForm().setPassedBasicCheckForm(false);
        donor.getForm().setTimeCompletedDonateForm(null);
        donorRepository.update(donor);
        loginInformation.setPerson(donor);
        loginInformationRepository.update(loginInformation);
    }

    /**
     * Function to get a Blood instance by ID
     *
     * @param bloodId - ID of the blood to be returned
     * @return the Blood instance from the repository with the specified ID
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException is no blood with that ID is found
     */
    public Blood getBloodById(Long bloodId) {
        return bloodRepository.getById(bloodId);
    }

    /**
     * Function to get all unanalysed blood
     *
     * @return a set of all blood that has not been analysed by Blood Analysts
     */
    public Set<Blood> getUnanalysedBlood() {

        return bloodRepository.getAll().stream()
                .filter(blood -> blood.getAnalysis() == null) //if the analysis field from a Blood instance is null
                .collect(Collectors.toSet());                  //the blood is not yet analysed
    }

    /**
     * Function to associate an Analysis to a Blood sample
     *
     * @param bloodId  - the ID of the Blood sample for which the analysis was performed
     * @param analysis - the analysis to be associated to the blood
     * @throws ServiceError if the Blood instance with the given ID already has an associated analysis
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the Blood with the specified ID is not
     * in the Repository
     */
    public void analyseBlood(Long bloodId, Analysis analysis) {

        Blood blood = bloodRepository.getById(bloodId);
        if (blood.getAnalysis() == null) {
            blood.setAnalysis(analysis);
            bloodRepository.update(blood);
        }
        else
            throw new ServiceError("Blood with id " + bloodId + " is already analysed!");
    }

    /**
     * Function to get all unseparated blood (i.e. into THROMBOCYTES, RED_BLOOD_CELLS and PLASMA)
     *
     * @return a set of all unseparated Blood
     */
    public Set<Blood> getUnseparatedBlood() {

        return bloodRepository.getAll().stream()
                .filter(blood -> !blood.isSeparated())
                .collect(Collectors.toSet());
    }

    /**
     * Function to separate a Blood sample. A Blood bag is separated into 1 unit of each type.
     * It adds to the BloodComponentRepository three instances of the BloodComponent class all with the associated
     * Blood identical to the one whose ID is given as parameter and each with the types THROMBOCYTES, RED_BLOOD_CELLS
     * or PLASMA (one of each) and sets the isSeparated field of the Blood instance to true
     *
     * @param bloodId - ID of the Blood instance to be separated
     * @throws ServiceError if the Blood instance is already separated
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the Blood with the specified ID is not
     * in the Repository
     */
    public void separateBlood(Long bloodId) {

        Blood blood = bloodRepository.getById(bloodId);
        if (!blood.isSeparated()) {
            blood.setSeparated(true);
            bloodRepository.update(blood);
            //add  1 unit of THROMBOCYTES derived from the Blood
            bloodComponentRepository.add(
                    BloodComponent.builder()
                            .blood(blood)
                            .type(BloodComponent.types.THROMBOCYTES)
                            .build()
            );
            //add 1 unit of PLASMA derived from the Blood
            bloodComponentRepository.add(
                    BloodComponent.builder()
                            .blood(blood)
                            .type(BloodComponent.types.PLASMA)
                            .build()
            );
            //add 1 unit of RED_BLOOD_CELLS derived from the Blood
            bloodComponentRepository.add(
                    BloodComponent.builder()
                            .blood(blood)
                            .type(BloodComponent.types.RED_BLOOD_CELLS)
                            .build()
            );
        } else
            throw new ServiceError("Blood with id " + bloodId + " is already separated!");
    }

    /**
     * Function to return all BloodComponents of type THROMBOCYTES that are a match for the Patient associated to the
     * Request instance with the ID given as parameter; It checks in the BloodComponentRepository for instances whose
     * bloodType and Rh (stored in the bloodType and rH fields of the Donor associated to the Blood from which
     * the BloodComponent was derived) is either equal to the ones the Patient has,
     * or are the universally compatible (O_I Rh-); Also it excludes expired components
     *
     * @param requestId - the ID of the request for which thrombocytes are needed
     * @return the set of compatible BloodComponents of type THROMBOCYTES
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the Request with the specified ID is not
     * in the Repository
     */
    public Set<BloodComponent> getOkThrombocytes(Long requestId) {

        Request request = requestRepository.getById(requestId);

        return bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getType().equals("thrombocytes"))
                .filter(bloodComponent -> !bloodComponent.getBlood().getRecoltationDate().plusDays(5).isBefore(LocalDate.now()))
                .filter(bloodComponent ->
                        (bloodComponent.getBlood().getDonor().getBloodType().equals(request.getPatient().getBloodType())
                                && bloodComponent.getBlood().getDonor().getRH().equals(request.getPatient().getRH()))
                                || (bloodComponent.getBlood().getDonor().getBloodType().equals("0")
                                && bloodComponent.getBlood().getDonor().getRH().equals("-")))
                .collect(Collectors.toSet());
    }

    /**
     * Function to return all BloodComponents of type RED_BLOOD_CELLS that are a match for the Patient associated to the
     * Request instance with the ID given as parameter; It checks in the BloodComponentRepository for instances whose
     * bloodType and Rh (stored in the bloodType and rH fields of the Donor associated to the Blood from which
     * the BloodComponent was derived) is either equal to the ones the Patient has,
     * or are universally compatible (O_I Rh-); Also it excludes expired components
     *
     * @param requestId - the ID of the request for which thrombocytes are needed
     * @return the set of compatible BloodComponents of type RED_BLOOD_CELLS
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the Request with the specified ID is not
     * in the Repository
     */
    public Set<BloodComponent> getOkRedBloodCells(Long requestId) {

        Request request = requestRepository.getById(requestId);

        return bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getType().equals("red blood cells"))
                .filter(bloodComponent -> !bloodComponent.getBlood().getRecoltationDate().plusDays(42).isBefore(LocalDate.now()))
                .filter(bloodComponent ->
                        (bloodComponent.getBlood().getDonor().getBloodType().equals(request.getPatient().getBloodType())
                                && bloodComponent.getBlood().getDonor().getRH().equals(request.getPatient().getRH()))
                                || (bloodComponent.getBlood().getDonor().getBloodType().equals("0")
                                && bloodComponent.getBlood().getDonor().getRH().equals("-")))
                .collect(Collectors.toSet());
    }

    /**
     * Function to return all BloodComponents of type PLASMA that are a match for the Patient associated to the
     * Request instance with the ID given as parameter; It checks in the BloodComponentRepository for instances whose
     * bloodType and Rh (stored in the bloodType and rH fields of the Donor associated to the Blood from which
     * the BloodComponent was derived) is either equal to the ones the Patient has,
     * or are the universally compatible (O_I Rh-); Also it excludes expired components
     *
     * @param requestId - the ID of the request for which thrombocytes are needed
     * @return the set of compatible BloodComponents of type PLASMA
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the Request with the specified ID is not
     * in the Repository
     */
    public Set<BloodComponent> getOkPlasma(Long requestId) {

        Request request = requestRepository.getById(requestId);

        return bloodComponentRepository.getAll().stream()
                .filter(bloodComponent -> bloodComponent.getType().equals("plasma"))
                .filter(bloodComponent -> !bloodComponent.getBlood().getRecoltationDate().plusMonths(5).isBefore(LocalDate.now()))
                .filter(bloodComponent ->
                        (bloodComponent.getBlood().getDonor().getBloodType().equals(request.getPatient().getBloodType())
                                && bloodComponent.getBlood().getDonor().getRH().equals(request.getPatient().getRH()))
                                || (bloodComponent.getBlood().getDonor().getBloodType().equals("0")
                                && bloodComponent.getBlood().getDonor().getRH().equals("-")))
                .collect(Collectors.toSet());
    }

    /**
     * Function to get a list of a users donated Blood sorted by recoltationDate (latest donation first)
     *
     * @param donorUsername - the username of the Donor for which to return the Blood
     * @return the ordered List of Blood instances
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the LoginInformation with the specified
     * donorUsername as ID is not in the Repository
     */
    public List<Blood> getUserBlood(String donorUsername) {
        Person person = loginInformationRepository.getById(donorUsername).getPerson();

        return bloodRepository.getAll().stream()
                .filter(blood -> blood.getDonor().getId().equals(person.getId()))
                .sorted((b1, b2) -> -b1.getRecoltationDate().compareTo(b2.getRecoltationDate()))
                .collect(Collectors.toList());
    }

    /**
     * Function to get the next donation date for a user. A Donor can donate 4 times a year (approx. every 4 months).
     * If the Donor has never donated, the current date is returned
     *
     * @param donorUsername - the username of the Donor for which to check
     * @return the next Date at which the specified Donor can donate
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the LoginInformation with the specified
     * donorUsername as ID is not in the Repository
     */
    public LocalDate getNextDonateTime(String donorUsername) {

        Optional<Blood> bloodOpt = getUserBlood(donorUsername).stream()
                .max(Comparator.comparing(Blood::getRecoltationDate));

        if (bloodOpt.isPresent()) {
            LocalDate lastDonated = bloodOpt.get().getRecoltationDate();
            return LocalDate.from(lastDonated.plusMonths(4));
        }

        return LocalDate.now().minusDays(1);
    }


    public boolean isCloseBy(Institution institution, Donor donor){
        return institution.getAddress().getCity().equals(donor.getResidence().getCity());
    }

    /**
     * Function that sets the message attribute for each Donor instance that did not donate in the last 4 months to
     * a request to come donate;
     */

    private LocalDate max(LocalDate a, LocalDate b){
        if(a.isBefore(b))
            return b;
        return a;
    }
    public void askUsersToDonate(Institution institution) {
        List<Blood> allBlood = bloodRepository.findAll();
        List<Donor> allDonors = donorRepository.findAll();
        Map<Donor, LocalDate> nextAvailable = new HashMap<>();
        allDonors.forEach(d -> nextAvailable.put(d, LocalDate.now().minusDays(1)));
        allBlood.forEach(b ->
            nextAvailable.put(b.getDonor(), max(nextAvailable.get(b.getDonor()), b.getRecoltationDate().plusMonths(4)))
        );
        Set<Donor> readyToDonateDonors = nextAvailable.entrySet()
                .stream()
                .filter(e -> e.getValue().isBefore(LocalDate.now()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        Set<Donor> readyAndNearBy = readyToDonateDonors.stream()
                .filter(d -> isCloseBy(institution, d)).collect(Collectors.toSet());
        Set<Donor> toAnnounce;
        if(readyAndNearBy.size() >= 10)
            toAnnounce = readyAndNearBy;
        else
            toAnnounce = readyToDonateDonors;
        toAnnounce
                .forEach(donor -> {
                    donor.setMessage("It's been a while since you last donated, and there is a blood shortage." +
                            " Please come donate whenever you can.");
                    donorRepository.update(donor);
                });
    }

    public long getNoOfPeopleWhoDonatedForPatient(Patient patient) {
        return bloodRepository
                .findAll()
                .stream()
                .filter(b -> b.getPatient() != null)
                .filter(b -> b.getPatient().equals(patient))
                .filter(Blood::isSeparated)
                .filter(Blood::isHealthy)
                .count();
    }
}
