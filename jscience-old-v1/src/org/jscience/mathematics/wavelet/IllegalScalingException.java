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

package org.jscience.mathematics.wavelet;

/**
 * This exception is used to indicate that the current chosen
 * multiresolution is not appropriate because the number of scaling functions
 * is incompatible (with the multiresolution).
 *
 * @author Daniel Lemire
 */
public class IllegalScalingException extends IllegalArgumentException {
/**
     * Creates a new IllegalScalingException object.
     */
    public IllegalScalingException() {
    }

/**
     * Creates a new IllegalScalingException object.
     *
     * @param s DOCUMENT ME!
     */
    public IllegalScalingException(String s) {
        super(s);
    }

/**
     * Creates a new IllegalScalingException object.
     *
     * @param n0  DOCUMENT ME!
     * @param min DOCUMENT ME!
     */
    public IllegalScalingException(int n0, int min) {
        super("The length parameter " + n0 + " must be at least " + min +
            ". Please change the wavelet or the number of iterations.");
    }
}
