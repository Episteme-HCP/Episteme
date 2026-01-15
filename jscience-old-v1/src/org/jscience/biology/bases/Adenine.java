package org.jscience.biology.bases;

import org.jscience.biology.Base;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;


/**
 * A class representing the Adenine Base.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//bonds to Thymine
public final class Adenine extends Base {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element nitrogenElement;

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
    private static Atom nitrogenAtom1;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom2;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom3;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom4;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom5;

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

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        nitrogenElement = PeriodicTable.getElement("Nitrogen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        carbonAtom3 = new Atom(carbonElement);
        carbonAtom4 = new Atom(carbonElement);
        carbonAtom5 = new Atom(carbonElement);
        nitrogenAtom1 = new Atom(nitrogenElement);
        nitrogenAtom2 = new Atom(nitrogenElement);
        nitrogenAtom3 = new Atom(nitrogenElement);
        nitrogenAtom4 = new Atom(nitrogenElement);
        nitrogenAtom5 = new Atom(nitrogenElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        hydrogenAtom3 = new Atom(hydrogenElement);
        hydrogenAtom4 = new Atom(hydrogenElement);
        hydrogenAtom5 = new Atom(hydrogenElement);

        bond1 = new Bond(carbonAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, nitrogenAtom1);
        bond3 = new Bond(nitrogenAtom1, hydrogenAtom2);
        bond4 = new Bond(nitrogenAtom1, carbonAtom2);
        bond5 = new Bond(carbonAtom2, carbonAtom3, Bond.DOUBLE);
        bond6 = new Bond(carbonAtom2, nitrogenAtom2);
        bond7 = new Bond(nitrogenAtom2, carbonAtom4, Bond.DOUBLE);
        bond8 = new Bond(carbonAtom4, hydrogenAtom3);
        bond9 = new Bond(carbonAtom4, nitrogenAtom3);
        bond10 = new Bond(nitrogenAtom3, carbonAtom5, Bond.DOUBLE);
        bond11 = new Bond(carbonAtom5, nitrogenAtom4);
        bond12 = new Bond(nitrogenAtom4, hydrogenAtom4);
        bond13 = new Bond(nitrogenAtom4, hydrogenAtom5);
        bond14 = new Bond(carbonAtom5, carbonAtom3);
        bond15 = new Bond(carbonAtom3, nitrogenAtom5);
        bond16 = new Bond(nitrogenAtom5, carbonAtom1, Bond.DOUBLE);
    }

/**
     * Constructs an Adenine molecule.
     */
    public Adenine() {
        super(carbonAtom1);
    }

    /**
     * Determines whether this is a purine or pyrimidine molecule.
     *
     * @return DOCUMENT ME!
     */
    public boolean isPurine() {
        return true;
    }
}
