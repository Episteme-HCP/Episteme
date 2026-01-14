/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.biology;

import java.util.HashSet;
import java.util.Set;


/**
 * A class representing a DNA strain and accounting for chromosome
 * information (the histones are missing).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//DNA are huge molecules (may be several megabytes)
//They are treated here for the information they contain rather than their exact molecular structure
//That is why DNA doesn't extend Molecule as it should
public class DNA extends Chain implements java.io.Serializable, Cloneable {
    /** The array of bases for a DNA strain. */
    private Base[] bases;

/**
     * Constructs a DNA representation. The array of base should not contain
     * the Uracil base. Only one of the two strains is coded, the
     * complementary strain is deduced from this one.
     *
     * @param bases DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public DNA(Base[] bases) {
        boolean valid;
        int i;

        if (bases != null) {
            i = 0;
            valid = true;

            while ((i < bases.length) && valid) {
                valid = (bases[i] != Base.URACIL);
            }

            if (valid) {
                this.bases = bases;
            } else {
                throw new IllegalArgumentException(
                    "Invalid base array for DNA constructor.");
            }
        } else {
            throw new IllegalArgumentException(
                "The DNA constructor can't have null arguments.");
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
            compl = Base.THYMINE;
        } else if (base.equals(Base.GUANINE)) {
            compl = Base.CYTOSINE;
        } else if (base.equals(Base.CYTOSINE)) {
            compl = Base.GUANINE;
        } else if (base.equals(Base.THYMINE)) {
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

        return new DNA(compl);
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
        result.add(Base.THYMINE);

        return result;
    }

    //shallow copy
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        DNA dna;

        dna = new DNA(getBases());

        //dna = (DNA) super.clone();
        //dna.bases = getBases();
        return dna;
    }
}
