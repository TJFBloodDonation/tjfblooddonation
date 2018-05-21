package ro.ubb.tjfblooddonation.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@EqualsAndHashCode
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginInformation implements IdClass<String> {
    @Id
    private String username;
    private String password;
    @ManyToOne//(cascade = CascadeType.ALL)
    private Person person;


    @Override
    public String getId() {
        return username;
    }
}
