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
public class SimilarityWeights {
    //-----------------------------------------------------------------------------
    // Attributes
    //-----------------------------------------------------------------------------
    /** DOCUMENT ME! */
    private HashMap weights = new HashMap();

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param fieldName DOCUMENT ME!
     * @param weight DOCUMENT ME!
     */
    protected void add(String fieldName, int weight) {
        //--- HashMaps require objects, no intrinsic data types
        //--- So we gotta convert int to Integer to store it
        Integer weightAsObject = new Integer(weight);

        weights.put(fieldName, weightAsObject);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        SimilarityWeights newItems = new SimilarityWeights();

        newItems.setValues(weights);

        return (Object) newItems;
    } //--- clone

    /**
     * DOCUMENT ME!
     *
     * @param traitName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int get(String traitName) {
        Integer value = (Integer) weights.get(traitName);

        return value.intValue();
    } //--- get

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return weights.values().iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator mapIterator() {
        return weights.entrySet().iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @param newValues DOCUMENT ME!
     */
    private void setValues(HashMap newValues) {
        weights = (HashMap) newValues.clone();
    } //--- setValues

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return weights.size();
    } //--- size
} //--- SimilarityWeights
