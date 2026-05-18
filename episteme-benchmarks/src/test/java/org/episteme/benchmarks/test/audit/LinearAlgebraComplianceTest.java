package org.episteme.benchmarks.test.audit;

 
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
import org.episteme.benchmarks.reporting.BenchmarkReporter;
import org.episteme.benchmarks.audit.mathematics.linearalgebra.LinearAlgebraAuditSuite;
 
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
 
/**
 * Universal Linear Algebra Audit Engine.
 * Verifies 68+ operations across Square, Rectangular, and Triangular matrices.
 */
public class LinearAlgebraComplianceTest {
 
    public enum PrecisionMode { FAST, NORMAL, EXACT }
 
    private static final List<ComplianceResult> globalResults = Collections.synchronizedList(new ArrayList<>());
    private static PrecisionMode globalMode = PrecisionMode.NORMAL;
    private static boolean reportGenerated = false;
    private static final Map<String, List<Map<String, Object>>> detailedFailures = new LinkedHashMap<>();
 
    private PrecisionMode mode;
    private int matrixSize;
    private int timeoutValue = 1800;
    private String reportFileName;
 
    private enum OpStatus {
        PASS("✅ PASS"),
        FAIL("❌ FAIL"),
        DISABLED("🔘 DISABLED"),
        UNSUPPORTED("➕ N/A");
 
        private final String icon;
        OpStatus(String icon) { this.icon = icon; }
        public String toString() { return icon; }
    }
 
    private static class ComplianceResult {
        String providerName;
        String environment;
        boolean available;
        Map<String, String> status = new LinkedHashMap<>();
        Map<String, Double> latencies = new LinkedHashMap<>();
    }
 
