package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IdCard implements Serializable {
    @Id
    @GeneratedValue
    private Long idCardId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    private String cnp;

}
