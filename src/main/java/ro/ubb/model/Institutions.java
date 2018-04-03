package ro.ubb.model;

import java.util.Objects;

public class Institutions {
    private long institutionId;
    private String name;
    private String type;
    private Long addressId;

    public long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(long institutionId) {
        this.institutionId = institutionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Institutions that = (Institutions) o;
        return institutionId == that.institutionId &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(addressId, that.addressId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(institutionId, name, type, addressId);
    }
}
