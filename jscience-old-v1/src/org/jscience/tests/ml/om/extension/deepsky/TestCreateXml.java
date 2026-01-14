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

/* ====================================================================
 * extension/deepSky/test/TestCreateXml
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.tests.ml.om.extension.deepsky;

import org.jscience.ml.om.IObservation;
import org.jscience.ml.om.RootElement;
import org.jscience.ml.om.util.SchemaException;

import java.io.File;


/**
 * Simple Test class that writes an XML
 */
public class TestCreateXml {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(
                "Need to pass xmlFilePath as argument. E.g. /home/john/myTestObservation.xml");

            return;
        }

        String xmlFilePath = args[0];

        DeepSkyTestUtil dst = new DeepSkyTestUtil();

        IObservation obs1 = dst.createDeepSkyObservation();
        IObservation obs2 = dst.createDeepSkyObservation2();
        IObservation obs3 = dst.createDeepSkyObservation3();

        RootElement observations = new RootElement();

        try {
            observations.addObservation(obs1);
            observations.addObservation(obs2);
            observations.addObservation(obs3);
        } catch (SchemaException schemaException) {
            System.err.println(
                "Cannot add DeepSkyObservation. Nested Exception is:" +
                schemaException);
        }

        try {
            observations.serializeAsXml(new File(xmlFilePath));
        } catch (SchemaException schemaException) {
            System.err.println("Cannot serialized XML. nested Exception is: " +
                schemaException);
        }
    }
}
