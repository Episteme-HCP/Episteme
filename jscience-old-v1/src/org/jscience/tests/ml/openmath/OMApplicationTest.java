/*
 *  $Id: OMApplicationTest.java,v 1.3 2007-10-23 18:23:58 virtualcall Exp $
 *
 *  Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e)
 *  All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which
 *  case the provisions of the LGPL license are applicable instead of those
 *  above. A copy of the LGPL license is available at http://www.gnu.org
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
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
