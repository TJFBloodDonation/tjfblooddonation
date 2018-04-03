package ro.ubb.model;

import java.util.Objects;

public class RedBloodCells {
    private String componentId;
    private String bloodId;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

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
        RedBloodCells that = (RedBloodCells) o;
        return Objects.equals(componentId, that.componentId) &&
                Objects.equals(bloodId, that.bloodId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(componentId, bloodId);
    }
}
