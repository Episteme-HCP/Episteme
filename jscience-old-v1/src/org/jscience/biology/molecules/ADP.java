package org.jscience.biology.molecules;

import org.jscience.chemistry.*;


/**
 * A class representing an Adenosine Di Phosphate molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we have choosen to represent the polar molecule (molecule in water)
public final class ADP extends Molecule {
    /** DOCUMENT ME! */
    private static Element carbonElement;

    /** DOCUMENT ME! */
    private static Element nitrogenElement;

    /** DOCUMENT ME! */
    private static Element hydrogenElement;

    /** DOCUMENT ME! */
    private static Element oxygenElement;

    /** DOCUMENT ME! */
    private static Element phosphorusElement;

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
    private static Atom hydrogenAtom14;

    /** DOCUMENT ME! */
    private static Atom hydrogenAtom15;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom3;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom4;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom5;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom6;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom7;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom8;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom9;

    /** DOCUMENT ME! */
    private static Atom oxygenAtom10;

    /** DOCUMENT ME! */
    private static Atom phosphorusAtom1;

    /** DOCUMENT ME! */
    private static Atom phosphorusAtom2;

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

    /** DOCUMENT ME! */
    private static Bond bond30;

    /** DOCUMENT ME! */
    private static Bond bond31;

    /** DOCUMENT ME! */
    private static Bond bond32;

    /** DOCUMENT ME! */
    private static Bond bond33;

    /** DOCUMENT ME! */
    private static Bond bond34;

    /** DOCUMENT ME! */
    private static Bond bond35;

    /** DOCUMENT ME! */
    private static Bond bond36;

    /** DOCUMENT ME! */
    private static Bond bond37;

    /** DOCUMENT ME! */
    private static Bond bond38;

    /** DOCUMENT ME! */
    private static Bond bond39;

    /** DOCUMENT ME! */
    private static Bond bond40;

    /** DOCUMENT ME! */
    private static Bond bond41;

    /** DOCUMENT ME! */
    private static Bond bond42;

    /** DOCUMENT ME! */
    private static Bond bond43;

    /** DOCUMENT ME! */
    private static Bond bond44;

    static {
        carbonElement = PeriodicTable.getElement("Carbon");
        nitrogenElement = PeriodicTable.getElement("Nitrogen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");
        oxygenElement = PeriodicTable.getElement("Oxygen");
        phosphorusElement = PeriodicTable.getElement("Phosphorus");

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
        hydrogenAtom6 = new Atom(hydrogenElement);
        hydrogenAtom7 = new Atom(hydrogenElement);
        hydrogenAtom8 = new Atom(hydrogenElement);
        hydrogenAtom9 = new Atom(hydrogenElement);
        hydrogenAtom10 = new Atom(hydrogenElement);
        hydrogenAtom11 = new Atom(hydrogenElement);
        hydrogenAtom12 = new Atom(hydrogenElement);
        hydrogenAtom13 = new Atom(hydrogenElement);
        hydrogenAtom14 = new Atom(hydrogenElement);
        hydrogenAtom15 = new Atom(hydrogenElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);
        oxygenAtom3 = new Atom(oxygenElement);
        oxygenAtom4 = new Atom(oxygenElement);
        oxygenAtom5 = new Atom(oxygenElement);
        oxygenAtom6 = new Atom(oxygenElement);
        oxygenAtom7 = new Atom(oxygenElement);
        oxygenAtom8 = new Atom(oxygenElement);
        oxygenAtom9 = new Atom(oxygenElement);
        oxygenAtom10 = new Atom(oxygenElement);
        phosphorusAtom1 = new Atom(phosphorusElement);
        phosphorusAtom2 = new Atom(phosphorusElement);

        //adenine
        bond1 = new Bond(carbonAtom1, hydrogenAtom1);
        bond2 = new Bond(carbonAtom1, nitrogenAtom1);

        bond3 = new Bond(nitrogenAtom1, carbonAtom2);
        bond4 = new Bond(carbonAtom2, carbonAtom3, Bond.DOUBLE);
        bond5 = new Bond(carbonAtom2, nitrogenAtom2);
        bond6 = new Bond(nitrogenAtom2, carbonAtom4, Bond.DOUBLE);
        bond7 = new Bond(carbonAtom4, hydrogenAtom2);
        bond8 = new Bond(carbonAtom4, nitrogenAtom3);
        bond9 = new Bond(nitrogenAtom3, carbonAtom5, Bond.DOUBLE);
        bond10 = new Bond(carbonAtom5, nitrogenAtom4);
        bond11 = new Bond(nitrogenAtom4, hydrogenAtom3);
        bond12 = new Bond(nitrogenAtom4, hydrogenAtom4);
        bond13 = new Bond(carbonAtom5, carbonAtom3);
        bond14 = new Bond(carbonAtom3, nitrogenAtom5);
        bond15 = new Bond(nitrogenAtom5, carbonAtom1, Bond.DOUBLE);

        //ribose
        bond16 = new Bond(carbonAtom6, hydrogenAtom5);
        bond17 = new Bond(carbonAtom6, nitrogenAtom1);

        bond18 = new Bond(carbonAtom6, carbonAtom7);
        bond19 = new Bond(carbonAtom7, oxygenAtom1);
        bond20 = new Bond(oxygenAtom1, hydrogenAtom6);
        bond21 = new Bond(carbonAtom7, hydrogenAtom7);
        bond22 = new Bond(carbonAtom7, carbonAtom8);
        bond23 = new Bond(carbonAtom8, oxygenAtom2);
        bond24 = new Bond(oxygenAtom2, hydrogenAtom8);
        bond25 = new Bond(carbonAtom8, hydrogenAtom9);
        bond26 = new Bond(carbonAtom8, carbonAtom9);
        bond27 = new Bond(carbonAtom9, hydrogenAtom10);
        bond28 = new Bond(carbonAtom9, carbonAtom10);
        bond29 = new Bond(carbonAtom10, hydrogenAtom11);
        bond30 = new Bond(carbonAtom10, hydrogenAtom12);
        bond31 = new Bond(carbonAtom10, oxygenAtom4);

        bond32 = new Bond(carbonAtom9, oxygenAtom3);
        bond33 = new Bond(oxygenAtom3, carbonAtom6);

        //phosphate
        bond34 = new Bond(phosphorusAtom1, oxygenAtom4);
        bond35 = new Bond(phosphorusAtom1, oxygenAtom5);

        //bond36 = new Bond(oxygenAtom5, hydrogenAtom13);
        bond37 = new Bond(phosphorusAtom1, oxygenAtom6, Bond.DOUBLE);
        bond38 = new Bond(phosphorusAtom1, oxygenAtom7);
        bond39 = new Bond(oxygenAtom7, phosphorusAtom2);
        bond40 = new Bond(phosphorusAtom2, oxygenAtom8);

        //bond41 = new Bond(oxygenAtom8, hydrogenAtom14);
        bond42 = new Bond(phosphorusAtom2, oxygenAtom9, Bond.DOUBLE);
        bond43 = new Bond(phosphorusAtom2, oxygenAtom10);

        //bond44 = new Bond(oxygenAtom10, hydrogenAtom15);
        oxygenAtom5.setCharge(-1);
        oxygenAtom8.setCharge(-1);
        oxygenAtom10.setCharge(-1);
    }

/**
     * Creates a new ADP object.
     */
    public ADP() {
        super(carbonAtom1);
    }
}
