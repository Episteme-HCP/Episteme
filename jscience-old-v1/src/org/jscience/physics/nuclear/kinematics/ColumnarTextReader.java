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

package org.jscience.physics.nuclear.kinematics;

import java.io.*;


/**
 * Class for reading in spreadsheet-style text files.
 */
public class ColumnarTextReader extends InputStreamReader {
    /**
     * DOCUMENT ME!
     */
    private StringReader sr;

    /**
     * DOCUMENT ME!
     */
    private LineNumberReader lnr;

    /**
     * Creates a new ColumnarTextReader object.
     *
     * @param is DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public ColumnarTextReader(InputStream is)
        throws FileNotFoundException, IOException {
        super(is);
        lnr = new LineNumberReader(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void nextLine() throws IOException {
        sr = new StringReader(lnr.readLine());
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public String readString(int len) throws IOException {
        char[] temp;
        temp = new char[len];
        sr.read(temp);

        return new String(temp).trim();
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public int readInt(int len) throws IOException {
        return Integer.parseInt(readString(len));
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public double readDouble(int len) throws IOException {
        return Double.parseDouble(readString(len));
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void skipChars(int len) throws IOException {
        readString(len);
    }
}
