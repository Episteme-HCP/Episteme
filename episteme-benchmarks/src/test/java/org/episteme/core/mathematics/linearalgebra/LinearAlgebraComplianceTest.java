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
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Universal Linear Algebra Audit Engine.
 */
public class LinearAlgebraComplianceTest {

    public enum PrecisionMode { FAST, NORMAL, EXACT }

    private static final String PROJECT_NAME = System.getProperty("org.episteme.project.name", "Episteme");
    
    private PrecisionMode mode;
    private int matrixSize;
    private String reportPath;

    private static class ComplianceResult {
        String providerName;
        String environment;
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
        System.out.println("[AuditEngine] Starting Universal Audit in mode: " + mode);

        List<LinearAlgebraProvider<?>> providers = discoverAuditProviders();
        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<?> prov : providers) {
            if (!prov.isAvailable()) continue;
            
            ComplianceResult res = new ComplianceResult();
            res.providerName = prov.getName();
            res.environment = prov.getEnvironmentInfo();
            
            AlgorithmService oldService = AlgorithmManager.getService();
            AlgorithmManager.setService(new TestingAlgorithmService(prov));
            
            try {
                if (mode == PrecisionMode.EXACT) {
                    runExactAudit(res, (LinearAlgebraProvider<RealBig>) prov);
                } else {
                    runStandardAudit(res, (LinearAlgebraProvider<Real>) prov);
                }
                results.add(res);
            } catch (Throwable t) {
                System.err.println("Audit failed for " + prov.getName() + ": " + t.getMessage());
            } finally {
                AlgorithmManager.setService(oldService);
            }
        }

        printMarkdownReport(results);
    }

    private void configureForMode() {
        switch (mode) {
            case FAST -> { matrixSize = 8; reportPath = "../docs/FLOAT_ALGEBRA_COMPLIANCE_REPORT.md"; }
            case EXACT -> { matrixSize = 4; reportPath = "../docs/HIGH_PRECISION_COMPLIANCE_REPORT.md"; }
            default -> { matrixSize = 12; reportPath = "../docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md"; }
        }
    }

    private void runExactAudit(ComplianceResult res, LinearAlgebraProvider<RealBig> prov) {
        Ring<RealBig> rbRing = (Ring<RealBig>) (Object) RealBig.ZERO.getScalarRing();
        LinearAlgebraAuditSuite.runAudit(prov, matrixSize, (op, test) -> auditOp(res, op, test), rbRing, "RB:");
        
        Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
        if (prov.isCompatible(complexRing)) {
            LinearAlgebraAuditSuite.runAudit((LinearAlgebraProvider<Complex>) prov, matrixSize, (op, test) -> auditOp(res, op, test), complexRing, "C:");
        }
    }

    private void runStandardAudit(ComplianceResult res, LinearAlgebraProvider<Real> prov) {
        Ring<Real> realRing = org.episteme.core.mathematics.sets.Reals.getInstance();
        LinearAlgebraAuditSuite.runAudit(prov, matrixSize, (op, test) -> auditOp(res, op, test), realRing, "R:");
        
        Ring<Complex> complexRing = Complex.of(1.0, 0.0).getScalarRing();
        if (prov.isCompatible(complexRing)) {
            LinearAlgebraAuditSuite.runAudit((LinearAlgebraProvider<Complex>) prov, matrixSize, (op, test) -> auditOp(res, op, test), complexRing, "C:");
        }
    }

    private void auditOp(ComplianceResult res, String opName, Supplier<?> test) {
        try {
            test.get();
            res.status.put(opName, "✅ PASS");
        } catch (Throwable e) {
            res.status.put(opName, "⚠️ FAIL (" + e.getClass().getSimpleName() + ")");
        }
    }

    private List<LinearAlgebraProvider<?>> discoverAuditProviders() {
        Set<LinearAlgebraProvider<?>> providers = new LinkedHashSet<>();
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        for (LinearAlgebraProvider<?> p : loader) providers.add(p);
        try {
            for (Backend b : BackendDiscovery.getInstance().getProviders()) {
                for (var ap : b.getAlgorithmProviders()) {
                    if (ap instanceof LinearAlgebraProvider<?> p) providers.add(p);
                }
            }
        } catch (Throwable t) {}
        return new ArrayList<>(providers);
    }

    private void printMarkdownReport(List<ComplianceResult> results) {
        if (results.isEmpty()) return;
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(PROJECT_NAME).append(" Linear Algebra Audit Report (").append(mode).append(")\n\n");
        Set<String> ops = new TreeSet<>();
        for (var r : results) ops.addAll(r.status.keySet());
        
        sb.append("| Provider | Environment |");
        for (String op : ops) sb.append(" ").append(op).append(" |");
        sb.append("\n| --- | --- |").append(" --- |".repeat(ops.size())).append("\n");

        for (var r : results) {
            sb.append("| ").append(r.providerName).append(" | ").append(r.environment).append(" |");
            for (String op : ops) sb.append(" ").append(r.status.getOrDefault(op, "N/A")).append(" |");
            sb.append("\n");
        }
        sb.append("\n*Generated by LinearAlgebraComplianceTest on ").append(new java.util.Date()).append("*\n");
        try {
            java.nio.file.Files.writeString(java.nio.file.Paths.get(reportPath), sb.toString());
        } catch (IOException e) { e.printStackTrace(); }
    }
}
