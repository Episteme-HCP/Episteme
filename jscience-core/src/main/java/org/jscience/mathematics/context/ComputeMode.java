package org.jscience.mathematics.context;

/**
 * Defines the preferred computation mode for linear algebra operations.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public enum ComputeMode {
    /**
     * Automatically choose the best device (CPU or GPU) based on problem size and
     * availability.
     */
    AUTO,

    /**
     * Force execution on the CPU (Central Processing Unit).
     */
    CPU,

    /**
     * Force execution on the GPU (Graphics Processing Unit).
     * Throws an exception if GPU is unavailable.
     */
    GPU
}
