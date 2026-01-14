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

/**
 * A LogExceptionHandler is used to control the behavior of a Logger in regards
 * to occuring LogException events.
 *
 * @author Holger Antelmann
 * @see LogException
 * @see Logger#setExceptionHandler(LogExceptionHandler)
 */
public interface LogExceptionHandler {
    /**
     * used to simply ignore exceptions thrown by a logger
     *
     * @see Logger#ignoreLogExceptions()
     */
    public static LogExceptionHandler EXCEPTION_IGNORER = new LogExceptionHandler() {
            public void handleLogException(Logger logger, LogException ignore,
                LogWriter irrelevant) {
            }
        };

    /**
     * is called when a logger encounteres a LogException while the
     * handler logs an entry. This method can control whether the exception is
     * propagated back to the Logger or ignored by the application.
     *
     * @see Logger#setExceptionHandler(LogExceptionHandler)
     */
    void handleLogException(Logger logger, LogException ex, LogWriter writer)
        throws LogException;
}
