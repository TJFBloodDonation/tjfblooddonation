package ro.ubb.model;

import java.util.Objects;


public class Analysis {
    private long analysisId;
    private Boolean hiv;
    private Boolean hb;
    private Boolean hcv;
    private Boolean sifilis;
    private Boolean htlv;
    private Boolean alt;

    public long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(long analysisId) {
        this.analysisId = analysisId;
    }

    public Boolean getHiv() {
        return hiv;
    }

    public void setHiv(Boolean hiv) {
        this.hiv = hiv;
    }

    public Boolean getHb() {
        return hb;
    }

    public void setHb(Boolean hb) {
        this.hb = hb;
    }

    public Boolean getHcv() {
        return hcv;
    }

    public void setHcv(Boolean hcv) {
        this.hcv = hcv;
    }

    public Boolean getSifilis() {
        return sifilis;
    }

    public void setSifilis(Boolean sifilis) {
        this.sifilis = sifilis;
    }

    public Boolean getHtlv() {
        return htlv;
    }

    public void setHtlv(Boolean htlv) {
        this.htlv = htlv;
    }

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
