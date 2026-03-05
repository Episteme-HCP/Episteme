package org.episteme.benchmarks.benchmark;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.benchmarks.benchmark.benchmarks.SystematicBenchmark;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Registry for dynamic benchmark instances.
 */
public class BenchmarkRegistry {

    public static List<RunnableBenchmark> discover() {
        List<RunnableBenchmark> all = new ArrayList<>();
        ClassLoader loader = BenchmarkRegistry.class.getClassLoader();
        try {
            // 1. Discover explicit benchmarks
            System.out.println("[DEBUG] Discovery: Loading explicit benchmarks...");
            ServiceLoader<RunnableBenchmark> benchLoader = ServiceLoader.load(RunnableBenchmark.class, loader);
            java.util.Iterator<RunnableBenchmark> benchIterator = benchLoader.iterator();
            int explicitCount = 0;
            while (benchIterator.hasNext()) {
                try {
                    RunnableBenchmark b = benchIterator.next();
                    explicitCount++;
                    System.out.println("[DEBUG] Found explicit benchmark: " + b.getName() + " (ID: " + b.getId() + ", Class: " + b.getClass().getSimpleName() + ")");
                    if (b instanceof SystematicBenchmark) {
                        System.out.println("[DEBUG]   - Is systematic, expanding...");
                        expandSystematic((SystematicBenchmark<?>) b, all);
                    } else {
                        all.add(b);
                    }
                } catch (java.util.ServiceConfigurationError | Exception e) {
                    System.err.println("[WARN] Skipping bad explicit benchmark: " + e.getMessage());
                }
            }
            System.out.println("[DEBUG] Discovery: Found " + explicitCount + " explicit benchmarks candidates.");

            // 2. Discover all generic AlgorithmProviders and wrap them
            ServiceLoader<org.episteme.core.technical.algorithm.AlgorithmProvider> providerLoader = 
                    ServiceLoader.load(org.episteme.core.technical.algorithm.AlgorithmProvider.class, loader);
            java.util.Iterator<org.episteme.core.technical.algorithm.AlgorithmProvider> providerIterator = providerLoader.iterator();
            while (providerIterator.hasNext()) {
                try {
                    org.episteme.core.technical.algorithm.AlgorithmProvider p = providerIterator.next();
                    System.out.println("[DEBUG] Processing provider: " + p.getName() + " (" + p.getClass().getName() + ")");
                    
                    // Avoid duplicates if already covered by systematic expansion
                    if (all.stream().anyMatch(b -> b.getId().contains(p.getName().toLowerCase().replace(" ", "-")))) {
                        continue;
                    }
                    
                    all.add(wrapProvider(p));
                } catch (java.util.ServiceConfigurationError | Exception e) {
                    System.err.println("[WARN] Skipping bad algorithm provider: " + e.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("[ERROR] Critical failure during benchmark discovery: " + t.getMessage());
        }
        
        return all;
    }

    private static RunnableBenchmark wrapProvider(org.episteme.core.technical.algorithm.AlgorithmProvider p) {
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
            @Override public void setup() { if (p instanceof org.episteme.core.mathematics.analysis.fft.FFTProvider) { /* Custom setup if needed */ } }
            @Override public void run() { 
                // Generic execution test
                if (p instanceof org.episteme.core.mathematics.analysis.fft.FFTProvider) {
                    ((org.episteme.core.mathematics.analysis.fft.FFTProvider)p).transform(new double[1024], new double[1024]);
                }
            }
            @Override public void teardown() {}
            @Override public int getSuggestedIterations() { return 100; }
            @Override public boolean isAvailable() { return p.isAvailable(); }
        };
    }

    private static <P extends org.episteme.core.technical.algorithm.AlgorithmProvider> void expandSystematic(SystematicBenchmark<P> base, List<RunnableBenchmark> list) {
        System.out.println("[DEBUG]   - Expanding systematic benchmark: " + base.getNameBase() + " using provider class: " + base.getProviderClass().getName());
        try {
            ClassLoader loader = BenchmarkRegistry.class.getClassLoader();
            ServiceLoader<P> sLoader = ServiceLoader.load(base.getProviderClass(), loader);
            java.util.Iterator<P> iterator = sLoader.iterator();
            int pCount = 0;
            while (iterator.hasNext()) {
                try {
                    P p = iterator.next();
                    pCount++;
                    System.out.println("[DEBUG]   - Found systematic provider implementation: " + p.getName() + " (Type: " + p.getAlgorithmType() + ")");
                    
                    // Check compatibility if it's a LinearAlgebraProvider
                    if (p instanceof LinearAlgebraProvider) {
                        if (!((LinearAlgebraProvider<?>) p).isCompatible(org.episteme.core.mathematics.sets.Reals.getInstance())) {
                            System.out.println("[DEBUG]     - Provider " + p.getName() + " is NOT compatible with Reals. Skipping.");
                            continue;
                        }
                    }

                    // Strict separation: Do not mix Sparse providers in Dense benchmarks
                    boolean isProviderSparse = p.getAlgorithmType().toLowerCase().contains("sparse");
                    boolean isBenchmarkDense = base.getDomain().toLowerCase().contains("dense");
                    
                    if (isProviderSparse && isBenchmarkDense) {
                         System.out.println("[DEBUG]     - Skipping Sparse provider " + p.getName() + " for Dense benchmark " + base.getNameBase());
                         continue; 
                    }
                    
                    RunnableBenchmark rb = new RunnableBenchmark() {
                        @Override public String getId() { return base.getIdPrefix() + "-" + p.getName().toLowerCase().replace(" ", "-"); }
                        @Override public String getName() { return base.getNameBase() + " (" + p.getName() + ")"; }
                        @Override public String getAlgorithmProvider() { return p.getName(); }
                        @Override public String getDescription() { return base.getDescription(); }
                        @Override public String getDomain() { return base.getDomain(); }
                        @Override public void setup() { base.setProvider(p); base.setup(); }
                        @Override public void run() { base.run(); }
                        @Override public void teardown() { base.teardown(); }
                        @Override public int getSuggestedIterations() { return base.getSuggestedIterations(); }
                        @Override public boolean isAvailable() { return p.isAvailable(); }
                    };
                    System.out.println("[DEBUG]     + Added benchmark instance: " + rb.getId() + " [Domain: " + rb.getDomain() + "]");
                    list.add(rb);
                } catch (java.util.ServiceConfigurationError | Exception e) {
                    System.err.println("[WARN] Skipping bad systematic provider: " + e.getMessage());
                }
            }
        } catch (Throwable t) {
                    System.err.println("[ERROR] Failed to discover providers for class " + base.getProviderClass().getSimpleName() + ": " + t.getMessage());
        }
    }
}
