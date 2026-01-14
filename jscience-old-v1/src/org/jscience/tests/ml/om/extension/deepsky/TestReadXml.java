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
 * extension/deepSky/test/TestReadXml
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om.extension.deepsky.test;

import org.jscience.ml.om.FGCAException;
import org.jscience.ml.om.RootElement;
import org.jscience.ml.om.util.SchemaLoader;

import java.io.File;


/**
 * Simple Test class that reads a XML and writes it again
 */
public class TestReadXml {
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                "Need to pass xmlFilePath and newXmlFilePath as arguments. E.g. /home/john/myTestObservation.xml /home/john/myNewTestObservation.xml");

            return;
        }

        SchemaLoader loader = null;

        try {
            // Read
            loader = new SchemaLoader();

            RootElement obs = loader.load(new File(args[0]),
                    new File("file:/home/dirk/programming/java/observation/xml/basic/comast14.xsd"));

            obs.serializeAsXml(new File(args[1]));
        } catch (FGCAException fgca) {
            System.err.println("Error while loading document: " +
                fgca.getMessage());
        }
    }
}
