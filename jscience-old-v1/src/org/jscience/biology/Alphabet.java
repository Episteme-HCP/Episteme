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

import org.jscience.util.Named;

import java.util.HashSet;
import java.util.Set;


/**
 * An class used to define the process by which mRNA is transformed
 * (:translation) into a protein according to a given code.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//startCodons is a 64 length String with an non space character where the codon (triple letters bases) codes for start
//aminoAcids is a 64 length String the letter code for amino acid corresponding to the codon (triple letters bases) and a space if not coding (stop codon)
//you can use T as U or U as T at will, for example AUG or ATG code for the same
//use this array of length 64 as the reference array for your strings contents
//Base1  TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG
//Base2  TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG
//Base3  TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG
public class Alphabet extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String aminoAcids;

    /** DOCUMENT ME! */
    private String startCodons;

/**
     * Creates a new Alphabet object.
     *
     * @param name        DOCUMENT ME!
     * @param aminoAcids  DOCUMENT ME!
     * @param startCodons DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Alphabet(String name, String aminoAcids, String startCodons) {
        if ((name != null) && (name.length() > 0) && (aminoAcids != null) &&
                (startCodons != null)) {
            if ((aminoAcids.length() == 64) && (startCodons.length() == 64)) {
                this.name = name;
                this.aminoAcids = aminoAcids;
                this.startCodons = startCodons;
            } else {
                throw new IllegalArgumentException(
                    "Alphabets aminoAcids and startCodons must be of length 64.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Alphabets constructor can't have null arguments (and name shouldn't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAminoAcids() {
        return aminoAcids;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStartCodons() {
        return startCodons;
    }

    /**
     * DOCUMENT ME!
     *
     * @param base1 DOCUMENT ME!
     * @param base2 DOCUMENT ME!
     * @param base3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isStartCodon(Base base1, Base base2, Base base3) {
        int i;

        if ((base1 != null) && (base2 != null) && (base3 != null)) {
            i = getPosition(base1, base2, base3);

            if (i >= 0) {
                return startCodons.charAt(i) != ' ';
            } else {
                return false;
            }
        } else {
            throw new IllegalArgumentException("The bases can't be null.");
        }
    }

    //return a Set of Base[3]
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getInitiationCodons() {
        HashSet result;

        result = new HashSet();

        for (int i = 0; i < 64; i++) {
            if (startCodons.charAt(i) != ' ') {
                result.add(getBaseTriplet(i));
            }
        }

        return result;
    }

    //returns null for termination codon
    /**
     * DOCUMENT ME!
     *
     * @param base1 DOCUMENT ME!
     * @param base2 DOCUMENT ME!
     * @param base3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AminoAcid getAminoAcid(Base base1, Base base2, Base base3) {
        int i;

        if ((base1 != null) && (base2 != null) && (base3 != null)) {
            i = getPosition(base1, base2, base3);

            if (i >= 0) {
                return AminoAcidFactory.getAminoAcid(aminoAcids.charAt(i));
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("The bases can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param base1 DOCUMENT ME!
     * @param base2 DOCUMENT ME!
     * @param base3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isStopCodon(Base base1, Base base2, Base base3) {
        int i;

        if ((base1 != null) && (base2 != null) && (base3 != null)) {
            i = getPosition(base1, base2, base3);

            if (i >= 0) {
                return aminoAcids.charAt(i) == ' ';
            } else {
                return false;
            }
        } else {
            throw new IllegalArgumentException("The bases can't be null.");
        }
    }

    //return a Set of Base[3]
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTerminationCodons() {
        Set result;

        result = new HashSet();

        for (int i = 0; i < 64; i++) {
            if (aminoAcids.charAt(i) == ' ') {
                result.add(getBaseTriplet(i));
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param base1 DOCUMENT ME!
     * @param base2 DOCUMENT ME!
     * @param base3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private int getPosition(Base base1, Base base2, Base base3) {
        int i;
        int j;
        int k;

        if ((base1 != null) && (base2 != null) && (base3 != null)) {
            k = getValue(base3);

            if (k != -1) {
                j = getValue(base2);

                if (j != -1) {
                    i = getValue(base1);

                    if (i != -1) {
                        return (i * 16) + (j * 4) + k;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            throw new IllegalArgumentException("The bases can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param base DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private int getValue(Base base) {
        if (base != null) {
            if (base.equals(Base.THYMINE)) {
                return 0;
            } else if (base.equals(Base.CYTOSINE)) {
                return 1;
            } else if (base.equals(Base.ADENINE)) {
                return 2;
            } else if (base.equals(Base.GUANINE)) {
                return 3;
            } else if (base.equals(Base.URACIL)) {
                return 0;
            } else {
                return -1;
            }
        } else {
            throw new IllegalArgumentException("The bases can't be null.");
        }
    }

    //pos in the 64 length array from 0 to 63
    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Base[] getBaseTriplet(int pos) {
        Base[] bases;
        int base0;
        int base1;

        if ((pos >= 0) && (pos < 64)) {
            bases = new Base[3];
            base0 = (int) Math.floor(pos / 16);
            base1 = (int) Math.floor((pos - (base0 * 16)) / 4);
            bases[0] = setValue(base0);
            bases[1] = setValue(base1);
            bases[2] = setValue(pos - (base0 * 16) - (base1 * 4));

            return bases;
        } else {
            throw new IllegalArgumentException(
                "The position can't be lower than 0 or greater than 63.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private Base setValue(int i) {
        if (i == 0) {
            return Base.THYMINE; //Base.URACIL;
        } else if (i == 1) {
            return Base.CYTOSINE;
        } else if (i == 2) {
            return Base.ADENINE;
        } else if (i == 3) {
            return Base.ADENINE;
        } else {
            throw new IllegalArgumentException(
                "You can only set value for one of the four canonical bases.");
        }
    }

    //checks for aminoacids and startcodons
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Alphabet value;

        if ((o != null) && (o instanceof Alphabet)) {
            value = (Alphabet) o;

            return ((getAminoAcids().equals(value.getAminoAcids())) &&
            (getStartCodons().equals(value.getStartCodons())));
        } else {
            return false;
        }
    }
}
