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
    private Long analysisId;
    private Boolean hiv;
    private Boolean hb;
    private Boolean hcv;
    private Boolean sifilis;
    private Boolean htlv;
    private Boolean alt;
    private Boolean imunoHematology;

    public Analysis(Boolean hiv, Boolean hb, Boolean hcv, Boolean sifilis, Boolean htlv, Boolean alt, Boolean imunoHematology) {
        this.hiv = hiv;
        this.hb = hb;
        this.hcv = hcv;
        this.sifilis = sifilis;
        this.htlv = htlv;
        this.alt = alt;
        this.imunoHematology = imunoHematology;
    }
}
