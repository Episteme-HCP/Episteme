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

package org.jscience.computing.ai.evolutionary.geneticalgorithms;

/**
 * Exception thrown when an invalid configuration of genetic algorithm is tried
 * to be used.
 * <p/>
 * For example if you extend a genetic-algorithm class like BinaryCodedGa and
 * you forgot to overwrite evaluateIndividual(...) function, this exception is
 * thrown from the BinaryCodedGa class.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class InvalidGAConfigurationException extends RuntimeException {
    /**
     * Constructs a new InvalidGAConfigurationException instance with the
     * given error message.
     *
     * @param message An error message describing the reason this exception is being thrown.
     */
    public InvalidGAConfigurationException(String message) {
        super(message);
    }
}
