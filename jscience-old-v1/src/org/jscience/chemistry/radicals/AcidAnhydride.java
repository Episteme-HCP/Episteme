package org.jscience.chemistry.radicals;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;

import java.util.HashSet;
import java.util.Set;


/**
 * A class storing the AcidAnhydride radical. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class AcidAnhydride extends Object implements Radical {
    /** DOCUMENT ME! */
    private Element carbonElement;

    /** DOCUMENT ME! */
    private Element oxygenElement;

    /** DOCUMENT ME! */
    private Atom carbonAtom1;

    /** DOCUMENT ME! */
    private Atom carbonAtom2;

    /** DOCUMENT ME! */
    private Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private Atom oxygenAtom3;

    /** DOCUMENT ME! */
    private Bond bond1;

    /** DOCUMENT ME! */
    private Bond bond2;

    /** DOCUMENT ME! */
    private Bond bond3;

    /** DOCUMENT ME! */
    private Bond bond4;

/**
     * Creates a new AcidAnhydride object.
     */
    public AcidAnhydride() {
        carbonElement = PeriodicTable.getElement("Carbon");
        oxygenElement = PeriodicTable.getElement("Oxygen");

        carbonAtom1 = new Atom(carbonElement);
        carbonAtom2 = new Atom(carbonElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);
        oxygenAtom3 = new Atom(oxygenElement);

        bond1 = new Bond(carbonAtom1, oxygenAtom1, Bond.DOUBLE);
        bond2 = new Bond(carbonAtom1, oxygenAtom2);
        bond3 = new Bond(oxygenAtom2, carbonAtom2);
        bond4 = new Bond(carbonAtom2, oxygenAtom3, Bond.DOUBLE);
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

        return result;
    }
}
