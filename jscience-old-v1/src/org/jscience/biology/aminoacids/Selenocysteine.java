package org.jscience.biology.aminoacids;

import org.jscience.biology.AminoAcid;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;


/**
 * A class representing the Selenocysteine molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class Selenocysteine extends AminoAcid {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

    /** DOCUMENT ME! */
    private static Element nitrogenElement;

    /** DOCUMENT ME! */
    private static Element seleniumElement;

    /** DOCUMENT ME! */
    private static Atom carbonAtom1;

    /** DOCUMENT ME! */
    private static Atom carbonAtom2;

    /** DOCUMENT ME! */
    private static Atom carbonAtom3;

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
    private static Atom nitrogenAtom;

    /** DOCUMENT ME! */
    private static Atom seleniumAtom;

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
        oxygenElement = PeriodicTable.getElement("Oxygen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");
        nitrogenElement = PeriodicTable.getElement("Nitrogen");
        seleniumElement = PeriodicTable.getElement("Selenium");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        carbonAtom3 = new Atom(carbonElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        hydrogenAtom3 = new Atom(hydrogenElement);
        hydrogenAtom4 = new Atom(hydrogenElement);
        hydrogenAtom5 = new Atom(hydrogenElement);
        hydrogenAtom6 = new Atom(hydrogenElement);
        hydrogenAtom7 = new Atom(hydrogenElement);
        nitrogenAtom = new Atom(nitrogenElement);
        seleniumAtom = new Atom(seleniumElement);

        bond1 = new Bond(oxygenAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, oxygenAtom1);
        bond3 = new Bond(carbonAtom1, oxygenAtom2, Bond.DOUBLE);
        bond4 = new Bond(carbonAtom1, carbonAtom2);
        bond5 = new Bond(carbonAtom2, hydrogenAtom2);
        bond6 = new Bond(carbonAtom2, nitrogenAtom);        
        bond7 = new Bond(nitrogenAtom, hydrogenAtom3);
        bond8 = new Bond(nitrogenAtom, hydrogenAtom4);
        bond9 = new Bond(carbonAtom2, carbonAtom3);
        bond10 = new Bond(carbonAtom3, hydrogenAtom5);
        bond11 = new Bond(carbonAtom3, hydrogenAtom6);
        bond12 = new Bond(carbonAtom3, seleniumAtom);
        bond13 = new Bond(seleniumAtom, hydrogenAtom7);
     }

/**
     * Creates a new Selenocysteine object.
     */
    public Selenocysteine() {
        super(carbonAtom1, "Selenocysteine");
    }
}
