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
