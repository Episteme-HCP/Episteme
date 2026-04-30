/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;

import java.util.ServiceLoader;
import org.episteme.core.mathematics.numbers.real.RealProvider;

import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.algorithm.AlgorithmService;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.io.IOException;



/**
 * Systematic compliance test for High-Precision LinearAlgebraProvider implementations.
 * Tests both RealBig (arbitrary-precision) and Complex domains across all standard operations.
 * 
 * <p>Providers that only support {@code double} (EJML, Colt, Commons Math, JBlas, ND4J)
 * are excluded since they cannot operate on BigReal or Complex matrix types.</p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class HighPrecisionComplianceTest {

    private static org.springframework.context.ConfigurableApplicationContext serverContext;

    // Using shared GrpcTestApplication for server lifecycle

    @org.junit.jupiter.api.BeforeAll
    public static void startServer() {
        if (Boolean.getBoolean("episteme.test.skip-server-startup")) {
            System.out.println("Skipping Episteme Server startup (episteme.test.skip-server-startup=true)");
            return;
        }

        // Port hardening: check if 50051 is occupied
        try (java.net.ServerSocket ss = new java.net.ServerSocket(50051)) {
            // Port is free
        } catch (IOException e) {
            System.err.println("CRITICAL: Port 50051 is BUSY. Attempting to start regardless, but likely to fail if it's not a legacy Episteme instance.");
        }
        
        org.episteme.core.technical.algorithm.AlgorithmManager.setService(new org.episteme.core.technical.algorithm.StandardAlgorithmService());
        try {
            serverContext = GrpcTestApplication.start();
            System.out.println("Episteme Server started successfully for compliance tests.");
            Thread.sleep(5000); // Give it time to bind
        } catch (Exception e) {
            System.err.println("Failed to start Episteme Server: " + e.getMessage());
            if (e.getMessage().contains("Address already in use")) {
                System.err.println("DIAGNOSTIC: A server is already running on port 50051. Testing against the existing instance.");
            }
        }
    }

    @org.junit.jupiter.api.AfterAll
    public static void stopServer() {
        if (serverContext != null) {
            serverContext.close();
        }
    }

    private static final String PROJECT_NAME = System.getProperty("org.episteme.project.name", "Episteme");
    
    
    private String getReportPath() {
        String userDir = System.getProperty("user.dir");
        java.nio.file.Path projectRoot = java.nio.file.Paths.get(userDir);
        if (projectRoot.endsWith("episteme-benchmarks")) projectRoot = projectRoot.getParent();
        
        String customPath = System.getProperty("org.episteme.report.path");
        if (customPath != null && !customPath.isEmpty()) {
            java.nio.file.Path path = java.nio.file.Paths.get(customPath);
            if (!path.isAbsolute()) {
                return projectRoot.resolve(path).toString();
            }
            return path.toString();
        }
        
        return projectRoot.resolve("docs/HIGH_PRECISION_COMPLIANCE_REPORT.md").toString();
    }

    // Providers to exclude from HP tests (double-only, broken, or unused)
    private static final Set<String> EXCLUDED_PROVIDERS = Set.of(
        "EJML", "Colt", "Commons Math", "JBlas", "ND4J",
        "CUDA", "OpenCL", "SIMD", "Unified", "FFMBLAS", "Native BLAS Provider FFM",
        "Native CPU-BLAS"
    );

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }
    @Test
    @SuppressWarnings("unchecked")
    public void generateHighPrecisionReport() {

        List<LinearAlgebraProvider<?>> providers = discoverHPProviders();
        Set<LinearAlgebraProvider<?>> allDiscovered = new HashSet<>(providers);
        List<ComplianceResult> results = new ArrayList<>();
        
        org.episteme.benchmarks.reporting.BenchmarkReporter reporter = new org.episteme.benchmarks.reporting.BenchmarkReporter("High-Precision Compliance Audit");

        try {
            for (LinearAlgebraProvider<?> rawProvider : providers) {
                System.out.println("Starting HP compliance tests for: " + rawProvider.getName());
                ComplianceResult res = new ComplianceResult();
                res.providerName = rawProvider.getName();
                
                if (!rawProvider.isAvailable()) {
                    res.environment = "DISABLED";
                    results.add(res);
                    continue;
                }
                res.environment = rawProvider.getEnvironmentInfo();
                
                // Strictly isolate the provider under test, but include necessary support providers
                AlgorithmService oldService = AlgorithmManager.getService();
                
                // Find support providers
                List<AlgorithmProvider> allowed = new ArrayList<>();
                allowed.add(rawProvider);
                
    
                // 2. Leaf and Sparse fallbacks for distributed/recursive providers (and gRPC bridge)
                if (rawProvider instanceof org.episteme.core.mathematics.linearalgebra.providers.DistributedLinearAlgebraProvider ||
                    rawProvider instanceof org.episteme.core.mathematics.linearalgebra.providers.CARMALinearAlgebraProvider ||
                    rawProvider instanceof org.episteme.core.mathematics.linearalgebra.providers.StrassenLinearAlgebraProvider ||
                    rawProvider.getName().contains("gRPC") || rawProvider.getName().contains("Remote")) {
                    
                    // Allow these decorators to delegate to all other HP-capable providers
                    List<LinearAlgebraProvider<?>> hpProviders = discoverHPProviders();
                    allDiscovered.addAll(hpProviders);
                    for (LinearAlgebraProvider<?> p : hpProviders) {
                        if (p != rawProvider) {
                            allowed.add(p);
                        }
                    }
                }
    
                // 3. Always include all discovered RealProviders so they can be used for internal steps
                allowed.addAll(ServiceLoader.load(RealProvider.class).stream().map(ServiceLoader.Provider::get).toList());
    
                AlgorithmManager.setService(new org.episteme.core.technical.algorithm.TestingAlgorithmService(allowed));
                
                try {
                    org.episteme.core.mathematics.context.MathContext.withPrecision(64).compute(() -> {
                        runRealBigTests(res, (LinearAlgebraProvider<RealBig>) rawProvider);
                        return null;
                    });
    
                    // === COMPLEX DOMAIN ===
                    Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
                    if (rawProvider.isCompatible(complexRing)) {
                        runComplexTests(res, (LinearAlgebraProvider<Complex>) rawProvider);
                    }
    
                    results.add(res);
                } finally {
                    AlgorithmManager.setService(oldService);
                }
            }
        } finally {
            for (LinearAlgebraProvider<?> p : allDiscovered) {
                try {
                    p.close();
                } catch (Exception e) {
                    System.err.println("Warning: Failed to close provider " + p.getName() + ": " + e.getMessage());
                }
            }
        }


        
        for (ComplianceResult r : results) {
            Map<String, Object> metrics = new HashMap<>();
            r.status.forEach((k, v) -> metrics.put(k, v));
            
            org.episteme.benchmarks.benchmark.BenchmarkResult res = new org.episteme.benchmarks.benchmark.BenchmarkResult(
                "compliance-hp-" + r.providerName.toLowerCase().replace(" ", "-"),
                r.providerName,
                r.environment != null ? r.environment : "N/A",
                "High Precision Compliance",
                r.status.values().stream().anyMatch(v -> v.contains("FAIL")) ? "FAILURE" : "SUCCESS",
                System.currentTimeMillis(),
                0L, 1L, 0.0, 0.0, 0L,
                new HashMap<>(),
                metrics
            );
            reporter.addResult(res);
        }

        reporter.generateReport("high_precision_compliance_audit");
        printMarkdownReport(results);
    }

    private void runRealBigTests(ComplianceResult res, LinearAlgebraProvider<RealBig> provider) {
        HighPrecisionAuditOperations.runRealBigAudit(provider, 3, (op, test) -> testOp(res, op, provider, test));
    }

    private void runComplexTests(ComplianceResult res, LinearAlgebraProvider<Complex> provider) {
        HighPrecisionAuditOperations.runComplexAudit(provider, 3, (op, test) -> testOp(res, op, provider, test));
    }

    // --- Provider Discovery ---
    private List<LinearAlgebraProvider<?>> discoverHPProviders() {
        Map<String, LinearAlgebraProvider<?>> providers = new LinkedHashMap<>();
        
        boolean disableGrpc = Boolean.getBoolean("episteme.backend.disable.grpc-math");
        
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> prov : loader) {
            if (isExcludedProvider(prov.getName())) continue;
            if (disableGrpc && (prov.getName().contains("Remote") || prov.getName().contains("gRPC"))) continue;
            providers.put(prov.getName(), prov);
        }

        try {
            for (Backend b : BackendDiscovery.getInstance().getProviders()) {
                if (disableGrpc && (b.getName().contains("Remote") || b.getName().contains("gRPC"))) continue;
                try {
                    for (var ap : b.getAlgorithmProviders()) {
                        if (ap instanceof LinearAlgebraProvider<?> p) {
                            if (isExcludedProvider(p.getName())) continue;
                            if (disableGrpc && (p.getName().contains("Remote") || p.getName().contains("gRPC"))) continue;
                            providers.putIfAbsent(p.getName(), p);
                        }
                    }
                } catch (Throwable t) {
                    System.err.println("Warning: Could not load providers for backend " + b.getName() + ": " + t.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("Warning: Backend discovery failed: " + t.getMessage());
        }
        return new ArrayList<>(providers.values());
    }

    private boolean isExcludedProvider(String name) {
        for (String dop : EXCLUDED_PROVIDERS) {
            if (name.contains(dop)) return true;
        }
        return false;
    }


    private <E> void testOp(ComplianceResult res, String opName, LinearAlgebraProvider<E> provider, java.util.function.Supplier<?> test) {
        try {
            test.get();
            res.status.put(opName, "✅ PASS");
        } catch (UnsupportedOperationException | NoSuchElementException e) {
            String msg = e.getMessage();
            res.status.put(opName, "❌ N/A" + (msg != null && !msg.isBlank() ? " (" + msg + ")" : ""));
        } catch (Throwable e) {
            // Find root cause
            Throwable root = e;
            while (root.getCause() != null && root.getCause() != root) root = root.getCause();
            
            if (root instanceof NoSuchElementException) {
                res.status.put(opName, "❌ N/A");
                return;
            }

            String className = root.getClass().getSimpleName();
            String msg = root.getMessage();
            String label = className;
            if (msg != null && !msg.isBlank()) {
                String cleanMsg = msg.replace("\n", " ").replace("|", "/");
                if (cleanMsg.length() > 40) cleanMsg = cleanMsg.substring(0, 37) + "...";
                label += ": " + cleanMsg;
            }
            res.status.put(opName, "⚠️ FAIL (" + label + ")");
        }
    }

    // --- Report Generation ---
    private void printMarkdownReport(List<ComplianceResult> results) {
        if (results.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(PROJECT_NAME).append(" High-Precision Compliance Report\n\n");

        Set<String> ops = new LinkedHashSet<>();
        for (var r : results) ops.addAll(r.status.keySet());

        sb.append("| Provider | Environment |");
        for (String op : ops) sb.append(" ").append(op).append(" |");
        sb.append("\n| --- | --- |").append(" --- |".repeat(ops.size())).append("\n");

        for (var r : results) {
            sb.append("| ").append(r.providerName).append(" | ").append(r.environment).append(" |");
            for (String op : ops) {
                sb.append(" ").append(r.status.getOrDefault(op, "N/A")).append(" |");
            }
            sb.append("\n");
        }
        sb.append("\n*Generated by HighPrecisionComplianceTest on ").append(new Date()).append("*\n");

        String report = sb.toString();
        System.out.println(report);

        try {
            String finalPath = getReportPath();
            java.nio.file.Path path = java.nio.file.Paths.get(finalPath);
            if (path.getParent() != null) {
                java.nio.file.Files.createDirectories(path.getParent());
            }
            java.nio.file.Files.writeString(path, report);
            System.out.println("Compliance report generated at: " + path.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
