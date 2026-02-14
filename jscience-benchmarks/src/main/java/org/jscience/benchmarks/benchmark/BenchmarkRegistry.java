package org.jscience.benchmarks.benchmark;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.benchmarks.benchmark.benchmarks.SystematicBenchmark;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Registry for dynamic benchmark instances.
 */
public class BenchmarkRegistry {

    public static List<RunnableBenchmark> discover() {
        List<RunnableBenchmark> all = new ArrayList<>();
        
        try {
            // 1. Discover explicit benchmarks
            ServiceLoader<RunnableBenchmark> benchLoader = ServiceLoader.load(RunnableBenchmark.class);
            for (RunnableBenchmark b : benchLoader) {
                try {
                    if (b instanceof SystematicBenchmark) {
                        expandSystematic((SystematicBenchmark<?>) b, all);
                    } else {
                        all.add(b);
                    }
                } catch (Throwable t) {
                    System.err.println("[WARN] Failed to load explicit benchmark: " + t.getMessage());
                }
            }

            // 2. Discover all generic AlgorithmProviders and wrap them
            ServiceLoader<org.jscience.core.technical.algorithm.AlgorithmProvider> providerLoader = 
                    ServiceLoader.load(org.jscience.core.technical.algorithm.AlgorithmProvider.class);
            for (org.jscience.core.technical.algorithm.AlgorithmProvider p : providerLoader) {
                try {
                    if (!p.isAvailable()) continue;
                    
                    // Avoid duplicates if already covered by systematic expansion
                    if (all.stream().anyMatch(b -> b.getId().contains(p.getName().toLowerCase().replace(" ", "-")))) {
                        continue;
                    }
                    
                    all.add(wrapProvider(p));
                } catch (Throwable t) {
                    System.err.println("[WARN] Failed to load provider benchmark for " + p.getName() + ": " + t.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("[ERROR] Critical failure during benchmark discovery: " + t.getMessage());
        }
        
        return all;
    }

    private static RunnableBenchmark wrapProvider(org.jscience.core.technical.algorithm.AlgorithmProvider p) {
        return new RunnableBenchmark() {
            @Override public String getId() { return "gen-" + p.getAlgorithmType() + "-" + p.getName().toLowerCase().replace(" ", "-"); }
            @Override public String getName() { 
                String type = p.getAlgorithmType();
                if (type == null || type.isEmpty()) return "Unknown";
                return type.substring(0, 1).toUpperCase() + type.substring(1);
            }
            @Override public String getAlgorithmProvider() { return p.getName(); }
            @Override public String getDescription() { return "Generic execution validation for " + p.getAlgorithmType(); }
            @Override public String getDomain() { 
                String type = p.getAlgorithmType();
                return type.substring(0, 1).toUpperCase() + type.substring(1);
            }
            @Override public void setup() { if (p instanceof org.jscience.core.technical.algorithm.FFTProvider) { /* Custom setup if needed */ } }
            @Override public void run() { 
                // Generic execution test
                if (p instanceof org.jscience.core.technical.algorithm.FFTProvider) {
                    ((org.jscience.core.technical.algorithm.FFTProvider)p).transform(new double[1024], new double[1024]);
                }
            }
            @Override public void teardown() {}
            @Override public int getSuggestedIterations() { return 100; }
        };
    }

    private static <P extends org.jscience.core.technical.algorithm.AlgorithmProvider> void expandSystematic(SystematicBenchmark<P> base, List<RunnableBenchmark> list) {
        try {
            ServiceLoader<P> loader = ServiceLoader.load(base.getProviderClass());
            for (P p : loader) {
                try {
                    // Check compatibility if it's a LinearAlgebraProvider
                    if (p instanceof LinearAlgebraProvider) {
                        if (!((LinearAlgebraProvider<?>) p).isCompatible(org.jscience.core.mathematics.sets.Reals.getInstance())) {
                            continue;
                        }
                    }


                    // Strict separation: Do not mix Sparse providers in Dense benchmarks
                    boolean isProviderSparse = p.getAlgorithmType().contains("Sparse");
                    boolean isBenchmarkDense = base.getDomain().contains("Dense") && !base.getDomain().contains("Sparse");
                    
                    if (isProviderSparse && isBenchmarkDense) {
                         continue; 
                    }
                    
                    list.add(new RunnableBenchmark() {
                        @Override public String getId() { return base.getIdPrefix() + "-" + p.getName().toLowerCase().replace(" ", "-"); }
                        @Override public String getName() { return base.getNameBase(); }
                        @Override public String getAlgorithmProvider() { return p.getName(); }
                        @Override public String getDescription() { return base.getDescription(); }
                        @Override public String getDomain() { return base.getDomain(); }
                        @Override public void setup() { base.setup(); base.setProvider(p); }
                        @Override public void run() { base.run(); }
                        @Override public void teardown() { base.teardown(); }
                        @Override public int getSuggestedIterations() { return base.getSuggestedIterations(); }
                    });
                } catch (Throwable t) {
                    System.err.println("[WARN] Failed to instantiate provider for " + base.getNameBase() + ": " + t.getMessage());
                }
            }
        } catch (Throwable t) {
                    System.err.println("[ERROR] Failed to discover providers for class " + base.getProviderClass().getSimpleName() + ": " + t.getMessage());
        }
    }
}
