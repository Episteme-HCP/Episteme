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

import org.jscience.util.Filter;


/**
 * provides filtering capabilities for a LogWriter. Extending classes only
 * need to implement <code>writeLogEntry(Object)</code>,
 *
 * @author Holger Antelmann
 */
public abstract class AbstractLogWriter implements LogWriter {
    /**
     * DOCUMENT ME!
     */
    protected Filter<LogEntry> filter = null;

    /**
     * DOCUMENT ME!
     */
    protected LogEntryFormatter formatter = null;

    /**
     * Creates a new AbstractLogWriter object.
     */
    protected AbstractLogWriter() {
    }

    /**
     * Creates a new AbstractLogWriter object.
     *
     * @param formatter DOCUMENT ME!
     */
    protected AbstractLogWriter(LogEntryFormatter formatter) {
        this(null, formatter);
    }

    /**
     * Creates a new AbstractLogWriter object.
     *
     * @param filter DOCUMENT ME!
     */
    protected AbstractLogWriter(Filter<LogEntry> filter) {
        this(filter, null);
    }

    /**
     * Creates a new AbstractLogWriter object.
     *
     * @param filter    DOCUMENT ME!
     * @param formatter DOCUMENT ME!
     */
    protected AbstractLogWriter(Filter<LogEntry> filter,
                                LogEntryFormatter formatter) {
        this.filter = filter;
        this.formatter = formatter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Filter<LogEntry> getLogFilter() {
        return filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void setLogFilter(Filter<LogEntry> filter) {
        this.filter = filter;
    }

    /**
     * returns true if either the filter is null or the filter accepts
     * the given entry
     *
     * @param entry DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean accept(LogEntry entry) {
        if (filter == null) {
            return true;
        }

        return filter.accept(entry);
    }

    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void write(LogEntry entry) throws LogException {
        if ((filter != null) && (!filter.accept(entry))) {
            return;
        }

        Object pattern = entry;

        if (formatter != null) {
            pattern = formatter.format(entry);
        }

        writeLogEntry(pattern);
    }

    /**
     * writes the pattern to the log entity. If the LogEntryFormatter
     * is null, the pattern is the LogEntry object itself; otherwise, the
     * pattern is the result of formatting the LogEntry with the given
     * LogEntryFormatter.
     *
     * @param pattern DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    protected abstract void writeLogEntry(Object pattern)
            throws LogException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected LogEntryFormatter getLogFormatter() {
        return formatter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param formatter DOCUMENT ME!
     */
    protected void setLogFormatter(LogEntryFormatter formatter) {
        this.formatter = formatter;
    }
}
