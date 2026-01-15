/*
 * $Id: OMStringTest.java,v 1.3 2007-10-23 18:23:59 virtualcall Exp $
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
 * A JUnit test for an OpenMath string.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMStringTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMStringTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMStringTest.class);

        return suite;
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testGetType() {
        OMString string = new OMString("Hello there!");
        assertTrue(string.getType().equals("OMSTR"));
    }

    /**
     * Test of setString method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testSetString() {
        OMString string = new OMString("hello there!");
        string.setString("Ooops");
        assertTrue(string.getString().equals("Ooops"));
    }

    /**
     * Test of getString method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testGetString() {
        OMString string = new OMString("hello there!");
        assertTrue(string.getString().equals("hello there!"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testIsAtom() {
        OMString string = new OMString("hello");
        assertTrue(string.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testIsComposite() {
        OMString string = new OMString("hello");
        assertTrue(!string.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testToString() {
        OMString string = new OMString("hello");
        assertTrue(string.toString().equals("<OMSTR>hello</OMSTR>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testClone() {
        OMString string = new OMString("hello");
        OMString clone = (OMString) string.clone();
        assertTrue(string.getString().equals(clone.getString()));
    }

    /**
     * Test of copy method, of class org.jscience.ml.openmath.OMString.
     */
    public void testCopy() {
        OMString string = new OMString("hello");
        OMString clone = (OMString) string.clone();
        assertTrue(string.getString().equals(clone.getString()));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testIsSame() {
        OMString string1 = new OMString("hello");
        OMString string2 = new OMString("hello");

        assertTrue(string1.isSame(string2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMString.
     */
    public void testIsValid() {
        OMString string = new OMString("hello");

        assertTrue(string.isValid());
    }
}
