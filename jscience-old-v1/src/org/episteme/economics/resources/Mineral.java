package org.episteme.economics.resources;

import org.episteme.economics.Community;

import org.episteme.earth.Place;

import org.episteme.measure.Amount;

import java.util.Date;


/**
 * A class representing Minerals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//stones, rocks, fossils not identified as such
//also see org.episteme.earth.SoilComposition
public class Mineral extends Solid {
/**
     * Creates a new Mineral object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     */
    public Mineral(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate) {
        super(name, description, amount, producer, productionPlace,
            productionDate);
    }
}
