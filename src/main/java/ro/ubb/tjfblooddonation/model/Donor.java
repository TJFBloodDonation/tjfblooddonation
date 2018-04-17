package ro.ubb.tjfblooddonation.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Date;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Donor extends Person {
    private String bloodType;
    private String rH;
    @OneToOne(cascade = CascadeType.ALL)
    private Address residence;
    private Date dateOfBirth;
    @OneToOne(cascade = CascadeType.ALL)
    private IdCard idCard;
    private String gender;


}
