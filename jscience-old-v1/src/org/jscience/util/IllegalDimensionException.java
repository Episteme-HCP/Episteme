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
 * The IllegalDimensionException class is thrown when a conflicting number
 * of dimensions is found in a system with dimensions, it is therefore a kind
 * of generalization of IndexOutOfBoundsException.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//may be this should extend IndexOutOfBoundsException or IllegalArgumentException
//may be we should extend Exception
public final class IllegalDimensionException extends RuntimeException {
/**
     * Creates a new IllegalDimensionException object.
     */
    public IllegalDimensionException() {
        super();
    }

/**
     * Creates a new IllegalDimensionException object.
     *
     * @param cause DOCUMENT ME!
     */
    public IllegalDimensionException(Throwable cause) {
        super(cause);
    }

/**
     * Creates a new IllegalDimensionException object.
     *
     * @param message DOCUMENT ME!
     */
    public IllegalDimensionException(String message) {
        super(message);
    }

/**
     * Creates a new IllegalDimensionException object.
     *
     * @param message DOCUMENT ME!
     * @param cause   DOCUMENT ME!
     */
    public IllegalDimensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
