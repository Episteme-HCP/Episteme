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

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class SimilarityCriteria {
    /** DOCUMENT ME! */
    private ArrayList criteria = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param criterion DOCUMENT ME!
     */
    protected void add(SimilarityCriterion criterion) {
        criteria.add(criterion);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @param fieldName DOCUMENT ME!
     * @param operator DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    protected void add(String fieldName, String operator, String value) {
        SimilarityCriterion criterion = new SimilarityCriterion(fieldName,
                operator, value);

        this.add(criterion);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        SimilarityCriteria newItems = new SimilarityCriteria();

        newItems.setValues(criteria);

        return (Object) newItems;
    } //--- clone

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return criteria.iterator();
    } //--- iterator

    /**
     * DOCUMENT ME!
     *
     * @param newValues DOCUMENT ME!
     */
    private void setValues(ArrayList newValues) {
        criteria = (ArrayList) newValues.clone();
    } //--- setValues

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return criteria.size();
    } //--- size
} //--- SimilarityCriteria
