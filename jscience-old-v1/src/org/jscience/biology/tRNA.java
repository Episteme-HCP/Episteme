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

/**
 * A class representing transfert RNA or tRNA. There are as many transfert
 * RNA as there are codons.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//tRNA could may be coded as a Molecule
//may be tRNA could extend RNA
public class tRNA extends Object {
    /** DOCUMENT ME! */
    private Base base1;

    /** DOCUMENT ME! */
    private Base base2;

    /** DOCUMENT ME! */
    private Base base3;

    /** DOCUMENT ME! */
    private Alphabet alphabet;

/**
     * Constructs a tRNA molecule.
     *
     * @param base1    DOCUMENT ME!
     * @param base2    DOCUMENT ME!
     * @param base3    DOCUMENT ME!
     * @param alphabet DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public tRNA(Base base1, Base base2, Base base3, Alphabet alphabet) {
        if ((base1 != null) && (base2 != null) && (base3 != null) &&
                (alphabet != null)) {
            this.base1 = base1;
            this.base2 = base2;
            this.base3 = base3;
            this.alphabet = alphabet;
        } else {
            throw new IllegalArgumentException(
                "The tRNA constructor can't have null arguments.");
        }
    }

    //return an array of Base of length 3
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Base[] getCodon() {
        Base[] result;

        result = new Base[3];
        result[0] = base1;
        result[1] = base2;
        result[2] = base3;

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Alphabet getAlphabet() {
        return alphabet;
    }

    //null is returned if stop codon
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AminoAcid getAminoAcid() {
        return alphabet.getAminoAcid(base1, base2, base3);
    }

    //alphabet equality and bases kind equality
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        tRNA value;
        Base[] bases1;
        Base[] bases2;

        if ((o != null) && (o instanceof tRNA)) {
            value = (tRNA) o;
            bases1 = getCodon();
            bases2 = value.getCodon();

            return ((bases1[0] == bases2[0]) && (bases1[1] == bases2[1]) &&
            (bases1[2] == bases2[2]) &&
            (getAlphabet().equals(value.getAlphabet())));
        } else {
            return false;
        }
    }
}
