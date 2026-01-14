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

import org.jscience.ml.openmath.OMObject;
import org.jscience.ml.openmath.io.OMXMLReader;
import org.jscience.ml.openmath.io.OMXMLWriter;

import org.xml.sax.InputSource;

import java.io.*;


/**
 * Pretty prints an OpenMath object.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class PrettyPrint {
    /**
     * Stores the input-stream.<p></p>
     */
    private InputStream inputStream = null;

    /**
     * Stores the output-stream.<p></p>
     */
    private OutputStream outputStream = null;

    /**
     * Sets the output-stream.<p></p>
     *
     * @param newOutputStream set the output stream.
     */
    public void setOutputStream(OutputStream newOutputStream) {
        outputStream = newOutputStream;
    }

    /**
     * Gets the output-stream.<p></p>
     *
     * @return the output stream.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Sets the input-stream.<p></p>
     *
     * @param newInputStream set the input stream.
     */
    public void setInputStream(InputStream newInputStream) {
        inputStream = newInputStream;
    }

    /**
     * Gets the input-stream.<p></p>
     *
     * @return the input stream.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Validates the document.<p></p>
     *
     * @throws Exception throw if we cannot pretty print the document for some
     *         obscure reason.
     */
    public void prettyPrint() throws Exception {
        OMXMLReader reader = new OMXMLReader(new InputSource(inputStream));
        OMObject object = reader.readObject();

        OutputStreamWriter writer = new OutputStreamWriter(new PrintStream(
                    outputStream));
        OMXMLWriter xmlWriter = new OMXMLWriter(writer);

        xmlWriter.writeObject(object);
        xmlWriter.flush();
    }

    /**
     * Invokes the pretty-printer stand-alone.<p></p>
     *
     * @param arguments the arguments passed to the program.
     */
    public static void main(String[] arguments) {
        try {
            FileInputStream inputStream = new FileInputStream(arguments[0]);
            PrettyPrint prettyPrint = new PrettyPrint();
            PrintStream outputStream = new PrintStream(System.out);

            prettyPrint.setInputStream(inputStream);
            prettyPrint.setOutputStream(outputStream);
            prettyPrint.prettyPrint();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
