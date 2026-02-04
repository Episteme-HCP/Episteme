package org.jscience.benchmarks.benchmark;

import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * A benchmark that is tied to a specific AlgorithmProvider.
 */
public interface ProviderBenchmark extends RunnableBenchmark {
    void setProvider(AlgorithmProvider provider);
}
