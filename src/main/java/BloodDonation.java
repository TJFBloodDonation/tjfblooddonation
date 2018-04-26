import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.tjfblooddonation.model.Address;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.model.IdCard;
import ro.ubb.tjfblooddonation.service.UsersService;

import java.sql.Date;

public class BloodDonation {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");
        UsersService usersService = context.getBean(UsersService.class);
        usersService.getAllDonors().forEach(System.out::println);
        System.out.println("----------------------------------");
        Address a = new Address(1, "a", "b", "c", "d");
        Address b = new Address(2, "a", "b", "c", "d");
        IdCard idCard = new IdCard(1, a, "cnp");
        Donor d = Donor.builder()
                .bloodType("0")
                .dateOfBirth(Date.valueOf("2000-12-12"))
                .gender("male")
                .idCard(idCard)
                .residence(b)
                .rH("positive")
                .build();
        d.setId("sebisivlad");
        d.setEmail("fjks@fdjlk.com");
        d.setPhoneNumber("jfdk");
        d.setFirstName("firts");
        d.setLastName("last");
        usersService.addDonor(d);
        usersService.getAllDonors().forEach(System.out::println);
    }
}
