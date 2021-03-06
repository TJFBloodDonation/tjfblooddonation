package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Form implements Serializable{
    @Id
    @GeneratedValue
    private long formId;
    private Boolean passedDonateForm;
    private Timestamp timeCompletedDonateForm;
    private Boolean passedBasicCheckForm;
}
