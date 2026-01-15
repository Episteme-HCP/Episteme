package org.jscience.geography;

import org.jscience.politics.Country;

import org.jscience.util.Named;
import org.jscience.util.Positioned;


/**
 * A class representing all the informations needed to reach a place.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this is the basic information
//in fact it happens that many more specific fields may be required (all highly place dependant)
//http://kropla.com/dialcode.htm for international phone country codes
public class Address extends Object implements Named, Positioned {
    /** DOCUMENT ME! */
    private String fullName; //for example the

    /** DOCUMENT ME! */
    private String company;

    /** DOCUMENT ME! */
    private String address1; //building...

    /** DOCUMENT ME! */
    private String address2; //street...

    /** DOCUMENT ME! */
    private String city;

    /** DOCUMENT ME! */
    private String state;

    /** DOCUMENT ME! */
    private String zipCode;

    /** DOCUMENT ME! */
    private Country country;

    /** DOCUMENT ME! */
    private String phoneNumber;

    /** DOCUMENT ME! */
    private Place place;

    //you have to build up a new object should any field in the constructor change
    /**
     * Creates a new Address object.
     *
     * @param fullName DOCUMENT ME!
     * @param address DOCUMENT ME!
     * @param city DOCUMENT ME!
     * @param zipCode DOCUMENT ME!
     * @param country DOCUMENT ME!
     */
    public Address(String fullName, String address, String city,
        String zipCode, Country country) {
        this(fullName, null, address, null, city, null, zipCode, country);
    }

/**
     * Creates a new Address object.
     *
     * @param fullName DOCUMENT ME!
     * @param address1 DOCUMENT ME!
     * @param address2 DOCUMENT ME!
     * @param city     DOCUMENT ME!
     * @param zipCode  DOCUMENT ME!
     * @param country  DOCUMENT ME!
     */
    public Address(String fullName, String address1, String address2,
        String city, String zipCode, Country country) {
        this(fullName, null, address1, address2, city, null, zipCode, country);
    }

/**
     * Creates a new Address object.
     *
     * @param fullName DOCUMENT ME!
     * @param address1 DOCUMENT ME!
     * @param address2 DOCUMENT ME!
     * @param city     DOCUMENT ME!
     * @param state    DOCUMENT ME!
     * @param zipCode  DOCUMENT ME!
     * @param country  DOCUMENT ME!
     */
    public Address(String fullName, String address1, String address2,
        String city, String state, String zipCode, Country country) {
        this(fullName, null, address1, address2, city, state, zipCode, country);
    }

/**
     * Creates a new Address object.
     *
     * @param fullName DOCUMENT ME!
     * @param company  DOCUMENT ME!
     * @param address1 DOCUMENT ME!
     * @param address2 DOCUMENT ME!
     * @param city     DOCUMENT ME!
     * @param state    DOCUMENT ME!
     * @param zipCode  DOCUMENT ME!
     * @param country  DOCUMENT ME!
     */
    public Address(String fullName, String company, String address1,
        String address2, String city, String state, String zipCode,
        Country country) {
        this.fullName = fullName;
        this.company = company;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return fullName;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCompany() {
        return company;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAddress1() {
        return address1;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAddress2() {
        return address2;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCity() {
        return city;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getState() {
        return state;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getZipCode() {
        return zipCode;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Country getCountry() {
        return country;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCountryName() {
        return country.getName();
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * DOCUMENT ME!
     *
     * @param phone DOCUMENT ME!
     */
    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
    }

    //usually set as a callback to a place
    /**
     * DOCUMENT ME!
     *
     * @param place DOCUMENT ME!
     */
    public void setPosition(Place place) {
        this.place = place;
    }
}
