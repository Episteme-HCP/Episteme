/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.physics.wave;

import java.util.stream.IntStream;

/**
 * Primitive-based support for Wave Equation simulation.
 * Optimized for high-performance double-precision computation.
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
