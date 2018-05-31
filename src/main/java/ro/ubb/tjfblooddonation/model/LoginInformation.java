package ro.ubb.tjfblooddonation.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ro.ubb.tjfblooddonation.utils.Hashing;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Data
public class LoginInformation implements IdClass<String> {
    @Id
    private String username;
    private String password;
    @ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    private Person person;

    @Builder
    public LoginInformation(String username, String password, Person person) {
        this.username = username;
        this.password = Hashing.hash(password);
        this.person = person;
    }

    @Override

    public String getId() {
        return username;
    }
}
