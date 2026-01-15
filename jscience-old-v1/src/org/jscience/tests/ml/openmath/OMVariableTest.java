/*
 * $Id: OMVariableTest.java,v 1.3 2007-10-23 18:23:59 virtualcall Exp $
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
 * A JUnit test for an OpenMath variable.<p></p>
 *
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Revision: 1.3 $
 */
public class OMVariableTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMVariableTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static test suite.
     *
     * @return the suite test.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMVariableTest.class);

        return suite;
    }

    /**
     * Test of getName method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testGetName() {
        OMVariable variable = new OMVariable("a");

        assertTrue(variable.getName().equals("a"));
    }

    /**
     * Test of setName method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testSetName() {
        OMVariable variable = new OMVariable("a");
        variable.setName("b");
        assertTrue(variable.getName().equals("b"));
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testGetType() {
        OMVariable variable = new OMVariable("a");
        assertTrue(variable.getType().equals("OMV"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testIsAtom() {
        OMVariable variable = new OMVariable("a");
        assertTrue(variable.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testIsComposite() {
        OMVariable variable = new OMVariable("a");
        assertTrue(!variable.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testToString() {
        OMVariable variable = new OMVariable("a");
        assertTrue(variable.toString().equals("<OMV name=\"a\"/>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testClone() {
        OMVariable variable = new OMVariable("a");
        assertTrue(((OMVariable) variable.clone()).getName().equals("a"));
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testCopy() {
        OMVariable variable = new OMVariable("a");
        assertTrue(((OMVariable) variable.copy()).getName().equals("a"));
    }

    /**
     * Test of isSame mathod of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testIsSame() {
        OMVariable var1 = new OMVariable("a");
        OMVariable var2 = new OMVariable("a");

        assertTrue(var1.isSame(var2));
    }

    /**
     * Test of isValid method of class
     * org.jscience.ml.openmath.OMVariable.
     */
    public void testIsValid() {
        OMVariable var1 = new OMVariable("a");

        assertTrue(var1.isValid());
    }
}
