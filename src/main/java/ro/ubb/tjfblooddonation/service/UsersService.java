package ro.ubb.tjfblooddonation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.repository.DonorRepository;

import java.util.List;
@Service
public class UsersService {
    @Autowired
    private DonorRepository donorRepository;

    public void addDonor(Donor d) {
        donorRepository.add(d);
    }

    public List<Donor> getAllDonors(){
        return donorRepository.getAll();
    }
}
