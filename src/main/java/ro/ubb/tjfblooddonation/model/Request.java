package ro.ubb.tjfblooddonation.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Request extends BaseEntity{
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    private HealthWorker healthWorker;
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    private Patient patient;
    private Byte plasmaUnits;
    private Byte thrombocytesUnits;
    private Byte redBloodCellsUnits;
    private LocalDate requestDate;
    private UrgencyLevel urgency;
    private String status;
    private Boolean isSatisfied;

    public enum UrgencyLevel {
        LOW, MEDIUM, HIGH
    }

    /**
     * Constructor without status
     */
    @Builder
    public Request(HealthWorker healthWorker, Patient patient, Byte plasmaUnits, Byte thrombocytesUnits,
                   Byte redBloodCellsUnits, LocalDate requestDate, UrgencyLevel urgency) {
        this.healthWorker = healthWorker;
        this.patient = patient;
        this.plasmaUnits = plasmaUnits;
        this.thrombocytesUnits = thrombocytesUnits;
        this.redBloodCellsUnits = redBloodCellsUnits;
        this.requestDate = requestDate;
        this.status = "processing";
        this.isSatisfied = false;
        this.urgency = urgency;
    }

    /**
     * All args constructor ( removed @AllArgsConstructor annotation from class
     * so as to set the id when instance is created)
     */
    @Builder
    public Request(HealthWorker healthWorker, Patient patient, Byte plasmaUnits, Byte thrombocytesUnits,
                   Byte redBloodCellsUnits, LocalDate requestDate, UrgencyLevel urgency, String status) {
        this.healthWorker = healthWorker;
        this.patient = patient;
        this.plasmaUnits = plasmaUnits;
        this.thrombocytesUnits = thrombocytesUnits;
        this.redBloodCellsUnits = redBloodCellsUnits;
        this.requestDate = requestDate;
        this.status = status;
        this.isSatisfied = false;
        this.urgency = urgency;
    }
}
