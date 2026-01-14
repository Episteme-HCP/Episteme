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

package org.jscience.biology.alignment;

/**
 * This abstract class is the superclass of all scoring schemes. It defines
 * basic operations that must be provided by all subclasses. Scoring schemes
 * are used by sequence alignment algorithms to compute the score of an
 * alignment.
 *
 * @author Sergio A. de Carvalho Jr.
 *
 * @see PairwiseAlignmentAlgorithm
 */
public abstract class ScoringScheme {
    /**
     * Determines whether this scoring scheme ignores the case of
     * characters when computing their score. It is set by the constructor and
     * cannot be changed afterwards.
     */
    protected boolean case_sensitive;

/**
     * Creates a new instance of an scoring scheme. The case of characters is
     * significant when subsequently computing their score.
     */
    public ScoringScheme() {
        this(true);
    }

/**
     * Creates a new instance of an scoring scheme. If
     * <CODE>case_sensitive</CODE> is <CODE>true</CODE>, the case of
     * characters is significant when subsequently computing their score;
     * otherwise the case is ignored.
     *
     * @param case_sensitive <CODE>true</CODE> if the case of characters must
     *                       be significant, <CODE>false</CODE> otherwise
     */
    public ScoringScheme(boolean case_sensitive) {
        this.case_sensitive = case_sensitive;
    }

    /**
     * Tells whether this scoring scheme ignores the case of characters
     * when computing their score.
     *
     * @return <CODE>true</CODE> if the case of characters is significant,
     *         <CODE>false</CODE> otherwise
     */
    public boolean isCaseSensitive() {
        return this.case_sensitive;
    }

    /**
     * Returns the score of a substitution of character <CODE>a</CODE>
     * for character <CODE>b</CODE> according to this scoring scheme. If this
     * substitution is not defined, an exception is raised.
     *
     * @param a first character
     * @param b second character
     *
     * @return score of substitution of <CODE>a</CODE> for <CODE>b</CODE>
     *
     * @throws IncompatibleScoringSchemeException if this substitution is not
     *         defined
     */
    public abstract int scoreSubstitution(char a, char b)
        throws IncompatibleScoringSchemeException;

    /**
     * Returns the score of an insertion of character <CODE>a</CODE>
     * according to this scoring scheme. If this character is not recognised,
     * an exception is raised.
     *
     * @param a the character to be inserted
     *
     * @return score of insertion of <CODE>a</CODE>
     *
     * @throws IncompatibleScoringSchemeException if character is not
     *         recognised by this scoring scheme
     */
    public abstract int scoreInsertion(char a)
        throws IncompatibleScoringSchemeException;

    /**
     * Returns the score of a deletion of character <CODE>a</CODE>
     * according to this scoring scheme. If this character is not recognised,
     * an exception is raised.
     *
     * @param a the character to be deleted
     *
     * @return score of insertion of <CODE>a</CODE>
     *
     * @throws IncompatibleScoringSchemeException if character is not
     *         recognised by this scoring scheme
     */
    public abstract int scoreDeletion(char a)
        throws IncompatibleScoringSchemeException;

    /**
     * Returns the maximum absolute score that this scoring scheme can
     * return for any substitution, deletion or insertion.
     *
     * @return maximum absolute score that can be returned
     */
    public abstract int maxAbsoluteScore();

    /**
     * Returns <CODE>true</CODE> if this scoring scheme supports
     * partial matches, <CODE>false</CODE> otherwise. A partial match is a
     * situation when two characters are not equal but, for any reason, are
     * regarded as similar by this scoring scheme, which then returns a
     * positive score. This is common when for scoring schemes that implement
     * amino acid scoring matrices.
     *
     * @return <CODE>true</CODE> if this scoring scheme supports partial
     *         matches, <CODE>false</CODE> otherwise
     */
    public abstract boolean isPartialMatchSupported();
}
