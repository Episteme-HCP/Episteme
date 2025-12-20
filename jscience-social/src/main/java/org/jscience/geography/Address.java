/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.geography;

import org.jscience.politics.Country;

/**
 * Represents a postal/physical address.
 * <p>
 * Modernized from v1 with support for international address formats.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Address {

    private final String addressee; // Person or company name
    private final String streetAddress; // Street number and name
    private final String addressLine2; // Apt, Suite, Floor, etc.
    private final String city;
    private final String stateProvince; // State, Province, Region
    private final String postalCode; // ZIP, Postcode
    private final Country country;
    private String phoneNumber;
    private Coordinate location; // GPS coordinates if known

    public Address(String addressee, String streetAddress, String city,
            String postalCode, Country country) {
        this(addressee, streetAddress, null, city, null, postalCode, country);
    }

    public Address(String addressee, String streetAddress, String addressLine2,
            String city, String stateProvince, String postalCode, Country country) {
        this.addressee = addressee;
        this.streetAddress = streetAddress;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.stateProvince = stateProvince;
        this.postalCode = postalCode;
        this.country = country;
    }

    // Getters
    public String getAddressee() {
        return addressee;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Coordinate getLocation() {
        return location;
    }

    // Setters
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    /**
     * Returns formatted address for postal mail.
     */
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        if (addressee != null)
            sb.append(addressee).append("\n");
        if (streetAddress != null)
            sb.append(streetAddress).append("\n");
        if (addressLine2 != null)
            sb.append(addressLine2).append("\n");

        // City, state zip format (US style)
        if (city != null)
            sb.append(city);
        if (stateProvince != null)
            sb.append(", ").append(stateProvince);
        if (postalCode != null)
            sb.append(" ").append(postalCode);
        sb.append("\n");

        if (country != null)
            sb.append(country.getName());
        return sb.toString().trim();
    }

    /**
     * Returns single-line address.
     */
    public String getOneLiner() {
        StringBuilder sb = new StringBuilder();
        if (streetAddress != null)
            sb.append(streetAddress);
        if (city != null)
            sb.append(", ").append(city);
        if (stateProvince != null)
            sb.append(", ").append(stateProvince);
        if (postalCode != null)
            sb.append(" ").append(postalCode);
        if (country != null)
            sb.append(", ").append(country.getName());
        return sb.toString();
    }

    @Override
    public String toString() {
        return getOneLiner();
    }

    // Factory methods for common addresses
    public static Address usAddress(String addressee, String street, String city,
            String state, String zip) {
        return new Address(addressee, street, null, city, state, zip, Country.USA);
    }
}
