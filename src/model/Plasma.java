package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Plasma {
    private String componentId;
    private String bloodId;

    @Basic
    @Column(name = "componentId", nullable = true, length = 10)
    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Basic
    @Column(name = "bloodId", nullable = true, length = 10)
    public String getBloodId() {
        return bloodId;
    }

    public void setBloodId(String bloodId) {
        this.bloodId = bloodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plasma plasma = (Plasma) o;
        return Objects.equals(componentId, plasma.componentId) &&
                Objects.equals(bloodId, plasma.bloodId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(componentId, bloodId);
    }
}
