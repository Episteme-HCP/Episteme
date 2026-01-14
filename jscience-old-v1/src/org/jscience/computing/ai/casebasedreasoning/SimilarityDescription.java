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

package org.jscience.computing.ai.casebasedreasoning;

/**
 * @author <small>baylor</small>
 */
public class SimilarityDescription implements Comparable {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    private Item itemBeingDescribed;
    private int rank;
    private float percentMatch;

    //-----------------------------------------------------------------------------
    // Methods
    //-----------------------------------------------------------------------------

    /**
     * Implements the compareTo method required by the Comparable interface
     * <p/>
     * We're gonna compare items based on their percent similarity
     * <p/>
     * Used by SimilarItems.rankItems()
     * <p/>
     * The stupid argument name (o) is Sun's fault, not mine
     * <p/>
     * <p/>
     * <p/>
     * Return codes:
     * <p/>
     * me > arg = -1  (better % match means we go first)
     * <p/>
     * me = arg =  0
     * <p/>
     * me < arg = +1  (it's a better match, it goes first)
     */
    public int compareTo(Object o) {
        //--- If this fails, it'll throw a ClassCastException, which we
        //---   expect the idiot who pass us this argument to handle
        SimilarityDescription arg = (SimilarityDescription) o;

        int result = 0; //--- default to being equal

        if (this.getPercentSimilarity() < arg.getPercentSimilarity()) {
            result = 1;
        }

        if (this.getPercentSimilarity() > arg.getPercentSimilarity()) {
            result = -1;
        }

        return result;
    } //--- compareTo

    //-----------------------------------------------------------------------------
    // Accessors
    //-----------------------------------------------------------------------------
    public Item getItem() {
        return itemBeingDescribed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRank() {
        return rank;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPercentSimilarity() {
        return percentMatch;
    }

    protected void setItem(Item newItem) {
        itemBeingDescribed = newItem;
    }

    protected void setPercentSimilarity(float newPercentMatch) {
        percentMatch = newPercentMatch;
    }

    protected void setRank(int newRank) {
        rank = newRank;
    }
} //--- SimilarityDescription
