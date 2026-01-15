/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.util.Filter;


/**
 * The LogWriter interface defines objects that can be used
 * as a handler for the Logger class.
 * It is left to the discretion of the LogWriter implementation
 * to perform the write synchronously or asynchronously.
 * LogWriter extends the interface Filter<LogEntry> so that each
 * LogWriter embedds the functionality of being able to filter
 * each LogEntry that is to be written.
 *
 * @author Holger Antelmann
 * @see LogException
 * @see Logger
 */
public interface LogWriter extends Filter<LogEntry> {
    /**
     * writes the given LogEntry to the log of this LogWriter. This
     * method must not check whether entries are accepted, as the Logger class
     * will only call this method once checked that this LogWriter accepts the
     * given entry.
     *
     * @param entry DOCUMENT ME!
     *
     * @throws LogException if an error occurred while writing the log
     */
    void write(LogEntry entry) throws LogException;
}
