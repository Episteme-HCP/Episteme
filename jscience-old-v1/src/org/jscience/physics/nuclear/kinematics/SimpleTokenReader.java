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
 * Reads in values from a text file separated by whitespace (spaces, tabs,
 * carriage returns & linefeeds). Everything after the # sign on any line is
 * ignored as a comment. Anything between quotation marks (" "), is read in as
 * a single string.
 *
 * @author <a href="mailto:dale@visser.name">Dale Visser</a>
 */
public class SimpleTokenReader {
    /**
     * DOCUMENT ME!
     */
    private StreamTokenizer st;

    /**
     * DOCUMENT ME!
     */
    FileReader fr;

/**
     * Create an instance for reading in the file f.
     *
     * @param f text file to read from
     * @throws IOException if something goes wrong opening the file
     */
    public SimpleTokenReader(File f) throws IOException {
        fr = new FileReader(f);
        st = new StreamTokenizer(new BufferedReader(fr));
        st.commentChar('#');
        st.quoteChar('\"');
        st.eolIsSignificant(false);
    }

    /**
     * Reads an integer from the file, if the next token is a number.
     * Floating point numbers are cast to integer.
     *
     * @return integer value
     *
     * @throws IOException if the next token is not a number
     */
    public int readInteger() throws IOException {
        st.nextToken();

        if (st.ttype != StreamTokenizer.TT_NUMBER) {
            throw new IOException(this.getClass().getName() +
                ".readInteger(): Wrong token type: " + st.ttype);
        }

        return (int) st.nval;
    }

    /**
     * Reads an double from the file, if the next token is a number.
     *
     * @return floating point value
     *
     * @throws IOException if the next token is not a number
     */
    public double readDouble() throws IOException {
        st.nextToken();

        if (st.ttype != StreamTokenizer.TT_NUMBER) {
            throw new IOException(this.getClass().getName() +
                ".readInteger(): Wrong token type: " + st.ttype);
        }

        return st.nval;
    }

    /**
     * Reads a string from the file, either a single word or the entire
     * between two quotation marks (" ").
     *
     * @return the next token, if it's a String
     *
     * @throws IOException if the next token is not a String
     */
    public String readString() throws IOException {
        st.nextToken();

        if (st.ttype != StreamTokenizer.TT_WORD) {
            throw new IOException(this.getClass().getName() +
                ".readString(): Wrong token type: " + st.ttype);
        }

        System.out.println(st.sval);

        return st.sval;
    }

    /**
     * One may call this to close the underlying file.
     *
     * @throws IOException if there's a problem closing the file
     */
    public void close() throws IOException {
        fr.close();
    }
}
