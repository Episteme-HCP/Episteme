package org.episteme.chemistry.radicals;

import org.episteme.chemistry.Atom;
import org.episteme.chemistry.Bond;
import org.episteme.chemistry.Element;
import org.episteme.chemistry.PeriodicTable;

import java.util.HashSet;
import java.util.Set;


/**
 * A class storing the Hydroxy radical. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class Hydroxy extends Object implements Radical {
    /** DOCUMENT ME! */
    private Element oxygenElement;

    /** DOCUMENT ME! */
    private Element hydrogenElement;

    /** DOCUMENT ME! */
    private Atom oxygenAtom;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom;

    /** DOCUMENT ME! */
    private Bond bond;

/**
     * Creates a new Hydroxy object.
     */
    public Hydroxy() {
        oxygenElement = PeriodicTable.getElement("Oxygen");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        oxygenAtom = new Atom(oxygenElement);
        hydrogenAtom = new Atom(hydrogenElement);

        bond = new Bond(oxygenAtom, hydrogenAtom);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getRadical() {
        return oxygenAtom;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getUnboundedAtoms() {
        HashSet result;

        result = new HashSet();
        result.add(oxygenAtom);

        return result;
    }
}
