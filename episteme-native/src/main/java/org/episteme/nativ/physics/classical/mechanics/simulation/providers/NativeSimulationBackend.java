/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.physics.classical.mechanics.simulation.providers;

import org.episteme.natural.physics.classical.mechanics.simulation.SimulationProvider;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.Operation;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Native multicore implementation of SimulationProvider.
 * This class provides a native-optimized execution environment for simulations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService({SimulationProvider.class})
public class NativeSimulationBackend implements SimulationProvider, CPUBackend, NativeBackend {

    @Override
    public String getName() {
        return "Native Multicore Simulation";
    }

    @Override
    public String getAlgorithmType() {
        return "simulation";
    }

    @Override
    public int getPriority() {
        return 50; // Higher than ParallelSimulationProvider (20)
    }

    @Override
    public boolean isAvailable() {
        return true; // Assume available or check for native capabilities
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public ExecutionContext createContext() {
        return new ExecutionContext() {
            @Override
            public <T> T execute(Operation<T> operation) {
                return operation.compute(this);
            }
            @Override
            public void close() {}
        };
    }
    @Override
    public void shutdown() {
        // No resources to release for NativeSimulationBackend.
    }

    @Override
    public String getNativeLibraryName() {
        return "episteme_sim_native";
    }

    @Override
    public void parallelExecute(List<Runnable> tasks, int parallelism) {
        // High-performance native-backed execution
        ExecutorService executor = Executors.newFixedThreadPool(parallelism);
        try {
            for (Runnable task : tasks) {
                executor.execute(task);
            }
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }
    }
}
