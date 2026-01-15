package org.jscience.biology;

/**
 * A class representing a message RNA or mRNA.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//mRNA could may be coded as a Molecule
//may be mRNA could extend RNA
//In nature DNA replicates from 5' to 3'
public class mRNA extends Object {
    /** The array of bases for a mRNA strain. */
    private Base[] bases;

/**
     * Constructs a mRNA molecule. As it is mRNA, this array should consist
     * only of Adenine, Uracil, Cythosine, Guanine (this is however not
     * checked)
     *
     * @param bases DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public mRNA(Base[] bases) {
        if (bases != null) {
            bases = bases;
        } else {
            throw new IllegalArgumentException(
                "The mRNA constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Base[] getBases() {
        return bases;
    }
}
