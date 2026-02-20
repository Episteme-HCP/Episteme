/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.classical.mechanics.simulation.providers;

import org.jscience.natural.physics.classical.mechanics.simulation.SimulationProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

import java.util.List;

/**
 * Native multicore implementation of SimulationProvider.
 * Currently delegates to Parallel (CPU) implementation as a placeholder for
 * a future high-performance native simulation kernel.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreSimulationProvider implements SimulationProvider {

    private final ParallelSimulationProvider fallback = new ParallelSimulationProvider();

    @Override
    public String getName() {
        return "Native Multicore Simulation";
    }

    @Override
    public String getAlgorithmType() {
        return "simulation";
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int parallelism) {
        fallback.parallelExecute(tasks, parallelism);
    }
}
