/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.io.ExtendedFile;

import org.jscience.util.Debug;
import org.jscience.util.Filter;
import org.jscience.util.StringUtils;

import java.io.File;
import java.io.IOException;

import java.util.Date;


/**
 * writes full stack trace of entries that log an exception to a given
 * file.
 *
 * @author Holger Antelmann
 */
public class StackTraceFileWriter extends AbstractLogWriter {
    /** DOCUMENT ME! */
    static Filter<LogEntry> exceptionFilter = new Filter<LogEntry>() {
            public boolean accept(LogEntry entry) {
                return entry.getThrown() != null;
            }
        };

    /** DOCUMENT ME! */
    static LogEntryFormatter traceFormat = new LogEntryFormatter() {
            public Object format(LogEntry entry) {
                String s = "logged exception at " + new Date(entry.getTime()) +
                    ":" + StringUtils.lb +
                    Debug.stackTraceAsString(entry.getThrown());

                return s;
            }
        };

    /** DOCUMENT ME! */
    ExtendedFile file;

/**
     * Creates a new StackTraceFileWriter object.
     *
     * @param logFile DOCUMENT ME!
     */
    public StackTraceFileWriter(File logFile) {
        super(exceptionFilter, traceFormat);
        file = new ExtendedFile(logFile);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pattern DOCUMENT ME!
     *
     * @throws LogException DOCUMENT ME!
     */
    public void writeLogEntry(Object pattern) {
        try {
            file.writeText(pattern.toString(), true);
        } catch (IOException ex) {
            throw new LogException(ex);
        }
    }
}
