package org.episteme.economics.resources;

import org.episteme.economics.Community;
import org.episteme.economics.money.Money;

import org.episteme.earth.Place;

import org.episteme.measure.Amount;
import org.episteme.measure.Identification;

import java.util.Date;


/**
 * A class representing wearable clothes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Clothes extends org.episteme.economics.resources.PhysicalObject {
/**
     * Creates a new Clothes object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param value           DOCUMENT ME!
     */
    public Clothes(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
    }
}
