package org.episteme.core.distributed;

import org.episteme.core.technical.backend.distributed.DistributedContext;

import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Service provider interface for distributed computing backends.
 * Allows discovery of MPI, Spark, or Grid-based compute contexts.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface DistributedComputingProvider extends AlgorithmProvider {
    
    /**
     * Creates a new distributed execution context.
     * @return a context for submitting distributed tasks.
     */
    DistributedContext createDistributedContext();
}
