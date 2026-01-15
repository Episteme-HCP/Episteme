package org.jscience.chemistry.radicals;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;

import java.util.HashSet;
import java.util.Set;


/**
 * A class storing the Thiol radical. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class Thiol extends Object implements Radical {
    /** DOCUMENT ME! */
    private Element sulphurElement;

    /** DOCUMENT ME! */
    private Element hydrogenElement;

    /** DOCUMENT ME! */
    private Atom sulphurAtom;

    /** DOCUMENT ME! */
    private Atom hydrogenAtom;

    /** DOCUMENT ME! */
    private Bond bond;

/**
     * Creates a new Thiol object.
     */
    public Thiol() {
        sulphurElement = PeriodicTable.getElement("Sulphur");
        hydrogenElement = PeriodicTable.getElement("Hydrogen");

        sulphurAtom = new Atom(sulphurElement);
        hydrogenAtom = new Atom(hydrogenElement);

        bond = new Bond(sulphurAtom, hydrogenAtom);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getRadical() {
        return sulphurAtom;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getUnboundedAtoms() {
        HashSet result;

        result = new HashSet();
        result.add(sulphurAtom);

        return result;
    }
}
