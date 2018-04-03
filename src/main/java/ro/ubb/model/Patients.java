package ro.ubb.model;

import java.util.Objects;

public class Patients {
    private String patientId;
    private String firstName;
    private String lastName;
    private String email;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patients patients = (Patients) o;
        return Objects.equals(patientId, patients.patientId) &&
                Objects.equals(firstName, patients.firstName) &&
                Objects.equals(lastName, patients.lastName) &&
                Objects.equals(email, patients.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(patientId, firstName, lastName, email);
    }
}
