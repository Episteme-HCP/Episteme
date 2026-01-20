package org.jscience.politics;

import org.jscience.util.Named;

import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a set of countries working together and may be with
 * no common boundaries.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Federation extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Set countries;

    //a Set of countries
    /**
     * Creates a new Federation object.
     *
     * @param name DOCUMENT ME!
     * @param countries DOCUMENT ME!
     */
    public Federation(String name, Set countries) {
        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (countries != null)) {
            iterator = countries.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Country;
            }

            if (valid) {
                this.name = name;
                this.countries = countries;
            } else {
                throw new IllegalArgumentException(
                    "The Set must contain only countries.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Federation constructor can't have null arguments (and name can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     */
    public void setName() {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCountries() {
        return countries;
    }

    /**
     * DOCUMENT ME!
     *
     * @param country DOCUMENT ME!
     */
    public void addCountry(Country country) {
        countries.add(country);
    }

    /**
     * DOCUMENT ME!
     *
     * @param country DOCUMENT ME!
     */
    public void removeCountry(Country country) {
        countries.remove(country);
    }
}
