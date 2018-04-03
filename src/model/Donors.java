package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Donors {
    private String donorId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String bloodType;
    private String rH;
    private Long residence;
    private Date dateOfBirth;
    private Long idCardId;
    private String gender;

    @Id
    @Column(name = "donorId", nullable = false, length = 10)
    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    @Basic
    @Column(name = "firstName", nullable = true, length = 100)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = true, length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phoneNumber", nullable = true, length = 20)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "bloodType", nullable = true, length = 3)
    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    @Basic
    @Column(name = "rH", nullable = true, length = 1)
    public String getrH() {
        return rH;
    }

    public void setrH(String rH) {
        this.rH = rH;
    }

    @Basic
    @Column(name = "residence", nullable = true)
    public Long getResidence() {
        return residence;
    }

    public void setResidence(Long residence) {
        this.residence = residence;
    }

    @Basic
    @Column(name = "dateOfBirth", nullable = true)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Basic
    @Column(name = "idCardId", nullable = true)
    public Long getIdCardId() {
        return idCardId;
    }

    public void setIdCardId(Long idCardId) {
        this.idCardId = idCardId;
    }

    @Basic
    @Column(name = "gender", nullable = true, length = 1)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donors donors = (Donors) o;
        return Objects.equals(donorId, donors.donorId) &&
                Objects.equals(firstName, donors.firstName) &&
                Objects.equals(lastName, donors.lastName) &&
                Objects.equals(email, donors.email) &&
                Objects.equals(phoneNumber, donors.phoneNumber) &&
                Objects.equals(bloodType, donors.bloodType) &&
                Objects.equals(rH, donors.rH) &&
                Objects.equals(residence, donors.residence) &&
                Objects.equals(dateOfBirth, donors.dateOfBirth) &&
                Objects.equals(idCardId, donors.idCardId) &&
                Objects.equals(gender, donors.gender);
    }

    @Override
    public int hashCode() {

        return Objects.hash(donorId, firstName, lastName, email, phoneNumber, bloodType, rH, residence, dateOfBirth, idCardId, gender);
    }
}
