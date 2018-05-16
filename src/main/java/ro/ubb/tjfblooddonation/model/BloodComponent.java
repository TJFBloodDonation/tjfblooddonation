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
    private String type;

    @Builder
    public BloodComponent(Blood blood, String componentType){
        this.blood = blood;
        this.type = componentType;
    }
}
