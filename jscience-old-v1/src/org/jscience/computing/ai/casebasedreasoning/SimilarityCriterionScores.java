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

import java.util.HashMap;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class SimilarityCriterionScores {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private HashMap scores = new HashMap();

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param score DOCUMENT ME!
     */
    public void add(SimilarityCriterionScore score) {
        this.scores.put(score.getID(), score);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @param ID DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimilarityCriterionScore get(String ID) {
        return (SimilarityCriterionScore) scores.get(ID);
    } //--- get

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return scores.values().iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return scores.size();
    } //--- size
}
