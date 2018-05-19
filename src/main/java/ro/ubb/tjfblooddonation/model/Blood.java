package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Blood extends BaseEntity{
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    private Donor donor;
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    private Institution institution;
    private LocalDate recoltationDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private Analysis analysis;
    private boolean isSeparated;

    /** Constructor without Analysis, since when the blood enters the system,
     *  the analysis is not yet completed
     */
    @Builder
    public Blood(Donor donor, Institution institution, LocalDate recoltationDate){
        this.donor = donor;
        this.institution = institution;
        this.recoltationDate = recoltationDate;
    }
}
