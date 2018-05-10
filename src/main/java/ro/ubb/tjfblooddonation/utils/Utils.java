package ro.ubb.tjfblooddonation.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubb.tjfblooddonation.repository.DonorRepository;

@Component
public class Utils {
    @Autowired
    private static DonorRepository donorRepository;

    public static Long getLastDonorID() {
        return donorRepository.getAll().stream().mapToLong( entity -> Long.valueOf(entity.getId().replaceAll("\\D+",""))).max().getAsLong();
    }
}
