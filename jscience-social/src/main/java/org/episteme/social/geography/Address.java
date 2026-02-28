/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.episteme.social.geography;

import org.episteme.natural.earth.Place;
import org.episteme.natural.earth.coordinates.EarthCoordinate;
import org.episteme.social.politics.Country;
import org.episteme.core.util.Named;
import org.episteme.core.util.Positioned;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a mailing or physical address for a location.
 * Links to a {@link Place} for precise geographical coordinates.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Address implements Named, Positioned<EarthCoordinate>, Serializable {

    private static final long serialVersionUID = 2L;

    @Attribute
    private String recipientName;

    @Attribute
    private String company;

    @Attribute
    private String streetLine1; // House number, street

    @Attribute
    private String streetLine2; // Suite, Apartment, Floor

    @Attribute
    private String city;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private org.episteme.social.politics.City cityObject;

    @Attribute
    private String stateProvince;

    @Attribute
    private String postalCode;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Country country;

    @Attribute
    private String phoneNumber;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Place place;

    public Address() {
    }

    public Address(String recipientName, String street, String city, String postalCode, Country country) {
        this.recipientName = recipientName;
        this.streetLine1 = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    /** Legacy constructor. */
    public Address(String street, org.episteme.social.politics.City city, String zipCode) {
        this("", street, city.getName(), zipCode, city.getCountry());
        this.cityObject = city;
    }

    @Override
    public String getName() {
        return recipientName;
    }

    public void setName(String name) {
        this.recipientName = name;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStreetLine1() {
        return streetLine1;
    }

    public void setStreetLine1(String streetLine1) {
        this.streetLine1 = streetLine1;
    }

    public String getStreetLine2() {
        return streetLine2;
    }

    public void setStreetLine2(String streetLine2) {
        this.streetLine2 = streetLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public EarthCoordinate getPosition() {
        return place != null ? place.getPosition() : null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (recipientName != null) sb.append(recipientName).append("\n");
        if (company != null) sb.append(company).append("\n");
        if (streetLine1 != null) sb.append(streetLine1).append("\n");
        if (streetLine2 != null) sb.append(streetLine2).append("\n");
        if (city != null) sb.append(city);
        if (stateProvince != null) sb.append(", ").append(stateProvince);
        if (postalCode != null) sb.append(" ").append(postalCode);
        if (country != null) sb.append("\n").append(country.getName());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address other)) return false;
        return Objects.equals(recipientName, other.recipientName) &&
               Objects.equals(streetLine1, other.streetLine1) &&
               Objects.equals(city, other.city) &&
               Objects.equals(postalCode, other.postalCode) &&
               Objects.equals(country, other.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientName, streetLine1, city, postalCode, country);
    }
}

