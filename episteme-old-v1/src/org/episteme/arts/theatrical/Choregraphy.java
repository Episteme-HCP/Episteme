package org.episteme.arts.theatrical;

import org.episteme.economics.Community;
import org.episteme.economics.money.Currency;

import org.episteme.measure.Amount;
import org.episteme.measure.Identification;

import org.episteme.arts.ArtsConstants;
import org.episteme.arts.Artwork;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing the ordered poses to be adopted by the actors of a
 * play.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Choregraphy extends Artwork {
    /** DOCUMENT ME! */
    private Vector steps;

    //a vector of String
    /**
     * Creates a new Choregraphy object.
     *
     * @param name DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param producer DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param authors DOCUMENT ME!
     * @param steps DOCUMENT ME!
     */
    public Choregraphy(String name, String description, Community producer,
        Date productionDate, Identification identification, Set authors,
        Vector steps) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.DANCE);

        Iterator iterator;
        boolean valid;

        if ((steps != null) && (steps.size() > 0)) {
            iterator = steps.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof String;
            }

            if (valid) {
                this.steps = steps;
            } else {
                throw new IllegalArgumentException(
                    "The Vector can consist only of Strings.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Vector of steps must be not null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getSteps() {
        return steps;
    }
}
