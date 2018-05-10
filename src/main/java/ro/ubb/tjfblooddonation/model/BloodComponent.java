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
public class BloodComponent extends BaseEntity<String>{
    @ManyToOne(cascade = CascadeType.ALL)
    private Blood blood;
    private String type;
    /** static field for id "counter" */
    private static Long idCount = 0L;

    @Builder
    public BloodComponent(Blood blood, String componentType){
        this.blood = blood;
        this.type = componentType;

        // read the idCount from a file or the DB in order to preserve
        // the already used values and avoid conflicts
        //idCount = ;

        this.setId(generateId());
    }

    @Override
    protected String generateId() {
        int zeros = String.valueOf(Long.MAX_VALUE).length() - String.valueOf(idCount).length();
        //persist idCard changes
        switch (this.type) {
            case "thrombocytes":
                return "TRO" + String.format("%0" + zeros + "d", idCount++);
            case "red blood cells":
                return "RBC" + String.format("%0" + zeros + "d", idCount++);
            case "plasma":
                return "PLA" + String.format("%0" + zeros + "d", idCount++);
            default:
                return null;
        }
    }
}
