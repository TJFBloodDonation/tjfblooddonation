package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Institution extends BaseEntity<String> {
    private String name;
    private String type;
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;
    /** static field for id "counter" */
    private static Long idCount = 0L;

    /**
     * All args constructor ( removed @AllArgsConstructor annotation from class
     * so as to set the id when instance is created)
     */
    @Builder
    public Institution(String name, String type, Address address) {
        this.name = name;
        this.type = type;
        this.address = address;

        // read the idCount from a file or the DB in order to preserve
        // the already used values and avoid conflicts
        //idCount = ;

        this.setId(generateId());
    }


    @Override
    protected String generateId() {
        int zeros = String.valueOf(Long.MAX_VALUE).length() - String.valueOf(idCount).length();
        //persist idCount changes
        return "INS" + String.format("%0" + zeros + "d", 0L);
    }
}
