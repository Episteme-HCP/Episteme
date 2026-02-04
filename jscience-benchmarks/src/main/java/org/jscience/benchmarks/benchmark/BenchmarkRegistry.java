package org.jscience.benchmarks.benchmark;

import org.jscience.core.technical.algorithm.FFTProvider;
import org.jscience.core.technical.algorithm.LinearAlgebraProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Registry for dynamic benchmark instances.
 */
public class BenchmarkRegistry {

    public static List<RunnableBenchmark> discover() {
        List<RunnableBenchmark> all = new ArrayList<>();
        
        // 1. Discover standard benchmarks
        ServiceLoader<RunnableBenchmark> loader = ServiceLoader.load(RunnableBenchmark.class);
        for (RunnableBenchmark b : loader) {
            // If it's a "Systematic" one, we skip it as we will expand it manually
            // OR we can make it return all variations.
            if (b instanceof SystematicFFTBenchmark) {
                expandFFT((SystematicFFTBenchmark) b, all);
            } else if (b instanceof SystematicMatrixBenchmark) {
                expandMatrix((SystematicMatrixBenchmark) b, all);
            } else {
                all.add(b);
            }
        }
        
        return all;
    }

    private static void expandFFT(SystematicFFTBenchmark base, List<RunnableBenchmark> list) {
        ServiceLoader<FFTProvider> loader = ServiceLoader.load(FFTProvider.class);
        for (FFTProvider p : loader) {
            list.add(new RunnableBenchmark() {
                @Override public String getName() { return "FFT [" + p.getName() + "]"; }
                @Override public String getDomain() { return base.getDomain(); }
                @Override public void setup() { base.setup(); base.setProvider(p); }
                @Override public void run() { base.run(); }
                @Override public void teardown() { base.teardown(); }
                @Override public int getSuggestedIterations() { return base.getSuggestedIterations(); }
            });
        }
    }

    private static void expandMatrix(SystematicMatrixBenchmark base, List<RunnableBenchmark> list) {
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> p : loader) {
            if (p.isCompatible(Reals.getInstance())) {
               list.add(new RunnableBenchmark() {
                   @Override public String getName() { return "Matrix Mult [" + p.getName() + "]"; }
                   @Override public String getDomain() { return base.getDomain(); }
                   @Override public void setup() { base.setup(); base.setProvider((LinearAlgebraProvider<Real>) p); }
                   @Override public void run() { base.run(); }
                   @Override public void teardown() { base.teardown(); }
                   @Override public int getSuggestedIterations() { return base.getSuggestedIterations(); }
               });
            }
        }
    }
}
