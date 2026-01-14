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
 * A JUnit test for an OpenMath integer.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMIntegerTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMIntegerTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMIntegerTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testGetType() {
        OMInteger integer = new OMInteger("12");
        assertTrue(integer.getType().equals("OMI"));
    }

    /**
     * Test of setInteger method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testSetInteger() {
        OMInteger integer = new OMInteger("12");
        integer.setInteger("13");
        assertTrue(integer.getInteger().equals("13"));
    }

    /**
     * Test of getInteger method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testGetInteger() {
        OMInteger integer = new OMInteger("14");
        assertTrue(integer.getInteger().equals("14"));
    }

    /**
     * Test of longValue method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testLongValue() {
        OMInteger integer = new OMInteger("15");
        assertTrue(integer.longValue() == 15);
    }

    /**
     * Test of intValue method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIntValue() {
        OMInteger integer = new OMInteger("16");
        assertTrue(integer.intValue() == 16);
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIsAtom() {
        OMInteger integer = new OMInteger("17");
        assertTrue(integer.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIsComposite() {
        OMInteger integer = new OMInteger("18");
        assertTrue(!integer.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testToString() {
        OMInteger integer = new OMInteger("19");
        assertTrue(integer.toString().equals("<OMI>19</OMI>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testClone() {
        OMInteger integer = new OMInteger("20");
        OMInteger clone = (OMInteger) integer.clone();
        assertTrue(integer.getInteger().equals(clone.getInteger()));
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testCopy() {
        OMInteger integer = new OMInteger("20");
        OMInteger clone = (OMInteger) integer.clone();
        assertTrue(integer.getInteger().equals(clone.getInteger()));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIsSame() {
        OMInteger integer1 = new OMInteger("1");
        OMInteger integer2 = new OMInteger("1");

        assertTrue(integer1.isSame(integer2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIsValid() {
        OMInteger integer = new OMInteger("1");

        assertTrue(integer.isValid());
    }
}
