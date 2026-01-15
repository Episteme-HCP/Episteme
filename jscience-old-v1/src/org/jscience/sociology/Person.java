package org.jscience.sociology;

import org.jscience.biology.human.Human;

import org.jscience.geography.Address;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an individual from the Human specie acting in the
 * modern society.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//see also http://www.oasis-open.org/committees/documents.php?wg_abbrev=humanmarkup for Human Markup Language
//A person should be given several roles at creation time: EconomicAgent, Patient, ResponsibleIndividual, Citizen
//A person is a human in a modern context
//also see VCard
public class Person extends Human {
    /** DOCUMENT ME! */
    private Address currentAddress;

    /** DOCUMENT ME! */
    private Set wives; //the people you are currently married with (not the ones you sleep with, use timelines, or the Place from the Address to have a hint about that)

    //the data used here should match that of the biography (if any) (there is no check made)
    /**
     * Creates a new Person object.
     *
     * @param givenName DOCUMENT ME!
     * @param lastName DOCUMENT ME!
     */
    public Person(String givenName, String lastName) {
        super(givenName, lastName);
        this.currentAddress = null;
        this.wives = Collections.EMPTY_SET;
    }

/**
     * Creates a new Person object.
     *
     * @param firstName   DOCUMENT ME!
     * @param lastName    DOCUMENT ME!
     * @param dateOfBirth DOCUMENT ME!
     */
    public Person(String firstName, String lastName, Date dateOfBirth) {
        super(firstName, lastName, dateOfBirth);
        this.currentAddress = null;
        this.wives = Collections.EMPTY_SET;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Address getCurrentAddress() {
        return currentAddress;
    }

    //the current address
    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     */
    public void setCurrentAddress(Address address) {
        this.currentAddress = address;
    }

    //returns the current wives
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getWives() {
        return wives;
    }

    //this also accounts for husbands
    /**
     * DOCUMENT ME!
     *
     * @param wife DOCUMENT ME!
     */
    public void addWife(Human wife) {
        if (wife != null) {
            wives.add(wife);
        } else {
            throw new IllegalArgumentException("You can't add a null wife.");
        }
    }

    //this also accounts for husbands
    /**
     * DOCUMENT ME!
     *
     * @param wife DOCUMENT ME!
     */
    public void removeWife(Human wife) {
        wives.remove(wife);
    }

    //this also accounts for husbands
    /**
     * DOCUMENT ME!
     *
     * @param wives DOCUMENT ME!
     */
    public void setWives(Set wives) {
        Iterator iterator;
        boolean valid;

        if (wives != null) {
            iterator = wives.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.wives = wives;
            } else {
                throw new IllegalArgumentException(
                    "The Set of wives should contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of wives shouldn't be null.");
        }
    }
}
