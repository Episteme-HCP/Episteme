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
 * A JUnit test for OpenMath foreign.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 */
public class OMForeignTest extends TestCase {
/**
     * Constructor. <p>
     */
    public OMForeignTest(String testName) {
        super(testName);
    }

    /**
     * Get the test suite.<p></p>
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMForeignTest.class);

        return suite;
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testClone() {
        OMForeign foreign = new OMForeign();
        foreign.setObject("12");

        OMForeign clone = (OMForeign) foreign.clone();

        assertTrue(foreign.getObject() == clone.getObject());
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testCopy() {
        OMForeign foreign = new OMForeign();
        foreign.setObject("12");

        OMForeign copy = (OMForeign) foreign.copy();

        assertTrue(foreign.getObject() == copy.getObject());
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testGetType() {
        OMForeign foreign = new OMForeign();

        assertTrue(foreign.getType().equals("OMFOREIGN"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testIsAtom() {
        OMForeign foreign = new OMForeign();

        assertTrue(foreign.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testIsComposite() {
        OMForeign foreign = new OMForeign();

        assertTrue(!foreign.isComposite());
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testIsSame() {
        OMForeign foreign1 = new OMForeign();
        foreign1.setObject("bla");

        OMForeign foreign2 = new OMForeign();
        foreign2.setObject("bla");

        assertTrue(foreign1.isSame(foreign2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testIsValid() {
        OMForeign foreign = new OMForeign();
        foreign.setObject(new String("bla"));

        assertTrue(foreign.isValid());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testToString() {
        OMForeign foreign = new OMForeign();

        assertTrue(foreign.toString().equals("<OMFOREIGN></OMFOREIGN>"));
    }
}
