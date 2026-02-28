package org.episteme.economics.resources;

import org.episteme.economics.Community;

import org.episteme.earth.Place;

import org.episteme.measure.Amount;

import java.util.Date;


/**
 * A class representing Solids.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//stones, rocks, fossils not identified as such
public class Solid extends Natural {
/**
     * Creates a new Solid object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     */
    public Solid(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate) {
        super(name, description, amount, producer, productionPlace,
            productionDate);
    }
}
