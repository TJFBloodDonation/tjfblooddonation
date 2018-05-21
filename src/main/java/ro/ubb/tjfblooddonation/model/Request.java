package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Request extends BaseEntity{
    @ManyToOne(cascade = CascadeType.ALL)
    private HealthWorker healthWorker;
    @ManyToOne(cascade = CascadeType.ALL)
    private Patient patient;
    private Byte plasmaUnits;
    private Byte thrombocytesUnits;
    private Byte redBloodCellsUnits;
    private Date requestDate;
    private Byte urgency;
    private String status;

    public enum urgencyLevels {
        LOW, MEDIUM, HIGH
    }

    /**
     * All args constructor ( removed @AllArgsConstructor annotation from class
     * so as to set the id when instance is created)
     */
    @Builder
    public Request(HealthWorker healthWorker, Patient patient, Byte plasmaUnits, Byte thrombocytesUnits,
                   Byte redBloodCellsUnits, Date requestDate, urgencyLevels urgency, String status) {
        this.healthWorker = healthWorker;
        this.patient = patient;
        this.plasmaUnits = plasmaUnits;
        this.thrombocytesUnits = thrombocytesUnits;
        this.redBloodCellsUnits = redBloodCellsUnits;
        this.requestDate = requestDate;
        this.status = status;

        switch (urgency) {
            case LOW:
                this.urgency = 1;
                break;
            case MEDIUM:
                this.urgency = 2;
                break;
            case HIGH:
                this.urgency = 3;
                break;
        }
    }
}
