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

/* ====================================================================
 * /FCGAException.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

/**
 * The root class for all exceptions used in the API.
 *
 * @author doergn@users.sourceforge.net
 *
 * @since 1.0
 */
public class FGCAException extends Exception {
    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------
/**
     * Constructs a new instance of a FGCAException.
     *
     * @param message The exceptions message
     */
    public FGCAException(String message) {
        super(addMessageFlavour(message));
    }

    // -------------------------------------------------------------------
/**
     * Constructs a new instance of a FGCAException.
     *
     * @param message The exceptions message
     * @param cause   The exceptions cause
     */
    public FGCAException(String message, Throwable cause) {
        super(addMessageFlavour(message), cause);
    }

    // ---------------
    // Private methods ---------------------------------------------------
    // ---------------

    // -------------------------------------------------------------------
    /**
     * Adds a special flavour around the exceptions message that sould
     * make it easier to point out FGCAExceptions.
     *
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String addMessageFlavour(String message) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("\n*********");
        buffer.append(message);
        buffer.append("*********\n");

        return buffer.toString();
    }
}
