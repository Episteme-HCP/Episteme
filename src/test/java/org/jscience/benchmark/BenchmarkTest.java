package org.jscience.benchmark;

import org.junit.jupiter.api.Test;

/**
 * Wrapper to run benchmarks via Maven Surefire.
 */
public class BenchmarkTest {

    @Test
    public void runBenchmarks() {
        // Invoke the main method of BenchmarkRunner
        BenchmarkRunner.main(new String[] {});
    }
}
