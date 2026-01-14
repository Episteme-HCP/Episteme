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

package org.jscience.io.openmath;

import org.jscience.ml.openmath.OMObject;

import org.xml.sax.InputSource;

import java.io.*;


/**
 * Pretty prints an OpenMath object.<p></p>
 *
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Version$
 */
public class PrettyPrint {
    /**
     * Stores the input-stream.<p></p>
     */
    private InputStream inputStream = null;

    /**
     * Stores the output-stream.<p></p>
     */
    private PrintStream outputStream = null;

    /**
     * Sets the output-stream.<p></p>
     *
     * @param output set the output stream.
     */
    public final void setOutputStream(final OutputStream output) {
        this.outputStream = new PrintStream(output);
    }

    /**
     * Sets the input-stream.<p></p>
     *
     * @param input set the input stream.
     */
    public final void setInputStream(final InputStream input) {
        this.inputStream = input;
    }

    /**
     * Validates the document.<p></p>
     *
     * @throws Exception throw if we cannot pretty print the document for some
     *         obscure reason.
     */
    public void prettyPrint() throws Exception {
        OMXMLReader tReader = new OMXMLReader(new InputSource(inputStream));
        OMObject tObject = tReader.readObject();
        OutputStreamWriter tWriter = new OutputStreamWriter(outputStream);
        OMXMLWriter tXMLWriter = new OMXMLWriter(tWriter);

        tXMLWriter.writeObject(tObject);
        tXMLWriter.flush();
    }

    /**
     * Invokes the pretty-printer stand-alone.<p></p>
     *
     * @param fArguments the arguments passed to the program.
     */
    public static void main(String[] fArguments) {
        try {
            FileInputStream tInputStream = new FileInputStream(fArguments[0]);

            PrettyPrint tValidator = new PrettyPrint();
            PrintStream tOutputStream = new PrintStream(System.out);

            tValidator.setInputStream(tInputStream);
            tValidator.setOutputStream(tOutputStream);

            tValidator.prettyPrint();
        } catch (Exception tException) {
            tException.printStackTrace();
        }
    }
}
