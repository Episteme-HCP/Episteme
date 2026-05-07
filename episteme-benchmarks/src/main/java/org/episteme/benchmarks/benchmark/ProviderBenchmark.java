package org.episteme.benchmarks.benchmark;

import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * A benchmark that is tied to a specific AlgorithmProvider.
 */
public interface ProviderBenchmark extends RunnableBenchmark {
    void setProvider(AlgorithmProvider provider);
}
