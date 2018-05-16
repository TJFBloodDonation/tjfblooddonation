package ro.ubb.tjfblooddonation.model;


import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Date;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Donor extends Person {
    private String bloodType;
    private String rH;
    @ManyToOne(cascade = CascadeType.ALL)
    private Address residence;
    private Date dateOfBirth;
    @ManyToOne(cascade = CascadeType.ALL)
    private IdCard idCard;
    private String gender;
    @Nullable
    @ManyToOne(cascade = CascadeType.ALL)
    private Form form;
    private String message;

    /** Constructor that excludes the form, since it is completed after registration and implicitly
     * after the Donor instance is created and also skips message, 'cause I'm not exactly sure what it means
     * It excludes the bloodType and rh in case tha patient did not input the when registering
     */
    @Builder
    public Donor(String firstName, String lastName, String email, String phoneNumber,
                 Address residenceAddress, Date birthDate, IdCard idCard, String gender){
        super(firstName,lastName,email,phoneNumber);
        this.residence = residenceAddress;
        this.dateOfBirth = birthDate;
        this.idCard = idCard;
        this.gender = gender;
    }

    /** Constructor that excludes the form, since it is completed after registration and implicitly
     * after the Donor instance is created and also skips message, 'cause I'm not exactly sure what it means
     * It includes the bloodType and rh in case tha patient inputted them when registering
     */
    @Builder
    public Donor(String firstName, String lastName, String email, String phoneNumber, String bloodType,
                 String rh, Address residenceAddress, Date birthDate, IdCard idCard, String gender){
        super(firstName,lastName,email,phoneNumber);
        this.bloodType = bloodType;
        this.rH = rh;
        this.residence = residenceAddress;
        this.dateOfBirth = birthDate;
        this.idCard = idCard;
        this.gender = gender;

    }
}
