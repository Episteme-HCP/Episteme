/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
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
        org.episteme.core.technical.algorithm.AlgorithmManager.setService(new org.episteme.core.technical.algorithm.StandardAlgorithmService());
        try {
            serverContext = GrpcTestApplication.start();
            System.out.println("Episteme Server started successfully for compliance tests.");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println("Failed to start Episteme Server: " + e.getMessage());
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
        String customPath = System.getProperty("org.episteme.report.path");
        if (customPath != null && !customPath.isEmpty()) return customPath;
        
        String userDir = System.getProperty("user.dir");
        java.nio.file.Path rootPath = java.nio.file.Paths.get(userDir);
        if (rootPath.endsWith("episteme-benchmarks")) rootPath = rootPath.getParent();
        return rootPath.resolve("docs/HIGH_PRECISION_COMPLIANCE_REPORT.md").toString();
    }

    // Providers to exclude from HP tests (double-only, broken, or unused)
    private static final Set<String> EXCLUDED_PROVIDERS = Set.of(
        "EJML", "Colt", "Commons Math", "JBlas", "ND4J",
        "CUDA", "OpenCL", "SIMD", "Unified", "FFMBLAS", "Native BLAS Provider FFM",
        "Native CPU-BLAS"
    );

    private static final String CPU_DENSE = "Episteme CPU (Dense)";

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }
    @Test
    @SuppressWarnings("unchecked")
    public void generateHighPrecisionReport() {

        List<LinearAlgebraProvider<?>> providers = discoverHPProviders();
        List<ComplianceResult> results = new ArrayList<>();

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
            
            // 1. Transcendental support for RealBig/Complex math - include ALL available
            for (AlgorithmProvider p : AlgorithmManager.getService().getProviders(org.episteme.core.mathematics.numbers.real.TranscendentalProvider.class)) {
                allowed.add(p);
            }
            
            // 2. Leaf and Sparse fallbacks for distributed/recursive providers (and gRPC bridge)
            if (rawProvider instanceof org.episteme.core.mathematics.linearalgebra.providers.DistributedLinearAlgebraProvider ||
                rawProvider instanceof org.episteme.core.mathematics.linearalgebra.providers.CARMALinearAlgebraProvider ||
                rawProvider instanceof org.episteme.core.mathematics.linearalgebra.providers.StrassenLinearAlgebraProvider ||
                rawProvider.getName().contains("gRPC") || rawProvider.getName().contains("Remote")) {
                
                // Always add CPU Dense and CPU Sparse as baseline fallbacks
                // AND include HP-capable providers for gRPC server-side execution in same JVM
                for (LinearAlgebraProvider<?> p : discoverHPProviders()) {
                    if (p.getName().equals(CPU_DENSE) || p.getName().equals("Episteme CPU (Sparse)") ||
                        p.getName().contains("MPFR") || p.getName().contains("Big")) {
                        allowed.add(p);
                    }
                }
            }

            AlgorithmManager.setService(new org.episteme.core.technical.algorithm.TestingAlgorithmService(allowed));
            
            try {
                org.episteme.core.mathematics.context.MathContext.exact().compute(() -> {
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

        printMarkdownReport(results);
    }

    private void runRealBigTests(ComplianceResult res, LinearAlgebraProvider<RealBig> provider) {
        HighPrecisionAuditOperations.runRealBigAudit(provider, 5, (op, test) -> testOp(res, op, provider, test));
    }

    private void runComplexTests(ComplianceResult res, LinearAlgebraProvider<Complex> provider) {
        HighPrecisionAuditOperations.runComplexAudit(provider, 5, (op, test) -> testOp(res, op, provider, test));
    }

    // --- Provider Discovery ---
    private List<LinearAlgebraProvider<?>> discoverHPProviders() {
        Map<String, LinearAlgebraProvider<?>> providers = new LinkedHashMap<>();
        
        @SuppressWarnings("rawtypes")
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> prov : loader) {
            if (isExcludedProvider(prov.getName())) continue;
            providers.put(prov.getName(), prov);
        }

        try {
            for (Backend b : BackendDiscovery.getInstance().getProviders()) {
                try {
                    for (var ap : b.getAlgorithmProviders()) {
                        if (ap instanceof LinearAlgebraProvider<?> p) {
                            if (isExcludedProvider(p.getName())) continue;
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
        } catch (UnsupportedOperationException e) {
            res.status.put(opName, "❌ N/A (" + e.getMessage() + ")");
        } catch (Throwable e) {
            // Find root cause
            Throwable root = e;
            while (root.getCause() != null && root.getCause() != root) root = root.getCause();
            
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
