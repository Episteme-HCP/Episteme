package org.jscience.benchmark;

import java.util.Map;

/**
 * Standard interface for all JScience benchmarks.
 * <p>
 * This interface allows benchmarks to be auto-discovered via ServiceLoader
 * and executed uniformly.
 * </p>
 */
public interface RunnableBenchmark {

    /**
     * @return Unique human-readable name of the benchmark
     */
    String getName();

    /**
     * @return The domain of this benchmark (e.g. "Linear Algebra", "Physics")
     */
    String getDomain();

    /**
     * Setup method called once before the benchmark runs.
     * Use this to initialize data structures.
     */
    void setup();

    /**
     * The core action to measure.
     * This method will be called repeatedly in a loop.
     */
    void run();

    /**
     * Cleanup method called once after the benchmark completes.
     */
    void teardown();

    /**
     * @return Estimated number of iterations required for a stable result
     */
    default int getSuggestedIterations() {
        return 10;
    }
}
