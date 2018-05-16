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
public class Institution extends BaseEntity {
    private String name;
    private String type;
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    /**
     * All args constructor ( removed @AllArgsConstructor annotation from class
     * so as to set the id when instance is created)
     */
    @Builder
    public Institution(String name, String type, Address address) {
        this.name = name;
        this.type = type;
        this.address = address;
    }
}
