/*
 * $Id: OMDOMReaderTest.java,v 1.3 2007-10-23 18:23:58 virtualcall Exp $
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
package org.jscience.ml.openmath.io;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.ml.openmath.OMForeign;
import org.jscience.ml.openmath.OMInteger;
import org.jscience.ml.openmath.OMObject;
import org.jscience.ml.openmath.OMReference;


/**
 * A JUnit test class for OMDOMReader.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org
 * @version $Revision: 1.3 $
 */
public class OMDOMReaderTest extends TestCase {
/**
     * Constructor. <p>
     *
     * @param testName the name of the test.
     */
    public OMDOMReaderTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Get the test suite.<p></p>
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMDOMReaderTest.class);

        return suite;
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMInteger() {
        try {
            String string = "<OMOBJ><OMI>1</OMI></OMOBJ>";
            OMDOMReader reader = new OMDOMReader(string);
            OMObject object = reader.readObject();
            OMInteger integer = new OMInteger("1");

            assertTrue(object.isSame(integer));
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMInteger2() {
        try {
            String string = "<OMOBJ><OMI id=\"blabla\">1</OMI></OMOBJ>";
            OMDOMReader reader = new OMDOMReader(string);
            OMObject object = reader.readObject();

            assertTrue(object instanceof OMInteger);
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMInteger5() {
        try {
            String string = "<OMI>1</OMI>";
            OMDOMReader reader = new OMDOMReader(string);
            OMObject object = reader.readObject();

            assertTrue(object instanceof OMInteger);
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMReference() {
        try {
            String string = "<OMOBJ><OMR href=\"#12\"/></OMOBJ>";
            OMDOMReader reader = new OMDOMReader(string);
            OMObject object = reader.readObject();

            assertTrue(object instanceof OMReference);
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
            fail(throwable.getMessage());
        }
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMForeign() {
        try {
            String string = "<OMOBJ><OMFOREIGN><bla/></OMFOREIGN></OMOBJ>";
            OMDOMReader reader = new OMDOMReader(string);
            OMObject object = reader.readObject();

            assertTrue(object instanceof OMForeign);
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
            fail(throwable.getMessage());
        }
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMForeign2() {
        try {
            String string = "<OMOBJ><OMFOREIGN><bla/></OMFOREIGN></OMOBJ>";
            OMDOMReader reader = new OMDOMReader(string);
            OMObject object = reader.readObject();
            OMForeign foreign = (OMForeign) object;

            assertTrue(foreign.getObject().toString().equals("<bla/>"));
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
            fail(throwable.getMessage());
        }
    }
}
