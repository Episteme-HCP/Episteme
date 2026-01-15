/*
 *  $Id: PrettyPrintTest.java,v 1.3 2007-10-23 18:23:59 virtualcall Exp $
 *
 *  Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 *  All Rights Reserved.
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
package org.jscience.ml.openmath.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringBufferInputStream;


/**
 * A JUnit test class for the PrettyPrint class.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class PrettyPrintTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public PrettyPrintTest(String testName) {
        super(testName);
    }

    /**
     * Gets the suite of tests.<p></p>
     *
     * @return the suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(PrettyPrintTest.class);

        return suite;
    }

    /**
     * Test of setOutputStream method, of class
     * org.jscience.ml.openmath.util.PrettyPrint.
     */
    public void testSetOutputStream() {
        PrettyPrint prettyPrint = new PrettyPrint();
        prettyPrint.setOutputStream(new ByteArrayOutputStream());

        assertTrue(prettyPrint.getOutputStream() != null);
    }

    /**
     * Test of getOutputStream method, of class
     * org.jscience.ml.openmath.util.PrettyPrint.
     */
    public void testGetOutputStream() {
        PrettyPrint prettyPrint = new PrettyPrint();
        prettyPrint.setOutputStream(new ByteArrayOutputStream());

        assertTrue(prettyPrint.getOutputStream() != null);
    }

    /**
     * Test of setInputStream method, of class
     * org.jscience.ml.openmath.util.PrettyPrint.
     */
    public void testSetInputStream() {
        PrettyPrint prettyPrint = new PrettyPrint();
        prettyPrint.setInputStream(new ByteArrayInputStream(new byte[10]));

        assertTrue(prettyPrint.getInputStream() != null);
    }

    /**
     * Test of getInputStream method, of class
     * org.jscience.ml.openmath.util.PrettyPrint.
     */
    public void testGetInputStream() {
        PrettyPrint prettyPrint = new PrettyPrint();
        prettyPrint.setInputStream(new ByteArrayInputStream(new byte[10]));

        assertTrue(prettyPrint.getInputStream() != null);
    }

    /**
     * Test of prettyPrint method, of class
     * org.jscience.ml.openmath.util.PrettyPrint.
     */
    public void testPrettyPrint() {
        try {
            PrettyPrint prettyPrint = new PrettyPrint();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            prettyPrint.setInputStream(new StringBufferInputStream(
                    "<OMOBJ><OMI>1</OMI></OMOBJ>"));
            prettyPrint.setOutputStream(outputStream);
            prettyPrint.prettyPrint();

            assertTrue(outputStream.toString().indexOf("<OMI>") != -1);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }
}
