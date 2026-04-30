package org.episteme.benchmarks.audit;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.analysis.fft.FFTProvider;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
import org.episteme.benchmarks.reporting.BenchmarkReporter;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

/**
 * Universal Multimodal Performance Audit.
 * Tests multiple providers across FAST, NORMAL, and EXACT modes in a single run.
 */
public class UniversalMultimodalAudit {

    private static final int ITERATIONS = 10;
    private static final int WARMUP = 3;

    @Test
    public void runUniversalAudit() {
        BenchmarkReporter reporter = new BenchmarkReporter("Universal Multimodal Performance Audit");
        reporter.addMetadata("Run Date", new Date().toString());
        reporter.addMetadata("JVM", System.getProperty("java.version"));
        
        List<AlgorithmProvider> providers = discoverProviders();
        System.out.println("[UniversalAudit] Found " + providers.size() + " total providers.");

        for (AlgorithmProvider provider : providers) {
            if (!provider.isAvailable()) {
                System.out.println("[UniversalAudit] Skipping unavailable provider: " + provider.getName());
                continue;
            }

            System.out.println("[UniversalAudit] >>> Auditing: " + provider.getName() + " (" + provider.getAlgorithmType() + ")");
            
            Map<String, Object> metrics = new LinkedHashMap<>();
            
            // Run all 3 modes in sequence
            // Run modes with distinction between Primitive/SIMD and Object-based paths
            auditMode(provider, "FAST (SIMD)", MathContext.fast(), metrics, 128, () -> createFastSIMDMatrix(128));
            auditMode(provider, "FAST (Object)", MathContext.fast(), metrics, 128, () -> createFastObjectMatrix(128));
            auditMode(provider, "NORMAL (SIMD)", MathContext.normal(), metrics, 64, () -> createNormalSIMDMatrix(64));
            auditMode(provider, "NORMAL (Object)", MathContext.normal(), metrics, 64, () -> createNormalObjectMatrix(64));
            auditMode(provider, "EXACT", MathContext.exact(), metrics, 8, () -> createExactMatrix(8));

            BenchmarkResult result = new BenchmarkResult(
                "audit-" + provider.getName().toLowerCase().replace(" ", "-"),
                provider.getName(),
                provider.getClass().getSimpleName(),
                provider.getAlgorithmType(),
                "SUCCESS",
                System.currentTimeMillis(),
                0L, 1L, 0.0, 0.0, 0L,
                new HashMap<>(),
                metrics
            );
            reporter.addResult(result);
        }

        reporter.generateReport("linear_algebra_performance_multimodal");
        reporter.exportToRoot("docs/LINEAR_ALGEBRA_PERFORMANCE_MULTIMODAL.md");
    }

    private void auditMode(AlgorithmProvider provider, String modeName, MathContext ctx, Map<String, Object> metrics, int size, Supplier<Object> matrixSupplier) {
        System.out.print("  -> Mode " + modeName + " (size=" + size + ")... ");
        MathContext previous = MathContext.getCurrent();
        try {
            if (provider instanceof org.episteme.core.technical.algorithm.AlgorithmProvider) {
                org.episteme.core.technical.algorithm.TestingAlgorithmService service = 
                    new org.episteme.core.technical.algorithm.TestingAlgorithmService(java.util.List.of(provider), true);
                
                // Block fallback for all interfaces implemented by this provider
                for (Class<?> iface : provider.getClass().getInterfaces()) {
                    if (org.episteme.core.technical.algorithm.AlgorithmProvider.class.isAssignableFrom(iface) && 
                        iface != org.episteme.core.technical.algorithm.AlgorithmProvider.class) {
                        service.blockFallbackFor((Class<? extends org.episteme.core.technical.algorithm.AlgorithmProvider>) iface);
                    }
                }
                org.episteme.core.technical.algorithm.AlgorithmManager.setService(service);
            }

            if (modeName.contains("EXACT")) {
                MathContext exact = MathContext.exact();
                MathContext.setCurrent(exact);
                // Lower precision for audit to 64 digits (default is 1000)
                org.episteme.core.mathematics.context.MathContext.getNumericalConfiguration().setMathContext(new java.math.MathContext(64));
            } else {
                MathContext.setCurrent(ctx);
            }
            
            // Define the task
            Runnable task = createTaskFor(provider, size, matrixSupplier);
            if (task == null) {
                System.out.println("No task defined.");
                metrics.put(modeName + ":latency", -1.0);
                return;
            }

            // Warmup
            for (int i = 0; i < WARMUP; i++) task.run();

            // Measure
            long start = System.nanoTime();
            for (int i = 0; i < ITERATIONS; i++) task.run();
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
            org.episteme.core.technical.algorithm.AlgorithmManager.setService(new org.episteme.core.technical.algorithm.StandardAlgorithmService());
            MathContext.setCurrent(previous);
        }
    }

