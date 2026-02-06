package org.jscience.benchmarks.benchmark;

import org.jscience.core.technical.algorithm.SimulationProvider;
import java.util.*;

/**
 * A benchmark that systematically tests all available SimulationProviders.
 */
public class SystematicSimulationBenchmark implements SystematicBenchmark<SimulationProvider> {

    private SimulationProvider currentProvider;
    private final List<Runnable> tasks = new ArrayList<>();
    private final int numTasks = 10000;
    private final int parallelism = Runtime.getRuntime().availableProcessors();

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "simulation-systematic"; }
    @Override public String getNameBase() { return "Systematic Parallel Simulation"; }
    @Override public String getDescription() { return "Systematically benchmarks parallel task execution and scheduling efficiency across multiple nodes or threads."; }
    @Override public String getDomain() { return "Parallel Computing"; }
    @Override public Class<SimulationProvider> getProviderClass() { return SimulationProvider.class; }

    @Override
    public void setup() {
        tasks.clear();
        for (int i = 0; i < numTasks; i++) {
            tasks.add(() -> {
                double x = 42.0;
                for (int j = 0; j < 50; j++) {
                    x = Math.sqrt(x);
                }
            });
        }
    }

    @Override
    public void setProvider(SimulationProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void run() {
        if (currentProvider != null) {
            currentProvider.parallelExecute(tasks, parallelism);
        }
    }

    @Override
    public void teardown() {
        tasks.clear();
    }
}
