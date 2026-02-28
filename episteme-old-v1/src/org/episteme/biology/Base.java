package org.episteme.biology;

import org.episteme.biology.bases.*;

import org.episteme.chemistry.Atom;
import org.episteme.chemistry.Molecule;


/**
 * A class representing a DNA/RNA Base.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Base extends Molecule {
    /** DOCUMENT ME! */
    public final static Base URACIL = new Uracil();

    /** DOCUMENT ME! */
    public final static Base THYMINE = new Thymine();

    /** DOCUMENT ME! */
    public final static Base ADENINE = new Adenine();

    /** DOCUMENT ME! */
    public final static Base GUANINE = new Guanine();

    /** DOCUMENT ME! */
    public final static Base CYTOSINE = new Cytosine();

/**
     * Constructs a Base molecule.
     *
     * @param a DOCUMENT ME!
     */
    public Base(Atom a) {
        super(a);
    }
}
