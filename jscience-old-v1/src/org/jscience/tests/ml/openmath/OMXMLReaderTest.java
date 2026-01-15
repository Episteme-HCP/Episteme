/*
 * $Id: OMXMLReaderTest.java,v 1.3 2007-10-23 18:23:59 virtualcall Exp $
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

import org.jscience.ml.openmath.*;


/**
 * A JUnit test for the OMXMLReader class.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMXMLReaderTest extends TestCase {
/**
     * Constructor. <p>
     */
    public OMXMLReaderTest(String testName) {
        super(testName);
    }

    /**
     * Get the test suite.<p></p>
     *
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMXMLReaderTest.class);

        return suite;
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMApplication() {
        try {
            String string = "<OMOBJ><OMA><OMS cd=\"arith1\" name=\"plus\"/><OMI>1</OMI><OMI>12</OMI></OMA></OMOBJ>";
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();

            OMApplication application = new OMApplication();
            application.addElement(new OMSymbol("arith1", "plus"));
            application.addElement(new OMInteger("1"));
            application.addElement(new OMInteger("12"));

            assertTrue(object.isSame(application));
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMApplication2() {
        try {
            String string = "<om:OMOBJ xmlns:om=\"http://www.openmath.org/OpenMath\">" +
                "<om:OMA>" + "<om:OMS cd=\"arith1\" name=\"plus\"/>" +
                "<om:OMI>1</om:OMI>" + "<om:OMI>12</om:OMI>" + "</om:OMA>" +
                "</om:OMOBJ>";
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();

            OMApplication application = new OMApplication();
            application.addElement(new OMSymbol("arith1", "plus"));
            application.addElement(new OMInteger("1"));
            application.addElement(new OMInteger("12"));

            assertTrue(object.isSame(application));
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMInteger() {
        try {
            String string = "<OMOBJ><OMI>1</OMI></OMOBJ>";
            OMXMLReader reader = new OMXMLReader(string);
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
            OMXMLReader reader = new OMXMLReader(string);
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
    public void testReadOMInteger3() {
        try {
            String string = "<OMOBJ><OMI>1</OMI></OMOBJ>";
            OMXMLReader reader = new OMXMLReader(string, true);
            OMObject object = reader.readObject();
            OMRoot root = (OMRoot) object;

            assertTrue(root.getType().equals("OMOBJ") &&
                root.getObject() instanceof OMInteger);
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of readObject method, of class
     * org.jscience.ml.openmath.io.OMXMLReader.
     */
    public void testReadOMInteger4() {
        try {
            String string = "<OMOBJ><OMI>1</OMI></OMOBJ>";
            OMXMLReader reader = new OMXMLReader(string, true);
            OMObject object = reader.readObject();

            assertTrue(object instanceof OMRoot);
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
            OMXMLReader reader = new OMXMLReader(string);
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
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();

            assertTrue(object instanceof OMReference);
        } catch (Throwable throwable) {
            throwable.printStackTrace();

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
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();

            assertTrue(object instanceof OMForeign);
        } catch (Throwable throwable) {
            throwable.printStackTrace();

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
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();
            OMForeign foreign = (OMForeign) object;

            assertTrue(foreign.getObject().toString().equals("<bla/>"));
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }
}
