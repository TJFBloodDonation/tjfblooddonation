package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Blood extends BaseEntity<String>{
    @ManyToOne(cascade = CascadeType.ALL)
    private Donor donor;
    @ManyToOne(cascade = CascadeType.ALL)
    private Institution institution;
    private Date recoltationDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private Analysis analysis;


}
