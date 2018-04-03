package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class LoginInformation {
    private String userName;
    private String password;
    private String donerId;
    private String healthWorkerId;

    @Id
    @Column(name = "userName", nullable = false, length = 50)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "donerId", nullable = true, length = 10)
    public String getDonerId() {
        return donerId;
    }

    public void setDonerId(String donerId) {
        this.donerId = donerId;
    }

    @Basic
    @Column(name = "healthWorkerId", nullable = true, length = 10)
    public String getHealthWorkerId() {
        return healthWorkerId;
    }

    public void setHealthWorkerId(String healthWorkerId) {
        this.healthWorkerId = healthWorkerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginInformation that = (LoginInformation) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(password, that.password) &&
                Objects.equals(donerId, that.donerId) &&
                Objects.equals(healthWorkerId, that.healthWorkerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userName, password, donerId, healthWorkerId);
    }
}
