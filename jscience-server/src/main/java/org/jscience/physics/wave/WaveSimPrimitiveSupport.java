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

package org.jscience.physics.wave;

import java.util.stream.IntStream;

/**
 * Primitive-based support for Wave Equation simulation.
 * Optimized for high-performance double-precision computation.
 
 * <p>
 * <b>Reference:</b><br>
 * Prim, R. C. (1957). Shortest connection networks and some generalizations. <i>Bell System Technical Journal</i>, 36(6), 1389-1401.
 * </p>
 *
 */
public class WaveSimPrimitiveSupport {

    /**
     * Solves one time step of the 2D Wave Equation using double primitives.
     * 
     * @param u       Current wave height state (t)
     * @param uPrev   Previous wave height state (t-1)
     * @param width   Grid width
     * @param height  Grid height
     * @param c       Wave speed
     * @param damping Damping factor
     */
    public void solve(double[][] u, double[][] uPrev, int width, int height, double c, double damping) {
        double[][] uNext = new double[width][height];
        double c2 = c * c;

        // Parallel processing for performance
        IntStream.range(1, width - 1).parallel().forEach(x -> {
            for (int y = 1; y < height - 1; y++) {
                double laplacian = u[x + 1][y] + u[x - 1][y] + u[x][y + 1] + u[x][y - 1] - 4 * u[x][y];
                uNext[x][y] = (2 * u[x][y] - uPrev[x][y] + c2 * laplacian) * damping;
            }
        });

        // Update state: uPrev < - u, u < - uNext
        for (int x = 0; x < width; x++) {
            System.arraycopy(u[x], 0, uPrev[x], 0, height);
            System.arraycopy(uNext[x], 0, u[x], 0, height);
        }
    }
}
