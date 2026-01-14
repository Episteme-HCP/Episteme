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

package org.jscience.architecture.traffic.xml;

import java.io.*;


/**
 * InputStreamReader with extended functionality for use  by the XML Loader
 */

// TODO :  - Write Junit test together with XMLLoader
class XMLInputReader extends InputStreamReader {
    /** DOCUMENT ME! */
    protected final int bufferSize = 100;

    /** DOCUMENT ME! */
    protected int bufferBegin;

    /** DOCUMENT ME! */
    protected int bufferEnd;

    /** DOCUMENT ME! */
    protected boolean atEOF;

    /** DOCUMENT ME! */
    protected char[] buffer;

/**
     * Make a new XMLInputReader
     *
     * @param file The file to read the data from
     * @throws IOException If the file cannot be opened because of an IO error
     */
    protected XMLInputReader(File file) throws IOException {
        super(new FileInputStream(file));
        atEOF = false;
        bufferEnd = 0;
        bufferBegin = 0;
        buffer = new char[bufferSize];
    }

    /**
     * Fill the internal buffer of this Reader
     *
     * @throws EOFException If the buffer can't be filled because we have
     *         arrived at the end of the underlying file.
     * @throws IOException If the reader cannot read because of an IO error
     */
    protected void fillBuffer() throws EOFException, IOException {
        if (bufferIsEmpty()) {
            bufferBegin = 0;
            bufferEnd = super.read(buffer, bufferBegin, bufferSize);

            if (bufferEnd == -1) {
                bufferEnd = 0;
                atEOF = true;
                throw new EOFException();
            }
        }
    }

    /**
     * Read next XML tag from the file
     *
     * @return The tag which is read
     *
     * @throws IOException If a new tag cannot be read because of an IO error
     * @throws EOFException If a new tag cannot be read because we have arrived
     *         at the end of the underlying file.
     */
    public String readNextTag() throws IOException, EOFException {
        ignoreUntilChar('<', false);

        String result = readUntilChar('>', true);

        //System.out.println("XML stream produced tag "+result);
        return result;
    }

    /* @return A boolean which indicates if the buffer is empty
    */
    protected boolean bufferIsEmpty() {
        return bufferBegin == bufferEnd;
    }

    /**
     * Read a line from the file
     *
     * @return A string which contains the next line of the file
     *
     * @throws IOException If a new tag cannot be read because of an IO error
     * @throws EOFException If a new tag cannot be read because we have arrived
     *         at the end of the underlying file.
     */
    protected String readln() throws IOException, EOFException {
        String result = readUntilChar('\n', false);
        bufferBegin++;

        return result;
    }

    /**
     * Read a string from the file until we encounter a specific
     * character.
     *
     * @param c The specific char
     * @param including True if the char should be included in the string,
     *        false if it should be discarded
     *
     * @return The result
     *
     * @throws EOFException If a new tag cannot be read because we have arrived
     *         at the end of the underlying file.
     * @throws IOException If a new tag cannot be read because of an IO error
     */
    protected String readUntilChar(char c, boolean including)
        throws EOFException, IOException {
        String result = "";
        int t;

        while (true) {
            fillBuffer();

            for (t = bufferBegin; t < bufferEnd; t++) {
                if (c == buffer[t]) {
                    int endPos = t - bufferBegin;

                    if (including) {
                        endPos++;
                    }

                    result += (new String(buffer, bufferBegin, endPos));
                    bufferBegin = t;

                    return result;
                }
            }

            result += (new String(buffer, bufferBegin, bufferEnd - bufferBegin));
            bufferBegin = bufferEnd;

            // This statement is superfluous, just to make really sure the
            // loop is eventually terminated if the char is never encountered.
            if (atEOF) {
                throw new EOFException();
            }
        }
    }

    /**
     * Move the buffer to the first occurence of a certain char in the
     * file.
     *
     * @param c The specific char
     * @param including True if the buffer should be moved to one position
     *        after the char, false if it should be positioned at the char
     *        itself.
     *
     * @throws IOException If a new tag cannot be read because of an IO error
     */
    protected void ignoreUntilChar(char c, boolean including)
        throws IOException {
        try {
            while (!atEOF) {
                fillBuffer();

                // Check the first char in the buffer
                if ((bufferBegin < bufferEnd) && (buffer[bufferBegin] == c)) {
                    if (including) {
                        bufferBegin++;
                    }

                    return;
                }

                for (; bufferBegin < bufferEnd; bufferBegin++) {
                    if (buffer[bufferBegin] == c) {
                        if (including) {
                            bufferBegin++;
                        }

                        return;
                    }
                }
            }
        } catch (EOFException e) { // EOFExceptions are OK. 
        }
    }
}
