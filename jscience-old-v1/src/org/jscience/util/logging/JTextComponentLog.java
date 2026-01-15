/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import javax.swing.text.JTextComponent;


/**
 * JTextComponentLog writes abbreviated log messages into a given
 * JTextComponent synchronously. Uses StringLineFormatter.
 *
 * @author Holger Antelmann
 *
 * @see StringLineFormatter
 * @see LogException
 * @see Logger
 * @see LogEntry
 */
public class JTextComponentLog extends AbstractLogWriter {
    /** DOCUMENT ME! */
    StringBuffer buffer;

    /** DOCUMENT ME! */
    JTextComponent jtc;

    /** DOCUMENT ME! */
    int limit = 0;

/**
     * Creates a new JTextComponentLog object.
     *
     * @param jtc DOCUMENT ME!
     */
    public JTextComponentLog(JTextComponent jtc) {
        super(new StringLineFormatter());
        this.jtc = jtc;
        buffer = new StringBuffer();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JTextComponent getComponent() {
        return jtc;
    }

    /**
     * purges the log buffer
     */
    public void purge() {
        buffer.delete(0, buffer.length());
        jtc.setText(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param limit DOCUMENT ME!
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLimit() {
        return limit;
    }

    /**
     * This method appends a short version of the entry to internal
     * buffer that is written to the embedded text component.
     *
     * @param pattern DOCUMENT ME!
     */
    public void writeLogEntry(Object pattern) {
        buffer.append(pattern.toString());

        if (limit > 0) {
            while (buffer.length() > limit)
                buffer.deleteCharAt(0);
        }

        jtc.setText(buffer.toString());
        jtc.setCaretPosition(buffer.length());
    }
}
