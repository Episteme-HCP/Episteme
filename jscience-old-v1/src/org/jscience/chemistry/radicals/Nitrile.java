package org.jscience.chemistry.radicals;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;

import java.util.HashSet;
import java.util.Set;


/**
 * A class storing the Nitrile radical. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class Nitrile extends Object implements Radical {
    /** DOCUMENT ME! */
    private Element carbonElement;

    /** DOCUMENT ME! */
    private Element nitrogenElement;

    /** DOCUMENT ME! */
    private Atom carbonAtom;

    /** DOCUMENT ME! */
    private Atom nitrogenAtom;

    /** DOCUMENT ME! */
    private Bond bond;

/**
     * Creates a new Nitrile object.
     */
    public Nitrile() {
        carbonElement = PeriodicTable.getElement("Carbon");
        nitrogenElement = PeriodicTable.getElement("Nitrogen");

        carbonAtom = new Atom(carbonElement);
        nitrogenAtom = new Atom(nitrogenElement);

        bond = new Bond(carbonAtom, nitrogenAtom, Bond.TRIPLE);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getRadical() {
        return carbonAtom;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getUnboundedAtoms() {
        HashSet result;

        result = new HashSet();
        result.add(carbonAtom);

        return result;
    }
}
