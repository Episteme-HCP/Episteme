/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.algorithm.nbody;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.NBodyProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.gpu.cuda.CUDABackend;
import java.util.logging.Logger;

/**
 * CUDA-accelerated N-Body simulation provider using JCuda.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class NativeCUDANBodyProvider implements NBodyProvider {
    private static final Logger LOGGER = Logger.getLogger(NativeCUDANBodyProvider.class.getName());
    private static final int GPU_THRESHOLD = 1000;



    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public boolean isAvailable() {
        // Disabled: No true GPU implementation yet (was CPU placeholder)
        return false;
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int numParticles = masses.length;
        if (numParticles >= GPU_THRESHOLD) {
            computeForcesCUDA(positions, masses, forces, G, softening);
        } else {
            computeForcesCPU(positions, masses, forces, G, softening);
        }
    }

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        double[] posD = new double[positions.length];
        double[] massD = new double[masses.length];
        double[] forceD = new double[forces.length];

        for (int i = 0; i < positions.length; i++) posD[i] = positions[i].doubleValue();
        for (int i = 0; i < masses.length; i++) massD[i] = masses[i].doubleValue();

        computeForces(posD, massD, forceD, G.doubleValue(), softening.doubleValue());

        for (int i = 0; i < forces.length; i++) forces[i] = Real.of(forceD[i]);
    }

    private void computeForcesCUDA(double[] positions, double[] masses, double[] forces, double G, double softening) {
        // CUDA hook...
        computeForcesCPU(positions, masses, forces, G, softening);
    }

    private void computeForcesCPU(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int n = masses.length;
        java.util.Arrays.fill(forces, 0);
        for (int i = 0; i < n; i++) {
            double xi = positions[i * 3], yi = positions[i * 3 + 1], zi = positions[i * 3 + 2], mi = masses[i];
            for (int j = i + 1; j < n; j++) {
                double dx = positions[j * 3] - xi, dy = positions[j * 3 + 1] - yi, dz = positions[j * 3 + 2] - zi;
                double dist2 = dx * dx + dy * dy + dz * dz + softening * softening;
                double dist = Math.sqrt(dist2);
                double f = G * mi * masses[j] / (dist2 * dist);
                double fx = f * dx, fy = f * dy, fz = f * dz;
                forces[i * 3] += fx; forces[i * 3 + 1] += fy; forces[i * 3 + 2] += fz;
                forces[j * 3] -= fx; forces[j * 3 + 1] -= fy; forces[j * 3 + 2] -= fz;
            }
        }
    }

    @Override
    public String getName() {
        return "N-Body (GPU/CUDA)";
    }
}

