/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.testing;

import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;

/**
 * Infrastructure for grouping benchmarks into functional suites.
 * Supports "sub-scenarios": running the same benchmark with different parameters.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class BenchmarkTestSuite {

    private final String name;
    private final List<BenchmarkScenario> scenarios = new ArrayList<>();

    public BenchmarkTestSuite(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addScenario(RunnableBenchmark benchmark, String description, Map<String, String> parameters) {
        scenarios.add(new BenchmarkScenario(benchmark, description, parameters));
    }

    public List<BenchmarkScenario> getScenarios() {
        return Collections.unmodifiableList(scenarios);
    }

    public static class BenchmarkScenario {
        private final RunnableBenchmark benchmark;
        private final String description;
        private final Map<String, String> parameters;

        public BenchmarkScenario(RunnableBenchmark benchmark, String description, Map<String, String> parameters) {
            this.benchmark = benchmark;
            this.description = description;
            this.parameters = parameters;
        }

        public RunnableBenchmark getBenchmark() { return benchmark; }
        public String getDescription() { return description; }
        public Map<String, String> getParameters() { return parameters; }
    }
}
