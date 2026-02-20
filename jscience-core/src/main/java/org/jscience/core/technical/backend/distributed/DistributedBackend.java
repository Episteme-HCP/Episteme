/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.distributed;

import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.distributed.DistributedComputingProvider;
import org.jscience.core.distributed.DistributedExecutionContext;

/**
 * Backend interface for distributed computing systems.
 * <p>
 * Extends {@link ComputeBackend} to bridge the distributed computing subsystem
 * with the unified backend infrastructure. Also implements
 * {@link DistributedComputingProvider} so that distributed backends are
 * discoverable via the algorithm provider registry.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface DistributedBackend extends ComputeBackend, DistributedComputingProvider {

    /**
     * Returns the number of available distributed nodes.
     *
     * @return the node count
     */
    int getNodeCount();

    @Override
    default String getType() {
        return "Distributed";
    }

    @Override
    default boolean supportsParallelOps() {
        return true;
    }

    @Override
    default HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    default String getAlgorithmType() {
        return "Distributed Computing";
    }

    /**
     * Resolves the diamond-conflict between {@link ComputeBackend}
     * (via {@link org.jscience.core.technical.backend.Backend}) and
     * {@link DistributedComputingProvider}
     * (via {@link org.jscience.core.technical.algorithm.AlgorithmProvider}),
     * both of which define {@code getPriority()}.
     */
    @Override
    default int getPriority() {
        return 0;
    }

    /**
     * Resolves the method conflict between {@link Backend#isAvailable()} 
     * and {@link org.jscience.core.technical.algorithm.AlgorithmProvider#isAvailable()}.
     */
    @Override
    boolean isAvailable();

    /**
     * Default implementation of {@link ComputeBackend#createContext()}.
     * <p>
     * Wraps the result of {@link #createDistributedContext()} in a
     * {@link DistributedExecutionContext} adapter.
     * </p>
     */
    @Override
    default ExecutionContext createContext() {
        return new DistributedExecutionContext(createDistributedContext());
    }
}
