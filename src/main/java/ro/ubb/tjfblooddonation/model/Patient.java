package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Patient extends Person{
    @ManyToOne(cascade = CascadeType.ALL)
    private Institution institution;
    @OneToOne(cascade = CascadeType.ALL)
    private IdCard idCard;


}
