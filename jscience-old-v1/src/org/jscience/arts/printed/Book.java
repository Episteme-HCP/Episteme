package org.jscience.arts.printed;

import org.jscience.economics.Community;
import org.jscience.economics.money.Currency;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import org.jscience.arts.ArtsConstants;
import org.jscience.arts.Artwork;

import java.util.Date;
import java.util.Set;


/**
 * This is meant to be the basic text rather than a complete edition (with
 * pictures, and a specific way to present the text).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we could use the class from org.jscience.linguistics.Text to store the text (although this would be quite inefficient)
//you should use the Identification to store the ISBN or ISSN
public class Book extends Artwork {
    /** DOCUMENT ME! */
    private String text;

/**
     * Creates a new Book object.
     *
     * @param name           DOCUMENT ME!
     * @param description    DOCUMENT ME!
     * @param producer       DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param authors        DOCUMENT ME!
     */
    public Book(String name, String description, Community producer,
        Date productionDate, Identification identification, Set authors) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.LITERATURE);
        text = new String();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setText(String html) {
        if (html != null) {
            text = html;
        } else {
            throw new IllegalArgumentException("You can't set a null text.");
        }
    }
}
