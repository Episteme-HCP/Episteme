package org.jscience.chemistry.radicals;

import org.jscience.chemistry.Atom;

import java.util.Set;


/**
 * An interface to be implemented by radicals. To ease building molecules.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//not represented ether -O- and peroxyde -O-O-
//nor carbonyl C=O
public interface Radical {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Atom getRadical();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getUnboundedAtoms();
}
