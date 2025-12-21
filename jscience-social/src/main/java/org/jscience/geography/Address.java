/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.geography;

import org.jscience.politics.Country;

/**
 * Represents a postal/physical address.
 * <p>
 * Modernized from v1 with support for international address formats.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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