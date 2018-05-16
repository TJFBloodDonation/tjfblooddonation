package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Patient extends Person{
    @ManyToOne(cascade = CascadeType.ALL)
    private Institution institution;
    @ManyToOne(cascade = CascadeType.ALL)
    private IdCard idCard;

    @Builder
    public Patient(String firstName, String lastName, String email, String phoneNumber,
                   Institution institution, IdCard idCard) {
        super(firstName, lastName, email, phoneNumber);
        this.institution = institution;
        this.idCard = idCard;
    }
}
