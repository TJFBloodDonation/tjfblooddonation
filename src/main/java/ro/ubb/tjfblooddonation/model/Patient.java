package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

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
    /** static field for id "counter" */
    private static Long idCount = 0L;

    public Patient(String firstName, String lastName, String email, String phoneNumber,
                   Institution institution, IdCard idCard) {
        super(firstName, lastName, email, phoneNumber);
        this.institution = institution;
        this.idCard = idCard;

        // read the idCount from a file or the DB in order to preserve
        // the already used values and avoid conflicts
        //idCount = ;

        this.setId(generateId());
    }

    @Override
    protected String generateId() {
        int zeros = String.valueOf(Long.MAX_VALUE).length() - String.valueOf(idCount).length();
        //persist idCount changes
        return "PAT" + String.format("%0" + zeros + "d", idCount++);
    }
}
