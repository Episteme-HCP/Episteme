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

package org.jscience.ml.openmath;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * A JUnit test for an OpenMath reference.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMReferenceTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMReferenceTest(String testName) {
        super(testName);
    }

    /**
     * Get the test suite.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMReferenceTest.class);

        return suite;
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testClone() {
        OMReference reference = new OMReference();
        reference.setHref("#12");

        OMReference clone = (OMReference) reference.clone();
        assertTrue(reference.getHref() == clone.getHref());
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testCopy() {
        OMReference reference = new OMReference();
        reference.setHref("#12");

        OMReference copy = (OMReference) reference.copy();
        assertTrue(reference.getHref() != copy.getHref());
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testGetType() {
        OMReference reference = new OMReference();
        assertTrue(reference.getType().equals("OMR"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testIsAtom() {
        OMReference reference = new OMReference();
        assertTrue(reference.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testIsComposite() {
        OMReference reference = new OMReference();
        assertTrue(!reference.isComposite());
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testIsSame() {
        OMReference reference1 = new OMReference();
        reference1.setHref("#11");

        OMReference reference2 = new OMReference();
        reference2.setHref("#11");

        assertTrue(reference1.isSame(reference2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testIsValid() {
        OMReference reference = new OMReference();
        reference.setHref("#11");

        assertTrue(reference.isValid());
    }

    /**
     * Test of getHref method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testGetHref() {
        OMReference reference = new OMReference();
        reference.setHref("#12");
        assertTrue(reference.getHref().equals("#12"));
    }

    /**
     * Test of setHref method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testSetHref() {
        OMReference reference = new OMReference();
        reference.setHref("#12");
        assertTrue(reference.getHref().equals("#12"));
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testToString() {
        OMReference reference = new OMReference();
        reference.setHref("#12");
        assertTrue(reference.toString().equals("<OMR href=\"#12\"/>"));
    }
}
