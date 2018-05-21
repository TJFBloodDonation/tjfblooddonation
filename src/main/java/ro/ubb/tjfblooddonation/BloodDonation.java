package ro.ubb.tjfblooddonation;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.tjfblooddonation.model.Address;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.model.IdCard;
import ro.ubb.tjfblooddonation.repository.BloodRepository;
import ro.ubb.tjfblooddonation.repository.DonorRepository;
import ro.ubb.tjfblooddonation.service.UsersService;
import ro.ubb.tjfblooddonation.utils.SpringFxmlLoader;

import java.sql.Date;


public class BloodDonation extends Application {
    private static final SpringFxmlLoader loader = new SpringFxmlLoader();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = loader.load("/fxml/Admin.fxml");
        primaryStage.setTitle("Blood donation!");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.show();
        //addDonor();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void addDonor(){
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
        d.setId("DON000001");
        d.setEmail("fjks@fdjlk.com");
        d.setPhoneNumber("jfdk");
        d.setFirstName("firts");
        d.setLastName("last");
        usersService.addDonor(d);
        usersService.getAllDonors().forEach(System.out::println);
/*
        Utils u = context.getBean(Utils.class);

        System.out.println(u.getLastDonorID());
        */
    }

}
//class BloodDnation {
//    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");
//        DonorRepository donorRepository = context.getBean(DonorRepository.class);
//        BloodRepository bloodRepository = context.getBean(BloodRepository.class);
//        bloodRepository.getAll().forEach(System.out::println);
//        donorRepository.getAll().forEach(System.out::println);
//        testRepo(donorRepository);
//        System.out.println("Done!");
///*
//        UsersService usersService = context.getBean(UsersService.class);
//        usersService.getAllDonors().forEach(System.out::println);
//        System.out.println("----------------------------------");
//        /*Address a = new Address(1, "a", "b", "c", "d");
//        Address b = new Address(2, "a", "b", "c", "d");
//        IdCard idCard = new IdCard(1, a, "cnp");
//        Donor d = Donor.builder()
//                .bloodType("0")
//                .dateOfBirth(Date.valueOf("2000-12-12"))
//                .gender("male")
//                .idCard(idCard)
//                .residence(b)
//                .rH("positive")
//                .build();

//        d.setId("DON000001");
//        d.setEmail("fjks@fdjlk.com");
//        d.setPhoneNumber("jfdk");
//        d.setFirstName("firts");
//        d.setLastName("last");
//        usersService.addDonor(d);
//        usersService.getAllDonors().forEach(System.out::println);*/
///*
//        Utils u = context.getBean(Utils.class);
//
//        System.out.println(u.getLastDonorID());
//        */
//        //testRepo(context.getBean(DonorRepository.class));
//    }
//
//    private static void assertEquals(Object a, Object b){
//        if(!a.equals(b))
//            throw new RuntimeException("Error " + a.toString() + " is not equal to " + b.toString());
//    }
//
//    public static void testRepo(DonorRepository donorRepository){
//        Address address = new Address(2, "a", "b", "c", "d");
//        IdCard idCard = new IdCard(1, address, "cnp");
//        Donor donor = new Donor("a", "a", "e", "0", "AB", "positive", address,
//                Date.valueOf("1999-2-2"), idCard, "male");
//
//        try{
//            donorRepository.getById(donor.getId());
//            assert(false);
//        }catch (RuntimeException e){
//            assert(true);
//        }
//        donor = donorRepository.add(donor);
//        try{
//            donorRepository.add(donor);
//            assert(false);
//        } catch (RuntimeException e){
//            assert(true);
//        }
//        assertEquals(donor.getBloodType(), "AB");
//        assertEquals(donorRepository.getById(donor.getId()), donor);
//        donor.setBloodType("A");
//        donor = donorRepository.update(donor);
//        assertEquals(donorRepository.getById(donor.getId()), donor);
//        assertEquals(donor.getBloodType(), "A");
//        donorRepository.remove(donor.getId());
//        try{
//            donorRepository.getById(donor.getId());
//            assert(false);
//        }catch (RuntimeException e){
//            assert(true);
//        }
//
//    }
//}
