package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Blood {
    private String bloodId;
    private String donorId;
    private Long institutionId;
    private Date recoltationDate;
    private Long analysisId;
    private Boolean isSeparated;
    private String patientId;

    @Id
    @Column(name = "bloodId", nullable = false, length = 10)
    public String getBloodId() {
        return bloodId;
    }

    public void setBloodId(String bloodId) {
        this.bloodId = bloodId;
    }

    @Basic
    @Column(name = "donorId", nullable = true, length = 10)
    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    @Basic
    @Column(name = "institutionId", nullable = true)
    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    @Basic
    @Column(name = "recoltationDate", nullable = true)
    public Date getRecoltationDate() {
        return recoltationDate;
    }

    public void setRecoltationDate(Date recoltationDate) {
        this.recoltationDate = recoltationDate;
    }

    @Basic
    @Column(name = "analysisId", nullable = true)
    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    @Basic
    @Column(name = "isSeparated", nullable = true)
    public Boolean getSeparated() {
        return isSeparated;
    }

    public void setSeparated(Boolean separated) {
        isSeparated = separated;
    }

    @Basic
    @Column(name = "patientId", nullable = true, length = 10)
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blood blood = (Blood) o;
        return Objects.equals(bloodId, blood.bloodId) &&
                Objects.equals(donorId, blood.donorId) &&
                Objects.equals(institutionId, blood.institutionId) &&
                Objects.equals(recoltationDate, blood.recoltationDate) &&
                Objects.equals(analysisId, blood.analysisId) &&
                Objects.equals(isSeparated, blood.isSeparated) &&
                Objects.equals(patientId, blood.patientId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bloodId, donorId, institutionId, recoltationDate, analysisId, isSeparated, patientId);
    }
}
