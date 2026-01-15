package org.jscience.biology.molecules;

import org.jscience.chemistry.*;


/**
 * A class representing the dioxygen (O2).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class O2 extends Molecule {
    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private static Bond bond1;

    static {
        oxygenElement = PeriodicTable.getElement("Oxygen");

        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);

        bond1 = new Bond(oxygenAtom1, oxygenAtom2, Bond.DOUBLE);
    }

/**
     * Creates a new O2 object.
     */
    public O2() {
        super(oxygenAtom1);
    }
}
