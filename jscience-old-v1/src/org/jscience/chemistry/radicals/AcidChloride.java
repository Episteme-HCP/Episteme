package org.jscience.chemistry.radicals;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;

import java.util.HashSet;
import java.util.Set;


/**
 * A class storing the AcidChloride radical. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class AcidChloride extends Object implements Radical {
    /** DOCUMENT ME! */
    private Element carbonElement;

    /** DOCUMENT ME! */
    private Element oxygenElement;

    /** DOCUMENT ME! */
    private Element chlorineElement;

    /** DOCUMENT ME! */
    private Atom carbonAtom;

    /** DOCUMENT ME! */
    private Atom oxygenAtom;

    /** DOCUMENT ME! */
    private Atom chlorineAtom;

    /** DOCUMENT ME! */
    private Bond bond1;

    /** DOCUMENT ME! */
    private Bond bond2;

/**
     * Creates a new AcidChloride object.
     */
    public AcidChloride() {
        carbonElement = PeriodicTable.getElement("Carbon");
        oxygenElement = PeriodicTable.getElement("Oxygen");
        chlorineElement = PeriodicTable.getElement("Chlorine");

        carbonAtom = new Atom(carbonElement);
        oxygenAtom = new Atom(oxygenElement);
        chlorineAtom = new Atom(chlorineElement);

        bond1 = new Bond(carbonAtom, oxygenAtom, Bond.DOUBLE);
        bond2 = new Bond(carbonAtom, chlorineAtom);
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
