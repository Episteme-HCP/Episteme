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
 * JUnit tests for the OMApplication class.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMApplicationTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name for the test.
     */
    public OMApplicationTest(String testName) {
        super(testName);
    }

    /**
     * Statis suite method.<p></p>
     *
     * @return a TestSuite for this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMApplicationTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class OMApplication.<p></p>
     */
    public void testGetType() {
        OMApplication application = new OMApplication();
        assertTrue(application.getType().equals("OMA"));
    }

    /**
     * Test of toString method, of class OMApplication.<p></p>
     */
    public void testToString() {
        OMApplication application = new OMApplication();
        application.addElement(new OMVariable("a"));
        application.addElement(new OMVariable("b"));
        assertTrue(application.toString()
                              .equals("<OMA><OMV name=\"a\"/><OMV name=\"b\"/></OMA>"));
    }

    /**
     * Test of clone method, of class OMApplication.<p></p>
     */
    public void testClone() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        OMSymbol symbol = new OMSymbol("a", "b");
        application.addElement(integer);
        application.addElement(symbol);

        OMApplication clone = (OMApplication) application.clone();
        assertTrue((clone.getElementAt(0) == application.getElementAt(0)) &&
            (clone.getElementAt(1) == application.getElementAt(1)));
    }

    /**
     * Test of copy method, of class OMApplication.<p></p>
     */
    public void testCopy() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        OMSymbol symbol = new OMSymbol("a", "b");
        application.addElement(integer);
        application.addElement(symbol);

        OMApplication copy = (OMApplication) application.copy();
        assertTrue((copy.getElementAt(0) != application.getElementAt(0)) &&
            (copy.getElementAt(1) != application.getElementAt(1)) &&
            copy.getElementAt(0) instanceof OMInteger &&
            copy.getElementAt(1) instanceof OMSymbol);
    }

    /**
     * Test of isComposite method, of class OMApplication.<p></p>
     */
    public void testIsComposite() {
        OMApplication application = new OMApplication();
        assertTrue(application.isComposite());
    }

    /**
     * Test of isAtom method, of class OMApplication.<p></p>
     */
    public void testIsAtom() {
        OMApplication application = new OMApplication();
        assertTrue(!application.isAtom());
    }

    /**
     * Test of isSame method, of class OMApplication.<p></p>
     */
    public void testIsSame() {
        OMApplication application1 = new OMApplication();
        application1.addElement(new OMSymbol("a", "b"));

        OMApplication application2 = new OMApplication();
        application2.addElement(new OMSymbol("a", "b"));
        assertTrue(application1.isSame(application2));
    }

    /**
     * Test of isValid method, of class OMApplication.<p></p>
     */
    public void testIsValid() {
        OMApplication application = new OMApplication();
        application.addElement(new OMSymbol("a", "b"));
        application.addElement(new OMSymbol("b", "c"));
        assertTrue(application.isValid());
    }

    /**
     * Test of getLength method, of class OMApplication.<p></p>
     */
    public void testGetLength() {
        OMApplication application = new OMApplication();
        application.addElement(new OMInteger("1"));
        application.addElement(new OMSymbol("a", "b"));
        assertTrue(application.getLength() == 2);
    }

    /**
     * Test of getElements method, of class OMApplication.
     */
    public void testGetElements() {
        OMApplication application = new OMApplication();
        application.addElement(new OMInteger("1"));
        application.addElement(new OMSymbol("a", "b"));
        assertTrue(application.getElements().size() == 2);
    }

    /**
     * Test of setElements method, of class OMApplication.
     */
    public void testSetElements() {
        OMApplication application = new OMApplication();
        Vector vector = new Vector();
        vector.addElement(new OMInteger("1"));
        vector.addElement(new OMSymbol("a", "b"));
        application.setElements(vector);
        assertTrue(application.getElements().size() == 2);
    }

    /**
     * Test of getElementAt method, of class OMApplication.
     */
    public void testGetElementAt() {
        OMApplication application = new OMApplication();
        application.addElement(new OMInteger("1"));
        application.addElement(new OMSymbol("a", "b"));
        assertTrue(application.getElementAt(0) instanceof OMInteger);
    }

    /**
     * Test of setElementAt method, of class OMApplication.
     */
    public void testSetElementAt() {
        OMApplication application = new OMApplication();
        application.addElement(new OMInteger("1"));
        application.addElement(new OMSymbol("a", "b"));
        application.setElementAt(new OMSymbol("a", "b"), 0);
        assertTrue(application.getElementAt(0) instanceof OMSymbol);
    }

    /**
     * Test of insertElementAt method, of class OMApplication.
     */
    public void testInsertElementAt() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        OMSymbol symbol = new OMSymbol("a", "b");
        application.addElement(integer);
        application.addElement(symbol);
        application.insertElementAt(integer, 1);
        assertTrue(application.getElementAt(2) == symbol);
    }

    /**
     * Test of removeElementAt method, of class OMApplication.
     */
    public void testRemoveElementAt() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        OMSymbol symbol = new OMSymbol("a", "b");
        application.addElement(integer);
        application.addElement(symbol);
        application.removeElementAt(0);
        assertTrue(application.getElementAt(0) == symbol);
    }

    /**
     * Test of addElement method, of class OMApplication.
     */
    public void testAddElement() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        application.addElement(integer);
        assertTrue(application.getLength() == 1);
    }

    /**
     * Test of removeElement method, of class OMApplication.
     */
    public void testRemoveElement() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        OMSymbol symbol = new OMSymbol("a", "b");
        application.addElement(integer);
        application.addElement(symbol);
        application.removeElement(integer);
        assertTrue(application.getElementAt(0) == symbol);
    }

    /**
     * Test of removeAllElements method, of class OMApplication.
     */
    public void testRemoveAllElements() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        OMSymbol symbol = new OMSymbol("a", "b");
        application.addElement(integer);
        application.addElement(symbol);
        application.removeAllElements();
        assertTrue(application.getLength() == 0);
    }

    /**
     * Test of firstElement method, of class OMApplication.
     */
    public void testFirstElement() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        OMSymbol symbol = new OMSymbol("a", "b");
        application.addElement(integer);
        application.addElement(symbol);
        assertTrue(application.firstElement() == integer);
    }

    /**
     * Test of lastElement method, of class OMApplication.
     */
    public void testLastElement() {
        OMApplication application = new OMApplication();
        OMInteger integer = new OMInteger("1");
        OMSymbol symbol = new OMSymbol("a", "b");
        application.addElement(integer);
        application.addElement(symbol);
        assertTrue(application.lastElement() == symbol);
    }
}
