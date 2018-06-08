package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Form implements Serializable{
    @Id
    @GeneratedValue
    private Long formId;
    private Boolean passedDonateForm;
    private Timestamp timeCompletedDonateForm;
    private Boolean passedBasicCheckForm;
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    private Patient patient;
}
