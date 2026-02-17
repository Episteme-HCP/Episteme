/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.physics.fluid;

import org.jscience.core.technical.algorithm.physics.FluidSimPrimitiveSupport;

/**
 * Native (double-based) implementation of Lattice Boltzmann Method for
 * Server/Worker usage.
 * Supports CPU and OpenCL (GPU) execution.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LatticeBoltzmann {

    public LatticeBoltzmann() {

    }

    public void evolve(double[][][] f, boolean[][] obstacle, double omega) {
        int width = f.length;
        int height = f[0].length;

        // Delegate to shared primitive support which handles D2Q9 evolution
        // (including parallel optimizations)
        FluidSimPrimitiveSupport support = new FluidSimPrimitiveSupport();
        support.evolve(f, obstacle, omega, width, height);
    }
}
