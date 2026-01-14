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

package org.jscience.util.logging;

import java.io.Closeable;
import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * writes LogEntry objects formatted as XML to either a PrintStream or
 * PrintWriter. As the XMLWriter uses a PrintStream/PrintWriter to write the
 * log entries, no exception is thrown during logging. You'll have to check
 * the PrintStream/PrintWriter object to check for errors.
 *
 * @author Holger Antelmann
 */
public class XMLWriter extends AbstractLogWriter implements Closeable {
    /** DOCUMENT ME! */
    PrintStream ps;

    /** DOCUMENT ME! */
    PrintWriter pw;

    /** DOCUMENT ME! */
    boolean flush = true;

/**
     * Creates a new XMLWriter object.
     *
     * @param ps DOCUMENT ME!
     */
    public XMLWriter(PrintStream ps) {
        super(new XMLLogFormatter());
        this.ps = ps;
    }

/**
     * Creates a new XMLWriter object.
     *
     * @param pw DOCUMENT ME!
     */
    public XMLWriter(PrintWriter pw) {
        super(new XMLLogFormatter());
        this.pw = pw;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrintStream getPrintStream() {
        return ps;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PrintWriter getPrintWriter() {
        return pw;
    }

    /**
     * only needed if <code>getAlwaysFlush()</code> returns false
     */
    public void flush() {
        if (ps != null) {
            ps.flush();
        }

        if (pw != null) {
            pw.flush();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setAlwaysFlush(boolean flag) {
        flush = flag;
    }

    /**
     * true by default
     *
     * @return DOCUMENT ME!
     */
    public boolean getAlwaysFlush() {
        return flush;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pattern DOCUMENT ME!
     */
    public synchronized void writeLogEntry(Object pattern) {
        if (ps != null) {
            ps.println(pattern.toString());
        }

        if (pw != null) {
            pw.println(pattern.toString());
        }

        if (flush) {
            if (ps != null) {
                ps.flush();
            }

            if (pw != null) {
                pw.flush();
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        if (ps != null) {
            ps.close();
        }

        if (pw != null) {
            pw.close();
        }
    }
}
