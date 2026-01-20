package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Date;


/**
 * A class representing a crafted thing.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//an object or tool that can be moved around and used by humans, mostly things that do fit in a house (lamp, tv, clothes...)
//this also counts for objects that are to be moved and installed like a fence, a stop sign, a bench
//and even buildings like "prebuilt houses" but not temporary settlements as some tribes use that are buildings
//see http://www.vterrain.org/Culture/culture_class.html
//a dice is an object you roll and that "outputs" a value
public abstract class Object extends Artifact {
    //usually, objects have a serial number
    //were made by a company
    //have a waranty
    //are sold in shops
    //have a set up guide
/**
     * Creates a new Object object.
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
    public Object(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
    }
}
