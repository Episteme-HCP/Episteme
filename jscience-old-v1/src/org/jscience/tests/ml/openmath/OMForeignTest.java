/*
 * $Id: OMForeignTest.java,v 1.3 2007-10-23 18:23:58 virtualcall Exp $
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
 * A JUnit test for OpenMath foreign.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 */
public class OMForeignTest extends TestCase {
/**
     * Constructor. <p>
     */
    public OMForeignTest(String testName) {
        super(testName);
    }

    /**
     * Get the test suite.<p></p>
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMForeignTest.class);

        return suite;
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testClone() {
        OMForeign foreign = new OMForeign();
        foreign.setObject("12");

        OMForeign clone = (OMForeign) foreign.clone();

        assertTrue(foreign.getObject() == clone.getObject());
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testCopy() {
        OMForeign foreign = new OMForeign();
        foreign.setObject("12");

        OMForeign copy = (OMForeign) foreign.copy();

        assertTrue(foreign.getObject() == copy.getObject());
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testGetType() {
        OMForeign foreign = new OMForeign();

        assertTrue(foreign.getType().equals("OMFOREIGN"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testIsAtom() {
        OMForeign foreign = new OMForeign();

        assertTrue(foreign.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testIsComposite() {
        OMForeign foreign = new OMForeign();

        assertTrue(!foreign.isComposite());
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testIsSame() {
        OMForeign foreign1 = new OMForeign();
        foreign1.setObject("bla");

        OMForeign foreign2 = new OMForeign();
        foreign2.setObject("bla");

        assertTrue(foreign1.isSame(foreign2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testIsValid() {
        OMForeign foreign = new OMForeign();
        foreign.setObject(new String("bla"));

        assertTrue(foreign.isValid());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMForeign.
     */
    public void testToString() {
        OMForeign foreign = new OMForeign();

        assertTrue(foreign.toString().equals("<OMFOREIGN></OMFOREIGN>"));
    }
}
