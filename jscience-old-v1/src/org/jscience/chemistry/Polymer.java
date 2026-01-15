package org.jscience.chemistry;

/**
 * The Polymer class represent molecules that are repeating many times.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//please note that proteins are polymers
//but also note we do not treat them as such
//subclasses should have the name of the Molecule followed by factory
//for example NylonFactory
public abstract class Polymer extends Object {
    /** DOCUMENT ME! */
    private Molecule repeatingPart;

/**
     * Creates a new Polymer object.
     *
     * @param repeatingPart DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Polymer(Molecule repeatingPart) {
        if (repeatingPart != null) {
            this.repeatingPart = repeatingPart;
        } else {
            throw new IllegalArgumentException(
                "The Polymer constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Molecule getRepeatingPart() {
        return repeatingPart;
    }

    //uses the repeating part and the polymerisation degree to produce any polymer.
    /**
     * DOCUMENT ME!
     *
     * @param polymerisationDegree DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Molecule getMolecule(int polymerisationDegree);
}
