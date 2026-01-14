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
