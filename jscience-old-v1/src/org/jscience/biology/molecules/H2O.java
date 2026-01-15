package org.jscience.biology.molecules;

import org.jscience.chemistry.*;


/**
 * A class representing the water molecule (H2O).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class H2O extends Molecule {
    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom1;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom2;

    /** DOCUMENT ME! */
    private static Bond bond1;

    /** DOCUMENT ME! */
    private static Bond bond2;

    static {
        oxygenElement = PeriodicTable.getElement("Oxygen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        oxygenAtom = new Atom(oxygenElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);

        bond1 = new Bond(oxygenAtom, hydrogenAtom1);
        bond2 = new Bond(oxygenAtom, hydrogenAtom2);
    }

/**
     * Creates a new H20 object.
     */
    public H2O() {
        super(oxygenAtom);
    }
}
