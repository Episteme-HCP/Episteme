package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.context.MathContext;
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

        MathContext targetCtx = MathContext.normal();
        if (precisionProp.equals("exact")) targetCtx = MathContext.exact();
        else if (precisionProp.equals("fast")) targetCtx = MathContext.fast();

        org.episteme.core.Episteme.getNumericalConfiguration().setRealPrecision(targetCtx.getRealPrecision());
        org.episteme.core.Episteme.setMathContext(targetCtx.getJavaMathContext());
        
        MathContext previous = MathContext.getCurrent();
        try {
            MathContext.setCurrent(targetCtx);
            List<LinearAlgebraProvider<?>> discovered = discoverProviders();
            List<LinearAlgebraProvider<?>> providers = new ArrayList<>();
            for (LinearAlgebraProvider<?> p : discovered) {
                boolean av = p.isAvailable();
                System.out.println("[PerfAudit] Provider " + p.getName() + " isAvailable: " + av);
                if (av) providers.add(p);
            }

            System.out.println("[PerfAudit] Total available providers: " + providers.size());
            if (providers.isEmpty()) {
                System.err.println("[PerfAudit] No available providers found in " + precisionProp + " mode. Aborting.");
                return;
            }

            LinearAlgebraProvider<?> referenceProvider = providers.stream()
                .filter(p -> p.getName().contains("Foundation") || p.getName().contains("Standard"))
                .findFirst()
                .orElse(providers.get(0));
            
            System.out.println("[PerfAudit] Using reference provider: " + referenceProvider.getName());

            System.out.println("[PerfAudit] Total available providers to benchmark: " + providers.size());
            for (LinearAlgebraProvider<?> prov : providers) {
                String safeName = prov.getName().replace(" ", "").replace("(", "").replace(")", "");
                if (Boolean.getBoolean("episteme.audit.skip." + safeName)) {
                    System.out.println("[PerfAudit] -> Skipping provider via audit skip property: " + prov.getName());
                    continue;
                }
                
                try {
                    System.out.println("[PerfAudit] >>> Starting benchmark for: " + prov.getName() + " [" + prov.getEnvironmentInfo() + "]");
                    Map<String, Object> metrics = new LinkedHashMap<>();
                    measureExecution(metrics, prov, referenceProvider, precisionProp);

                    System.out.println("[PerfAudit] Finished benchmark for " + prov.getName() + ". Total metrics collected: " + metrics.size());
                    if (metrics.isEmpty()) {
                        System.err.println("[PerfAudit] WARNING: No metrics were collected for provider " + prov.getName());
                    }
                    
                    BenchmarkResult res = new BenchmarkResult(
                        "perf-" + prov.getName().toLowerCase().replace(" ", "-").replace("(", "").replace(")", ""),
                        prov.getName(),
                        prov.getClass().getSimpleName(),
                        "Linear Algebra Performance",
                        "SUCCESS",
                        System.currentTimeMillis(),
                        0L, 1L, 0.0, 0.0, 0L,
                        new HashMap<>(),
                        metrics
                    );
                    reporter.addResult(res);
                } catch (Throwable t) {
                    System.err.println("[PerfAudit] ! Benchmark failed for " + prov.getName() + ": " + t.toString());
                    t.printStackTrace();
                }
            }
        } finally {
            MathContext.setCurrent(previous);
        }

        reporter.generateReport("performance_audit_" + precisionProp);
    }

    private void measureExecution(Map<String, Object> metrics, LinearAlgebraProvider<?> prov, LinearAlgebraProvider<?> ref, String precision) {
        double tolerance = 1.0; // High tolerance for performance measurement (we focus on timing)
        if (precision.equals("exact")) {
            @SuppressWarnings("unchecked")
            Ring<Real> rbRing = (Ring<Real>) (Object) RealBig.ZERO.getScalarRing();
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Real> castedProv = (LinearAlgebraProvider<Real>) (LinearAlgebraProvider<?>) prov;
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Real> castedRef = (LinearAlgebraProvider<Real>) (LinearAlgebraProvider<?>) ref;
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
        System.out.println("[PerfAudit] Discovering LinearAlgebraProviders via ServiceLoader...");
        List<LinearAlgebraProvider<?>> providers = new ArrayList<>();
        try {
            @SuppressWarnings("rawtypes")
            ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
            Iterator<LinearAlgebraProvider> it = loader.iterator();
            while (it.hasNext()) {
                try {
                    System.out.println("[PerfAudit] -> Scanning for next provider...");
                    LinearAlgebraProvider<?> p = it.next();
                    System.out.println("[PerfAudit] -> Discovered: " + p.getName() + " (" + p.getClass().getName() + ")");
                    providers.add(p);
                } catch (Throwable t) {
                    System.err.println("[PerfAudit] ! Failed to activate a provider: " + t.toString());
                }
            }
        } catch (Throwable t) {
            System.err.println("[PerfAudit] Error during provider discovery: " + t.getMessage());
            t.printStackTrace();
        }
        
        if (providers.isEmpty()) {
            System.err.println("[PerfAudit] CRITICAL: No LinearAlgebraProviders found! Manual fallback to known providers...");
            // Manual fallbacks if classpath issues exist during test run
            try {
               providers.add((LinearAlgebraProvider<?>) Class.forName("org.episteme.core.mathematics.linearalgebra.providers.StandardLinearAlgebraProvider").getDeclaredConstructor().newInstance());
               providers.add((LinearAlgebraProvider<?>) Class.forName("org.episteme.core.mathematics.linearalgebra.backends.EJMLBackend").getDeclaredConstructor().newInstance());
            } catch (Exception e) {
               System.err.println("[PerfAudit] Manual fallback failed: " + e.getMessage());
            }
        }
        
        System.out.println("[PerfAudit] Total providers discovered: " + providers.size());
        return providers;
    }
}
