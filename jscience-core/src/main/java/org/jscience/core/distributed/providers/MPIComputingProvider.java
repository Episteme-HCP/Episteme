/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.distributed.providers;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.distributed.DistributedComputingProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.distributed.MPIDistributedContext;

/**
 * MPI Distributed Computing Provider.
 * Wraps MPJ Express for high-performance distributed computing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MPIComputingProvider implements DistributedComputingProvider {

    private boolean available;

    public MPIComputingProvider() {
        try {
            Class.forName("org.mpjexpress.mpi.MPI");
            available = true;
        } catch (ClassNotFoundException e) {
            available = false;
        }
    }

    @Override
    public String getName() {
        return "Native MPI (MPJ Express Wrapper)";
    }

    @Override
    public String getAlgorithmType() {
        return "Distributed Computing";
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    public DistributedContext createContext() {
        if (!available) throw new UnsupportedOperationException("MPI not available");
        // Returning existing context implementation
        return new MPIDistributedContext(new String[0]);
    }
}
