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


}
