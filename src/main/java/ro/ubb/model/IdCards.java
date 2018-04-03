package ro.ubb.model;

import java.util.Objects;

public class IdCards {
    private long idCardId;
    private Long addressId;
    private String cnp;

    public long getIdCardId() {
        return idCardId;
    }

    public void setIdCardId(long idCardId) {
        this.idCardId = idCardId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdCards idCards = (IdCards) o;
        return idCardId == idCards.idCardId &&
                Objects.equals(addressId, idCards.addressId) &&
                Objects.equals(cnp, idCards.cnp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idCardId, addressId, cnp);
    }
}
