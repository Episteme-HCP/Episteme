/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.client.algorithms;

/**
 * Generic interface for Scientific Algorithms.
 * 
 * @param <I> Input type
 * @param <O> Output type
 */
public interface Algorithm<I, O> {

    /**
     * Executes the algorithm.
     * 
     * @param input The input data.
     * @return The result.
     */
    O execute(I input);

    /**
     * Returns the name of the algorithm.
     * 
     * @return Name string.
     */
    String getName();

    /**
     * Returns the algorithmic complexity (Big O notation).
     * 
     * @return Complexity string (e.g., "O(n log n)").
     */
    String getComplexity();

    /**
     * Returns the quality score (higher is better).
     * Useful for competing algorithms.
     * 
     * @return Score between 0.0 and 1.0.
     */
    default double getQualityScore() {
        return 0.5;
    }
}
