package org.jscience.biology.bases;

import org.jscience.biology.Base;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;


/**
 * A class representing the Cytosine Base.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//bonds to Guanine
public final class Cytosine extends Base {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element nitrogenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Atom carbonAtom1;

    /** DOCUMENT ME! */
    private static Atom carbonAtom2;

    /** DOCUMENT ME! */
    private static Atom carbonAtom3;

    /** DOCUMENT ME! */
    private static Atom carbonAtom4;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom1;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom2;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom3;

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
    private static Atom oxygenAtom;

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

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        nitrogenElement = PeriodicTable.getElement("Nitrogen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");
        oxygenElement = PeriodicTable.getElement("Oxygen");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        carbonAtom3 = new Atom(carbonElement);
        carbonAtom4 = new Atom(carbonElement);
        nitrogenAtom1 = new Atom(nitrogenElement);
        nitrogenAtom2 = new Atom(nitrogenElement);
        nitrogenAtom3 = new Atom(nitrogenElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        hydrogenAtom3 = new Atom(hydrogenElement);
        hydrogenAtom4 = new Atom(hydrogenElement);
        hydrogenAtom5 = new Atom(hydrogenElement);
        oxygenAtom = new Atom(oxygenElement);

        bond1 = new Bond(carbonAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, carbonAtom2, Bond.DOUBLE);
        bond3 = new Bond(carbonAtom2, hydrogenAtom2);
        bond4 = new Bond(carbonAtom2, nitrogenAtom1);
        bond5 = new Bond(nitrogenAtom1, hydrogenAtom3);
        bond6 = new Bond(nitrogenAtom1, carbonAtom3);
        bond7 = new Bond(carbonAtom3, oxygenAtom, Bond.DOUBLE);
        bond8 = new Bond(carbonAtom3, nitrogenAtom2);
        bond9 = new Bond(nitrogenAtom2, carbonAtom4, Bond.DOUBLE);
        bond10 = new Bond(carbonAtom4, carbonAtom1);
        bond11 = new Bond(carbonAtom4, nitrogenAtom3);
        bond12 = new Bond(nitrogenAtom3, hydrogenAtom4);
        bond13 = new Bond(nitrogenAtom3, hydrogenAtom5);
    }

/**
     * Constructs an Cytosine molecule.
     */
    public Cytosine() {
        super(carbonAtom1);
    }

    /**
     * Determines whether this is a purine or pyrimidine molecule.
     *
     * @return DOCUMENT ME!
     */
    public boolean isPurine() {
        return false;
    }
}
