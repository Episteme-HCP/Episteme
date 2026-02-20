/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.classical.mechanics.simulation.providers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import org.jscience.natural.physics.classical.mechanics.simulation.SimulationProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Parallel implementation of the SimulationProvider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({SimulationProvider.class, AlgorithmProvider.class})
public class ParallelSimulationProvider implements SimulationProvider {

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public String getName() {
        return "Parallel Simulation Engine (ForkJoin)";
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int threadCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Phaser phaser = new Phaser(1); // Register main thread

        for (Runnable task : tasks) {
            phaser.register();
            executor.submit(() -> {
                try {
                    task.run();
                } finally {
                    phaser.arriveAndDeregister();
                }
            });
        }

        phaser.arriveAndAwaitAdvance();
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
