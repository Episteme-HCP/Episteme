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
 * A JUnit test for an OpenMath string.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMStringTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMStringTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMStringTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testGetType() {
        OMString string = new OMString("Hello there!");
        assertTrue(string.getType().equals("OMSTR"));
    }

    /**
     * Test of setString method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testSetString() {
        OMString string = new OMString("hello there!");
        string.setString("Ooops");
        assertTrue(string.getString().equals("Ooops"));
    }

    /**
     * Test of getString method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testGetString() {
        OMString string = new OMString("hello there!");
        assertTrue(string.getString().equals("hello there!"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testIsAtom() {
        OMString string = new OMString("hello");
        assertTrue(string.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testIsComposite() {
        OMString string = new OMString("hello");
        assertTrue(!string.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testToString() {
        OMString string = new OMString("hello");
        assertTrue(string.toString().equals("<OMSTR>hello</OMSTR>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testClone() {
        OMString string = new OMString("hello");
        OMString clone = (OMString) string.clone();
        assertTrue(string.getString().equals(clone.getString()));
    }

    /**
     * Test of copy method, of class org.jscience.ml.openmath.OMString.
     */
    public void testCopy() {
        OMString string = new OMString("hello");
        OMString clone = (OMString) string.clone();
        assertTrue(string.getString().equals(clone.getString()));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testIsSame() {
        OMString string1 = new OMString("hello");
        OMString string2 = new OMString("hello");

        assertTrue(string1.isSame(string2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testIsValid() {
        OMString string = new OMString("hello");

        assertTrue(string.isValid());
    }
}
