package ro.ubb.model;

import java.util.Objects;


public class Addresses {
    private long addressId;
    private String country;
    private String region;
    private String city;
    private String street;

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Addresses addresses = (Addresses) o;
        return addressId == addresses.addressId &&
                Objects.equals(country, addresses.country) &&
                Objects.equals(region, addresses.region) &&
                Objects.equals(city, addresses.city) &&
                Objects.equals(street, addresses.street);
    }

    @Override
    public int hashCode() {

        return Objects.hash(addressId, country, region, city, street);
    }
}
