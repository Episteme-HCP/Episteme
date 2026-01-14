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

import java.io.StringWriter;


/**
 * A JUnit test for the OMXMLWriter class.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMXMLWriterTest extends TestCase {
/**
     * Constructor. <p>
     */
    public OMXMLWriterTest(String testName) {
        super(testName);
    }

    /**
     * Return the test suite.<p></p>
     *
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(OMXMLWriterTest.class);

        return suite;
    }

    /**
     * Test of writeObject method, of
     * class.org.jscience.ml.openmath.io.OMXMLWriter.
     */
    public void testWriteApplication() {
        try {
            OMApplication application = new OMApplication();
            application.addElement(new OMSymbol("a", "b"));
            application.addElement(new OMInteger("1"));

            StringWriter stringWriter = new StringWriter();
            OMXMLWriter writer = new OMXMLWriter(stringWriter);
            writer.writeObject(application);
            writer.flush();

            String string = stringWriter.toString();
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();

            assertTrue(application.isSame(object));
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of writeObject method, of class
     * org.jscience.ml.openmath.io.OMXMLWriter.
     */
    public void testWriteInteger() {
        try {
            OMInteger integer = new OMInteger("1");
            StringWriter stringWriter = new StringWriter();
            OMXMLWriter writer = new OMXMLWriter(stringWriter);
            writer.writeObject(integer);
            writer.flush();

            String string = stringWriter.toString();
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();

            assertTrue(integer.isSame(object));
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of writeObject method, of class
     * org.jscience.ml.openmath.io.OMXMLWriter.
     */
    public void testWriteReference() {
        try {
            OMReference reference = new OMReference("#11");
            StringWriter stringWriter = new StringWriter();
            OMXMLWriter writer = new OMXMLWriter(stringWriter);
            writer.writeObject(reference);
            writer.flush();

            String string = stringWriter.toString();
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();

            assertTrue(reference.isSame(object));
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }

    /**
     * Test of writeObject method, of class
     * org.jscience.ml.openmath.io.OMXMLWriter.
     */
    public void testWriteForeign() {
        try {
            OMForeign foreign = new OMForeign();
            StringWriter stringWriter = new StringWriter();
            OMXMLWriter writer = new OMXMLWriter(stringWriter);
            writer.writeObject(foreign);
            writer.flush();

            String string = stringWriter.toString();
            OMXMLReader reader = new OMXMLReader(string);
            OMObject object = reader.readObject();

            assertTrue(foreign.isSame(object));
        } catch (Throwable throwable) {
            throwable.printStackTrace();

            fail(throwable.getMessage());
        }
    }
}
