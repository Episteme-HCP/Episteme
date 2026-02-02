/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.simulation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import org.jscience.core.technical.algorithm.SimulationProvider;

/**
 * Parallel implementation of the SimulationProvider.
 * Uses a thread pool to execute simulation tasks in parallel.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class ParallelSimulationProvider implements SimulationProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public String getName() {
        return "Parallel Simulation (CPU)";
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int parallelism) {
        if (tasks.isEmpty()) return;
        
        ExecutorService executor = Executors.newFixedThreadPool(parallelism, r -> {
            Thread t = new Thread(r, "JScience-Simulation-Worker");
            t.setDaemon(true);
            return t;
        });
        
        try {
            Phaser phaser = new Phaser(1);
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
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
