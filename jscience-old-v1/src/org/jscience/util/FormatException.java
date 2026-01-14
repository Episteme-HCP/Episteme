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

package org.jscience.util;

/**
 * indicates that some content was not in the expected format
 *
 * @author Holger Antelmann
 */
public class FormatException extends Exception {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1514322501591901535L;

    /** DOCUMENT ME! */
    Object formattedContent = null;

/**
     * Creates a new FormatException object.
     */
    public FormatException() {
    }

/**
     * Creates a new FormatException object.
     *
     * @param content DOCUMENT ME!
     */
    public FormatException(Object content) {
        formattedContent = content;
    }

/**
     * Creates a new FormatException object.
     *
     * @param msg DOCUMENT ME!
     */
    public FormatException(String msg) {
        super(msg);
    }

/**
     * Creates a new FormatException object.
     *
     * @param msg     DOCUMENT ME!
     * @param content DOCUMENT ME!
     */
    public FormatException(String msg, Object content) {
        super(msg);
        formattedContent = content;
    }

/**
     * Creates a new FormatException object.
     *
     * @param cause DOCUMENT ME!
     */
    public FormatException(Throwable cause) {
        super(cause);
    }

/**
     * Creates a new FormatException object.
     *
     * @param msg   DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public FormatException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     */
    public void setFormattedContent(Object content) {
        formattedContent = content;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getFormattedContent() {
        return formattedContent;
    }
}
