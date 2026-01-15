/*
 * $Id: OMXMLWriterTest.java,v 1.3 2007-10-23 18:23:59 virtualcall Exp $
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
