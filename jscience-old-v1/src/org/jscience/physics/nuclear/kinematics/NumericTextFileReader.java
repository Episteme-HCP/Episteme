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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;


/**
 * Simple class to encapsulate reading text files containing only numbers.
 * No formatting other than numbers separated by whitespace is assumed. It is
 * up to the user to interpret the numbers read properly.
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class NumericTextFileReader extends Object {
    /**
     * DOCUMENT ME!
     */
    private StreamTokenizer tokenizer;

    /**
     * DOCUMENT ME!
     */
    private double[] numberList = new double[5000];

    /**
     * DOCUMENT ME!
     */
    private int size = 0;

    /**
     * DOCUMENT ME!
     */
    private int nextAccess = 0;

/**
     * Creates new NumericTextFileReader
     */
    public NumericTextFileReader(File input) {
        try {
            tokenizer = new StreamTokenizer(new FileReader(input));
            tokenizer.eolIsSignificant(false); //treat end of line as white space
            processFile();
        } catch (IOException ioe) {
            System.err.println(getClass().getName() + " constructor: " + ioe);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void processFile() throws IOException {
        tokenizer.nextToken();

        while (tokenizer.ttype != StreamTokenizer.TT_EOF) {
            if (tokenizer.ttype != StreamTokenizer.TT_NUMBER) {
                throw new IOException(this.getClass().getName() +
                    ".read(): Wrong token type: " + tokenizer.ttype);
            }

            numberList[size] = tokenizer.nval;
            size++;
            tokenizer.nextToken();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize() {
        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double read() {
        nextAccess++;

        return numberList[nextAccess - 1];
    }
}