    private Runnable createTaskFor(AlgorithmProvider provider, int size, Supplier<Object> matrixSupplier) {
        if (provider instanceof LinearAlgebraProvider) {
            LinearAlgebraProvider<Object> la = (LinearAlgebraProvider<Object>) provider;
            return () -> {
                try {
                    org.episteme.core.mathematics.linearalgebra.Matrix<Object> mat = (org.episteme.core.mathematics.linearalgebra.Matrix<Object>) matrixSupplier.get();
                    la.multiply(mat, mat);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }
        
        if (provider instanceof FFTProvider) {
            FFTProvider fft = (FFTProvider) provider;
            return () -> {
                if (MathContext.getCurrent().isHighPrecision()) {
                    try {
                        fft.transform(new double[size], new double[size]);
                    } catch (UnsupportedOperationException e) {
                        // ignore
                    }
                } else {
                    fft.transform(new double[size], new double[size]);
                }
            };
        }

        return null;
    }

    private Object createFastSIMDMatrix(int n) {
        float[] data = new float[n * n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i * n + j] = (float)(i + j);
        return new org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealFloatMatrix(n, n, data);
    }

    private Object createFastObjectMatrix(int n) {
        org.episteme.core.mathematics.numbers.real.RealFloat[][] data = new org.episteme.core.mathematics.numbers.real.RealFloat[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = org.episteme.core.mathematics.numbers.real.RealFloat.create((float)(i + j));
        return org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix.of(data, org.episteme.core.mathematics.sets.Reals.getInstance());
    }

    private Object createNormalSIMDMatrix(int n) {
        double[] data = new double[n * n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i * n + j] = (double)(i + j);
        return new org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealDoubleMatrix(n, n, data);
    }

    private Object createNormalObjectMatrix(int n) {
        org.episteme.core.mathematics.numbers.real.RealDouble[][] data = new org.episteme.core.mathematics.numbers.real.RealDouble[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = org.episteme.core.mathematics.numbers.real.RealDouble.create((double)(i + j));
        return org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix.of(data, org.episteme.core.mathematics.sets.Reals.getInstance());
    }

    private Object createExactMatrix(int n) {
        org.episteme.core.mathematics.numbers.real.Real[][] data = new org.episteme.core.mathematics.numbers.real.Real[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = org.episteme.core.mathematics.numbers.real.RealBig.create(java.math.BigDecimal.valueOf(i + j));
        return org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix.of(data, org.episteme.core.mathematics.sets.Reals.getInstance());
    }

    private List<AlgorithmProvider> discoverProviders() {
        Set<AlgorithmProvider> set = new LinkedHashSet<>();
        
        // Generic discovery
        ServiceLoader.load(AlgorithmProvider.class).forEach(set::add);
        
        // Specialized discovery (in case they don't register as AlgorithmProvider)
        ServiceLoader.load(LinearAlgebraProvider.class).forEach(set::add);
        ServiceLoader.load(FFTProvider.class).forEach(set::add);
        
        return new ArrayList<>(set);
    }
}
