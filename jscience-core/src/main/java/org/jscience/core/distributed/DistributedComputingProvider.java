package org.jscience.core.distributed;

import org.jscience.core.technical.backend.distributed.DistributedContext;

import org.jscience.core.technical.algorithm.AlgorithmProvider;

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
