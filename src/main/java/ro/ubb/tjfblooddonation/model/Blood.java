package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
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
    /** static field for id "counter" */
    private static Long idCount = 0L;

    /** Constructor without Analysis, since when the blood enters the system,
     *  the analysis is not yet completed
     */
    @Builder
    public Blood(Donor donor, Institution institution, Date recoltationDate){
        this.donor = donor;
        this.institution = institution;
        this.recoltationDate = recoltationDate;

        // read the idCount from a file or the DB in order to preserve
        // the already used values and avoid conflicts
        //idCount = ;

        this.setId(generateId());
    }

    @Override
    protected String generateId() {
        int zeros = String.valueOf(Long.MAX_VALUE).length() - String.valueOf(idCount).length();
        //persist idCard changes
        return "BLD" + String.format("%0" + zeros + "d", idCount++);
    }
}
