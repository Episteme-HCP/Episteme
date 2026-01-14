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

import java.util.stream.IntStream;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Multicore implementation of WaveProvider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreWaveProvider implements WaveProvider {

    @Override
    public void solve(Real[][] u, Real[][] uPrev, int width, int height, Real c, Real damping) {
        Real[][] uNext = new Real[width][height];
        Real c2 = c.multiply(c);

        // Parallel Stream for rows
        IntStream.range(1, width - 1).parallel().forEach(x -> {
            for (int y = 1; y < height - 1; y++) {
                // Laplacian: u[x+1][y] + u[x-1][y] + u[x][y+1] + u[x][y-1] - 4*u[x][y]
                Real laplacian = u[x + 1][y].add(u[x - 1][y])
                        .add(u[x][y + 1]).add(u[x][y - 1])
                        .subtract(u[x][y].multiply(Real.of(4)));

                // uNext = (2*u - uPrev + c^2 * laplacian) * damping
                uNext[x][y] = u[x][y].multiply(Real.of(2))
                        .subtract(uPrev[x][y])
                        .add(c2.multiply(laplacian))
                        .multiply(damping);
            }
        });

        // Copy new state back
        // We write uNext values into u, and u values into uPrev (history shift)
        // But uNext depends on u.
        // So: uPrev < -u, u < -uNext
        for (int x = 0; x < width; x++) {
            System.arraycopy(u[x], 0, uPrev[x], 0, height);
            System.arraycopy(uNext[x], 0, u[x], 0, height);
        }
    }

    @Override
    public void solve(double[][] u, double[][] uPrev, int width, int height, double c, double damping) {
        double[][] uNext = new double[width][height];
        double c2 = c * c;

        IntStream.range(1, width - 1).parallel().forEach(x -> {
            for (int y = 1; y < height - 1; y++) {
                double laplacian = u[x + 1][y] + u[x - 1][y] + u[x][y + 1] + u[x][y - 1] - 4 * u[x][y];
                uNext[x][y] = 2 * u[x][y] - uPrev[x][y] + c2 * laplacian;
                uNext[x][y] *= (1.0 - damping);
            }
        });

        // Copy states
        for (int x = 0; x < width; x++) {
            System.arraycopy(u[x], 0, uPrev[x], 0, height);
            System.arraycopy(uNext[x], 0, u[x], 0, height);
        }
    }

    @Override
    public String getName() {
        return "Multicore Wave Equation (Real)";
    }
}
