package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Collections;
import java.util.Date;
import java.util.Set;


/**
 * A class representing an object which purpose is to be moved.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a train, boat, plane, bus, bicycle
//this can be also an automatic metro for example (nearly a robot my mostly an automatic vehicle)
//animals as vehicules are nevertheless Creatures
public abstract class Vehicle extends Machine implements Store {
    //the behavior should be used to follow at least a track or change own position
    //vehicle carry people or else they are robots
    //planes and trains (and boats) should have a realtime reader for GPS
    /** DOCUMENT ME! */
    private Set contents;

/**
     * Creates a new Vehicle object.
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
    public Vehicle(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
        this.contents = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getContents() {
        return contents;
    }

    /**
     * DOCUMENT ME!
     */
    public abstract void getIn();

    /**
     * DOCUMENT ME!
     */
    public abstract void getOut();
}
