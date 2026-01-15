/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
