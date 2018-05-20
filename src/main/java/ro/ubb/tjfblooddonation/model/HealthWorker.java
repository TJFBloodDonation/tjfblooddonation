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
public class HealthWorker extends Person{
    private String type;
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    private Institution institution;

    public enum types {
        ADMIN, CLINIC_STAFF, DOCTOR, BLOOD_ANALYST
    }

    @Builder
    public HealthWorker(String firstName, String lastName, String email, String phoneNumber,
                        types type, Institution institution) {
        super(firstName, lastName, email, phoneNumber);
        this.institution = institution;

        switch (type) {
            case ADMIN:
                this.type = "admin";
                break;
            case CLINIC_STAFF:
                this.type = "clinicStaff";
                break;
            case DOCTOR:
                this.type = "doctor";
                break;
            case BLOOD_ANALYST:
                this.type = "bloodAnalyst";
        }
    }
}
