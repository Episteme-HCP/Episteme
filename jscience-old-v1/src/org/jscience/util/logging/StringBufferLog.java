/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

/**
 * StringBufferLog writes log messages into a StringBuffer.
 *
 * @author Holger Antelmann
 */
public class StringBufferLog extends AbstractLogWriter {
    /** DOCUMENT ME! */
    static String lineBreak = System.getProperty("line.separator");

    /** DOCUMENT ME! */
    protected StringBuffer buffer;

/**
     * Creates a new StringBufferLog object.
     *
     * @param buffer DOCUMENT ME!
     */
    public StringBufferLog(StringBuffer buffer) {
        super(new StringLineFormatter());
        this.buffer = buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StringBuffer getStringBuffer() {
        return buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pattern DOCUMENT ME!
     */
    public void writeLogEntry(Object pattern) {
        buffer.append(pattern.toString());
    }
}
