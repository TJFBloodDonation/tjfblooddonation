package ro.ubb.model;

import java.sql.Date;
import java.util.Objects;

public class Blood {
    private String bloodId;
    private String donorId;
    private Long institutionId;
    private Date recoltationDate;
    private Long analysisId;

    public Blood(String bloodId, String donorId, Long institutionId, Date recoltationDate, Long analysisId, Boolean isSeparated, String patientId) {
        this.bloodId = bloodId;
        this.donorId = donorId;
        this.institutionId = institutionId;
        this.recoltationDate = recoltationDate;
        this.analysisId = analysisId;
        this.isSeparated = isSeparated;
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return "Blood{" +
                "bloodId='" + bloodId + '\'' +
                ", donorId='" + donorId + '\'' +
                ", institutionId=" + institutionId +
                ", recoltationDate=" + recoltationDate +
                ", analysisId=" + analysisId +
                ", isSeparated=" + isSeparated +
                ", patientId='" + patientId + '\'' +
                '}';
    }

    public Blood() {
    }

    private Boolean isSeparated;
    private String patientId;

    public String getBloodId() {
        return bloodId;
    }

    public void setBloodId(String bloodId) {
        this.bloodId = bloodId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public Date getRecoltationDate() {
        return recoltationDate;
    }

    public void setRecoltationDate(Date recoltationDate) {
        this.recoltationDate = recoltationDate;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public Boolean getSeparated() {
        return isSeparated;
    }

    public void setSeparated(Boolean separated) {
        isSeparated = separated;
    }

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
