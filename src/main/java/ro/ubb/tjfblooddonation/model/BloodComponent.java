package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BloodComponent extends BaseEntity<String>{
    @ManyToOne(cascade = CascadeType.ALL)
    private Blood blood;
    private String type;


}
