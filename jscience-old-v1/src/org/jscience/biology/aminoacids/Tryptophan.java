package org.jscience.biology.aminoacids;

import org.jscience.biology.AminoAcid;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;


/**
 * A class representing the Tryptophan molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class Tryptophan extends AminoAcid {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

    /** DOCUMENT ME! */
    private static Element nitrogenElement;

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
    private static Atom carbonAtom6;

    /** DOCUMENT ME! */
    private static Atom carbonAtom7;

    /** DOCUMENT ME! */
    private static Atom carbonAtom8;

    /** DOCUMENT ME! */
    private static Atom carbonAtom9;

    /** DOCUMENT ME! */
    private static Atom carbonAtom10;

    /** DOCUMENT ME! */
    private static Atom carbonAtom11;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom2;

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
    private static Atom hydrogenAtom11;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom12;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom13;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom1;

    /** DOCUMENT ME! */
    private static Atom nitrogenAtom2;

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

    /** DOCUMENT ME! */
    private static Bond bond20;

    /** DOCUMENT ME! */
    private static Bond bond21;

    /** DOCUMENT ME! */
    private static Bond bond22;

    /** DOCUMENT ME! */
    private static Bond bond23;

    /** DOCUMENT ME! */
    private static Bond bond24;

    /** DOCUMENT ME! */
    private static Bond bond25;

    /** DOCUMENT ME! */
    private static Bond bond26;

    /** DOCUMENT ME! */
    private static Bond bond27;

    /** DOCUMENT ME! */
    private static Bond bond28;

    /** DOCUMENT ME! */
    private static Bond bond29;

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        oxygenElement = PeriodicTable.getElement("Oxygen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");
        nitrogenElement = PeriodicTable.getElement("Nitrogen");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        carbonAtom3 = new Atom(carbonElement);
        carbonAtom4 = new Atom(carbonElement);
        carbonAtom5 = new Atom(carbonElement);
        carbonAtom6 = new Atom(carbonElement);
        carbonAtom7 = new Atom(carbonElement);
        carbonAtom8 = new Atom(carbonElement);
        carbonAtom9 = new Atom(carbonElement);
        carbonAtom10 = new Atom(carbonElement);
        carbonAtom11 = new Atom(carbonElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);
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
        hydrogenAtom11 = new Atom(hydrogenElement);
        hydrogenAtom12 = new Atom(hydrogenElement);
        hydrogenAtom13 = new Atom(hydrogenElement);
        nitrogenAtom1 = new Atom(nitrogenElement);
        nitrogenAtom2 = new Atom(nitrogenElement);

        bond1 = new Bond(oxygenAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, oxygenAtom1);
        bond3 = new Bond(carbonAtom1, oxygenAtom2, Bond.DOUBLE);
        bond4 = new Bond(carbonAtom1, carbonAtom2);
        bond5 = new Bond(carbonAtom2, hydrogenAtom2);
        bond6 = new Bond(carbonAtom2, nitrogenAtom1);
        bond7 = new Bond(nitrogenAtom1, hydrogenAtom3);
        bond8 = new Bond(nitrogenAtom1, hydrogenAtom4);
        bond9 = new Bond(carbonAtom2, carbonAtom3);
        bond10 = new Bond(carbonAtom3, hydrogenAtom5);
        bond11 = new Bond(carbonAtom3, hydrogenAtom6);
        bond12 = new Bond(carbonAtom3, carbonAtom4);
        bond13 = new Bond(carbonAtom4, hydrogenAtom7);
        bond14 = new Bond(carbonAtom4, carbonAtom5);
        bond15 = new Bond(carbonAtom5, carbonAtom6);
        bond16 = new Bond(carbonAtom6, hydrogenAtom8);
        bond17 = new Bond(carbonAtom6, carbonAtom7, Bond.DOUBLE);
        bond18 = new Bond(carbonAtom7, hydrogenAtom9);
        bond19 = new Bond(carbonAtom7, carbonAtom8);
        bond20 = new Bond(carbonAtom8, hydrogenAtom10);
        bond21 = new Bond(carbonAtom8, carbonAtom9, Bond.DOUBLE);
        bond22 = new Bond(carbonAtom9, hydrogenAtom11);
        bond23 = new Bond(carbonAtom9, carbonAtom10);
        bond24 = new Bond(carbonAtom10, nitrogenAtom2);
        bond25 = new Bond(carbonAtom10, carbonAtom5, Bond.DOUBLE);
        bond26 = new Bond(nitrogenAtom2, hydrogenAtom12);
        bond27 = new Bond(nitrogenAtom2, carbonAtom11);
        bond28 = new Bond(carbonAtom11, hydrogenAtom13);
        bond29 = new Bond(carbonAtom11, carbonAtom4, Bond.DOUBLE);
    }

/**
     * Creates a new Tryptophan object.
     */
    public Tryptophan() {
        super(carbonAtom1, "Tryptophan");
    }
}
