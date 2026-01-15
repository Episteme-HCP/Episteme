package org.jscience.economics.resources;

import org.jscience.economics.Community;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;

import java.util.Date;


/**
 * A class representing something in nature.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a planet, a star or a sentient living
//subclasses could include solid, liquid along to mineral (which should subclass solid)
public class Natural extends Thing {
/**
     * Creates a new Natural object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param quantity        DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     */
    public Natural(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate) {
        super(name, description, amount, producer, productionPlace,
            productionDate);
    }
}
