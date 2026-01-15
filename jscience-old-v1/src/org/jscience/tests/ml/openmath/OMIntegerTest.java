/*
 * $Id: OMIntegerTest.java,v 1.3 2007-10-23 18:23:58 virtualcall Exp $
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
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which
 *  case the provisions of the LGPL license are applicable instead of those
 *  above. A copy of the LGPL license is available at http://www.gnu.org
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
 * A JUnit test for an OpenMath integer.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMIntegerTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMIntegerTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMIntegerTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testGetType() {
        OMInteger integer = new OMInteger("12");
        assertTrue(integer.getType().equals("OMI"));
    }

    /**
     * Test of setInteger method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testSetInteger() {
        OMInteger integer = new OMInteger("12");
        integer.setInteger("13");
        assertTrue(integer.getInteger().equals("13"));
    }

    /**
     * Test of getInteger method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testGetInteger() {
        OMInteger integer = new OMInteger("14");
        assertTrue(integer.getInteger().equals("14"));
    }

    /**
     * Test of longValue method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testLongValue() {
        OMInteger integer = new OMInteger("15");
        assertTrue(integer.longValue() == 15);
    }

    /**
     * Test of intValue method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIntValue() {
        OMInteger integer = new OMInteger("16");
        assertTrue(integer.intValue() == 16);
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIsAtom() {
        OMInteger integer = new OMInteger("17");
        assertTrue(integer.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIsComposite() {
        OMInteger integer = new OMInteger("18");
        assertTrue(!integer.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testToString() {
        OMInteger integer = new OMInteger("19");
        assertTrue(integer.toString().equals("<OMI>19</OMI>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testClone() {
        OMInteger integer = new OMInteger("20");
        OMInteger clone = (OMInteger) integer.clone();
        assertTrue(integer.getInteger().equals(clone.getInteger()));
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testCopy() {
        OMInteger integer = new OMInteger("20");
        OMInteger clone = (OMInteger) integer.clone();
        assertTrue(integer.getInteger().equals(clone.getInteger()));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIsSame() {
        OMInteger integer1 = new OMInteger("1");
        OMInteger integer2 = new OMInteger("1");

        assertTrue(integer1.isSame(integer2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMInteger.
     */
    public void testIsValid() {
        OMInteger integer = new OMInteger("1");

        assertTrue(integer.isValid());
    }
}
