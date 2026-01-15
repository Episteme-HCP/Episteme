/*
 * $Id: OMReferenceTest.java,v 1.3 2007-10-23 18:23:59 virtualcall Exp $
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
 * A JUnit test for an OpenMath reference.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMReferenceTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMReferenceTest(String testName) {
        super(testName);
    }

    /**
     * Get the test suite.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMReferenceTest.class);

        return suite;
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testClone() {
        OMReference reference = new OMReference();
        reference.setHref("#12");

        OMReference clone = (OMReference) reference.clone();
        assertTrue(reference.getHref() == clone.getHref());
    }

    /**
     * Test of copy method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testCopy() {
        OMReference reference = new OMReference();
        reference.setHref("#12");

        OMReference copy = (OMReference) reference.copy();
        assertTrue(reference.getHref() != copy.getHref());
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testGetType() {
        OMReference reference = new OMReference();
        assertTrue(reference.getType().equals("OMR"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testIsAtom() {
        OMReference reference = new OMReference();
        assertTrue(reference.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testIsComposite() {
        OMReference reference = new OMReference();
        assertTrue(!reference.isComposite());
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testIsSame() {
        OMReference reference1 = new OMReference();
        reference1.setHref("#11");

        OMReference reference2 = new OMReference();
        reference2.setHref("#11");

        assertTrue(reference1.isSame(reference2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testIsValid() {
        OMReference reference = new OMReference();
        reference.setHref("#11");

        assertTrue(reference.isValid());
    }

    /**
     * Test of getHref method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testGetHref() {
        OMReference reference = new OMReference();
        reference.setHref("#12");
        assertTrue(reference.getHref().equals("#12"));
    }

    /**
     * Test of setHref method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testSetHref() {
        OMReference reference = new OMReference();
        reference.setHref("#12");
        assertTrue(reference.getHref().equals("#12"));
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMReference.
     */
    public void testToString() {
        OMReference reference = new OMReference();
        reference.setHref("#12");
        assertTrue(reference.toString().equals("<OMR href=\"#12\"/>"));
    }
}
