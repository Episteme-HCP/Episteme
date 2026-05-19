package org.episteme.benchmarks.test.audit;


import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
import org.episteme.benchmarks.reporting.BenchmarkReporter;
import org.episteme.benchmarks.audit.mathematics.linearalgebra.LinearAlgebraAuditSuite;

import org.junit.jupiter.api.Test;
import java.util.*;

/**
 * Universal Performance Audit for Linear Algebra.
 * Measures Latency and Throughput for all 68+ operations.
 */
public class LinearAlgebraPerformanceAudit {

    private static final int MATRIX_SIZE = 512;

    @Test
    public void runPerformanceAudit() {
        String precisionProp = System.getProperty("org.episteme.test.precision", "normal").toLowerCase();
        
        String modeDesc = "";
        if (precisionProp.equals("exact")) modeDesc = "Décimales illimitées";
        else if (precisionProp.equals("normal")) modeDesc = "Double (64-bit)";
        else if (precisionProp.equals("fast")) modeDesc = "Float (32-bit)";
        
        BenchmarkReporter reporter = new BenchmarkReporter("Universal Linear Algebra Performance Audit (" + precisionProp.toUpperCase() + ")");
        
        String env = System.getProperty("episteme.audit.environment");
        if (env == null || env.isEmpty()) {
            env = System.getenv("COMPUTERNAME");
            if (env == null || env.isEmpty()) {
                env = System.getenv("HOSTNAME");
            }
            if (env == null || env.isEmpty()) {
                env = System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")";
            }
        }
        
        reporter.addMetadata("Description", modeDesc);
        reporter.addMetadata("Precision", precisionProp);
        reporter.addMetadata("Environment", env);
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
                if (av) {
                    String name = p.getName().toLowerCase();
                    String className = p.getClass().getSimpleName().toLowerCase();
                    boolean isGrpcOrDistributed = name.contains("grpc") || className.contains("grpc") || name.contains("distributed") || className.contains("distributed");
                    boolean include = false;
                    
                    if (isGrpcOrDistributed) {
                        include = true;
                    } else if (precisionProp.equals("exact")) {
                        if (name.contains("episteme") || name.contains("foundation") || name.contains("core") ||
                            name.contains("dense") || name.contains("strassen") || name.contains("standard") || 
                            name.contains("karma") || name.contains("mpfr") || name.contains("sparse")) {
                            include = true;
                        }
                    } else if (precisionProp.equals("normal")) {
                        if (!name.contains("mpfr") && !name.contains("float") && !className.contains("float")) {
                            include = true;
                        }
                    } else if (precisionProp.equals("fast")) {
                        if (!name.contains("mpfr")) {
                            include = true;
                        }
                    }

                    if (include) {
                        if (MATRIX_SIZE >= 1024) {
                            if (!(name.contains("cuda") || name.contains("opencl") || name.contains("blas") || 
                                  name.contains("ejml") || name.contains("jblas") || name.contains("simd") || 
                                  name.contains("nd4j") || name.contains("grpc") || name.contains("distributed"))) {
                                include = false;
                                System.out.println("[PerfAudit] -> Skipping slow CPU provider for large matrix size: " + p.getName());
                            }
                        }
                    }

                    if (include) {
                        providers.add(p);
                    } else {
                        System.out.println("[PerfAudit] -> Skipping provider due to precision constraints or size: " + p.getName());
                    }
                }
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

        reporter.generateReport("linear_algebra_performance_" + precisionProp);
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
            int warmupIters = MATRIX_SIZE >= 1024 ? (MATRIX_SIZE >= 4096 ? 0 : 1) : 3;
            int measureIters = MATRIX_SIZE >= 1024 ? (MATRIX_SIZE >= 4096 ? 1 : 2) : 10;
            
            // Warmup
            for (int i = 0; i < warmupIters; i++) test.get();
            long start = System.nanoTime();
            for (int i = 0; i < measureIters; i++) test.get();
            long end = System.nanoTime();
            double latency = (end - start) / (1_000_000.0 * measureIters);
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
            loader.stream().forEach(provider -> {
                try {
                    System.out.println("[PerfAudit] -> Scanning for next provider...");
                    LinearAlgebraProvider<?> p = provider.get();
                    System.out.println("[PerfAudit] -> Discovered: " + p.getName() + " (" + p.getClass().getName() + ")");
                    providers.add(p);
                } catch (Throwable t) {
                    System.err.println("[PerfAudit] ! Failed to activate provider " + provider.type().getName() + ": " + t.toString());
                }
            });
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

    @Test
    public void reconstructPdfsFromJsons() throws java.io.IOException {
        System.out.println("[Reconstruct] Starting live reconstruction of PDFs from JSON results...");
        
        java.nio.file.Path rootPath = java.nio.file.Paths.get(System.getProperty("user.dir"));
        if (rootPath.endsWith("episteme-benchmarks")) {
            rootPath = rootPath.getParent();
        }
        
        java.io.File auditResultsDir = rootPath.resolve("tmp/audit_results").toFile();
        if (!auditResultsDir.exists() || !auditResultsDir.isDirectory()) {
            System.err.println("[Reconstruct] Directory tmp/audit_results does not exist locally: " + auditResultsDir.getAbsolutePath());
            return;
        }

        java.io.File[] files = auditResultsDir.listFiles((dir, name) -> 
            name.equals("performance_audit_fast.json") || name.equals("performance_audit_normal.json") || name.equals("performance_audit_exact.json")
        );
        if (files == null || files.length == 0) {
            System.err.println("[Reconstruct] No base performance_audit_*.json files found in " + auditResultsDir.getAbsolutePath());
            return;
        }

        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        List<BenchmarkResult> allResults = new ArrayList<>();
        
        for (java.io.File f : files) {
            System.out.println("[Reconstruct] -> Parsing JSON file: " + f.getName());
            
            // Read JSON file
            String content = java.nio.file.Files.readString(f.toPath());
            
            // Extract title from context
            String title = "Universal Linear Algebra Performance Audit";
            java.util.regex.Matcher titleMatcher = java.util.regex.Pattern.compile("\"title\"\\s*:\\s*\"([^\"]+)\"").matcher(content);
            if (titleMatcher.find()) {
                title = titleMatcher.group(1);
            }
            
            // Determine precision mode from title
            String mode = "NORMAL";
            if (title.toUpperCase().contains("FAST")) mode = "FAST";
            else if (title.toUpperCase().contains("EXACT")) mode = "EXACT";

            // Extract environment if present or use system fallback
            String env = "GCP"; // default fallback for target files
            if (content.contains("GCP")) env = "GCP";
            else if (content.contains("AWS")) env = "AWS";

            // Extract each run
            java.util.regex.Matcher runMatcher = java.util.regex.Pattern.compile("\\{\\s*\"name\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"provider\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"domain\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"status\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"metrics\"\\s*:\\s*\\{([^}]+)\\}").matcher(content);
            
            List<BenchmarkResult> resultsList = new ArrayList<>();
            while (runMatcher.find()) {
                String name = runMatcher.group(1);
                String provider = runMatcher.group(2);
                String domain = runMatcher.group(3);
                String status = runMatcher.group(4);
                String metricsStr = runMatcher.group(5);
                
                Map<String, Object> metricsMap = new LinkedHashMap<>();
                java.util.regex.Matcher metricMatcher = java.util.regex.Pattern.compile("\"([^\"]+)\"\\s*:\\s*(-?[0-9.]+([eE][+-]?[0-9]+)?)").matcher(metricsStr);
                while (metricMatcher.find()) {
                    String mKey = metricMatcher.group(1);
                    double mVal = Double.parseDouble(metricMatcher.group(2));
                    metricsMap.put(mKey, mVal);
                }
                
                Map<String, String> envInfo = new HashMap<>();
                envInfo.put("precision", mode);
                envInfo.put("Environment", env);
                
                BenchmarkResult res = new BenchmarkResult(
                    "perf-" + name.toLowerCase().replace(" ", "-").replace("(", "").replace(")", ""),
                    name,
                    provider,
                    domain,
                    status,
                    System.currentTimeMillis(),
                    0L, 1L, 0.0, 0.0, 0L,
                    envInfo,
                    metricsMap
                );
                resultsList.add(res);
                allResults.add(res);
            }
            
            System.out.println("[Reconstruct] -> Reconstructed " + resultsList.size() + " runs from " + f.getName());
            
            BenchmarkReporter reporter = new BenchmarkReporter(title);
            reporter.addMetadata("Precision", mode.toLowerCase());
            reporter.addMetadata("Environment", env);
            reporter.addMetadata("Matrix Size", MATRIX_SIZE + "x" + MATRIX_SIZE);
            reporter.addMetadata("Reconstructed", "True (Live from JSON)");
            
            for (BenchmarkResult r : resultsList) {
                reporter.addResult(r);
            }
            
            String jsonOutPath = new java.io.File(auditResultsDir, "performance_audit_" + mode.toLowerCase() + "_" + timestamp + ".json").getAbsolutePath();
            String pdfOutPath = new java.io.File(auditResultsDir, "performance_audit_" + mode.toLowerCase() + "_" + timestamp + ".pdf").getAbsolutePath();
            
            // Copy base JSON to timestamped JSON
            java.nio.file.Files.copy(f.toPath(), java.nio.file.Paths.get(jsonOutPath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[Reconstruct] -> Copied JSON to: " + jsonOutPath);
            
            // Generate PDF
            reporter.generatePdfReport(pdfOutPath, BenchmarkReporter.ReportType.valueOf(mode));
            System.out.println("[Reconstruct] -> Successfully rebuilt PDF: " + pdfOutPath);
        }
        
        // Let's generate a merged comparative report!
        if (allResults.size() > 0) {
            String allPdfPath = new java.io.File(auditResultsDir, "performance_audit_all_" + timestamp + ".pdf").getAbsolutePath();
            System.out.println("[Reconstruct] -> Generating consolidated comparative report: " + allPdfPath);
            
            BenchmarkReporter reporter = new BenchmarkReporter("Universal Linear Algebra Performance Audit (FAST vs NORMAL vs EXACT)");
            reporter.addMetadata("Environment", "AWS & GCP Combined");
            reporter.addMetadata("Matrix Size", MATRIX_SIZE + "x" + MATRIX_SIZE);
            reporter.addMetadata("Consolidated", "True");
            
            for (BenchmarkResult r : allResults) {
                reporter.addResult(r);
            }
            
            reporter.generatePdfReport(allPdfPath, BenchmarkReporter.ReportType.ALL);
            System.out.println("[Reconstruct] -> Consolidated PDF generated successfully: " + allPdfPath);
        }
    }
}
