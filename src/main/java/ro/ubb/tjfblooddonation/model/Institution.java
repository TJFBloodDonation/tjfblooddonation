package ro.ubb.tjfblooddonation.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Institution extends BaseEntity {
    private String name;
    private String type;
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    public enum types {
        CLINIC, HOSPITAL, LABORATORY
    }

    /**
     * All args constructor ( removed @AllArgsConstructor annotation from class
     * so as to set the id when instance is created)
     */
    @Builder
    public Institution(String name, types type, Address address) {
        this.name = name;
        this.address = address;

        this.type = typeToString(type);
    }

    public static types stringToType(String s){
        switch (s){
            case "clinic":
                return types.CLINIC;
            case "hospital":
                return types.HOSPITAL;
            case "laboratory":
                return types.LABORATORY;
            default:
                return null;
        }
    }
    public static String typeToString(types type){
        switch (type) {
            case CLINIC:
                return "clinic";
            case HOSPITAL:
                return "hospital";
            case LABORATORY:
                return "laboratory";
        }
        return null;
    }
}
