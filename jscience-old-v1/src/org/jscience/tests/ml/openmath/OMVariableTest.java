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
 * A JUnit test for an OpenMath variable.<p></p>
 *
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Revision: 1.3 $
 */
public class OMVariableTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMVariableTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static test suite.
     *
     * @return the suite test.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMVariableTest.class);

        return suite;
    }

    /**
     * Test of getName method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testGetName() {
        OMVariable variable = new OMVariable("a");

        assertTrue(variable.getName().equals("a"));
    }

    /**
     * Test of setName method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testSetName() {
        OMVariable variable = new OMVariable("a");
        variable.setName("b");
        assertTrue(variable.getName().equals("b"));
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testGetType() {
        OMVariable variable = new OMVariable("a");
        assertTrue(variable.getType().equals("OMV"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testIsAtom() {
        OMVariable variable = new OMVariable("a");
        assertTrue(variable.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testIsComposite() {
        OMVariable variable = new OMVariable("a");
        assertTrue(!variable.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testToString() {
        OMVariable variable = new OMVariable("a");
        assertTrue(variable.toString().equals("<OMV name=\"a\"/>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testClone() {
        OMVariable variable = new OMVariable("a");
        assertTrue(((OMVariable) variable.clone()).getName().equals("a"));
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testCopy() {
        OMVariable variable = new OMVariable("a");
        assertTrue(((OMVariable) variable.copy()).getName().equals("a"));
    }

    /**
     * Test of isSame mathod of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testIsSame() {
        OMVariable var1 = new OMVariable("a");
        OMVariable var2 = new OMVariable("a");

        assertTrue(var1.isSame(var2));
    }

    /**
     * Test of isValid method of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testIsValid() {
        OMVariable var1 = new OMVariable("a");

        assertTrue(var1.isValid());
    }
}
