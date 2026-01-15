/*
 * $Id: OMByteArrayTest.java,v 1.3 2007-10-23 18:23:58 virtualcall Exp $
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


/**
 * Models a JUnit test for an OpenMath byte-array.<p></p>
 *
 * @author Manfred Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMByteArrayTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMByteArrayTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMByteArrayTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testGetType() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.getType().equals("OMB"));
    }

    /**
     * Test of setByteArray method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testSetByteArray() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.getByteArrayAsString().equals("1234"));
    }

    /**
     * Test of getByteArray method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testGetByteArray() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.getByteArray() != null);
    }

    /**
     * Test of getByteArrayAsString method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testGetByteArrayAsString() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.getByteArrayAsString().equals("1234"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testIsAtom() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testIsComposite() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(!byteArray.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testToString() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");
        assertTrue(byteArray.toString().equals("<OMB>1234</OMB>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testClone() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");

        OMByteArray clone = (OMByteArray) byteArray.clone();
        assertTrue(byteArray.getByteArrayAsString()
                            .equals(clone.getByteArrayAsString()));
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testCopy() {
        OMByteArray byteArray = new OMByteArray();
        byteArray.setByteArray("1234");

        OMByteArray clone = (OMByteArray) byteArray.copy();
        assertTrue(byteArray.getByteArrayAsString()
                            .equals(clone.getByteArrayAsString()));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testIsSame() {
        OMByteArray array1 = new OMByteArray("1234");
        OMByteArray array2 = new OMByteArray("1234");

        assertTrue(array1.isSame(array2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMByteArray.
     */
    public void testIsValid() {
        OMByteArray array = new OMByteArray("1234");

        assertTrue(array.isValid());
    }
}
