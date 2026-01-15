package org.jscience.biology;

import java.util.HashSet;
import java.util.Set;


/**
 * A class representing RNA strain.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//RNA are huge molecules (may be several megabytes)
//They are treated here for the information they contain rather than their exact molecular structure
//That is why RNA doesn't extend Molecule as it should
public class RNA extends Chain implements java.io.Serializable, Cloneable {
    /** The array of bases for a RNA strain. */
    private Base[] bases;

/**
     * Constructs a RNA representation. The array of base should not contain
     * Thymine.
     *
     * @param bases DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public RNA(Base[] bases) {
        boolean valid;
        int i;

        if (bases != null) {
            i = 0;
            valid = true;

            while ((i < bases.length) && valid) {
                valid = (bases[i] != Base.THYMINE);
            }

            if (valid) {
                this.bases = bases;
            } else {
                throw new IllegalArgumentException(
                    "Invalid base array for RNA constructor.");
            }
        } else {
            throw new IllegalArgumentException(
                "The RNA constructor can't have null arguments.");
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return bases.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param base DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Base getComplementary(Base base) {
        Base compl;

        if (base.equals(Base.ADENINE)) {
            compl = Base.URACIL;
        } else if (base.equals(Base.GUANINE)) {
            compl = Base.CYTOSINE;
        } else if (base.equals(Base.CYTOSINE)) {
            compl = Base.GUANINE;
        } else if (base.equals(Base.URACIL)) {
            compl = Base.ADENINE;
        } else {
            compl = null;
        }

        return compl;
    }

    /**
     * Returns a complementary strain deduced from this one.
     *
     * @return DOCUMENT ME!
     */
    public Chain getComplementary() {
        Base[] compl;

        compl = new Base[bases.length];

        for (int i = 0; i < bases.length; i++) {
            compl[i] = getComplementary(bases[i]);
        }

        return new RNA(compl);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getValidBases() {
        HashSet result;

        result = new HashSet();
        result.add(Base.ADENINE);
        result.add(Base.GUANINE);
        result.add(Base.CYTOSINE);
        result.add(Base.URACIL);

        return result;
    }

    //shallow copy
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        RNA rna;

        rna = new RNA(getBases());

        //rna = (RNA) super.clone();
        //rna.bases = getBases();
        return rna;
    }
}
