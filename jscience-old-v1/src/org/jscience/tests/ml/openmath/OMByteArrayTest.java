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
 * Models a JUnit test for an OpenMath byte-array.<p></p>
 *
 * @author Manfred Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMByteArrayTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMByteArrayTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMByteArrayTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testGetType() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.getType().equals("OMB"));
    }

    /**
     * Test of setByteArray method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testSetByteArray() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.getByteArrayAsString().equals("1234"));
    }

    /**
     * Test of getByteArray method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testGetByteArray() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.getByteArray() != null);
    }

    /**
     * Test of getByteArrayAsString method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testGetByteArrayAsString() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.getByteArrayAsString().equals("1234"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testIsAtom() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testIsComposite() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(!byteArray.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testToString() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.toString().equals("<OMB>1234</OMB>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testClone() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");

        OMByteArray clone = (OMByteArray) byteArray.clone();
        assertTrue(byteArray.getByteArrayAsString()
                            .equals(clone.getByteArrayAsString()));
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testCopy() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");

        OMByteArray clone = (OMByteArray) byteArray.copy();
        assertTrue(byteArray.getByteArrayAsString()
                            .equals(clone.getByteArrayAsString()));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testIsSame() {
        OMByteArray array1 = new OMByteArray("1234");
        OMByteArray array2 = new OMByteArray("1234");

        assertTrue(array1.isSame(array2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testIsValid() {
        OMByteArray array = new OMByteArray("1234");

        assertTrue(array.isValid());
    }
}
