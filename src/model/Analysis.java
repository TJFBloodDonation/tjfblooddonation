package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Analysis {
    private long analysisId;
    private Boolean hiv;
    private Boolean hb;
    private Boolean hcv;
    private Boolean sifilis;
    private Boolean htlv;
    private Boolean alt;

    @Id
    @Column(name = "analysisId", nullable = false)
    public long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(long analysisId) {
        this.analysisId = analysisId;
    }

    @Basic
    @Column(name = "hiv", nullable = true)
    public Boolean getHiv() {
        return hiv;
    }

    public void setHiv(Boolean hiv) {
        this.hiv = hiv;
    }

    @Basic
    @Column(name = "hb", nullable = true)
    public Boolean getHb() {
        return hb;
    }

    public void setHb(Boolean hb) {
        this.hb = hb;
    }

    @Basic
    @Column(name = "hcv", nullable = true)
    public Boolean getHcv() {
        return hcv;
    }

    public void setHcv(Boolean hcv) {
        this.hcv = hcv;
    }

    @Basic
    @Column(name = "sifilis", nullable = true)
    public Boolean getSifilis() {
        return sifilis;
    }

    public void setSifilis(Boolean sifilis) {
        this.sifilis = sifilis;
    }

    @Basic
    @Column(name = "htlv", nullable = true)
    public Boolean getHtlv() {
        return htlv;
    }

    public void setHtlv(Boolean htlv) {
        this.htlv = htlv;
    }

    @Basic
    @Column(name = "alt", nullable = true)
    public Boolean getAlt() {
        return alt;
    }

    public void setAlt(Boolean alt) {
        this.alt = alt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Analysis analysis = (Analysis) o;
        return analysisId == analysis.analysisId &&
                Objects.equals(hiv, analysis.hiv) &&
                Objects.equals(hb, analysis.hb) &&
                Objects.equals(hcv, analysis.hcv) &&
                Objects.equals(sifilis, analysis.sifilis) &&
                Objects.equals(htlv, analysis.htlv) &&
                Objects.equals(alt, analysis.alt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(analysisId, hiv, hb, hcv, sifilis, htlv, alt);
    }
}
