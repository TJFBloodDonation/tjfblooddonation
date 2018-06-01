package ro.ubb.tjfblooddonation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.tjfblooddonation.exceptions.ServiceError;
import ro.ubb.tjfblooddonation.model.Person;
import ro.ubb.tjfblooddonation.model.Request;
import ro.ubb.tjfblooddonation.repository.BloodComponentRepository;
import ro.ubb.tjfblooddonation.repository.BloodRepository;
import ro.ubb.tjfblooddonation.repository.LoginInformationRepository;
import ro.ubb.tjfblooddonation.repository.RequestRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class  RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private BloodComponentRepository bloodComponentRepository;
    @Autowired
    private LoginInformationRepository loginInformationRepository;
    @Autowired
    private BloodRepository bloodRepository;

    /**
     * Function to add a request to the repository; Checks if the HealthWorker is a DOCTOR before adding the Request
     *
     * @param request - the request to be added
     * @throws ServiceError if the HealthWorker associated to the request is not of type DOCTOR
     */
    public void addRequest(Request request) {

        if(request.getHealthWorker().getType().equals("doctor"))
            requestRepository.add(request);
        else
            throw new ServiceError("Request can't be performed by anyone but a doctor.");
    }

    /**
     * Function to get a list of unsatisfied Requests, sorted by urgency (most urgent Requests at the top);
     * If patients in Requests are active Donors (i.e donated in the last year), they will appear at the top
     * of the list, sorted by urgency, and they have priority over all other patients
     *
     * @return the List of sorted Requests
     */
    public List<Request> getUnsatisfiedRequests() {

        List<String> cnps = getCnpsOfActiveDonors();
        System.out.println("\n" + cnps + "\n");

        List<Request> donorRequests = requestRepository.getAll().stream()
                .filter(request -> !request.getIsSatisfied())
                .filter(request -> cnps.contains(request.getPatient().getIdCard().getCnp()))
                .sorted((r1,r2) -> -r1.getUrgency().compareTo(r2.getUrgency()))
                .collect(Collectors.toList());

        List<Request> normalPatientRequests = requestRepository.getAll().stream()
                .filter(request -> !request.getIsSatisfied())
                .filter(request -> !cnps.contains(request.getPatient().getIdCard().getCnp()))
                .sorted((r1,r2) -> -r1.getUrgency().compareTo(r2.getUrgency()))
                .collect(Collectors.toList());

        donorRequests.addAll(normalPatientRequests);

        return donorRequests;
    }

    /**
     * Function to get a Request by ID
     *
     * @param requestId -  the ID of the Request to be returned
     * @return the Request instance with the specified ID
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the Request with the specified ID is not
     * in the Repository
     */
    public Request getRequestById(Long requestId) {
        return requestRepository.getById(requestId);
    }

    /**
     * Function to fulfill a Request. It will set the isSatisfied field of the Request with the given ID as true
     * and will remove from the BloodComponentRepository the BloodComponents with the IDs contained within the three
     * Lists of BloodComponent IDs selected to satisfy the request.
     *
     * @param requestId - the ID of the Request to be satisfied
     * @param thrombocytes - the List of IDs of the selected BloodComponents ot type THROMBOCYTES
     * @param plasma - the List of IDs of the selected BloodComponents ot type PLASMA
     * @param redBloodCells - the List of IDS of the selected BloodComponents ot type RED_BLOOD_CELLS
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if no Request with the specified ID
     * is present in the Repository
     * @throws ServiceError if the requested number of components and the provided one do not match
     */
    public void fulfillRequest(Long requestId, List<Long> thrombocytes, List<Long> plasma, List<Long> redBloodCells) {
        Request request = requestRepository.getById(requestId);

        String err = "";

        if(thrombocytes.size() != request.getThrombocytesUnits())
            err += "Wrong number of thrombocytes units. The request was made for " + request.getThrombocytesUnits() +
                " units, and only " + thrombocytes.size() + " were selected.";
        if(plasma.size() != request.getPlasmaUnits())
            err += "Wrong number of plasma units. The request was made for " + request.getPlasmaUnits() +
                " units, and only " + plasma.size() + " were selected.";
        if( redBloodCells.size() != request.getRedBloodCellsUnits())
            err += "Wrong number of red blood cells units. The request was made for " + request.getRedBloodCellsUnits() +
                " units, and only " + redBloodCells.size() + " were selected.";

        if(!err.equals(""))
            throw new ServiceError(err);

        request.setIsSatisfied(true);
        requestRepository.update(request);

        thrombocytes.forEach(thrombocytesID -> bloodComponentRepository.remove(thrombocytesID));
        plasma.forEach(plasmaID -> bloodComponentRepository.remove(plasmaID));
        redBloodCells.forEach(redBloodCellsID -> bloodComponentRepository.remove(redBloodCellsID));

    }

    /**
     * Function to get all Requests submitted by a HealthWorker of type DOCTOR, sorted by the date when they
     * were submitted (i.e. most recent request at the top)
     *
     * @param healthWorkerUsername - the username of the DOCTOR for which to search for Requests
     * @return the sorted List of Requests
     * @throws ro.ubb.tjfblooddonation.exceptions.RepositoryException if the LoginInformation with the specified
     * healthWorkerUsername as ID is not in the Repository
     */
    public List<Request> getDoctorRequest(String healthWorkerUsername) {
        Person healthWorker = loginInformationRepository.getById(healthWorkerUsername).getPerson();
        return requestRepository.getAll().stream()
                 .filter(request -> request.getHealthWorker().getId().equals(healthWorker.getId()))
                .sorted(Comparator.comparing(Request::getRequestDate))
                .collect(Collectors.toList());
    }

    /**
     * Helper function that returns the list of CNPs of all active Donors,
     * i.e. Donors that donated in the last year
     *
     * @return the List of CNPs
     */
    private List<String> getCnpsOfActiveDonors() {
        return bloodRepository.getAll().stream()
                .filter(blood -> blood.getRecoltationDate().isAfter(LocalDate.now().minusYears(1)))
                .map(blood -> blood.getDonor().getIdCard().getCnp())
                .collect(Collectors.toList());
    }
}
