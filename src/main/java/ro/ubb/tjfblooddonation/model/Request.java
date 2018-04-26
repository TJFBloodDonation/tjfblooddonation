package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Date;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Request extends BaseEntity<String>{
    @ManyToOne(cascade = CascadeType.ALL)
    private HealthWorker healthWorker;
    @ManyToOne(cascade = CascadeType.ALL)
    private Patient patient;
    private Byte plasmaUnits;
    private Byte trombocitesUnits;
    private Byte redBloodCellsUnits;
    private Date requestDate;
    private String urgency;
    private String status;



}
