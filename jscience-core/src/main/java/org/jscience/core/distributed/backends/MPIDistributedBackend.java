/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.distributed.backends;

import org.jscience.core.technical.backend.distributed.DistributedBackend;
import org.jscience.core.technical.backend.distributed.DistributedContext;
import org.jscience.core.distributed.MPIDistributedContext;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * MPI Distributed Computing Backend.
 * Wraps MPJ Express for high-performance distributed computing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MPIDistributedBackend implements DistributedBackend {

    private boolean available;

    public MPIDistributedBackend() {
        try {
            Class.forName("mpi.MPI");
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
    public boolean isAvailable() {
        return available;
    }

    @Override
    public int getNodeCount() {
        return available ? Runtime.getRuntime().availableProcessors() : 0;
    }

    @Override
    public DistributedContext createDistributedContext() {
        if (!available) throw new UnsupportedOperationException("MPI not available");
        return new MPIDistributedContext(new String[0]);
    }
}
