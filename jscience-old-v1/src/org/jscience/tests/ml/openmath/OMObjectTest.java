/*
 * $Id: OMObjectTest.java,v 1.3 2007-10-23 18:23:58 virtualcall Exp $
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

import java.util.Hashtable;


/**
 * A JUnit test for an OpenMath object.
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMObjectTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMObjectTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMObjectTest.class);

        return suite;
    }

    /**
     * Test of getAttributes method, of class
     * org.jscience.ml.openmath.OMObject.
     */
    public void testGetAttributes() {
        OMVariable variable = new OMVariable("a");
        Hashtable attributes = new Hashtable();
        variable.setAttributes(attributes);

        assertTrue(variable.getAttributes() != null);
    }

    /**
     * Test of setAttributes method, of class
     * org.jscience.ml.openmath.OMObject.
     */
    public void testSetAttributes() {
        OMVariable variable = new OMVariable("a");
        Hashtable attributes = new Hashtable();
        attributes.put("id", "hallo");
        variable.setAttributes(attributes);

        assertTrue(((String) variable.getAttribute("id")).equals("hallo"));
    }

    /**
     * Test of getAttribute method, of class
     * org.jscience.ml.openmath.OMObject.
     */
    public void testGetAttribute() {
        OMVariable variable = new OMVariable("a");
        variable.setAttribute("id", "hallo");
        assertTrue(variable.getAttribute("id").equals("hallo"));
    }

    /**
     * Test of setAttribute method, of class
     * org.jscience.ml.openmath.OMObject.
     */
    public void testSetAttribute() {
        OMVariable variable = new OMVariable("a");
        variable.setAttribute("id", "bye");
        assertTrue(variable.getAttribute("id").equals("bye"));
    }

    /**
     * Test of removeAttribute method, of class
     * org.jscience.ml.openmath.OMObject.
     */
    public void testRemoveAttribute() {
        OMVariable variable = new OMVariable("a");
        variable.setAttribute("id", "bye");
        variable.removeAttribute("id");

        assertTrue(variable.getAttribute("id") == null);
    }
}
