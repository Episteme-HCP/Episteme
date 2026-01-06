package org.jscience.physics.fluid;

/**
 * Native (double-based) implementation of Lattice Boltzmann Method for
 * Server/Worker usage.
 * Supports CPU and OpenCL (GPU) execution.
 */
public class NativeLatticeBoltzmann {

    public NativeLatticeBoltzmann() {

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
