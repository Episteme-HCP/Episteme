package org.jscience.arts.musical;

import org.jscience.economics.Community;
import org.jscience.economics.money.Currency;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import org.jscience.arts.ArtsConstants;
import org.jscience.arts.Artwork;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing a musical composition.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Composition extends Artwork {
    /** DOCUMENT ME! */
    private double key;

    /** DOCUMENT ME! */
    private double tempo;

    /** DOCUMENT ME! */
    private Vector notes;

/**
     * Creates a new Composition object.
     *
     * @param name           DOCUMENT ME!
     * @param description    DOCUMENT ME!
     * @param producer       DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param authors        DOCUMENT ME!
     * @param notes          DOCUMENT ME!
     */
    public Composition(String name, String description, Community producer,
        Date productionDate, Identification identification, Set authors,
        Vector notes) {
        this(name, description, producer, productionDate, identification,
            authors, ArtsConstants.C, 80, notes);
    }

    //a vector of notes
    /**
     * Creates a new Composition object.
     *
     * @param name DOCUMENT ME!
     * @param description DOCUMENT ME!
     * @param producer DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param authors DOCUMENT ME!
     * @param key DOCUMENT ME!
     * @param tempo DOCUMENT ME!
     * @param notes DOCUMENT ME!
     */
    public Composition(String name, String description, Community producer,
        Date productionDate, Identification identification, Set authors,
        double key, double tempo, Vector notes) {
        super(name, description, Amount.ZERO, producer, producer.getPosition(),
            productionDate, identification, Amount.valueOf(0, Currency.USD),
            authors, ArtsConstants.MUSIC);

        Iterator iterator;
        boolean valid;

        if ((notes != null) && (notes.size() > 0)) {
            if ((key > 0) && (tempo > 0)) {
                iterator = notes.iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Note;
                }

                if (valid) {
                    this.key = key;
                    this.tempo = tempo;
                    this.notes = notes;
                } else {
                    throw new IllegalArgumentException(
                        "The Vector can consist only of Notes.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The key and tempo must be greater or equal to 0.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Composition constructor can't have null arguments (or empty notes).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getKey() {
        return key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTempo() {
        return tempo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getNotes() {
        return notes;
    }

    //may be we should provide a method to check that two melodies are identical
    //we could also store the resulting piece of music using Clip
}
