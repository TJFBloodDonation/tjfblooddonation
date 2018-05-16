package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Blood extends BaseEntity{
    @ManyToOne(cascade = CascadeType.ALL)
    private Donor donor;
    @ManyToOne(cascade = CascadeType.ALL)
    private Institution institution;
    private Date recoltationDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private Analysis analysis;

    /** Constructor without Analysis, since when the blood enters the system,
     *  the analysis is not yet completed
     */
    @Builder
    public Blood(Donor donor, Institution institution, Date recoltationDate){
        this.donor = donor;
        this.institution = institution;
        this.recoltationDate = recoltationDate;
    }
}
