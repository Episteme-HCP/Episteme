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

package org.jscience.io;

import java.io.*;

import java.util.Vector;


/**
 * Text reader, reads data text files/streams. This class uses buffered
 * I/O.
 *
 * @author Mark Hale
 * @version 1.7
 */
public final class TextReader extends InputStreamReader {
    /** DOCUMENT ME! */
    private final BufferedReader reader = new BufferedReader(this);

/**
     * Reads text data from an input stream.
     *
     * @param stream DOCUMENT ME!
     */
    public TextReader(InputStream stream) {
        super(stream);
    }

/**
     * Reads a text file with the specified system dependent file name.
     *
     * @param name the system dependent file name.
     * @throws FileNotFoundException If the file is not found.
     */
    public TextReader(String name) throws FileNotFoundException {
        super(new FileInputStream(name));
    }

/**
     * Reads a text file with the specified File object.
     *
     * @param file the file to be opened for reading.
     * @throws FileNotFoundException If the file is not found.
     */
    public TextReader(File file) throws FileNotFoundException {
        super(new FileInputStream(file));
    }

    /**
     * Read a single character.
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException If an I/O error occurs.
     */
    public int read() throws IOException {
        return reader.read();
    }

    /**
     * Reads data to an array.
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException If an I/O error occurs.
     */
    public double[][] readArray() throws IOException {
        final Vector data = new Vector(10);

        for (int ch = read(); (ch != '\n') && (ch != -1);) {
            if (isNumber(ch)) {
                StringBuffer str = new StringBuffer();

                do {
                    str.append((char) ch);
                    ch = read();
                } while (isNumber(ch));

                data.addElement(str.toString());
            }

            while (!isNumber(ch) && (ch != '\n') && (ch != -1))
                ch = read();
        }

        int cols = data.size();
        int rows = 1;

        for (int ch = read(); ch != -1;) {
            if (isNumber(ch)) {
                StringBuffer str = new StringBuffer();

                do {
                    str.append((char) ch);
                    ch = read();
                } while (isNumber(ch));

                data.addElement(str.toString());
            }

            while (!isNumber(ch) && (ch != '\n') && (ch != -1))
                ch = read();

            if (ch == '\n') {
                ch = read();
                rows++;
            }
        }

        double[][] array = new double[rows][cols];

        for (int j, i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++)
                array[i][j] = Double.parseDouble(data.elementAt((i * cols) + j)
                                                     .toString());
        }

        return array;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ch DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isNumber(int ch) {
        if (Character.isDigit((char) ch) || (ch == '.') || (ch == '+') ||
                (ch == '-') || (ch == 'e') || (ch == 'E')) {
            return true;
        } else {
            return false;
        }
    }
}
