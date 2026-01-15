/*
 * $Id: OMSymbolTest.java,v 1.3 2007-10-23 18:23:59 virtualcall Exp $
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
 * A JUnit test for an OpenMath symbol.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMSymbolTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMSymbolTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Static suite method.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMSymbolTest.class);

        return suite;
    }

    /**
     * Test of getCD method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testGetCd() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.getCd().equals("cd"));
    }

    /**
     * Test of setCD method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testSetCd() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        symbol.setCD("hello");
        assertTrue(symbol.getCd().equals("hello"));
    }

    /**
     * Test of getName method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testGetName() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.getName().equals("name"));
    }

    /**
     * Test of setName method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testSetName() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        symbol.setName("hello");
        assertTrue(symbol.getName().equals("hello"));
    }

    /**
     * Test of getType method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testGetType() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.getType().equals("OMS"));
    }

    /**
     * Test of isAtom method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testIsAtom() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.isAtom());
    }

    /**
     * Test of isComposite method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testIsComposite() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(!symbol.isComposite());
    }

    /**
     * Test of toString method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testToString() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        assertTrue(symbol.toString().equals("<OMS cd=\"cd\" name=\"name\"/>"));
    }

    /**
     * Test of clone method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testClone() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        OMSymbol clone = (OMSymbol) symbol.clone();

        assertTrue((symbol.getCd() == clone.getCd()) &&
            (symbol.getName() == clone.getName()));
    }

    /**
     * Test of copy method, of class org.jscience.ml.openmath.OMSymbol.
     */
    public void testCopy() {
        OMSymbol symbol = new OMSymbol("cd", "name");
        OMSymbol clone = (OMSymbol) symbol.clone();

        assertTrue((symbol.getCd() == clone.getCd()) &&
            (symbol.getName() == clone.getName()));
    }

    /**
     * Test of isSame method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testIsSame() {
        OMSymbol symbol1 = new OMSymbol("a", "b");
        OMSymbol symbol2 = new OMSymbol("a", "b");

        assertTrue(symbol1.isSame(symbol2));
    }

    /**
     * Test of isValid method, of class
     * org.jscience.ml.openmath.OMSymbol.
     */
    public void testIsValid() {
        OMSymbol symbol = new OMSymbol("a", "b");

        assertTrue(symbol.isValid());
    }
}
