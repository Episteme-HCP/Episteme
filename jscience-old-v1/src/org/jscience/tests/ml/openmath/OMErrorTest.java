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

import java.util.Vector;


/**
 * A JUnit test for an OpenMath error.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMErrorTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMErrorTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMErrorTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testGetType() {
        OMError error = new OMError();
        assertTrue(error.getType().equals("OME"));
    }

    /**
     * Test of setSymbol method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testSetSymbol() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        assertTrue(error.getSymbol() != null);
    }

    /**
     * Test of getSymbol method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testGetSymbol() {
        OMError error = new OMError(new OMSymbol("cd", "name"));
        assertTrue(error.getSymbol() != null);
    }

    /**
     * Test of getElements method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testGetElements() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        assertTrue(!error.getElements().isEmpty());
    }

    /**
     * Test of setElements method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testSetElements() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));

        Vector elements = new Vector();
        elements.add(new OMInteger("1"));
        error.setElements(elements);
        assertTrue(!error.getElements().isEmpty());
    }

    /**
     * Test of getElementAt method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testGetElementAt() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        error.addElement(new OMVariable("a"));
        error.addElement(new OMInteger("2"));
        assertTrue(error.getElementAt(1).getType().equals("OMV"));
    }

    /**
     * Test of setElementAt method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testSetElementAt() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        error.addElement(new OMVariable("a"));
        error.addElement(new OMInteger("2"));
        error.setElementAt(new OMInteger("3"), 1);
        assertTrue(error.getElementAt(1).getType().equals("OMI"));
    }

    /**
     * Test of insertElementAt method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testInsertElementAt() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        error.addElement(new OMVariable("a"));
        error.addElement(new OMInteger("2"));
        error.insertElementAt(new OMInteger("3"), 1);
        assertTrue(error.getElementAt(2).getType().equals("OMV"));
    }

    /**
     * Test of removeElementAt method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testRemoveElementAt() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        error.addElement(new OMVariable("a"));
        error.addElement(new OMInteger("2"));
        error.removeElementAt(0);
        assertTrue(error.getElementAt(0).getType().equals("OMV"));
    }

    /**
     * Test of addElement method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testAddElement() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        assertTrue(error.getElementAt(0).getType().equals("OMI"));
    }

    /**
     * Test of removeElement method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testRemoveElement() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));

        OMInteger integer = new OMInteger("1");
        error.addElement(integer);
        error.removeElement(integer);
        assertTrue(error.getElements().isEmpty());
    }

    /**
     * Test of removeAllElements method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testRemoveAllElements() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        error.addElement(new OMVariable("a"));
        error.addElement(new OMInteger("2"));
        error.removeAllElements();
        assertTrue(error.getElements().isEmpty());
    }

    /**
     * Test of firstElement method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testFirstElement() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        error.addElement(new OMVariable("a"));
        error.addElement(new OMInteger("2"));
        assertTrue(error.firstElement().getType().equals("OMI"));
    }

    /**
     * Test of lastElement method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testLastElement() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        error.addElement(new OMVariable("a"));
        assertTrue(error.lastElement().getType().equals("OMV"));
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testToString() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));
        assertTrue(error.toString()
                        .equals("<OME><OMS cd=\"cd\" name=\"name\"/><OMI>1</OMI></OME>"));
    }

    /**
     * Test of clone method, of class org.jscience.ml.openmath.OMError.
     */
    public void testClone() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));

        OMError clone = (OMError) error.clone();

        assertTrue((clone.getSymbol() == error.getSymbol()) &&
            (clone.getElements() == error.getElements()));
    }

    /**
     * Test of copy method, of class org.jscience.ml.openmath.OMError.
     */
    public void testCopy() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        error.addElement(new OMInteger("1"));

        OMError clone = (OMError) error.copy();

        assertTrue((clone.getSymbol() != error.getSymbol()) &&
            (clone.getElements() != error.getElements()));
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testIsComposite() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        assertTrue(error.isComposite());
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testIsAtom() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("cd", "name"));
        assertTrue(!error.isAtom());
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testIsSame() {
        OMError error1 = new OMError();
        error1.setSymbol(new OMSymbol("error1", "error"));
        error1.addElement(new OMInteger("1"));

        OMError error2 = new OMError();
        error2.setSymbol(new OMSymbol("error1", "error"));
        error2.addElement(new OMInteger("1"));

        assertTrue(error1.isSame(error2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMError.
     */
    public void testIsValid() {
        OMError error = new OMError();
        error.setSymbol(new OMSymbol("error1", "error"));
        error.addElement(new OMInteger("1"));

        assertTrue(error.isValid());
    }
}
