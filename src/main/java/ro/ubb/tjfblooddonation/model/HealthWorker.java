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

    @Builder
    public HealthWorker(String firstName, String lastName, String email, String phoneNumber,
                        String type, Institution institution) {
        super(firstName, lastName, email, phoneNumber);
        this.institution = institution;
        this.type = type;
    }
}
