package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginInformation extends BaseEntity<String> {
    private String password;
    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;

    public String getUsername() {
        return super.id;
    }

    public void setUsername(String username) {
        super.id = username;
    }
}
