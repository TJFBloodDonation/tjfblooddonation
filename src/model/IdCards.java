package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class IdCards {
    private long idCardId;
    private Long addressId;
    private String cnp;

    @Id
    @Column(name = "idCardId", nullable = false)
    public long getIdCardId() {
        return idCardId;
    }

    public void setIdCardId(long idCardId) {
        this.idCardId = idCardId;
    }

    @Basic
    @Column(name = "addressId", nullable = true)
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Basic
    @Column(name = "cnp", nullable = true, length = 14)
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
