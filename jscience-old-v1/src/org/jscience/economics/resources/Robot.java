package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.economics.TaskProcessor;
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Date;


/**
 * A class representing robots.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//something that is built to serve humans but has some autonomy in its behavior
public abstract class Robot extends Machine implements TaskProcessor {
    //most robot move
    /**
     * Creates a new Robot object.
     *
     * @param name DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param amount DOCUMENT ME!
     * @param producer DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public Robot(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
    }
}
