package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
import org.episteme.benchmarks.reporting.BenchmarkReporter;

import org.junit.jupiter.api.Test;
import java.util.*;

/**
 * Universal Performance Audit for Linear Algebra.
 * Measures Latency and Throughput for all 68+ operations.
 */
public class LinearAlgebraPerformanceAudit {

    private static final int MATRIX_SIZE = 5;

    @Test
    public void runPerformanceAudit() {
        String precisionProp = System.getProperty("org.episteme.test.precision", "normal").toLowerCase();
        BenchmarkReporter reporter = new BenchmarkReporter("Universal Linear Algebra Performance Audit (" + precisionProp.toUpperCase() + ")");
        
        reporter.addMetadata("Precision", precisionProp);
        reporter.addMetadata("Matrix Size", MATRIX_SIZE + "x" + MATRIX_SIZE);

        List<LinearAlgebraProvider<?>> providers = discoverProviders();

        LinearAlgebraProvider<?> referenceProvider = providers.stream()
            .filter(p -> p.getName().contains("Standard"))
            .findFirst()
            .orElse(providers.get(0));

        for (LinearAlgebraProvider<?> prov : providers) {
            if (!prov.isAvailable()) continue;
            
            try {
                System.out.println("[PerfAudit] Benchmarking exhaustive operations for: " + prov.getName());
                Map<String, Object> metrics = new LinkedHashMap<>();
                measureExecution(metrics, prov, referenceProvider, precisionProp);

                BenchmarkResult res = new BenchmarkResult(
                    "perf-" + prov.getName().toLowerCase().replace(" ", "-"),
                    prov.getName(),
                    prov.getClass().getSimpleName(),
                    "Linear Algebra Performance",
                    "SUCCESS",
                    System.currentTimeMillis(),
                    0, 0, 0, 0, 0,
                    new HashMap<>(),
                    metrics
                );
                reporter.addResult(res);
            } catch (Throwable t) {
                System.err.println("Benchmark failed for " + prov.getName() + ": " + t.getMessage());
            }
        }

        reporter.generateReport();
    }

    private void measureExecution(Map<String, Object> metrics, LinearAlgebraProvider<?> prov, LinearAlgebraProvider<?> ref, String precision) {
        double tolerance = 1.0; // High tolerance for performance measurement (we focus on timing)
        if (precision.equals("exact")) {
            @SuppressWarnings("unchecked")
            Ring<RealBig> rbRing = (Ring<RealBig>) (Object) RealBig.ZERO.getScalarRing();
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<RealBig> castedProv = (LinearAlgebraProvider<RealBig>) (LinearAlgebraProvider<?>) prov;
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<RealBig> castedRef = (LinearAlgebraProvider<RealBig>) (LinearAlgebraProvider<?>) ref;
            LinearAlgebraAuditSuite.runFullAudit(castedProv, castedRef, MATRIX_SIZE, (op, test) -> measure(metrics, op, test), rbRing, "RB:", tolerance);
            
            Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
            if (prov.isCompatible(complexRing)) {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Complex> complexProv = (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider<?>) prov;
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Complex> complexRef = (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider<?>) ref;
                LinearAlgebraAuditSuite.runFullAudit(complexProv, complexRef, MATRIX_SIZE, (op, test) -> measure(metrics, op, test), complexRing, "C:", tolerance);
            }
        } else {
            Ring<Real> realRing = org.episteme.core.mathematics.sets.Reals.getInstance();
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Real> castedProv = (LinearAlgebraProvider<Real>) (LinearAlgebraProvider<?>) prov;
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Real> castedRef = (LinearAlgebraProvider<Real>) (LinearAlgebraProvider<?>) ref;
            LinearAlgebraAuditSuite.runFullAudit(castedProv, castedRef, MATRIX_SIZE, (op, test) -> measure(metrics, op, test), realRing, "R:", tolerance);
            
            Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
            if (prov.isCompatible(complexRing)) {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Complex> complexProv = (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider<?>) prov;
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Complex> complexRef = (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider<?>) ref;
                LinearAlgebraAuditSuite.runFullAudit(complexProv, complexRef, MATRIX_SIZE, (op, test) -> measure(metrics, op, test), complexRing, "C:", tolerance);
            }
        }
    }

    private void measure(Map<String, Object> metrics, String op, java.util.function.Supplier<?> test) {
        try {
            // Warmup
            for (int i = 0; i < 3; i++) test.get();
            long start = System.nanoTime();
            int iters = 10;
            for (int i = 0; i < iters; i++) test.get();
            long end = System.nanoTime();
            double latency = (end - start) / (1_000_000.0 * iters);
            metrics.put(op + ":latency", latency);
            metrics.put(op + ":throughput", 1000.0 / Math.max(latency, 1e-9));
        } catch (Throwable t) {
            metrics.put(op + ":latency", -1.0);
        }
    }

    private List<LinearAlgebraProvider<?>> discoverProviders() {
        Set<LinearAlgebraProvider<?>> providers = new LinkedHashSet<>();
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> p : loader) providers.add(p);
        return new ArrayList<>(providers);
    }
}
