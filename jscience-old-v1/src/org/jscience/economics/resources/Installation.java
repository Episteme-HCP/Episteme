package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Date;


/**
 * A class representing an installation.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a structure build by humans, that modifies the landscape and that usually serve for a purpose
//a dam in concrete (not only a ground hill), a bridge, tunnel (not only the hole but the concrete)
//this also account for art (sculptures...)
public class Installation extends Artifact {
    //objects are meant to be sold and therefore have a price
    //yes, installations also in some way have a cost as they are not built by nature itself
    //but this cost is supported by the community, not by individuals, and is meaningless in terms of sale
    //as each one is art in some way
/**
     * Creates a new Installation object.
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
    public Installation(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
    }
}
