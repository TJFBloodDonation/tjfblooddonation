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
        Parent root = loader.load("/fxml/logIn.fxml");
        primaryStage.setTitle("Blood donation!");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.show();
        //addDonor();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void addDonor() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");
        UsersService usersService = context.getBean(UsersService.class);
        usersService.getAllDonors().forEach(System.out::println);
        System.out.println("----------------------------------");
        Address a = new Address("a", "b", "c", "d");
        Address b = new Address("a", "b", "c", "d");
        IdCard idCard = new IdCard(a, "cnp");
        Donor d = Donor.builder()
                .bloodType("0")
                .dateOfBirth(Date.valueOf("2000-12-12"))
                .gender("male")
                .idCard(idCard)
                .residence(b)
                .rH("positive")
                .build();
        d.setEmail("fjks@fdjlk.com");
        d.setPhoneNumber("jfdk");
        d.setFirstName("firts");
        d.setLastName("last");
        usersService.addDonor(d);
        usersService.getAllDonors().forEach(System.out::println);
    }
}
