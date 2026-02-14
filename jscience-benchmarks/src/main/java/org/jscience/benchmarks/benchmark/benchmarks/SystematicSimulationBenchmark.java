/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;

import org.jscience.core.technical.algorithm.SimulationProvider;
import com.google.auto.service.AutoService;
import java.util.*;

/**
 * A benchmark that systematically tests all available SimulationProviders.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicSimulationBenchmark implements SystematicBenchmark<SimulationProvider> {

    private SimulationProvider currentProvider;
    private final List<Runnable> tasks = new ArrayList<>();
    private final int numTasks = 10000;
    private final int parallelism = Runtime.getRuntime().availableProcessors();

    @Override public String getId() { return getIdPrefix(); }
    @Override public String getName() { return getNameBase(); }
    @Override public String getIdPrefix() { return "simulation-systematic"; }
    @Override public String getNameBase() { return "Systematic Parallel Simulation"; }
    @Override public String getDescription() { return "Parallel task execution and scheduling (multi-threaded workload distribution)"; }
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
