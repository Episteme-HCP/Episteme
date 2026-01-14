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

package org.jscience.biology.alignment;

/**
 * Signals that an scoring scheme is not compatible with the sequences
 * being aligned.
 *
 * @author Sergio A. de Carvalho Jr.
 *
 * @see ScoringScheme
 * @see PairwiseAlignmentAlgorithm
 */
public class IncompatibleScoringSchemeException extends Exception {
/**
     * Constructs an <CODE>IncompatibleScoringSchemeException</CODE> with null
     * as its error detail message.
     */
    public IncompatibleScoringSchemeException() {
        super();
    }

/**
     * Constructs an <CODE>IncompatibleScoringSchemeException</CODE> with the
     * specified detail message.
     *
     * @param message an error message
     */
    public IncompatibleScoringSchemeException(String message) {
        super(message);
    }

/**
     * Constructs an <CODE>IncompatibleScoringSchemeException</CODE> with the
     * specified cause (and a detail message that typically contains the class
     * and detail message of cause).
     *
     * @param cause a cause
     */
    public IncompatibleScoringSchemeException(Throwable cause) {
        super(cause);
    }

/**
     * Constructs an <CODE>IncompatibleScoringSchemeException</CODE> with the
     * specified detail message and cause.
     *
     * @param message an error message
     * @param cause   a cause
     */
    public IncompatibleScoringSchemeException(String message, Throwable cause) {
        super(message, cause);
    }
}
