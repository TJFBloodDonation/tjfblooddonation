package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Requests {
    private String requestId;
    private String healthWorkerId;
    private String patientId;
    private Byte plasmaUnits;
    private Byte trombocitesUnits;
    private Byte redBloodCellsUnits;
    private Date requesrDate;
    private String urgency;
    private String status;

    @Id
    @Column(name = "requestId", nullable = false, length = 10)
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Basic
    @Column(name = "healthWorkerId", nullable = true, length = 10)
    public String getHealthWorkerId() {
        return healthWorkerId;
    }

    public void setHealthWorkerId(String healthWorkerId) {
        this.healthWorkerId = healthWorkerId;
    }

    @Basic
    @Column(name = "patientId", nullable = true, length = 10)
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @Basic
    @Column(name = "plasmaUnits", nullable = true)
    public Byte getPlasmaUnits() {
        return plasmaUnits;
    }

    public void setPlasmaUnits(Byte plasmaUnits) {
        this.plasmaUnits = plasmaUnits;
    }

    @Basic
    @Column(name = "trombocitesUnits", nullable = true)
    public Byte getTrombocitesUnits() {
        return trombocitesUnits;
    }

    public void setTrombocitesUnits(Byte trombocitesUnits) {
        this.trombocitesUnits = trombocitesUnits;
    }

    @Basic
    @Column(name = "redBloodCellsUnits", nullable = true)
    public Byte getRedBloodCellsUnits() {
        return redBloodCellsUnits;
    }

    public void setRedBloodCellsUnits(Byte redBloodCellsUnits) {
        this.redBloodCellsUnits = redBloodCellsUnits;
    }

    @Basic
    @Column(name = "requesrDate", nullable = true)
    public Date getRequesrDate() {
        return requesrDate;
    }

    public void setRequesrDate(Date requesrDate) {
        this.requesrDate = requesrDate;
    }

    @Basic
    @Column(name = "urgency", nullable = true, length = 7)
    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 15)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requests requests = (Requests) o;
        return Objects.equals(requestId, requests.requestId) &&
                Objects.equals(healthWorkerId, requests.healthWorkerId) &&
                Objects.equals(patientId, requests.patientId) &&
                Objects.equals(plasmaUnits, requests.plasmaUnits) &&
                Objects.equals(trombocitesUnits, requests.trombocitesUnits) &&
                Objects.equals(redBloodCellsUnits, requests.redBloodCellsUnits) &&
                Objects.equals(requesrDate, requests.requesrDate) &&
                Objects.equals(urgency, requests.urgency) &&
                Objects.equals(status, requests.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(requestId, healthWorkerId, patientId, plasmaUnits, trombocitesUnits, redBloodCellsUnits, requesrDate, urgency, status);
    }
}
