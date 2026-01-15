package org.jscience.biology.molecules.carbohydrates;

import org.jscience.chemistry.*;


/**
 * A class representing the Deoxyribose carbohydrate molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//as a ring shape, in solution
public final class Deoxyribose extends Molecule {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

    /** DOCUMENT ME! */
    private static Atom carbonAtom1;

    /** DOCUMENT ME! */
    private static Atom carbonAtom2;

    /** DOCUMENT ME! */
    private static Atom carbonAtom3;

    /** DOCUMENT ME! */
    private static Atom carbonAtom4;

    /** DOCUMENT ME! */
    private static Atom carbonAtom5;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom3;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom4;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom1;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom2;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom3;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom4;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom5;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom6;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom7;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom8;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom9;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom10;

    /** DOCUMENT ME! */
    private static Bond bond1;

    /** DOCUMENT ME! */
    private static Bond bond2;

    /** DOCUMENT ME! */
    private static Bond bond3;

    /** DOCUMENT ME! */
    private static Bond bond4;

    /** DOCUMENT ME! */
    private static Bond bond5;

    /** DOCUMENT ME! */
    private static Bond bond6;

    /** DOCUMENT ME! */
    private static Bond bond7;

    /** DOCUMENT ME! */
    private static Bond bond8;

    /** DOCUMENT ME! */
    private static Bond bond9;

    /** DOCUMENT ME! */
    private static Bond bond10;

    /** DOCUMENT ME! */
    private static Bond bond11;

    /** DOCUMENT ME! */
    private static Bond bond12;

    /** DOCUMENT ME! */
    private static Bond bond13;

    /** DOCUMENT ME! */
    private static Bond bond14;

    /** DOCUMENT ME! */
    private static Bond bond15;

    /** DOCUMENT ME! */
    private static Bond bond16;

    /** DOCUMENT ME! */
    private static Bond bond17;

    /** DOCUMENT ME! */
    private static Bond bond18;

    /** DOCUMENT ME! */
    private static Bond bond19;

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        oxygenElement = PeriodicTable.getElement("Oxygen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        carbonAtom3 = new Atom(carbonElement);
        carbonAtom4 = new Atom(carbonElement);
        carbonAtom5 = new Atom(carbonElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);
        oxygenAtom3 = new Atom(oxygenElement);
        oxygenAtom4 = new Atom(oxygenElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        hydrogenAtom3 = new Atom(hydrogenElement);
        hydrogenAtom4 = new Atom(hydrogenElement);
        hydrogenAtom5 = new Atom(hydrogenElement);
        hydrogenAtom6 = new Atom(hydrogenElement);
        hydrogenAtom7 = new Atom(hydrogenElement);
        hydrogenAtom8 = new Atom(hydrogenElement);
        hydrogenAtom9 = new Atom(hydrogenElement);
        hydrogenAtom10 = new Atom(hydrogenElement);

        bond1 = new Bond(carbonAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, oxygenAtom1);
        bond3 = new Bond(oxygenAtom1, hydrogenAtom2);
        bond4 = new Bond(carbonAtom1, carbonAtom2);
        bond5 = new Bond(carbonAtom2, hydrogenAtom3);
        bond6 = new Bond(carbonAtom2, hydrogenAtom4);
        bond7 = new Bond(carbonAtom2, carbonAtom3);
        bond8 = new Bond(carbonAtom3, oxygenAtom2);
        bond9 = new Bond(oxygenAtom2, hydrogenAtom5);
        bond10 = new Bond(carbonAtom3, hydrogenAtom6);
        bond11 = new Bond(carbonAtom3, carbonAtom4);
        bond12 = new Bond(carbonAtom4, hydrogenAtom7);
        bond13 = new Bond(carbonAtom4, carbonAtom5);
        bond14 = new Bond(carbonAtom5, hydrogenAtom8);
        bond15 = new Bond(carbonAtom5, hydrogenAtom9);
        bond16 = new Bond(carbonAtom5, oxygenAtom3);
        bond17 = new Bond(oxygenAtom3, hydrogenAtom10);
        bond18 = new Bond(carbonAtom4, oxygenAtom4);
        bond19 = new Bond(oxygenAtom4, carbonAtom1);
    }

/**
     * Creates a new Deoxyribose object.
     */
    public Deoxyribose() {
        super(carbonAtom1);
    }
}
