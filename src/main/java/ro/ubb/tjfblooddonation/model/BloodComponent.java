package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class BloodComponent extends BaseEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    private Blood blood;
    private types type;

    public enum types {
        THROMBOCYTES, PLASMA, RED_BLOOD_CELLS
    }

    @Builder
    public BloodComponent(Blood blood, types type){
        this.blood = blood;
        this.type = type;
    }
}
