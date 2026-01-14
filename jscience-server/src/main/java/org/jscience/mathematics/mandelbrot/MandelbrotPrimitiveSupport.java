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

package org.jscience.mathematics.mandelbrot;

import java.util.stream.IntStream;

/**
 * Primitive-based support for Mandelbrot Set generation.
 * Optimized for high-performance double-precision computation.
 */
public class MandelbrotPrimitiveSupport {

    /**
     * Generates the Mandelbrot set into an integer array using double primitives.
     * 
     * @param result  Result array [width][height]
     * @param width   Width
     * @param height  Height
     * @param x0      Mindividual x-coord
     * @param y0      Mindividual y-coord
     * @param dx      X range
     * @param dy      Y range
     * @param maxIter Maximum iterations
     */
    public void generate(int[][] result, int width, int height, double x0, double y0, double dx, double dy,
            int maxIter) {
        IntStream.range(0, width).parallel().forEach(i -> {
            for (int j = 0; j < height; j++) {
                double zx = 0;
                double zy = 0;
                double cx = x0 + i * dx / width;
                double cy = y0 + j * dy / height;
                int iter = 0;
                while (zx * zx + zy * zy < 4 && iter < maxIter) {
                    double tmp = zx * zx - zy * zy + cx;
                    zy = 2 * zx * zy + cy;
                    zx = tmp;
                    iter++;
                }
                result[i][j] = iter;
            }
        });
    }
}
