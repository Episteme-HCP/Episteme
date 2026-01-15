package org.jscience.chemistry.radicals;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;

import java.util.HashSet;
import java.util.Set;


/**
 * A class storing the Aromatic radical. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class Aromatic extends Object implements Radical {
    /** DOCUMENT ME! */
    private Element carbonElement;

    /** DOCUMENT ME! */
    private Element hydrogenElement;

    /** DOCUMENT ME! */
    private Atom carbonAtom1;

    /** DOCUMENT ME! */
    private Atom carbonAtom2;

    /** DOCUMENT ME! */
    private Atom carbonAtom3;

    /** DOCUMENT ME! */
    private Atom carbonAtom4;

    /** DOCUMENT ME! */
    private Atom carbonAtom5;

    /** DOCUMENT ME! */
    private Atom carbonAtom6;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom1;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom2;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom3;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom4;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom5;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom6;

    /** DOCUMENT ME! */
    private Bond bond1;

    /** DOCUMENT ME! */
    private Bond bond2;

    /** DOCUMENT ME! */
    private Bond bond3;

    /** DOCUMENT ME! */
    private Bond bond4;

    /** DOCUMENT ME! */
    private Bond bond5;

    /** DOCUMENT ME! */
    private Bond bond6;

/**
     * Creates a new Aromatic object.
     */
    public Aromatic() {
        carbonElement = PeriodicTable.getElement("Carbon");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        carbonAtom3 = new Atom(carbonElement);
        carbonAtom4 = new Atom(carbonElement);
        carbonAtom5 = new Atom(carbonElement);
        carbonAtom6 = new Atom(carbonElement);
        hydrogenAtom1 = new Atom(hydrogenElement);
        hydrogenAtom2 = new Atom(hydrogenElement);
        hydrogenAtom3 = new Atom(hydrogenElement);
        hydrogenAtom4 = new Atom(hydrogenElement);
        hydrogenAtom5 = new Atom(hydrogenElement);
        hydrogenAtom6 = new Atom(hydrogenElement);

        bond1 = new Bond(carbonAtom1, carbonAtom2, Bond.DOUBLE);
        bond2 = new Bond(carbonAtom2, carbonAtom3);
        bond3 = new Bond(carbonAtom3, carbonAtom4, Bond.DOUBLE);
        bond4 = new Bond(carbonAtom4, carbonAtom5);
        bond5 = new Bond(carbonAtom5, carbonAtom6, Bond.DOUBLE);
        bond6 = new Bond(carbonAtom6, carbonAtom1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getRadical() {
        return carbonAtom1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getUnboundedAtoms() {
        HashSet result;

        result = new HashSet();
        result.add(carbonAtom1);
        result.add(carbonAtom2);
        result.add(carbonAtom3);
        result.add(carbonAtom4);
        result.add(carbonAtom5);
        result.add(carbonAtom6);

        return result;
    }
}
