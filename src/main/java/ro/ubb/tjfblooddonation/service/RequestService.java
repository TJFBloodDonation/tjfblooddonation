package ro.ubb.tjfblooddonation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.tjfblooddonation.model.Person;
import ro.ubb.tjfblooddonation.model.Request;
import ro.ubb.tjfblooddonation.repository.LoginInformationRepository;
import ro.ubb.tjfblooddonation.repository.RequestRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private LoginInformationRepository loginInformationRepository;

    public void addRequest(Request request) {
        requestRepository.add(request);
    }

    public List<Request> getUnsatisfiedRequests() {
        return requestRepository.getAll().stream()
                .filter(request -> !request.getIsSatisfied())
                .sorted((r1,r2) -> -r1.getUrgency().compareTo(r2.getUrgency()))
                .collect(Collectors.toList());
    }

    public Request getRequestById(Long requestId) {
        return requestRepository.getById(requestId);
    }

    public void fulfillRequest(Long requestId, List<Long> thrombocytes, List<Long> plasma, List<Long> redBloodCells) {
        return ;
    }

    public List<Request> getDoctorRequest(String healthWorkerUsername) {
        Person healthWorker = loginInformationRepository.getById(healthWorkerUsername).getPerson();
        return requestRepository.getAll().stream()
                .filter(request -> request.getHealthWorker().getId().equals(healthWorker.getId()))
                .sorted(Comparator.comparing(Request::getRequestDate))
                .collect(Collectors.toList());
    }
}
