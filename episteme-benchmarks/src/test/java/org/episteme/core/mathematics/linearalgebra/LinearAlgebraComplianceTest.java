package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.AlgorithmService;
import org.episteme.core.technical.algorithm.TestingAlgorithmService;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Universal Linear Algebra Audit Engine.
 * Verifies 68+ operations across Square, Rectangular, and Triangular matrices.
 */
public class LinearAlgebraComplianceTest {

    public enum PrecisionMode { FAST, NORMAL, EXACT }

    private PrecisionMode mode;
    private int matrixSize;
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
    }

    @Test
    public void runUniversalAudit() throws IOException {
        String precisionProp = System.getProperty("org.episteme.test.precision", "normal").toLowerCase();
        mode = switch(precisionProp) {
            case "fast" -> PrecisionMode.FAST;
            case "exact" -> PrecisionMode.EXACT;
            default -> PrecisionMode.NORMAL;
        };

        configureForMode();
        List<LinearAlgebraProvider<?>> providers = discoverAllProviders();
        LinearAlgebraProvider<?> referenceProvider = providers.stream()
            .filter(p -> p.getName().contains("Standard"))
            .findFirst()
            .orElse(providers.get(0));

        System.out.println("[AuditEngine] Starting Linear Algebra Audit (Mode: " + mode + ")");
        System.out.println("[AuditEngine] Ground Truth Reference: " + referenceProvider.getName());
        
        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<?> prov : providers) {
            ComplianceResult res = new ComplianceResult();
            res.providerName = prov.getName();
            res.environment = prov.getEnvironmentInfo();
            res.available = prov.isAvailable();
            
            if (prov.isAvailable()) {
                try {
                    if (mode == PrecisionMode.EXACT) {
                        runExactAudit(res, (LinearAlgebraProvider<RealBig>) (LinearAlgebraProvider) prov, (LinearAlgebraProvider<RealBig>) (LinearAlgebraProvider) referenceProvider);
                    } else {
                        runStandardAudit(res, (LinearAlgebraProvider<Real>) (LinearAlgebraProvider) prov, (LinearAlgebraProvider<Real>) (LinearAlgebraProvider) referenceProvider);
                    }
                } catch (Throwable t) {
                    System.err.println("Audit failed for " + prov.getName() + ": " + t.getMessage());
                }
            }
            results.add(res);
        }

        printMarkdownReport(results);
    }

    private void configureForMode() {
        switch (mode) {
            case FAST -> { matrixSize = 8; reportFileName = "UNIVERSAL_AUDIT_FAST.md"; }
            case EXACT -> { matrixSize = 6; reportFileName = "UNIVERSAL_AUDIT_EXACT.md"; }
            default -> { matrixSize = 12; reportFileName = "UNIVERSAL_AUDIT_NORMAL.md"; }
        }
    }

    private void runExactAudit(ComplianceResult res, LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> ref) {
        Ring<RealBig> rbRing = (Ring<RealBig>) (Object) RealBig.ZERO.getScalarRing();
        double tolerance = 1e-30; // High precision tolerance
        LinearAlgebraAuditSuite.runFullAudit(prov, ref, matrixSize, (op, test) -> auditOp(res, op, test), rbRing, "RB:", tolerance);
        
        Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
        if (prov.isCompatible(complexRing)) {
            LinearAlgebraAuditSuite.runFullAudit((LinearAlgebraProvider<Complex>) (LinearAlgebraProvider) prov, (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider) ref, matrixSize, (op, test) -> auditOp(res, op, test), complexRing, "C:", tolerance);
        }
    }

    private void runStandardAudit(ComplianceResult res, LinearAlgebraProvider<Real> prov, LinearAlgebraProvider<Real> ref) {
        Ring<Real> realRing = org.episteme.core.mathematics.sets.Reals.getInstance();
        double tolerance = (mode == PrecisionMode.FAST) ? 1e-7 : 1e-14;
        if (System.getProperty("org.episteme.test.tolerance.strict") != null) {
            tolerance = Double.parseDouble(System.getProperty("org.episteme.test.tolerance.strict"));
        }
        LinearAlgebraAuditSuite.runFullAudit(prov, ref, matrixSize, (op, test) -> auditOp(res, op, test), realRing, "R:", tolerance);
        
        Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
        if (prov.isCompatible(complexRing)) {
            LinearAlgebraAuditSuite.runFullAudit((LinearAlgebraProvider<Complex>) (LinearAlgebraProvider) prov, (LinearAlgebraProvider<Complex>) (LinearAlgebraProvider) ref, matrixSize, (op, test) -> auditOp(res, op, test), complexRing, "C:", tolerance);
        }
    }

    private void auditOp(ComplianceResult res, String opName, java.util.function.Supplier<?> test) {
        try {
            test.get();
            res.status.put(opName, OpStatus.PASS.toString());
        } catch (UnsupportedOperationException e) {
            res.status.put(opName, OpStatus.UNSUPPORTED.toString());
        } catch (Throwable e) {
            res.status.put(opName, "❌ " + e.getClass().getSimpleName());
        }
    }

    private List<LinearAlgebraProvider<?>> discoverAllProviders() {
        Map<String, LinearAlgebraProvider<?>> providers = new TreeMap<>();
        
        // 1. ServiceLoader discovery (Standard SPI)
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> p : loader) providers.put(p.getName(), p);
        
        // 2. Backend discovery (Including native/GPU backends even if hardware is missing)
        try {
            for (Backend b : BackendDiscovery.getInstance().getProviders()) {
                try {
                    for (var ap : b.getAlgorithmProviders()) {
                        if (ap instanceof LinearAlgebraProvider<?> p) {
                            providers.put(p.getName(), p);
                        }
                    }
                } catch (Throwable t) {
                    System.err.println("[AuditEngine] Could not retrieve providers from backend: " + b.getName());
                }
            }
        } catch (Throwable t) {}
        
        return new ArrayList<>(providers.values());
    }

    private void printMarkdownReport(List<ComplianceResult> results) {
        if (results.isEmpty()) return;
        StringBuilder sb = new StringBuilder();
        sb.append("# Episteme Universal Linear Algebra Audit Report (").append(mode).append(")\n\n");
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
            
            String overallStatus = r.available ? (passReal > 0 ? "⚠️ Partial" : "❌ Fail") : "🔘 Disabled";
            if (r.available && passReal == totalReal && passComplex == totalComplex && totalReal > 0) overallStatus = "✅ Ready";
            
            sb.append("| ").append(r.providerName).append(" | ").append(r.environment).append(" | ")
              .append(overallStatus).append(" | ").append(statusReal).append(" | ").append(statusComplex).append(" |\n");
        }
        sb.append("\n");

        // --- Detailed Category Tables ---
        List<String> categories = Arrays.asList("Arithmetic", "Solvers", "Decompositions", "Rect:", "Tri:", "Vec:", "Func:", "Sparse:");
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
            }
            if (catToOps.containsKey(cat)) catToOps.get(cat).add(op.substring(op.indexOf(":") + 1));
        }

        for (var entry : catToOps.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            
            String catName = entry.getKey().replace(":", "");
            sb.append("### Category: ").append(catName).append("\n\n");
            
            List<String> baseOps = new ArrayList<>(entry.getValue());
            String realPrefix = (mode == PrecisionMode.EXACT) ? "RB:" : "R:";
            
            // Build header: Reals then Complexes
            sb.append("| Provider |");
            for (String op : baseOps) sb.append(" ").append(realPrefix).append(op).append(" |");
            for (String op : baseOps) sb.append(" C:").append(op).append(" |");
            sb.append("\n| :--- |").append(" :---: |".repeat(baseOps.size() * 2)).append("\n");

            for (var r : results) {
                sb.append("| ").append(r.providerName).append(" |");
                // Real domain
                for (String op : baseOps) {
                    String status = r.status.getOrDefault(realPrefix + op, r.available ? OpStatus.UNSUPPORTED.toString() : OpStatus.DISABLED.toString());
                    sb.append(" ").append(status).append(" |");
                }
                // Complex domain
                for (String op : baseOps) {
                    String status = r.status.getOrDefault("C:" + op, r.available ? OpStatus.UNSUPPORTED.toString() : OpStatus.DISABLED.toString());
                    sb.append(" ").append(status).append(" |");
                }
                sb.append("\n");
            }
            sb.append("\n");
        }

        sb.append("---\n*Generated by Universal Audit Engine on ").append(new Date()).append("*\n");
        
        try {
            Path docsPath = Paths.get(System.getProperty("user.dir")).resolve("docs").resolve(reportFileName);
            if (!docsPath.getParent().toFile().exists()) {
                docsPath = Paths.get(System.getProperty("user.dir")).getParent().resolve("docs").resolve(reportFileName);
            }
            java.nio.file.Files.writeString(docsPath, sb.toString());
            System.out.println("[AuditEngine] Report generated at: " + docsPath.toAbsolutePath());
        } catch (IOException e) { 
            System.err.println("[AuditEngine] Failed to write report: " + e.getMessage());
        }
    }
}
