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
