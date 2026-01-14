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
