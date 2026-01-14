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

/*
 * BasisNotFoundException.java
 *
 * Created on July 25, 2004, 11:22 AM
 */
package org.jscience.chemistry.quantum.basis;

/**
 * A runtime exception thrown is the requested basis is not found.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class BasisNotFoundException extends java.lang.RuntimeException {
/**
     * Creates a new instance of <code>BasisNotFoundException</code> without
     * detail message.
     */
    public BasisNotFoundException() {
    }

/**
     * Constructs an instance of <code>BasisNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BasisNotFoundException(String msg) {
        super(msg);
    }
} // end of class BasisNotFoundException
