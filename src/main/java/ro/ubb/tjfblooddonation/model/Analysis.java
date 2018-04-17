package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Analysis implements Serializable {
    @Id
    @GeneratedValue
    private long analysisId;
    private Boolean hiv;
    private Boolean hb;
    private Boolean hcv;
    private Boolean sifilis;
    private Boolean htlv;
    private Boolean alt;


}
