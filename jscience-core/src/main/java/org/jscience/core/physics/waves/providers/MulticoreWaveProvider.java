/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics.waves.providers;

import java.util.stream.IntStream;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.physics.waves.WaveProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Multicore implementation of WaveProvider.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreWaveProvider implements WaveProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public void solve(Real[][] u, Real[][] uPrev, int width, int height, Real c, Real damping) {
        Real[][] uNext = new Real[width][height];
        Real c2 = c.multiply(c);

        IntStream.range(1, width - 1).parallel().forEach(x -> {
            for (int y = 1; y < height - 1; y++) {
                Real laplacian = u[x + 1][y].add(u[x - 1][y])
                        .add(u[x][y + 1]).add(u[x][y - 1])
                        .subtract(u[x][y].multiply(Real.of(4)));

                uNext[x][y] = u[x][y].multiply(Real.of(2))
                        .subtract(uPrev[x][y])
                        .add(c2.multiply(laplacian))
                        .multiply(damping);
            }
        });

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

        for (int x = 0; x < width; x++) {
            System.arraycopy(u[x], 0, uPrev[x], 0, height);
            System.arraycopy(uNext[x], 0, u[x], 0, height);
        }
    }

    @Override
    public String getName() {
        return "Multicore Wave Equation (CPU)";
    }
}
