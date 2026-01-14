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

package org.jscience.technical.backend.algorithms;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.AlgorithmProvider;

/**
 * Provider interface for Mandelbrot set generation.
 * Enables switching between Multicore (CPU) and potentially GPU/Distributed
 * implementations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MandelbrotProvider extends AlgorithmProvider {

    /**
     * Computes Mandelbrot set iterations using primitive doubles.
     * Suitable for standard zoom levels and high performance.
     *
     * @param xMin          minimum real coordinate
     * @param xMax          maximum real coordinate
     * @param yMin          minimum imaginary coordinate
     * @param yMax          maximum imaginary coordinate
     * @param width         image width
     * @param height        image height
     * @param maxIterations maximum iterations per pixel
     * @return 2D integer array [width][height] containing iteration counts
     */
    int[][] compute(double xMin, double xMax, double yMin, double yMax, int width, int height, int maxIterations);

    /**
     * Computes Mandelbrot set iterations using arbitrary precision Real numbers.
     * Required for deep zooms.
     *
     * @param xMin          minimum real coordinate
     * @param xMax          maximum real coordinate
     * @param yMin          minimum imaginary coordinate
     * @param yMax          maximum imaginary coordinate
     * @param width         image width
     * @param height        image height
     * @param maxIterations maximum iterations per pixel
     * @return 2D integer array [width][height] containing iteration counts
     */
    int[][] computeReal(Real xMin, Real xMax, Real yMin, Real yMax, int width, int height, int maxIterations);

}
