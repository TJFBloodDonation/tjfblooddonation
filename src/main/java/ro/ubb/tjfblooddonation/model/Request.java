package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Date;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Request extends BaseEntity<String>{
    @ManyToOne(cascade = CascadeType.ALL)
    private HealthWorker healthWorker;
    @ManyToOne(cascade = CascadeType.ALL)
    private Patient patient;
    private Byte plasmaUnits;
    private Byte thrombocytesUnits;
    private Byte redBloodCellsUnits;
    private Date requestDate;
    private String urgency;
    private String status;
    /** static field for id "counter" */
    private static Long idCount = 0L;

    /**
     * All args constructor ( removed @AllArgsConstructor annotation from class
     * so as to set the id when instance is created)
     */
    @Builder
    public Request(HealthWorker healthWorker, Patient patient, Byte plasmaUnits, Byte thrombocytesUnits,
                   Byte redBloodCellsUnits, Date requestDate, String urgency, String status) {
        this.healthWorker = healthWorker;
        this.patient = patient;
        this.plasmaUnits = plasmaUnits;
        this.thrombocytesUnits = thrombocytesUnits;
        this.redBloodCellsUnits = redBloodCellsUnits;
        this.requestDate = requestDate;
        this.urgency = urgency;
        this.status = status;

        // read the idCount from a file or the DB in order to preserve
        // the already used values and avoid conflicts
        //idCount = ;

        this.setId(generateId());
    }

    @Override
    protected String generateId() {
        int zeros = String.valueOf(Long.MAX_VALUE).length() - String.valueOf(idCount).length();
        //persist idCount changes
        return "REQ" + String.format("%0" + zeros + "d", idCount++);
    }
}
