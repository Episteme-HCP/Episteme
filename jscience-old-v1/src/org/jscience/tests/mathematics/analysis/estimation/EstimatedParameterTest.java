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

package org.jscience.tests.mathematics.analysis.estimation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class EstimatedParameterTest extends TestCase {
/**
     * Creates a new EstimatedParameterTest object.
     *
     * @param name DOCUMENT ME!
     */
    public EstimatedParameterTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testConstruction() {
        EstimatedParameter p1 = new EstimatedParameter("p1", 1.0);
        assertTrue(p1.getName().equals("p1"));
        checkValue(p1.getEstimate(), 1.0);
        assertTrue(!p1.isBound());

        EstimatedParameter p2 = new EstimatedParameter("p2", 2.0, true);
        assertTrue(p2.getName().equals("p2"));
        checkValue(p2.getEstimate(), 2.0);
        assertTrue(p2.isBound());
    }

    /**
     * DOCUMENT ME!
     */
    public void testBound() {
        EstimatedParameter p = new EstimatedParameter("p", 0.0);
        assertTrue(!p.isBound());
        p.setBound(true);
        assertTrue(p.isBound());
        p.setBound(false);
        assertTrue(!p.isBound());
    }

    /**
     * DOCUMENT ME!
     */
    public void testEstimate() {
        EstimatedParameter p = new EstimatedParameter("p", 0.0);
        checkValue(p.getEstimate(), 0.0);

        for (double e = 0.0; e < 10.0; e += 0.5) {
            p.setEstimate(e);
            checkValue(p.getEstimate(), e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(EstimatedParameterTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param expected DOCUMENT ME!
     */
    private void checkValue(double value, double expected) {
        assertTrue(Math.abs(value - expected) < 1.0e-10);
    }
}
