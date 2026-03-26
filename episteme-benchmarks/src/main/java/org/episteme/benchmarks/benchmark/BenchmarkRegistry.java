package org.episteme.benchmarks.benchmark;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.benchmarks.benchmark.benchmarks.SystematicBenchmark;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Iterator;

/**
 * Registry for dynamic benchmark instances.
 */
public class BenchmarkRegistry {

    public static List<RunnableBenchmark> discover() {
        List<RunnableBenchmark> all = new ArrayList<>();
        // Use context class loader to ensure visibility across modules
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) loader = BenchmarkRegistry.class.getClassLoader();
        
        try {
            // 1. Discover explicit benchmarks
            ServiceLoader<RunnableBenchmark> benchLoader = ServiceLoader.load(RunnableBenchmark.class, loader);
            Iterator<RunnableBenchmark> benchIterator = benchLoader.iterator();
            while (true) {
                try {
                    if (!benchIterator.hasNext()) break;
                    RunnableBenchmark b = benchIterator.next();
                    if (b instanceof SystematicBenchmark) {
                        expandSystematic((SystematicBenchmark<?>) b, all, loader);
                    } else {
                        all.add(b);
                    }
                } catch (Throwable e) {
                    System.err.println("[WARN] Skipping bad explicit benchmark: " + e.getMessage());
                }
            }

            // 2. Discover all generic AlgorithmProviders and wrap them
            ServiceLoader<org.episteme.core.technical.algorithm.AlgorithmProvider> providerLoader = 
                    ServiceLoader.load(org.episteme.core.technical.algorithm.AlgorithmProvider.class, loader);
            Iterator<org.episteme.core.technical.algorithm.AlgorithmProvider> providerIterator = providerLoader.iterator();
            while (true) {
                try {
                    if (!providerIterator.hasNext()) break;
                    org.episteme.core.technical.algorithm.AlgorithmProvider p = providerIterator.next();
                    
                    // Avoid duplicates if already covered by systematic expansion
                    String pName = p.getName().toLowerCase();
                    if (all.stream().anyMatch(b -> {
                        String bId = b.getId().toLowerCase();
                        String matchPart = pName.replace(" ", "-").replace("(", "").replace(")", "").replace("/", "-");
                        return bId.contains(matchPart);
                    })) {
                        continue;
                    }
                    
                    all.add(wrapProvider(p));
                } catch (Throwable e) {
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
            @Override public void setup() { 
                if (p instanceof org.episteme.core.mathematics.analysis.fft.FFTProvider) { /* Custom setup if needed */ }
                else if (p instanceof LinearAlgebraProvider) {
                    // Initialize some small matrices for verification
                }
            }
            @Override public void run() { 
                // Generic execution test
                if (p instanceof org.episteme.core.mathematics.analysis.fft.FFTProvider) {
                    ((org.episteme.core.mathematics.analysis.fft.FFTProvider)p).transform(new double[1024], new double[1024]);
                } else if (p instanceof LinearAlgebraProvider) {
                    // Execute a small dummy operation to verify the backend is truly working
                    try {
                        org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix A = org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix.of(new double[][]{{1,2},{3,4}});
                        @SuppressWarnings("unchecked")
                        LinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real> provider = (LinearAlgebraProvider<org.episteme.core.mathematics.numbers.real.Real>) p;
                        provider.multiply(A, A);
                    } catch (Throwable t) {
                        throw new RuntimeException("Generic LinearAlgebra validation failed: " + t.getMessage(), t);
                    }
                }
            }
            @Override public void teardown() {}
            @Override public int getSuggestedIterations() { return 100; }
            @Override public boolean isAvailable() { return p.isAvailable(); }
            @Override public org.episteme.core.technical.algorithm.AlgorithmProvider getAlgorithmProviderInstance() { return p; }
        };
    }

    private static <P extends org.episteme.core.technical.algorithm.AlgorithmProvider> void expandSystematic(SystematicBenchmark<P> base, List<RunnableBenchmark> list, ClassLoader loader) {
        System.out.println("[DEBUG]   - Expanding systematic benchmark: " + base.getNameBase() + " using provider class: " + base.getProviderClass().getName());
        try {
            ServiceLoader<P> sLoader = ServiceLoader.load(base.getProviderClass(), loader);
            Iterator<P> iterator = sLoader.iterator();
            boolean found = false;
            while (true) {
                try {
                    if (!iterator.hasNext()) break;
                    P p = iterator.next();
                    found = true;
                    addSystematicInstance(base, p, list);
                } catch (Throwable e) {
                    System.err.println("[WARN] Skipping bad systematic provider: " + e.getMessage());
                }
            }
            
            // Fallback: search in general AlgorithmProvider list if no specific providers found
            if (!found) {
                System.out.println("[DEBUG]   - No direct providers for " + base.getProviderClass().getSimpleName() + ". Attempting fallback discovery from general AlgorithmProviders...");
                ServiceLoader<org.episteme.core.technical.algorithm.AlgorithmProvider> genLoader = 
                        ServiceLoader.load(org.episteme.core.technical.algorithm.AlgorithmProvider.class, loader);
                for (org.episteme.core.technical.algorithm.AlgorithmProvider p : genLoader) {
                    if (base.getProviderClass().isInstance(p)) {
                        @SuppressWarnings("unchecked")
                        P casted = (P) p;
                        addSystematicInstance(base, casted, list);
                        found = true;
                    }
                }
            }
            
            if (!found) {
                System.out.println("[DEBUG]   - Discovery finished with 0 providers for " + base.getProviderClass().getSimpleName());
            }
        } catch (Throwable t) {
             System.err.println("[ERROR] Failed to discover providers for class " + base.getProviderClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    private static <P extends org.episteme.core.technical.algorithm.AlgorithmProvider> void addSystematicInstance(SystematicBenchmark<P> base, P p, List<RunnableBenchmark> list) {
        System.out.println("[DEBUG]   - Found systematic provider implementation: " + p.getName() + " (Type: " + p.getAlgorithmType() + ")");
        
        // MPFR and ND4J are now included for auditing as requested by user.

        // Check compatibility if it's a LinearAlgebraProvider
        if (p instanceof LinearAlgebraProvider) {
            LinearAlgebraProvider<?> la = (LinearAlgebraProvider<?>) p;
            String domain = base.getDomain().toLowerCase();
            
            // Heuristic for domain compatibility
            if (domain.contains("complex") && !la.isCompatible(org.episteme.core.mathematics.numbers.complex.Complex.ZERO)) {
                System.out.println("[DEBUG]     - Provider " + p.getName() + " is NOT compatible with Complex. Skipping.");
                return;
            }
            
            if (domain.contains("real") && !la.isCompatible(org.episteme.core.mathematics.sets.Reals.getInstance())) {
                System.out.println("[DEBUG]     - Provider " + p.getName() + " is NOT compatible with Reals. Skipping.");
                return;
            }
        }

        // Strict separation: Do not mix Sparse providers in Dense benchmarks
        boolean isProviderSparse = p.getAlgorithmType().toLowerCase().contains("sparse");
        boolean isBenchmarkDense = base.getDomain().toLowerCase().contains("dense");
        
        if (isProviderSparse && isBenchmarkDense) {
             System.out.println("[DEBUG]     - Skipping Sparse provider " + p.getName() + " for Dense benchmark " + base.getNameBase());
             return; 
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
            @Override public org.episteme.core.technical.algorithm.AlgorithmProvider getAlgorithmProviderInstance() { return p; }
        };
        System.out.println("[DEBUG]     + Added benchmark instance: " + rb.getId() + " [Domain: " + rb.getDomain() + "]");
        list.add(rb);
    }
}
