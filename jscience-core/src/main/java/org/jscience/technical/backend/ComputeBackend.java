/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend;

/**
 * Service Provider Interface for pluggable compute backends.
 * <p>
 * Implementations can provide CPU, GPU, quantum, or distributed computing
 * backends. Now unified with the BackendProvider system.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ComputeBackend extends BackendProvider {

    @Override
    default String getType() {
        return "tensor"; // Default type for compute backends
    }

    @Override
    default String getId() {
        return getClass().getSimpleName().toLowerCase().replace("provider", "");
    }

    @Override
    default String getDescription() {
        return getName() + " compute backend";
    }

    @Override
    default Object createBackend() {
        return this; // ComputeBackend IS the backend instance
    }

    /**
     * Creates an execution context for running operations.
     * <p>
     * The context should be used within a try-with-resources block to ensure
     * proper cleanup of resources.
     * </p>
     * 
     * @return new execution context
     * @throws IllegalStateException if backend is not available
     */
    ExecutionContext createContext();

    /**
     * Checks if this backend supports parallel operations.
     * 
     * @return true if parallel execution is supported
     */
    default boolean supportsParallelOps() {
        return false;
    }

    /**
     * Checks if this backend supports floating-point arithmetic.
     * 
     * @return true if floating-point is supported
     */
    default boolean supportsFloatingPoint() {
        return true;
    }

    /**
     * Checks if this backend supports complex number arithmetic.
     * 
     * @return true if complex numbers are supported
     */
    default boolean supportsComplexNumbers() {
        return true;
    }
}
