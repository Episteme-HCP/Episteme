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
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class SimilarityCriterionScore {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private String criterionID;

    /** DOCUMENT ME! */
    private float normalizedValue;

    /** DOCUMENT ME! */
    private float weightedValue;

    //-----------------------------------------------------------------------------
    /**
     * Creates a new SimilarityCriterionScore object.
     *
     * @param criterionID DOCUMENT ME!
     */
    protected SimilarityCriterionScore(String criterionID) {
        setID(criterionID);
    }

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getNormalizedValue() {
        return normalizedValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    protected void setNormalizedValue(float value) {
        this.normalizedValue = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getID() {
        return criterionID;
    }

    /**
     * DOCUMENT ME!
     *
     * @param criterionID DOCUMENT ME!
     */
    protected void setID(String criterionID) {
        this.criterionID = criterionID;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getWeightedValue() {
        return weightedValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    protected void setWeightedValue(float value) {
        this.weightedValue = value;
    }
} //--- SimilarityCriterionScore
