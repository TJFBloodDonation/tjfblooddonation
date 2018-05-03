package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HealthWorker extends Person{
    private String type;
    @ManyToOne(cascade = CascadeType.ALL)
    private Institution institution;
    /** static field for id "counter" */
    private static Long idCount = 0L;

    @Builder
    public HealthWorker(String firstName, String lastName, String email, String phoneNumber,
                        String type, Institution institution) {
        super(firstName, lastName, email, phoneNumber);
        this.institution = institution;
        this.type = type;

        // read the idCount from a file or the DB in order to preserve
        // the already used values and avoid conflicts
        //idCount = ;

        this.setId(generateId());
    }

    @Override
    protected String generateId() {
        int zeros = String.valueOf(Long.MAX_VALUE).length() - String.valueOf(idCount).length();
        //persist idCount changes
        switch (this.type) {
            case "clinic staff":
                return "CST" + String.format("%0" + zeros + "d", idCount++);
            case "doctor":
                return "DTR" + String.format("%0" + zeros + "d", idCount++);
            case "blood analyst":
                return "ANL" + String.format("%0" + zeros + "d", idCount++);
            case "administrator":
                return "ADM" + String.format("%0" + (zeros - 1) + "d", 0L);
            default:
                return null;
        }
    }
}
