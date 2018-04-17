package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person extends BaseEntity<String>{
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
}
