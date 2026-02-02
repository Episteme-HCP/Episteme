/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import java.util.List;

/**
 * Service provider interface for discrete-event and continuous simulations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface SimulationProvider extends AlgorithmProvider {
    
    /**
     * Executes a set of simulation tasks in parallel.
     * 
     * @param tasks the tasks to execute
     * @param parallelism the degree of parallelism
     */
    void parallelExecute(List<Runnable> tasks, int parallelism);

    @Override
    default String getName() {
        return "Simulation Provider";
    }
}
