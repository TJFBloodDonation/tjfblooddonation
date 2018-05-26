package ro.ubb.tjfblooddonation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.tjfblooddonation.exceptions.LogInException;
import ro.ubb.tjfblooddonation.exceptions.RepositoryException;
import ro.ubb.tjfblooddonation.exceptions.ServiceError;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.repository.DonorRepository;
import ro.ubb.tjfblooddonation.repository.HealthWorkerRepository;
import ro.ubb.tjfblooddonation.repository.LoginInformationRepository;
import ro.ubb.tjfblooddonation.utils.Hashing;
import ro.ubb.tjfblooddonation.utils.InfoCheck;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsersService {

    /*Elena O(Task UserService Part 1): Just so you guys know,
    I used the names Sebi added in the tasks for the Repository functions*/

    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private HealthWorkerRepository healthWorkerRepository;
    @Autowired
    private LoginInformationRepository loginInformationRepository;


    /**
     * Function that returns a list of all Donors
     *
     * @return the List of Donors
     */
    public List<Donor> getAllDonors(){
        return donorRepository.findAll();
    }

    /**
     * Function to add a Donor into the DonorRepository directly; My guess is that it is remnant of the time we tested
     * Hibernate when creating the database, and should be removed
     *
     * @param donor -  the Donor to be added
     */
    public void addDonor(Donor donor) {     //Elena O: this function was already here, so I didn't touch it...but created my own version bellow
        donorRepository.save(donor);
    }

    /**
     * Function to add a user account; It also adds the user to the appropriate repository
     * (DonorRepository od HealthWorkerRepository), depending on the class of which the user passed as parameter is an
     * instance of
     *
     * @param username - the username of the account to be created
     * @param password - the password of the account
     * @param user - the Person instance associated to the account
     */
    public void createUserAccount(String username, String password, Person user){
        LoginInformation loginInformation = new LoginInformation();
        loginInformation.setPassword(Hashing.hash(password));
        loginInformation.setUsername(username);
        loginInformation.setPerson(user);
        if(user instanceof Donor)
            donorRepository.add((Donor)user);

        if(user instanceof HealthWorker)
            healthWorkerRepository.add((HealthWorker)user);

        loginInformationRepository.add(loginInformation);
    }

    /**
     * Function to update the Donor/HealthWorker associated to the LoginInformation with the given username as ID
     *
     * @param username - the username of the Donor to be updated
     * @param user - a Person (either Donor or HealthWorker) containing the updated information
     */
    public void updateUserAccount(String username, Person user) {
        Person person = loginInformationRepository.getById(username).getPerson();
        if(user instanceof Donor)
            donorRepository.update((Donor)user);
        if(person instanceof HealthWorker)
            healthWorkerRepository.update((HealthWorker)user);
        loginInformationRepository.getById(username).setPerson(user);
    }

    /**
     * Function to remove the account associated to a user; It will als remove the Donor/HealthWorker instance
     * associated to the LoginInformation
     *
     * @param username - the username of the account to be deleted
     */
    public void deleteUserAccount(String username) {
        Person person = loginInformationRepository.getById(username).getPerson();
        loginInformationRepository.remove(username);
        if(person instanceof Donor)
            donorRepository.remove(person.getId());
        if(person instanceof HealthWorker)
            healthWorkerRepository.remove(person.getId());
    }

    /**
     * Function to get the Donor instance associated to a username
     *
     * @param donorUsername - the username of the Donor
     * @return the Donor instance associated to the LoginInformation with the given username as ID
     * @throws ServiceError if the username given is not associated to a Donor account
     */
    public Donor getDonor(String donorUsername){
        Person person = loginInformationRepository.getById(donorUsername).getPerson();
            if(person instanceof Donor)
                return (Donor)person;
            else
                throw new ServiceError("The provided id is not a donor");
    }

    /**
     * Function that associates the Forms sent from the UI with the Donor with the given username
     *
     * @param donorUsername - username of the Donor
     * @param form - the completed Form
     * @throws ServiceError if the username is not a valid one
     */
    public void completeForm(String donorUsername, Form form) {
        try {
            LoginInformation loginInformation = loginInformationRepository.getById(donorUsername);
            Person person = loginInformation.getPerson();

            Donor donor = null;
            if(person instanceof Donor)
                donor = (Donor) person;

            if(donor != null) {
                donor.setForm(form);

                donorRepository.update(donor);
                loginInformation.setPerson(donor);
                loginInformationRepository.update(loginInformation);
            }
        }
        catch (RepositoryException ex) {
            throw new ServiceError("Invalid donor username.");
        }
    }

    /**
     * Function to update the blood type and Rh of the donor with the given username
     *
     * @param donorUsername - username of the Donor
     * @param bloodType - the blood type
     * @param rH - the Rh
     * @throws ServiceError if the username is invalid
     */
    public void updateDonorInfo(String donorUsername, String bloodType, String rH) {
        try {
            LoginInformation loginInformation = loginInformationRepository.getById(donorUsername);
            Person person = loginInformation.getPerson();

            Donor donor = null;
            if(person instanceof Donor)
                donor = (Donor) person;

            if(donor != null) {
                donor.setBloodType(bloodType);
                donor.setRH(rH);

                donorRepository.update(donor);
                loginInformation.setPerson(donor);
                loginInformationRepository.update(loginInformation);
            }
        }
        catch (RepositoryException ex) {
            throw new ServiceError("Invalid donor username.");
        }
    }

    /**
     * Function to check if a Donor can give blood (i.e. they completed the DonateFrom not more than a week
     * before the current date and passed it, the staff completed the BasicCheckForm and they passed it)
     *
     * @param donorUsername - username of the Donor to check
     * @return true if the Donor with the given username can donate, false otherwise
     * @throws ServiceError if the username is invalid, or the Donor can't donate; The error message in the last case
     * contains details as to why the user can't donate
     */
    public boolean checkIfCanDonate(String donorUsername) {
        try {
            LoginInformation loginInformation = loginInformationRepository.getById(donorUsername);
            Person person = loginInformation.getPerson();

            Donor donor = null;
            if(person instanceof Donor)
                donor = (Donor) person;

            if(donor != null) {
                String err = InfoCheck.canDonate(donor);
                if(!err.equals(""))
                    throw new ServiceError(err);
                return true;
            }
        }
        catch (RepositoryException ex) {
            throw new ServiceError("Invalid donor username.");
        }

        return false;
    }

    /**
     * Function that returns a Set of all Donor that can donate at the current time
     * (i.e. they completed the DonateFrom, not more than a week before the current date and passed it,
     * the staff completed the BasicCheckForm and they passed it)
     *
     * @return the Set of eligible Donors
     */
    public Set<Donor> getDonorsReadyForDonating() {
        return donorRepository.getAll().stream()
                .filter(donor -> InfoCheck.canDonate(donor).equals(""))
                .collect(Collectors.toSet());
    }

    public Person getPerson(String username, String password){
        LoginInformation loginInformation = this.loginInformationRepository.getById(username);
        if(Hashing.hash(password).equals(loginInformation.getPassword())){
            return loginInformation.getPerson();
        }
        throw new LogInException("Wrong password!");
    }

}

