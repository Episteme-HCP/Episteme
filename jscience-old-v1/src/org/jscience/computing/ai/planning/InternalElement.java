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

package org.jscience.computing.ai.planning;

/**
 * Each domain element (i.e., method, operator, or axiom) at compile time
 * is represented as an instance of a class derived from this abstract class.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public abstract class InternalElement extends CompileTimeObject {
    /**
     * The number of objects already instantiated from this class
     * before this object was instantiated. This is used as a unique
     * identifier for this object to distinguish it from the other objects of
     * this class.
     */
    private int cnt;

    /** Every element has a head, which is a predicate. */
    private Predicate head;

/**
     * To initialize this internal domain element.
     *
     * @param headIn head of this element.
     * @param cntIn  index of this element in the domain description.
     */
    public InternalElement(Predicate headIn, int cntIn) {
        head = headIn;
        cnt = cntIn;
    }

    /**
     * To get the number of objects already instantiated from this
     * class before this object was instantiated. This is used as a unique
     * identifier for this object to distinguish it from the other objects of
     * this class.
     *
     * @return the number of objects already instantiated from this class
     *         before this object was instantiated.
     */
    public int getCnt() {
        return cnt;
    }

    /**
     * To get the head of this internal domain element.
     *
     * @return the head of this internal domain element.
     */
    public Predicate getHead() {
        return head;
    }
}
