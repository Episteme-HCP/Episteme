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

package org.jscience.mathematics.analysis;

/**
 * This class represents exceptions thrown by mappings
 */

//may be we should extend Exception
public class MappingException extends RuntimeException {
/**
     * Simple constructor. Build an exception with a default message
     */
    public MappingException() {
        super("Mapping exception.");
    }

/**
     * Simple constructor. Build an exception with the specified message
     *
     * @param message exception message
     */
    public MappingException(String message) {
        super(message);
    }

/**
     * Simple constructor. Build an exception from a cause
     *
     * @param cause cause of this exception
     */
    public MappingException(Throwable cause) {
        super(cause);
    }

/**
     * Simple constructor. Build an exception from a message and a cause
     *
     * @param message exception message
     * @param cause   cause of this exception
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
