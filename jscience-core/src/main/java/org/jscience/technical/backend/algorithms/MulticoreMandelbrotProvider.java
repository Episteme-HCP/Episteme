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
import java.util.stream.IntStream;

/**
 * Multicore implementation of MandelbrotProvider.
 * Uses parallel streams to distribute row computation across CPU cores.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreMandelbrotProvider implements MandelbrotProvider {

    @Override
    public int[][] compute(double xMin, double xMax, double yMin, double yMax, int width, int height,
            int maxIterations) {
        int[][] result = new int[width][height];
        double dx = (xMax - xMin) / width;
        double dy = (yMax - yMin) / height;

        // Parallelize over X (columns)
        IntStream.range(0, width).parallel().forEach(px -> {
            double x0 = xMin + px * dx;
            for (int py = 0; py < height; py++) {
                double y0 = yMin + py * dy;
                double x = 0;
                double y = 0;
                int iter = 0;

                // Optimized loop
                double x2 = 0;
                double y2 = 0;
                while (x2 + y2 <= 4.0 && iter < maxIterations) {
                    y = 2 * x * y + y0;
                    x = x2 - y2 + x0;
                    x2 = x * x;
                    y2 = y * y;
                    iter++;
                }
                result[px][py] = iter;
            }
        });

        return result;
    }

    @Override
    public int[][] computeReal(Real xMin, Real xMax, Real yMin, Real yMax, int width, int height, int maxIterations) {
        int[][] result = new int[width][height];

        // Determine precision from context or input
        // Since we are in a provider, we respect the caller's context or setup new one.
        // But parallel streams might lose thread-local context unless handled
        // carefully.
        // For Real operations, we assume standard context overhead is acceptable.

        // Pre-calculate constants to avoid repeated Real object creation
        final Real FOUR = Real.of(4.0);
        final Real TWO = Real.of(2.0);

        Real dx = xMax.subtract(xMin).divide(Real.of(width));
        Real dy = yMax.subtract(yMin).divide(Real.of(height));

        IntStream.range(0, width).parallel().forEach(px -> {
            // Re-establish context if needed, but Real is generally immutable/safe
            // However, rigorous exact math context might be thread-local.
            // org.jscience.mathematics.context.MathContext is ThreadLocal.
            // We should ideally propagate it or rely on default.

            Real x0 = xMin.add(dx.multiply(Real.of(px)));

            for (int py = 0; py < height; py++) {
                Real y0 = yMin.add(dy.multiply(Real.of(py)));
                Real x = Real.ZERO;
                Real y = Real.ZERO;
                int iter = 0;

                // Real operations are expensive, so we don't unroll as much as double
                while (x.multiply(x).add(y.multiply(y)).compareTo(FOUR) <= 0 && iter < maxIterations) {
                    Real xTemp = x.multiply(x).subtract(y.multiply(y)).add(x0);
                    y = x.multiply(y).multiply(TWO).add(y0);
                    x = xTemp;
                    iter++;
                }
                result[px][py] = iter;
            }
        });

        return result;
    }

    @Override
    public String getName() {
        return "Multicore Mandelbrot";
    }
}
