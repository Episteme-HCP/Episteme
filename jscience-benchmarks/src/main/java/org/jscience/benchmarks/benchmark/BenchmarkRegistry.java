package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
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
            if (b instanceof SystematicBenchmark) {
                expandSystematic((SystematicBenchmark<?>) b, all);
            } else {
                all.add(b);
            }
        }
        
        // 2. Discover ComparisonBenchmarks (future step)
        
        return all;
    }

    private static <P extends org.jscience.core.technical.algorithm.AlgorithmProvider> void expandSystematic(SystematicBenchmark<P> base, List<RunnableBenchmark> list) {
        ServiceLoader<P> loader = ServiceLoader.load(base.getProviderClass());
        for (P p : loader) {
            // Check compatibility if it's a LinearAlgebraProvider
            if (p instanceof LinearAlgebraProvider) {
                if (!((LinearAlgebraProvider<?>) p).isCompatible(org.jscience.core.mathematics.sets.Reals.getInstance())) {
                    continue;
                }
            }
            
            list.add(new RunnableBenchmark() {
                @Override public String getId() { return base.getIdPrefix() + "-" + p.getName().toLowerCase().replace(" ", "-"); }
                @Override public String getName() { return base.getNameBase() + " [" + p.getName() + "]"; }
                @Override public String getDescription() { return base.getDescription() + " using " + p.getName() + " provider."; }
                @Override public String getDomain() { return base.getDomain(); }
                @Override public void setup() { base.setup(); base.setProvider(p); }
                @Override public void run() { base.run(); }
                @Override public void teardown() { base.teardown(); }
                @Override public int getSuggestedIterations() { return base.getSuggestedIterations(); }
            });
        }
    }
}
