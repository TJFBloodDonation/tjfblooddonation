package ro.ubb.tjfblooddonation.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Institution extends BaseEntity {
    private String name;
    private String type;
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    public enum types {
        CLINIC, HOSPITAL, LABORATORY
    }

    /**
     * All args constructor ( removed @AllArgsConstructor annotation from class
     * so as to set the id when instance is created)
     */
    @Builder
    public Institution(String name, types type, Address address) {
        this.name = name;
        this.address = address;

        switch (type) {
            case CLINIC:
                this.type = "clinic";
                break;
            case HOSPITAL:
                this.type = "hospital";
                break;
            case LABORATORY:
                this.type = "laboratory";
                break;
        }
    }
}
