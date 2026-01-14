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
 * LogException is thrown when a problem is encountered while writing to a
 * LogWriter
 *
 * @author Holger Antelmann
 *
 * @see LogWriter
 */
public class LogException extends RuntimeException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -6491689248403436729L;

/**
     * Creates a new LogException object.
     */
    public LogException() {
        super();
    }

/**
     * Creates a new LogException object.
     *
     * @param text DOCUMENT ME!
     */
    public LogException(String text) {
        super(text);
    }

/**
     * Creates a new LogException object.
     *
     * @param cause DOCUMENT ME!
     */
    public LogException(Throwable cause) {
        super(cause);
    }

/**
     * Creates a new LogException object.
     *
     * @param text  DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public LogException(String text, Throwable cause) {
        super(text, cause);
    }
}
