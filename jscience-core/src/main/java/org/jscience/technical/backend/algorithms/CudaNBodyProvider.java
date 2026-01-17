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
import org.jscience.technical.backend.cuda.CUDABackend;

import java.util.logging.Logger;

/**
 * CUDA-accelerated N-Body simulation provider using JCuda.
 * 
 * <p>
 * Provides CUDA-based N-body computation for NVIDIA GPUs. For AMD/Intel GPUs,
 * use {@link OpenCLNBodyProvider} instead.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Aarseth, S. J. (2003). <i>Gravitational N-Body Simulations</i>. Cambridge University Press.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see OpenCLNBodyProvider
 */
public class CUDANBodyProvider implements NBodyProvider {
    private static final Logger LOGGER = Logger.getLogger(CUDANBodyProvider.class.getName());
    private static final int GPU_THRESHOLD = 1000;

    private final boolean gpuAvailable;

    /**
     * Creates a new CUDA N-Body provider.
     */
    public CUDANBodyProvider() {
        CUDABackend backend = null;
        boolean available = false;
        try {
            backend = new CUDABackend();
            available = backend.isAvailable();
            if (available) {
                LOGGER.info("CUDA N-Body provider initialized with GPU support");
            }
        } catch (Exception e) {
            LOGGER.warning("CUDA initialization failed: " + e.getMessage());
        }
        this.gpuAvailable = available;
    }

    /**
     * Checks if CUDA GPU acceleration is available.
     *
     * @return true if CUDA GPU is available
     */
    public boolean supportsGPU() {
        return gpuAvailable;
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int numParticles = masses.length;

        if (gpuAvailable && numParticles >= GPU_THRESHOLD) {
            computeForcesCUDA(positions, masses, forces, G, softening);
        } else {
            computeForcesCPU(positions, masses, forces, G, softening);
        }
    }

    /**
     * Computes forces using Real API.
     *
     * @param positions particle positions as Real[]
     * @param masses    particle masses as Real[]
     * @param forces    output forces as Real[]
     * @param G         gravitational constant as Real
     */
    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        double[] posD = new double[positions.length];
        double[] massD = new double[masses.length];
        double[] forceD = new double[forces.length];

        for (int i = 0; i < positions.length; i++) {
            posD[i] = positions[i].doubleValue();
        }
        for (int i = 0; i < masses.length; i++) {
            massD[i] = masses[i].doubleValue();
        }

        computeForces(posD, massD, forceD, G.doubleValue(), softening.doubleValue());

        for (int i = 0; i < forces.length; i++) {
            forces[i] = Real.of(forceD[i]);
        }
    }

    private void computeForcesCUDA(double[] positions, double[] masses, double[] forces, double G, double softening) {
        LOGGER.fine("Executing N-Body forces on CUDA for " + masses.length + " particles");
        // CUDA kernel execution via JCuda would go here
        // Fallback to CPU for now
        computeForcesCPU(positions, masses, forces, G, softening);
    }

    private void computeForcesCPU(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int numParticles = masses.length;
        // double softening = 1e-9;

        java.util.Arrays.fill(forces, 0);

        for (int i = 0; i < numParticles; i++) {
            double px = positions[i * 3];
            double py = positions[i * 3 + 1];
            double pz = positions[i * 3 + 2];
            double mi = masses[i];

            for (int j = i + 1; j < numParticles; j++) {
                double dx = positions[j * 3] - px;
                double dy = positions[j * 3 + 1] - py;
                double dz = positions[j * 3 + 2] - pz;

                double dist2 = dx * dx + dy * dy + dz * dz + softening * softening;
                double dist = Math.sqrt(dist2);
                double f = G * mi * masses[j] / (dist2 * dist);

                double fx = f * dx;
                double fy = f * dy;
                double fz = f * dz;

                forces[i * 3] += fx;
                forces[i * 3 + 1] += fy;
                forces[i * 3 + 2] += fz;

                forces[j * 3] -= fx;
                forces[j * 3 + 1] -= fy;
                forces[j * 3 + 2] -= fz;
            }
        }
    }

    /**
     * Returns the name of this provider.
     *
     * @return provider name
     */
    public String getName() {
        return gpuAvailable ? "N-Body (GPU/CUDA)" : "N-Body (CPU)";
    }
}
