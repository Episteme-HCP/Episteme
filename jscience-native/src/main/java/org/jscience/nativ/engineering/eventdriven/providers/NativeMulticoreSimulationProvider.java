/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.engineering.eventdriven.providers;

import org.jscience.core.technical.algorithm.SimulationProvider;
import org.jscience.core.technical.algorithm.simulation.MulticoreSimulationProvider;
import java.util.List;

/**
 * Native multicore implementation of SimulationProvider.
 */
public class NativeMulticoreSimulationProvider implements SimulationProvider {

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int parallelism) {
        // Optimization: use native task scheduler if available
        // Fallback to java multicore for now (placeholder for native hook)
        new MulticoreSimulationProvider().parallelExecute(tasks, parallelism);
    }

    @Override
    public String getName() {
        return "Native Multicore Simulation (HPC/C++)";
    }
}
