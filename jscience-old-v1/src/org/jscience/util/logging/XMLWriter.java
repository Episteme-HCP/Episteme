/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
