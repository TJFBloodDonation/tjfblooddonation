package ro.ubb.tjfblooddonation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.tjfblooddonation.exceptions.LogInException;
import ro.ubb.tjfblooddonation.exceptions.ServiceError;
import ro.ubb.tjfblooddonation.model.*;
import ro.ubb.tjfblooddonation.repository.DonorRepository;
import ro.ubb.tjfblooddonation.repository.HealthWorkerRepository;
import ro.ubb.tjfblooddonation.repository.InstitutionRepository;
import ro.ubb.tjfblooddonation.repository.LoginInformationRepository;
import ro.ubb.tjfblooddonation.utils.Credentials;
import ro.ubb.tjfblooddonation.utils.Hashing;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    /*Elena O(Task UserService Part 1): Just so you guys know, I used the names Sebi added in the tasks for the Repository functions*/

    private Credentials credentials;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private HealthWorkerRepository healthWorkerRepository;
    @Autowired
    private LoginInformationRepository loginInformationRepository;
    @Autowired
    private InstitutionRepository institutionRepository;

    public List<Donor> getAllDonors(){
        return donorRepository.findAll();
    }

    public void addDonor(Donor d) {     //Elena O: this function was already here, so I didn't touch it...but created my own version bellow
        donorRepository.save(d);
    }

    public void createDonorAccount(String username, String password, Donor donor){
        LoginInformation loginInformation = new LoginInformation();
        loginInformation.setPassword(Hashing.hash(password));
        loginInformation.setUsername(username);
        loginInformation.setPerson(donor);
        donorRepository.add(donor);
        loginInformationRepository.add(loginInformation);           //also need to add a new account(LoginInformation entity)

    }
    public void deleteDonorAccount(String username){
        Long donorId = loginInformationRepository.getById(username).getPerson().getId();       //here we are using the Person attribute of the LoginInformation entity to get the id
        donorRepository.remove(donorId);
        loginInformationRepository.remove(username);                        //also need to remove user from LoginInformation
    }

    public void updateDonorAccount(String username, String password, Donor donor){
        if(credentials.isDonor(username, password)) {
            //not sure if I should set here the id of the donor to match
            donorRepository.update(donor);
            loginInformationRepository.getById(username).setPerson(donor);   //Also need to update the person linked to the LoginInformation of the use
        }
    }

    public void createHealthWorkerAccont(String healthWorkerUsername, String healthWorkerPassword, HealthWorker healthWorker){
        //check if user exists in login info repo + check if admin with function isAdmin() -- verifies id(username)
        if(loginInformationRepository.existsById(healthWorkerUsername))
            throw new ServiceError("Username is already taken!");
        LoginInformation loginInformation = new LoginInformation();
        loginInformation.setPassword(Hashing.hash(healthWorkerPassword));
        loginInformation.setUsername(healthWorkerUsername);         //so, based on the schema, the id(username) will not be generated automaticly by the system, but rather given by the admin???
        loginInformation.setPerson(healthWorker);
        healthWorkerRepository.add(healthWorker);
        loginInformationRepository.add(loginInformation);           //also need to add a new account(LoginInformation entity)
    }

    public void deleteHealthWorkerAccount(String username) {
        Long healthWorkerId = loginInformationRepository.getById(username).getPerson().getId();       //here we are using the Person attribute of the LoginInformation entity to get the id
        loginInformationRepository.remove(username);                        //also need to remove user from LoginInformation
        healthWorkerRepository.remove(healthWorkerId);
    }

    public void updateHealthWorkerAccount(String username, String password, HealthWorker healthWorker){
//        healthWorkerRepository.update(healthWorker);
        if(!password.equals(""))
            password = Hashing.hash(password);
        else
            password = loginInformationRepository.getById(username).getPassword();
        loginInformationRepository.update(new LoginInformation(username, password, healthWorker));   //Also need to update the person linked to the LoginInformation of the user
    }

    public Donor getDonor(String username, String password, String donorUsername){
        if(loginInformationRepository.getById(username) == null && credentials.isHealthWorker(username, password)) {            //here I could have also searched in the HW repository, but since we have decided upon the id's that tell us all we need to know about a user's status, found that unnecessary
            Person person = loginInformationRepository.getById(username).getPerson();
            if(person instanceof Donor)
                return (Donor)person;
            else
                throw new ServiceError("The provided id is not a donor");
        }
        return null;
    }

    public Person getPerson(String username, String password){
        LoginInformation loginInformation = this.loginInformationRepository.getById(username);
        if(Hashing.hash(password).equals(loginInformation.getPassword())){
            return loginInformation.getPerson();
        }
        throw new LogInException("Wrong password!");
    }

    public List<HealthWorker> getAllHealthWorkers(){
        return healthWorkerRepository.getAll();
    }

    public HealthWorker getHealthWorker(Long id){
        return healthWorkerRepository.getById(id);
    }

    public List<LoginInformation> getHealthWorkersAccounts(){
        return loginInformationRepository.getAll().stream().filter(l -> l.getPerson() instanceof HealthWorker).collect(Collectors.toList());
    }

    public LoginInformation getLoginInformationByUsername(String username){
        return loginInformationRepository.getById(username);
    }

    public List<Institution> getAllInstitutions() {
        return institutionRepository.getAll();
    }

    public void addInstitution(Institution institution){
        institutionRepository.add(institution);
    }
}

