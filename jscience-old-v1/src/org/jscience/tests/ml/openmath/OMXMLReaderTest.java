/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
