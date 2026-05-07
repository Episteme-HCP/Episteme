package org.episteme.benchmarks.test.audit;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import org.episteme.benchmarks.benchmark.benchmarks.SystematicBenchmark;
import org.episteme.benchmarks.reporting.BenchmarkReporter;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.ServiceLoader;

/**
 * Universal Multimodal Performance Audit based on Systematic Benchmarks.
 */
public class UniversalMultimodalAudit {

    private static final int ITERATIONS = 5;
    private static final int WARMUP = 2;

    @Test
    public void runUniversalAudit() {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        BenchmarkReporter reporter = new BenchmarkReporter("Universal Multimodal Performance Audit");
        reporter.addMetadata("Run Date", new Date().toString());
        reporter.addMetadata("JVM", System.getProperty("java.version"));
        
        List<SystematicBenchmark<?>> systematicBenchmarks = discoverSystematicBenchmarks();
        System.out.println("[UniversalAudit] Found " + systematicBenchmarks.size() + " systematic benchmarks.");

        for (SystematicBenchmark<?> benchmark : systematicBenchmarks) {
            System.out.println("[UniversalAudit] Processing Category: " + benchmark.getNameBase());
            
            List<? extends AlgorithmProvider> providers = discoverProvidersFor(benchmark);
            for (AlgorithmProvider provider : providers) {
                if (!provider.isAvailable()) continue;

                System.out.println("[UniversalAudit]   -> Provider: " + provider.getName());
                
                Map<String, Object> metrics = new LinkedHashMap<>();
                
                // Run all 3 modes
                runBenchmarkMode(benchmark, provider, "FAST", MathContext.fast(), metrics);
                runBenchmarkMode(benchmark, provider, "NORMAL", MathContext.normal(), metrics);
                
                if (benchmark.isHighPrecision(provider)) {
                    runBenchmarkMode(benchmark, provider, "EXACT", MathContext.exact(), metrics);
                } else {
                    System.out.println("      - Mode EXACT... SKIPPED (Unsupported)");
                    metrics.put("EXACT:status", "SKIPPED");
                }

                BenchmarkResult result = new BenchmarkResult(
                    benchmark.getIdPrefix() + "-" + provider.getName().toLowerCase().replace(" ", "-"),
                    provider.getName(),
                    benchmark.getNameBase(),
                    benchmark.getDomain(),
                    "SUCCESS",
                    System.currentTimeMillis(),
                    0L, 1L, 0.0, 0.0, 0L,
                    new HashMap<>(),
                    metrics
                );
                reporter.addResult(result);
            }
        }

        reporter.generateReport("linear_algebra_performance_multimodal", BenchmarkReporter.ReportType.ALL);
        reporter.generateReport("linear_algebra_performance_multimodal", BenchmarkReporter.ReportType.FAST);
        reporter.generateReport("linear_algebra_performance_multimodal", BenchmarkReporter.ReportType.NORMAL);
        reporter.generateReport("linear_algebra_performance_multimodal", BenchmarkReporter.ReportType.EXACT);
        
        reporter.exportToRoot("docs/LINEAR_ALGEBRA_PERFORMANCE_MULTIMODAL_" + timestamp + ".md");
    }

    private <P extends AlgorithmProvider> void runBenchmarkMode(SystematicBenchmark<P> benchmark, AlgorithmProvider provider, String modeName, MathContext ctx, Map<String, Object> metrics) {
        System.out.print("      - Mode " + modeName + "... ");
        
        MathContext previous = MathContext.getCurrent();
        try {
            @SuppressWarnings("unchecked")
            P castedProvider = (P) provider;
            benchmark.setProvider(castedProvider);
            
            // Apply MathContext
            if (modeName.equals("EXACT")) {
                MathContext exact = MathContext.exact();
                MathContext.setCurrent(exact);
                org.episteme.core.mathematics.context.MathContext.getNumericalConfiguration().setMathContext(new java.math.MathContext(64));
            } else {
                MathContext.setCurrent(ctx);
            }
            
            // Setup data
            benchmark.setup();

            // Warmup
            for (int i = 0; i < WARMUP; i++) benchmark.run();

            // Measure
            long start = System.nanoTime();
            for (int i = 0; i < ITERATIONS; i++) benchmark.run();
            long end = System.nanoTime();

            double latency = (end - start) / (1_000_000.0 * ITERATIONS);
            double throughput = 1000.0 / Math.max(latency, 1e-9);

            metrics.put(modeName + ":latency", latency);
            metrics.put(modeName + ":throughput", throughput);
            System.out.printf("%.3f ms\n", latency);

        } catch (Throwable t) {
            System.err.println("FAILED: " + t.getMessage());
            metrics.put(modeName + ":error", t.getMessage());
        } finally {
            benchmark.teardown();
            MathContext.setCurrent(previous);
        }
    }

    private List<SystematicBenchmark<?>> discoverSystematicBenchmarks() {
        List<SystematicBenchmark<?>> benchmarks = new ArrayList<>();
        ServiceLoader<RunnableBenchmark> loader = ServiceLoader.load(RunnableBenchmark.class);
        for (RunnableBenchmark rb : loader) {
            if (rb instanceof SystematicBenchmark<?> sb) {
                benchmarks.add(sb);
            }
        }
        
        // Manual fallback if ServiceLoader fails
        if (benchmarks.isEmpty()) {
            benchmarks.add(new org.episteme.benchmarks.benchmark.benchmarks.SystematicMatrixBenchmark());
            benchmarks.add(new org.episteme.benchmarks.benchmark.benchmarks.SystematicSolveBenchmark());
            benchmarks.add(new org.episteme.benchmarks.benchmark.benchmarks.SystematicInverseBenchmark());
            benchmarks.add(new org.episteme.benchmarks.benchmark.benchmarks.SystematicFFTBenchmark());
            benchmarks.add(new org.episteme.benchmarks.benchmark.benchmarks.SystematicSparseMatrixBenchmark());
        }
        
        return benchmarks;
    }

    private <P extends AlgorithmProvider> List<P> discoverProvidersFor(SystematicBenchmark<P> benchmark) {
        Class<P> providerClass = benchmark.getProviderClass();
        Set<P> providers = new LinkedHashSet<>();
        
        // 1. ServiceLoader
        ServiceLoader.load(providerClass).forEach(providers::add);
        
        // 2. Backend Discovery
        try {
            for (org.episteme.core.technical.backend.Backend b : org.episteme.core.technical.backend.BackendDiscovery.getInstance().getProviders()) {
                for (AlgorithmProvider ap : b.getAlgorithmProviders()) {
                    if (providerClass.isInstance(ap)) {
                        providers.add(providerClass.cast(ap));
                    }
                }
            }
        } catch (Throwable t) {}
        
        return new ArrayList<>(providers);
    }
}
