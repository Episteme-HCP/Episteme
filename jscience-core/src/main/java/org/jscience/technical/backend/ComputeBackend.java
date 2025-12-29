/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.technical.backend;

/**
 * Service Provider Interface for pluggable compute backends.
 * <p>
 * Implementations can provide CPU, GPU, quantum, or distributed computing
 * backends. Backends are discovered automatically via
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ComputeBackend {

    /**
     * Returns the name of this backend (e.g., "CPU", "GPU-CUDA", "IBM-Qiskit").
     * 
     * @return backend name
     */
    String getName();

    /**
     * Checks if this backend is currently available.
     * <p>
     * For example, a GPU backend might return false if no GPU is detected.
     * </p>
     * 
     * @return true if backend can be used, false otherwise
     */
    boolean isAvailable();

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

    /**
     * Returns the priority of this backend for automatic selection.
     * Higher values indicate higher priority.
     * <p>
     * Default priorities:
     * <ul>
     * <li>CPU: 0 (fallback)</li>
     * <li>GPU: 10</li>
     * <li>Quantum: 5 (specialized use cases)</li>
     * </ul>
     * </p>
     * 
     * @return priority value
     */
    default int getPriority() {
        return 0;
    }
}