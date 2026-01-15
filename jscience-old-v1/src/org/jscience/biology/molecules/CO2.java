package org.jscience.biology.molecules;

import org.jscience.chemistry.*;


/**
 * A class representing the carbon dioxide (CO2).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class CO2 extends Molecule {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Atom carbonAtom;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private static Bond bond1;

    /** DOCUMENT ME! */
    private static Bond bond2;

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        oxygenElement = PeriodicTable.getElement("Oxygen");

        carbonAtom = new Atom(carbonElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);

        bond1 = new Bond(carbonAtom, oxygenAtom1, Bond.DOUBLE);
        bond2 = new Bond(carbonAtom, oxygenAtom2, Bond.DOUBLE);
    }

/**
     * Creates a new CO2 object.
     */
    public CO2() {
        super(carbonAtom);
    }
}
