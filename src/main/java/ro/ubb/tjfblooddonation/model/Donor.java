package ro.ubb.tjfblooddonation.model;


import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Donor extends Person {
    private String bloodType;
    private String rH;
    @ManyToOne(cascade = CascadeType.ALL)
    private Address residence;
    private LocalDate dateOfBirth;
    @ManyToOne(cascade = CascadeType.ALL)
    private IdCard idCard;
    private String gender;
    @ManyToOne(cascade = CascadeType.ALL)
    private Form form;
    private String message;


    /** Constructor that excludes the form, since it is completed after registration and implicitly
     * after the Donor instance is created and also skips message, 'cause I'm not exactly sure what it means
     * It includes the bloodType and rh in case tha patient inputted them when registering
     */
    @Builder
    public Donor(String firstName, String lastName, String email, String phoneNumber, String bloodType,
                 String rH, Address residence, LocalDate dateOfBirth, IdCard idCard, String gender){
        super(firstName,lastName,email,phoneNumber);
        this.bloodType = bloodType;
        this.rH = rH;
        this.residence = residence;
        this.dateOfBirth = dateOfBirth;
        this.idCard = idCard;
        this.gender = gender;

    }
}
