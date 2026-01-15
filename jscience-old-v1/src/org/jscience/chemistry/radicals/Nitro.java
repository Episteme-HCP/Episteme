package org.jscience.chemistry.radicals;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;

import java.util.HashSet;
import java.util.Set;


/**
 * A class storing the Nitro radical. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//described with single bonds between all three atoms
public final class Nitro extends Object implements Radical {
    /** DOCUMENT ME! */
    private Element nitrogenElement;

    /** DOCUMENT ME! */
    private Element oxygenElement;

    /** DOCUMENT ME! */
    private Atom nitrogenAtom;

    /** DOCUMENT ME! */
    private Atom oxygenAtom1;

    /** DOCUMENT ME! */
    private Atom oxygenAtom2;

    /** DOCUMENT ME! */
    private Bond bond1;

    /** DOCUMENT ME! */
    private Bond bond2;

    /** DOCUMENT ME! */
    private Bond bond3;

/**
     * Creates a new Nitro object.
     */
    public Nitro() {
        nitrogenElement = PeriodicTable.getElement("Nitrogen");
        oxygenElement = PeriodicTable.getElement("Oxygen");

        nitrogenAtom = new Atom(nitrogenElement);
        oxygenAtom1 = new Atom(oxygenElement);
        oxygenAtom2 = new Atom(oxygenElement);

        bond1 = new Bond(nitrogenAtom, oxygenAtom1);
        bond2 = new Bond(nitrogenAtom, oxygenAtom2);
        bond3 = new Bond(oxygenAtom1, oxygenAtom2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getRadical() {
        return nitrogenAtom;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getUnboundedAtoms() {
        HashSet result;

        result = new HashSet();
        result.add(nitrogenAtom);

        return result;
    }
}
