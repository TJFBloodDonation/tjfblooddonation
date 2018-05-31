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
        this.type = HealthWorker.typeToString(type);
    }

    public static String typeToString(types type){
        switch (type) {
            case ADMIN:
                return "admin";
            case CLINIC_STAFF:
                return "clinicStaff";
            case DOCTOR:
                return "doctor";
            case BLOOD_ANALYST:
                return "bloodAnalyst";
        }
        return null;
    }
    public static types stringToType(String s){
        switch (s){
            case "admin":
                return types.ADMIN;
            case "clinicStaff":
                return types.CLINIC_STAFF;
            case "doctor":
                return types.DOCTOR;
            case "bloodAnalyst":
                return types.BLOOD_ANALYST;
        }
        return null;
    }
}
