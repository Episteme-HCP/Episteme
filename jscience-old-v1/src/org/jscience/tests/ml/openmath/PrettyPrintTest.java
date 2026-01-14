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
