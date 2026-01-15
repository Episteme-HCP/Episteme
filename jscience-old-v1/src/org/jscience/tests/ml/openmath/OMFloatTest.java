/*
 * $Id: OMFloatTest.java,v 1.3 2007-10-23 18:23:58 virtualcall Exp $
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
 * A JUnit test for OpenMath float.<p></p>
 *
 * @author Manfred Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMFloatTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMFloatTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMFloatTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testGetType() {
        OMFloat flt = new OMFloat("1.2", "dec");
        assertTrue(flt.getType().equals("OMF"));
    }

    /**
     * Test of setFloat method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testSetFloat() {
        OMFloat flt = new OMFloat();
        flt.setFloat("1.3");
        assertTrue(flt.getFloat().equals("1.3"));
    }

    /**
     * Test of getFloat method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testGetFloat() {
        OMFloat flt = new OMFloat("1.4", "dec");
        assertTrue(flt.getFloat().equals("1.4"));
    }

    /**
     * Test of setBase method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testSetBase() {
        OMFloat flt = new OMFloat();
        flt.setBase("hex");
        assertTrue(flt.getBase().equals("hex"));
    }

    /**
     * Test of getBase method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testGetBase() {
        OMFloat flt = new OMFloat("1.5", "dec");
        assertTrue(flt.getBase().equals("dec"));
    }

    /**
     * Test of doubleValue method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testDoubleValue() {
        OMFloat flt = new OMFloat("1.6", "dec");
        assertTrue(flt.doubleValue() == 1.6);
    }

    /**
     * Test of floatValue method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testFloatValue() {
        OMFloat flt = new OMFloat("1.7", "dec");
        assertTrue(flt.floatValue() == 1.7f);
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testIsAtom() {
        OMFloat flt = new OMFloat("1.8", "dec");
        assertTrue(flt.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testIsComposite() {
        OMFloat flt = new OMFloat("1.9", "dec");
        assertTrue(!flt.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testToString() {
        OMFloat flt = new OMFloat("1.10", "dec");
        assertTrue(flt.toString().equals("<OMF dec=\"1.10\"/>"));
    }

    /**
     * Test of clone method, of class org.jscience.ml.openmath.OMFloat.
     */
    public void testClone() {
        OMFloat flt = new OMFloat("1.11", "dec");
        OMFloat clone = (OMFloat) flt.clone();
        assertTrue(clone.floatValue() == 1.11f);
    }

    /**
     * Test of copy method, of class org.jscience.ml.openmath.OMFloat.
     */
    public void testCopy() {
        OMFloat flt = new OMFloat("1.12", "dec");
        OMFloat clone = (OMFloat) flt.copy();
        assertTrue(clone.floatValue() == 1.12f);
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testIsSame() {
        OMFloat flt1 = new OMFloat("1.12", "dec");
        OMFloat flt2 = new OMFloat("1.12", "dec");

        assertTrue(flt1.isSame(flt2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMFloat.
     */
    public void testIsValid() {
        OMFloat flt = new OMFloat("1.12", "dec");
        assertTrue(flt.isValid());
    }
}
