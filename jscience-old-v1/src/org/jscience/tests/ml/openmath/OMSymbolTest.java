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
 * A JUnit test for an OpenMath symbol.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMSymbolTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMSymbolTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMSymbolTest.class);

        return suite;
    }

    /**
     * Test of getCD method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testGetCd() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.getCd().equals("cd"));
    }

    /**
     * Test of setCD method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testSetCd() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        symbol.setCD("hello");
        assertTrue(symbol.getCd().equals("hello"));
    }

    /**
     * Test of getName method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testGetName() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.getName().equals("name"));
    }

    /**
     * Test of setName method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testSetName() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        symbol.setName("hello");
        assertTrue(symbol.getName().equals("hello"));
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testGetType() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.getType().equals("OMS"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testIsAtom() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testIsComposite() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(!symbol.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testToString() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.toString().equals("<OMS cd=\"cd\" name=\"name\"/>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testClone() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        OMSymbol clone = (OMSymbol) symbol.clone();

        assertTrue((symbol.getCd() == clone.getCd()) &&
            (symbol.getName() == clone.getName()));
    }

    /**
     * Test of copy method, of class org.jscience.ml.openmath.OMSymbol.
     */
    public void testCopy() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        OMSymbol clone = (OMSymbol) symbol.clone();

        assertTrue((symbol.getCd() == clone.getCd()) &&
            (symbol.getName() == clone.getName()));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testIsSame() {
        OMSymbol symbol1 = new OMSymbol("a", "b");
        OMSymbol symbol2 = new OMSymbol("a", "b");

        assertTrue(symbol1.isSame(symbol2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testIsValid() {
        OMSymbol symbol = new OMSymbol("a", "b");

        assertTrue(symbol.isValid());
    }
}
