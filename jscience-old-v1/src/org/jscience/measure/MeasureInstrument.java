package org.jscience.measure;

import org.jscience.geography.Place;

import org.jscience.util.Named;
import org.jscience.util.Positioned;


/**
 * The MeasureInstrument class is the base class for physical instruments.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class MeasureInstrument implements Named, Identified, Positioned {
    /** DOCUMENT ME! */
    private String name; //name of test

    /** DOCUMENT ME! */
    private Identification identification; //unique identification value

    /** DOCUMENT ME! */
    private Place place; //where this instrument is located

/**
     * Creates a new MeasureInstrument object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public MeasureInstrument(String name, Identification identification,
        Place place) {
        if ((name != null) && (name.length() > 0) && (identification != null) &&
                (place != null)) {
            this.name = name;
            this.identification = identification;
            this.place = place;
        } else {
            throw new IllegalArgumentException(
                "The MeasureInstrument constructor can't have null arguments (and name can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
    }
}
