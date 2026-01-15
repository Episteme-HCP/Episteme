/*
 * $Id: OMRootTest.java,v 1.3 2007-10-23 18:23:59 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
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
 *  by Manfred N. Riem are Copyright (c) 2001,2004. All Rights Reserved.
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
