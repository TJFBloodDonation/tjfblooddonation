package ro.ubb.tjfblooddonation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address implements Serializable {
    @Id
    @GeneratedValue
    private Long addressId;
    private String country;
    private String region;
    private String city;
    private String street;
}
