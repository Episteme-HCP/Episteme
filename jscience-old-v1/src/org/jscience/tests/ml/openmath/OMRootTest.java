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
 * A JUnit test for an OpenMath root object.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMRootTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMRootTest(String testName) {
        super(testName);
    }

    /**
     * Get the test suite.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMRootTest.class);

        return suite;
    }

    /**
     * Test of clone method, of class org.jscience.ml.openmath.OMRoot.
     */
    public void testClone() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);

        OMRoot clone = (OMRoot) root.clone();
        assertTrue(root.getObject() == clone.getObject());
    }

    /**
     * Test of copy method, of class org.jscience.ml.openmath.OMRoot.
     */
    public void testCopy() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);

        OMRoot copy = (OMRoot) root.copy();
        assertTrue(root.getObject() != copy.getObject());
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMRoot.
     */
    public void testGetType() {
        OMRoot root = new OMRoot();
        assertTrue(root.getType().equals("OMOBJ"));
    }

    /**
     * Test of isAtom method, of class org.jscience.ml.openmath.OMRoot.
     */
    public void testIsAtom() {
        OMRoot root = new OMRoot();
        assertTrue(!root.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMRoot.
     */
    public void testIsComposite() {
        OMRoot root = new OMRoot();
        assertTrue(root.isComposite());
    }

    /**
     * Test of isSame method, of class org.jscience.ml.openmath.OMRoot.
     */
    public void testIsSame() {
        OMRoot root1 = new OMRoot();
        root1.setObject(new OMInteger("1"));

        OMRoot root2 = new OMRoot();
        root2.setObject(new OMInteger("1"));

        assertTrue(root1.isSame(root2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMRoot.
     */
    public void testIsValid() {
        OMRoot root = new OMRoot();
        root.setObject(new OMInteger("1"));

        assertTrue(root.isValid());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMRoot.
     */
    public void testToString() {
        OMRoot root = new OMRoot();
        root.setObject(new OMInteger("1"));
        assertTrue(root.toString().equals("<OMOBJ><OMI>1</OMI></OMOBJ>"));
    }

    /**
     * Test of setObject method, of class
     * org.jscience.ml.openmath.OMRoot.
     */
    public void testSetObject() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);
        assertTrue(root.getObject() == integer);
    }

    /**
     * Test of getObject method, of class
     * org.jscience.ml.openmath.OMRoot.
     */
    public void testGetObject() {
        OMRoot root = new OMRoot();
        OMInteger integer = new OMInteger("1");
        root.setObject(integer);
        assertTrue(root.getObject() == integer);
    }
}
