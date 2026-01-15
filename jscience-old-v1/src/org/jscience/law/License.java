package org.jscience.law;

import org.jscience.biology.human.Human;

import org.jscience.economics.Organization;
import org.jscience.economics.Property;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import org.jscience.sociology.Person;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing a piece of paper by an authority that gives someone
 * the right to do something. This includes birth certificate, driving
 * licence, ID, diplomas...
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//there is no set method because this is meant to be a paper that you can't fill or modify
//there is no date or place where it was established (although the authority and identification give a hint on this) but you can subclass this class
//computer licences, for example GPL, should be found in org.jscience.sociology.License, not in org.jscience.law.Contract
//although you can have a different idea on this
public class License extends Object implements Property, Identified {
    /** DOCUMENT ME! */
    private Human owner;

    /** DOCUMENT ME! */
    private Organization authority;

    /** DOCUMENT ME! */
    private Identification identification;

    /** DOCUMENT ME! */
    private Vector rights;

/**
     * Creates a new License object.
     *
     * @param owner          DOCUMENT ME!
     * @param authority      DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param right          DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public License(Human owner, Organization authority,
        Identification identification, String right) {
        Vector strings;

        if ((owner != null) && (authority != null) && (identification != null) &&
                (right != null) && (right.length() > 0)) {
            this.owner = owner;
            this.authority = authority;
            this.identification = identification;
            strings = new Vector();
            strings.add(right);
            this.rights = strings;
        } else {
            throw new IllegalArgumentException(
                "The Licence constructor can't have null or empty arguments.");
        }
    }

/**
     * Creates a new License object.
     *
     * @param person         DOCUMENT ME!
     * @param authority      DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param rights         DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public License(Person person, Organization authority,
        Identification identification, Vector rights) {
        Iterator iterator;
        boolean valid;

        if ((person != null) && (authority != null) &&
                (identification != null) && (rights != null) &&
                (rights.size() > 0)) {
            iterator = rights.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.owner = person;
                this.authority = authority;
                this.identification = identification;
                this.rights = rights;
            } else {
                throw new IllegalArgumentException(
                    "The rights Vector must contain only Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Licence constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOwners() {
        HashSet result;

        result = new HashSet();
        result.add(owner);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Organization getAuthority() {
        return authority;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getRights() {
        return rights;
    }

    //unless otherwise stated (by a subclass) the actual value is 0
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getValue() {
        return Amount.valueOf(0, Currency.USD);
    }
}
