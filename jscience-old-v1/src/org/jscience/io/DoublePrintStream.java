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

import java.io.PrintStream;


/**
 * a PrintStream that propagates writes to two PrintStream objects
 *
 * @author Holger Antelmann
 */
public class DoublePrintStream extends PrintStream {
    /** DOCUMENT ME! */
    PrintStream stream1;

    /** DOCUMENT ME! */
    PrintStream stream2;

/**
     * Creates a new DoublePrintStream object.
     *
     * @param stream1 DOCUMENT ME!
     * @param stream2 DOCUMENT ME!
     */
    public DoublePrintStream(PrintStream stream1, PrintStream stream2) {
        super(stream1);
        this.stream1 = stream1;
        this.stream2 = stream2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrintStream getPrintStream1() {
        return stream1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrintStream getPrintStream2() {
        return stream2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrintStream append(char c) {
        super.append(c);
        stream2.append(c);

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buf DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param len DOCUMENT ME!
     */
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        stream2.write(buf, off, len);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void write(int n) {
        super.write(n);
        stream2.write(n);
    }

    /**
     * DOCUMENT ME!
     */
    public void flush() {
        super.flush();
        stream2.flush();
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        super.close();
        stream2.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean checkError() {
        return (super.checkError() || stream2.checkError());
    }
}