    @Test
    public void runUniversalAudit() throws IOException {
        String precisionProp = System.getProperty("org.episteme.test.precision", "normal").toLowerCase();
        mode = switch(precisionProp) {
            case "fast" -> PrecisionMode.FAST;
            case "exact" -> PrecisionMode.EXACT;
            default -> PrecisionMode.NORMAL;
        };
 
        globalMode = mode;
        configureForMode();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!globalResults.isEmpty() && !reportGenerated) {
                System.out.println("[AuditEngine] JVM Shutdown detected. Ensuring results are persisted...");
                printMarkdownReport(new ArrayList<>(globalResults));
                writeDetailedFailures();
            }
        }));
 
        List<LinearAlgebraProvider<?>> providers = discoverAllProviders();
        String refFilter = System.getProperty("org.episteme.test.reference", "Standard");
        LinearAlgebraProvider<?> referenceProvider = providers.stream()
            .filter(p -> p.getName().contains(refFilter))
            .findFirst()
            .orElse(providers.get(0));

        System.out.println("[AuditEngine] Starting Linear Algebra Audit (Mode: " + mode + ")");
        System.out.println("[AuditEngine] Ground Truth Reference: " + referenceProvider.getName());
        
        List<ComplianceResult> results = new ArrayList<>();
 
        String filter = System.getProperty("org.episteme.test.provider.filter");
        List<String> filterList = (filter != null && !filter.isEmpty()) 
            ? Arrays.asList(filter.toLowerCase().split(",")) 
            : null;

        String exclude = System.getProperty("org.episteme.test.provider.exclude");
        List<String> excludeList = (exclude != null && !exclude.isEmpty())
            ? Arrays.asList(exclude.toLowerCase().split(","))
            : null;

        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
        try {
            for (LinearAlgebraProvider<?> prov : providers) {
                String provName = prov.getName().toLowerCase();
                if (filterList != null) {
                    boolean matches = false;
                    for (String f : filterList) {
                        if (provName.contains(f.trim())) {
                            matches = true;
                            break;
                        }
                    }
                    if (!matches) continue;
                }
                
                if (excludeList != null) {
                    boolean excluded = false;
                    for (String e : excludeList) {
                        if (provName.contains(e.trim())) {
                            excluded = true;
                            break;
                        }
                    }
                    if (excluded) continue;
                }
                
                // Dynamic filtering based on mode and capabilities
                if (!isProviderSuitableForMode(prov, mode)) continue;

                ComplianceResult res = new ComplianceResult();
                res.providerName = prov.getName();
                res.environment = prov.getEnvironmentInfo();
                res.available = prov.isAvailable();
                
                if (prov.isAvailable()) {
                    // Verify Zero Fallback Policy
                    verifyZeroFallback(prov, res);

                    java.util.concurrent.Future<?> future = executor.submit(() -> {
                        try {
                            // Explicitly set context for the audit thread as ThreadLocal/InheritableThreadLocal 
                            // can be unreliable with certain Executor implementations if the thread was reused.
                            if (mode == PrecisionMode.EXACT) {
                                org.episteme.core.mathematics.context.MathContext.setCurrent(org.episteme.core.mathematics.context.MathContext.exact());
                                org.episteme.core.mathematics.context.MathContext.getNumericalConfiguration().setMathContext(new java.math.MathContext(128));
                            } else if (mode == PrecisionMode.FAST) {
                                org.episteme.core.mathematics.context.MathContext.setCurrent(org.episteme.core.mathematics.context.MathContext.fast());
                            } else {
                                org.episteme.core.mathematics.context.MathContext.setCurrent(org.episteme.core.mathematics.context.MathContext.normal());
                            }

                            try {
                                if (mode == PrecisionMode.EXACT) {
                                    @SuppressWarnings("unchecked")
                                    LinearAlgebraProvider<RealBig> castedProv = (LinearAlgebraProvider<RealBig>) (LinearAlgebraProvider<?>) prov;
                                    @SuppressWarnings("unchecked")
                                    LinearAlgebraProvider<RealBig> castedRef = (LinearAlgebraProvider<RealBig>) (LinearAlgebraProvider<?>) referenceProvider;
                                    runExactAudit(res, castedProv, castedRef);
                                } else {
                                    @SuppressWarnings("unchecked")
                                    LinearAlgebraProvider<Real> castedProv = (LinearAlgebraProvider<Real>) (LinearAlgebraProvider<?>) prov;
                                    @SuppressWarnings("unchecked")
                                    LinearAlgebraProvider<Real> castedRef = (LinearAlgebraProvider<Real>) (LinearAlgebraProvider<?>) referenceProvider;
                                    runStandardAudit(res, castedProv, castedRef);
                                }
                            } catch (AssertionError e) {
                                System.err.println("[AuditEngine] Audit failure for " + prov.getName() + ": " + e.getMessage());
                                // We continue so the report can be generated at the end of runUniversalAudit
                            }
                        } catch (Throwable t) {
                            throw new RuntimeException(t);
                        }
                    });

                    try {
                        future.get(timeoutValue, java.util.concurrent.TimeUnit.SECONDS);
                    } catch (java.util.concurrent.TimeoutException e) {
                        future.cancel(true);
                        System.err.println("[AuditEngine] TIMEOUT (" + timeoutValue + "s) during audit of " + prov.getName());
                        res.status.put("RB:CRITICAL", "❌ TIMEOUT");
                        res.status.put("C:CRITICAL", "❌ TIMEOUT");
                    } catch (Throwable t) {
                        if (t.getCause() instanceof io.grpc.StatusRuntimeException || t instanceof io.grpc.StatusRuntimeException || 
                            (t.getMessage() != null && t.getMessage().contains("Remote server is unavailable"))) {
                            System.out.println("[AuditEngine] gRPC Backend Unreachable: " + prov.getName() + ". Marking as DISABLED.");
                            res.status.put("gRPC:READY", OpStatus.DISABLED.toString());
                            res.available = false;
                        } else {
                            System.err.println("[AuditEngine] Critical failure during audit of " + prov.getName() + ": " + t.getMessage());
                            res.status.put("CRITICAL", "❌ " + t.getClass().getSimpleName());
                        }
                    }
                } else {
                    res.status.put("AVAILABILITY", OpStatus.DISABLED.toString());
                }
                results.add(res);
                globalResults.add(res);
            }
        } finally {
            executor.shutdownNow();
        }

        BenchmarkReporter reporter = new BenchmarkReporter("Universal Linear Algebra Compliance Audit (Mode: " + mode + ")");
        reporter.addMetadata("Mode", mode.toString());
        reporter.addMetadata("Reference", referenceProvider.getName());
        reporter.addMetadata("Matrix Size", String.valueOf(matrixSize));
 
        // Disable heavy file generation (JSON/PDF) for compliance audits by default
        boolean forceReport = Boolean.getBoolean("org.episteme.test.compliance.generate-report");
        reporter.setGenerateFiles(forceReport);
 
        for (ComplianceResult r : results) {
            Map<String, Object> metrics = new HashMap<>();
            r.status.forEach((k, v) -> metrics.put(k, v));
            
            String overallStatus = r.available ? "SUCCESS" : "DISABLED";
            if (r.status.values().stream().anyMatch(v -> v.contains("FAIL"))) overallStatus = "FAILURE";
 
            double totalLatency = r.latencies.values().stream().mapToDouble(Double::doubleValue).sum();
            double avgLatency = r.latencies.isEmpty() ? 0.0 : totalLatency / r.latencies.size();
 
            BenchmarkResult res = new BenchmarkResult(
                "compliance-" + r.providerName.toLowerCase().replace(" ", "-"),
                r.providerName,
                r.environment,
                "Linear Algebra Compliance",
                overallStatus,
                System.currentTimeMillis(),
                (long)totalLatency, 1L, avgLatency, avgLatency > 0 ? 1000.0/avgLatency : 0.0, 0L,
                new HashMap<>(),
                metrics
            );
            reporter.addResult(res);
        }
 
        reporter.generateReport("linear_algebra_audit_" + mode.toString().toLowerCase());
        printMarkdownReport(results);
        writeDetailedFailures();
    }
 
    private void configureForMode() {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        switch (mode) {
            case FAST -> { matrixSize = 8; reportFileName = "LINEAR_ALGEBRA_AUDIT_FAST_" + timestamp + ".md"; org.episteme.core.mathematics.context.MathContext.setCurrent(org.episteme.core.mathematics.context.MathContext.fast()); }
            case EXACT -> { 
                matrixSize = 8; 
                reportFileName = "LINEAR_ALGEBRA_AUDIT_EXACT_" + timestamp + ".md"; 
                org.episteme.core.mathematics.context.MathContext.setCurrent(org.episteme.core.mathematics.context.MathContext.exact());
                // Force precision well beyond 64-bit double limit for accurate audit
                org.episteme.core.mathematics.context.MathContext.getNumericalConfiguration().setMathContext(new java.math.MathContext(128));
                // Increase timeout for exact mode
                timeoutValue = 900;
            }
            default -> { matrixSize = 12; reportFileName = "LINEAR_ALGEBRA_AUDIT_NORMAL_" + timestamp + ".md"; org.episteme.core.mathematics.context.MathContext.setCurrent(org.episteme.core.mathematics.context.MathContext.normal()); }
        }
    }
 
    private void runExactAudit(ComplianceResult res, LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> ref) {
        @SuppressWarnings("unchecked")
        Ring<RealBig> rbRing = (Ring<RealBig>) (Object) RealBig.ZERO.getScalarRing();
        double tolerance = 1e-100; 
        
        if (prov.isCompatible(rbRing)) {
            LinearAlgebraAuditSuite.runFullAudit(prov, ref, matrixSize, (op, test) -> auditOp(res, op, test), rbRing, "RB:", tolerance);
        }
        
        Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
        if (prov.isCompatible(complexRing)) {
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Complex> complexProv = (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider<?>) prov;
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Complex> complexRef = (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider<?>) ref;
            LinearAlgebraAuditSuite.runFullAudit(complexProv, complexRef, matrixSize, (op, test) -> auditOp(res, op, test), complexRing, "C:", tolerance);
        }
    }
 
    private void runStandardAudit(ComplianceResult res, LinearAlgebraProvider<Real> prov, LinearAlgebraProvider<Real> ref) {
        Ring<Real> realRing;
        if (mode == PrecisionMode.FAST) {
            @SuppressWarnings("unchecked")
            Ring<Real> casted = (Ring<Real>) (Object) org.episteme.core.mathematics.numbers.real.Real.zeroE().getScalarRing();
            realRing = casted;
        } else {
            realRing = org.episteme.core.mathematics.sets.Reals.getInstance();
        }
        
        double tolerance = switch(mode) {
            case FAST -> 5e-7;    // Adjusted for 32-bit float accumulation
            case NORMAL -> 5e-13; // Adjusted for double precision stability
            default -> 1e-7;
        };
        
        if (prov.isCompatible(realRing)) {
            LinearAlgebraAuditSuite.runFullAudit(prov, ref, matrixSize, (op, test) -> auditOp(res, op, test), realRing, "R:", tolerance);
        }
        
        Ring<Complex> complexRing;
        if (mode == PrecisionMode.FAST) {
            // Create a Complex ring based on RealFloat
            org.episteme.core.mathematics.numbers.real.Real zf = org.episteme.core.mathematics.numbers.real.Real.zeroE();
            @SuppressWarnings("unchecked")
            Ring<Complex> casted = (Ring<Complex>) (Object) Complex.of(zf, zf).getScalarRing();
            complexRing = casted;
        } else {
            complexRing = Complex.of(1.0, 0.0).getScalarRing();
        }
        
        if (prov.isCompatible(complexRing)) {
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Complex> complexProv = (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider<?>) prov;
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Complex> complexRef = (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider<?>) ref;
            LinearAlgebraAuditSuite.runFullAudit(complexProv, complexRef, matrixSize, (op, test) -> auditOp(res, op, test), complexRing, "C:", tolerance);
        }
    }
 
    private void auditOp(ComplianceResult res, String opName, java.util.function.Supplier<?> test) {
        long start = System.nanoTime();
        try {
            test.get();
            long end = System.nanoTime();
            double ms = (end - start) / 1_000_000.0;
            res.status.put(opName, OpStatus.PASS.toString());
            res.latencies.put(opName, ms);
        } catch (UnsupportedOperationException e) {
            res.status.put(opName, OpStatus.UNSUPPORTED.toString());
            res.latencies.put(opName, 0.0);
        } catch (Throwable e) {
            if (e.getCause() instanceof io.grpc.StatusRuntimeException || e instanceof io.grpc.StatusRuntimeException ||
                (e.getMessage() != null && e.getMessage().contains("Remote server is unavailable"))) {
                res.status.put(opName, OpStatus.DISABLED.toString());
                res.latencies.put(opName, 0.0);
            } else {
                res.status.put(opName, "❌ " + e.getClass().getSimpleName());
                res.latencies.put(opName, -1.0);
            }
            
            // Capture detailed failure info
            Map<String, Object> failureInfo = new HashMap<>();
            failureInfo.put("op", opName);
            failureInfo.put("error", e.getClass().getSimpleName());
            failureInfo.put("message", e.getMessage());
            
            detailedFailures.computeIfAbsent(res.providerName, k -> new ArrayList<>()).add(failureInfo);
 
            if (res.providerName.toLowerCase().contains("native")) {
                System.err.println("[AuditEngine] Failure in " + res.providerName + " for " + opName + ": " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }
 
    private void writeDetailedFailures() {
        try {
            Path path = Paths.get("target/audit_diagnostics.json");
            if (!java.nio.file.Files.exists(path.getParent())) java.nio.file.Files.createDirectories(path.getParent());
            
            StringBuilder sb = new StringBuilder("{\n");
            detailedFailures.forEach((provider, failures) -> {
                sb.append("  \"").append(provider).append("\": [\n");
                for (int i = 0; i < failures.size(); i++) {
                    Map<String, Object> f = failures.get(i);
                    String msg = f.get("message") != null ? f.get("message").toString().replace("\"", "\\\"").replace("\n", " ") : "null";
                    sb.append("    { \"op\": \"").append(f.get("op")).append("\", \"error\": \"").append(f.get("error"))
                      .append("\", \"message\": \"").append(msg).append("\" }");
                    if (i < failures.size() - 1) sb.append(",");
                    sb.append("\n");
                }
                sb.append("  ],\n");
            });
            if (sb.length() > 2) sb.setLength(sb.length() - 2); // Remove last comma
            sb.append("\n}");
            java.nio.file.Files.writeString(path, sb.toString());
            System.out.println("[AuditEngine] Detailed failure diagnostics written to: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[AuditEngine] Failed to write detailed diagnostics: " + e.getMessage());
        }
    }
 
    private List<LinearAlgebraProvider<?>> discoverAllProviders() {
        Map<String, LinearAlgebraProvider<?>> providers = new TreeMap<>();
        String excludeFilter = System.getProperty("org.episteme.audit.exclude", "");
        String[] excludes = excludeFilter.isEmpty() ? new String[0] : excludeFilter.split(",");
        boolean skipBackendDiscovery = Boolean.getBoolean("org.episteme.audit.skipDiscovery");
        
        // 1. ServiceLoader discovery (Standard SPI)
        try {
            @SuppressWarnings("rawtypes")
            ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
            loader.stream().forEach(provider -> {
                String className = provider.type().getName();
                boolean excluded = false;
                for (String ex : excludes) {
                    if (className.toLowerCase().contains(ex.trim().toLowerCase())) {
                        excluded = true;
                        break;
                    }
                }
                
                if (excluded) {
                    System.out.println("[AuditEngine] ServiceLoader: Skipping excluded class: " + className);
                    return;
                }

                try {
                    System.out.println("[AuditEngine] ServiceLoader: Attempting to instantiate " + className + "...");
                    LinearAlgebraProvider<?> p = provider.get();
                    System.out.println("[AuditEngine] ServiceLoader: Found " + p.getName());
                    
                    // Also check name for exclusion
                    boolean nameExcluded = false;
                    for (String ex : excludes) {
                        if (p.getName().toLowerCase().contains(ex.trim().toLowerCase())) {
                            nameExcluded = true;
                            break;
                        }
                    }

                    if (nameExcluded) {
                        System.out.println("[AuditEngine] ServiceLoader: Skipping excluded provider (by name): " + p.getName());
                    } else {
                        providers.put(p.getName(), p);
                    }
                } catch (Throwable t) {
                    System.err.println("[AuditEngine] ServiceLoader: Skipping broken provider [" + className + "]: " + t.getMessage());
                }
            });
        } catch (Throwable t) {
            System.err.println("[AuditEngine] ServiceLoader failed: " + t.getMessage());
        }
        
        // 2. Backend discovery (Including native/GPU backends even if hardware is missing)
        if (!skipBackendDiscovery) {
            try {
                System.out.println("[AuditEngine] BackendDiscovery: Starting...");
                for (Backend b : BackendDiscovery.getInstance().getProviders()) {
                    System.out.println("[AuditEngine] BackendDiscovery: Checking backend " + b.getName() + " (" + b.getClass().getSimpleName() + ")");
                    try {
                        for (var ap : b.getAlgorithmProviders()) {
                            if (ap instanceof LinearAlgebraProvider<?> p) {
                                System.out.println("[AuditEngine] BackendDiscovery: Found provider " + p.getName() + " from backend " + b.getName());
                                
                                boolean excluded = false;
                                for (String ex : excludes) {
                                    if (p.getName().toLowerCase().contains(ex.trim().toLowerCase())) {
                                        excluded = true;
                                        break;
                                    }
                                }
                                if (excluded) continue;
                                
                                providers.put(p.getName(), p);
                            }
                        }
                    } catch (Throwable t) {
                        System.err.println("[AuditEngine] Could not retrieve providers from backend: " + b.getName());
                    }
                }
            } catch (Throwable t) {}
        } else {
            System.out.println("[AuditEngine] BackendDiscovery: Skipped by configuration.");
        }
        
        return new ArrayList<>(providers.values());
    }
 
    private void printMarkdownReport(List<ComplianceResult> results) {
        if (results.isEmpty()) return;
        PrecisionMode activeMode = (mode != null) ? mode : globalMode;
        StringBuilder sb = new StringBuilder();
        sb.append("# Episteme Linear Algebra Audit Report (").append(activeMode).append(")\n\n");
        sb.append("This report summarizes the compliance and feature support for all discovered Linear Algebra backends. ");
        sb.append("Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).\n\n");
        
        // --- Summary Table (Global Checklist) ---
        sb.append("## Global Status Summary\n\n");
        sb.append("| Provider | Environment | Status | Real Domain | Complex Domain |\n");
        sb.append("| :--- | :--- | :--- | :---: | :---: |\n");
        for (var r : results) {
            long passReal = r.status.entrySet().stream().filter(e -> (e.getKey().startsWith("R:") || e.getKey().startsWith("RB:")) && e.getValue().contains("PASS")).count();
            long totalReal = r.status.keySet().stream().filter(k -> k.startsWith("R:") || k.startsWith("RB:")).count();
            
            long passComplex = r.status.entrySet().stream().filter(e -> e.getKey().startsWith("C:") && e.getValue().contains("PASS")).count();
            long totalComplex = r.status.keySet().stream().filter(k -> k.startsWith("C:")).count();
            
            String statusReal = totalReal > 0 ? (passReal + "/" + totalReal) : "N/A";
            String statusComplex = totalComplex > 0 ? (passComplex + "/" + totalComplex) : "N/A";
            
            String overallStatus;
            if (!r.available) overallStatus = "🔘 Disabled";
            else if (totalReal == 0 && totalComplex == 0) overallStatus = "➕ N/A";
            else if (passReal == totalReal && passComplex == totalComplex) overallStatus = "✅ Ready";
            else if (passReal > 0 || passComplex > 0) overallStatus = "⚠️ Partial";
            else overallStatus = "❌ Fail";
            
            sb.append("| ").append(r.providerName).append(" | ").append(r.environment).append(" | ")
              .append(overallStatus).append(" | ").append(statusReal).append(" | ").append(statusComplex).append(" |\n");
        }
        sb.append("\n");
 
        // --- Detailed Category Tables ---
        List<String> categories = Arrays.asList("Fallback", "Arithmetic", "Solvers", "Decompositions", "Rect:", "Tri:", "Vec:", "Func:", "Sparse:");
        Map<String, Set<String>> catToOps = new LinkedHashMap<>();
        for (String cat : categories) catToOps.put(cat, new TreeSet<>());
 
        Set<String> allOps = new TreeSet<>();
        for (var r : results) allOps.addAll(r.status.keySet());
 
        for (String op : allOps) {
            String cat = "Other";
            for (String c : categories) {
                if (op.contains(c)) { cat = c; break; }
            }
            if (cat.equals("Other")) {
                if (op.contains("Add") || op.contains("Sub") || op.contains("Mul") || op.contains("Scale") || op.contains("Trans")) cat = "Arithmetic";
                else if (op.contains("Inv") || op.contains("Det") || op.contains("Solve") || op.contains("Trace")) cat = "Solvers";
                else if (op.contains("LU") || op.contains("QR") || op.contains("SVD") || op.contains("Chol") || op.contains("Eigen")) cat = "Decompositions";
            }
            if (catToOps.containsKey(cat)) catToOps.get(cat).add(op.substring(op.indexOf(":") + 1));
            else {
                // Fallback for uncategorized ops
                catToOps.computeIfAbsent("Other", k -> new TreeSet<>()).add(op);
            }
        }
 
        for (var entry : catToOps.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            
            String catName = entry.getKey().replace(":", "");
            sb.append("### Category: ").append(catName).append("\n\n");
            
            List<String> baseOps = new ArrayList<>(entry.getValue());
            String realPrefix = (mode == PrecisionMode.EXACT) ? "RB:" : "R:";
            if (catName.equals("Fallback")) realPrefix = "Fallback:";
            
            // Build header: Reals then Complexes
            sb.append("| Provider |");
            for (String op : baseOps) sb.append(" ").append(realPrefix).append(op).append(" |");
            if (!catName.equals("Fallback")) {
                for (String op : baseOps) sb.append(" C:").append(op).append(" |");
            }
            sb.append("\n| :--- |").append(" :---: |".repeat(baseOps.size() * (catName.equals("Fallback") ? 1 : 2))).append("\n");
 
            for (var r : results) {
                sb.append("| ").append(r.providerName).append(" |");
                // Real domain (or Fallback domain)
                for (String op : baseOps) {
                    String status = r.status.getOrDefault(realPrefix + op, r.available ? OpStatus.UNSUPPORTED.toString() : OpStatus.DISABLED.toString());
                    sb.append(" ").append(status).append(" |");
                }
                // Complex domain (skip for Fallback)
                if (!catName.equals("Fallback")) {
                    for (String op : baseOps) {
                        String status = r.status.getOrDefault("C:" + op, r.available ? OpStatus.UNSUPPORTED.toString() : OpStatus.DISABLED.toString());
                        sb.append(" ").append(status).append(" |");
                    }
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
 
        sb.append("---\n*Generated by Universal Audit Engine on ").append(new Date()).append("*\n");
        
        try {
            String customPath = System.getProperty("org.episteme.report.path");
            Path docsPath;
            if (customPath != null && !customPath.isEmpty()) {
                docsPath = Paths.get(customPath);
            } else {
                docsPath = Paths.get(System.getProperty("user.dir")).resolve("docs").resolve(reportFileName);
                if (!docsPath.getParent().toFile().exists()) {
                    // Fallback to parent directory docs if sub-module docs doesn't exist
                    Path parentDocs = Paths.get(System.getProperty("user.dir")).getParent().resolve("docs");
                    if (parentDocs.toFile().exists()) {
                        docsPath = parentDocs.resolve(reportFileName);
                    }
                }
            }
            
            if (docsPath.getParent() != null && !docsPath.getParent().toFile().exists()) {
                java.nio.file.Files.createDirectories(docsPath.getParent());
            }
            
            java.nio.file.Files.writeString(docsPath, sb.toString());
            System.out.println("[AuditEngine] Report generated at: " + docsPath.toAbsolutePath());
            
            // Also write to reports/ directory
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            if (projectRoot.getFileName().toString().equals("episteme-benchmarks")) {
                projectRoot = projectRoot.getParent();
            }
            Path reportsPath = projectRoot.resolve("reports").resolve("la_compliance_report_" + activeMode.toString().toLowerCase() + ".md");
            if (!reportsPath.getParent().toFile().exists()) java.nio.file.Files.createDirectories(reportsPath.getParent());
            
            java.nio.file.Files.writeString(reportsPath, sb.toString());
            System.out.println("[AuditEngine] Report mirrored at: " + reportsPath.toAbsolutePath());
            reportGenerated = true;
        } catch (IOException e) { 
            System.err.println("[AuditEngine] Failed to write report: " + e.getMessage());
        }
    }
    private boolean isProviderSuitableForMode(LinearAlgebraProvider<?> prov, PrecisionMode mode) {
        if (!prov.isAvailable()) return false;
        
        // Use MathContext to check if provider scores high for the current mode
        org.episteme.core.mathematics.context.MathContext original = org.episteme.core.mathematics.context.MathContext.getCurrent();
        try {
            switch (mode) {
                case EXACT -> {
                    org.episteme.core.mathematics.context.MathContext.setCurrent(org.episteme.core.mathematics.context.MathContext.exact());
                    double score = prov.score(org.episteme.core.technical.algorithm.OperationContext.DEFAULT);
                    // High precision providers should have score >= 1000 in exact mode
                    return score >= 1000.0;
                }
                case NORMAL -> {
                    org.episteme.core.mathematics.context.MathContext.setCurrent(org.episteme.core.mathematics.context.MathContext.normal());
                    // Any provider supporting double or Real is suitable for NORMAL
                    // But we exclude MPFR from NORMAL to keep it clean if desired, or keep it.
                    // For now, allow everything that is available.
                    return true;
                }
                case FAST -> {
                    // Exclude high-precision-only providers from FAST mode to avoid noise
                    double score = prov.score(org.episteme.core.technical.algorithm.OperationContext.DEFAULT);
                    return score < 1000.0; 
                }
            }
        } finally {
            org.episteme.core.mathematics.context.MathContext.setCurrent(original);
        }
        return false;
    }

    private void verifyZeroFallback(LinearAlgebraProvider<?> prov, ComplianceResult res) {
        // Critical methods that MUST be overridden to avoid Generic/Default fallbacks
        String[] criticalMethods = {
            "lu", "qr", "cholesky", "eigen", "svd", 
            "solve", "inverse", "determinant", "solveTriangular"
        };
        
        Class<?> clazz = prov.getClass();
        for (String methodName : criticalMethods) {
            boolean overridden = false;
            // Check for both E and generic Matrix/Vector types
            for (java.lang.reflect.Method m : clazz.getMethods()) {
                if (m.getName().equals(methodName) && m.getDeclaringClass() != LinearAlgebraProvider.class) {
                    overridden = true;
                    break;
                }
            }
            if (!overridden) {
                res.status.put("Fallback:" + methodName, "⚠️ DEFAULT");
            } else {
                res.status.put("Fallback:" + methodName, "✅ NATIVE");
            }
        }
    }
}
