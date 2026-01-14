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
 * A class representing an Enzyme. An enzyme is a protein used as catalyst
 * in some biological reactions. (Some enzymes which ARE NOT proteins have
 * recently been discovered, though. We do not consider this by now as they do
 * not fit with the common definition.).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//it is up to you to build out a real molecule out of this sequence
public class Enzyme extends Protein {
    /** DOCUMENT ME! */
    public final static int UNKNOWN = -1;

    /** DOCUMENT ME! */
    public final static int OXYDO_REDUCTASE = 0;

    /** DOCUMENT ME! */
    public final static int TRANSFERASE = 1;

    /** DOCUMENT ME! */
    public final static int HYDROLASE = 2;

    /** DOCUMENT ME! */
    public final static int LYASE = 3;

    /** DOCUMENT ME! */
    public final static int ISOMERASE = 4;

    /** DOCUMENT ME! */
    public final static int LIGASE = 5;

    /** DOCUMENT ME! */
    private int kind;

/**
     * Creates a new Enzyme object.
     *
     * @param mrna   DOCUMENT ME!
     * @param coding DOCUMENT ME!
     */
    public Enzyme(mRNA mrna, Alphabet coding) {
        super(mrna, coding);
        this.kind = UNKNOWN;
    }

/**
     * Creates a new Enzyme object.
     *
     * @param aminoacids DOCUMENT ME!
     */
    public Enzyme(AminoAcid[] aminoacids) {
        super(aminoacids);
        this.kind = UNKNOWN;
    }

/**
     * Creates a new Enzyme object.
     *
     * @param acids DOCUMENT ME!
     */
    public Enzyme(String acids) {
        super(acids);
        this.kind = UNKNOWN;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */
    public void setKind(int kind) {
        this.kind = kind;
    }
}
