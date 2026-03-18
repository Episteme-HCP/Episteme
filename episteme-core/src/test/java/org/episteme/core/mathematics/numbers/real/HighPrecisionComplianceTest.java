package org.episteme.core.mathematics.numbers.real;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.sets.Reals;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive compliance tests for high-precision arithmetic and linear algebra.
 * Verifies RealBig support across various operations and providers.
 */
public class HighPrecisionComplianceTest {

    private static final String PROJECT_NAME = System.getProperty("org.episteme.project.name", "Episteme");
    private static final String REPORT_PATH = System.getProperty("org.episteme.report.path", "../docs/HIGH_PRECISION_COMPLIANCE_REPORT.md");

    private static class ComplianceResult {
        String component;
        String testName;
        String status;
        String details;
    }

    @Test
    public void generateFullComplianceReport() {
        List<ComplianceResult> results = new ArrayList<>();

        // 1. Basic Arithmetic Tests (RealBig Core)
        results.add(testOperation("Core", "Addition (1.1 + 2.2)", () -> {
            MathContext.exact().compute(() -> {
                Real a = Real.of("1.1");
                Real b = Real.of("2.2");
                assertEquals("3.3", a.add(b).toString());
                return null;
            });
            return "Pass";
        }));

        results.add(testOperation("Core", "40-Digit Division (1/7)", () -> {
            return MathContext.exact().withJavaMathContext(new java.math.MathContext(40)).compute(() -> {
                Real result = Real.of(1).divide(Real.of(7));
                String s = result.toString();
                String prefix = "0.1428571428571428571428571428571428571428";
                assertTrue(s.startsWith(prefix.substring(0, 39)), "Precision loss! Got: " + s);
                return "Digits: " + s;
            });
        }));

        results.add(testOperation("Core", "Sqrt 50-Digit (sqrt 2)", () -> {
            return MathContext.exact().withJavaMathContext(new java.math.MathContext(50)).compute(() -> {
                Real sqrt2 = Real.of(2).sqrt();
                Real squared = sqrt2.multiply(sqrt2);
                BigDecimal diff = squared.bigDecimalValue().subtract(new BigDecimal("2")).abs();
                assertTrue(diff.compareTo(new BigDecimal("1e-48")) < 0, "Precision error: " + diff);
                return "Diff: " + diff.toPlainString();
            });
        }));

        // 2. Discover Providers and test Vector/Matrix ops
        Ring<Real> ring = Reals.getInstance();
        ServiceLoader<LinearAlgebraProvider> loader = ServiceLoader.load(LinearAlgebraProvider.class);
        
        for (LinearAlgebraProvider p : loader) {
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Real> provider = (LinearAlgebraProvider<Real>) p;
            if (!provider.isCompatible(ring)) continue;
            
            String pName = provider.getName();
            
            // Vector Dot Product Test
            results.add(testOperation(pName, "Vector Dot Product (High-Prec)", () -> {
                return MathContext.exact().withJavaMathContext(new java.math.MathContext(40)).compute(() -> {
                    DenseVectorStorage<Real> s1 = new DenseVectorStorage<>(2);
                    s1.set(0, Real.of("0.3333333333333333333333333333333333333333"));
                    s1.set(1, Real.of("3"));
                    Vector<Real> v1 = new GenericVector<>(s1, provider, ring);
                    
                    DenseVectorStorage<Real> s2 = new DenseVectorStorage<>(2);
                    s2.set(0, Real.of("3"));
                    s2.set(1, Real.of("0.3333333333333333333333333333333333333333"));
                    Vector<Real> v2 = new GenericVector<>(s2, provider, ring);
                    
                    Real dot = v1.dot(v2);
                    // 0.333... * 3 = 0.999... which should be basically 1 in high prec
                    Real sum = Real.of("0.9999999999999999999999999999999999999999").multiply(Real.of(2));
                    assertTrue(dot.compareTo(sum) >= 0, "Lower than expected: " + dot);
                    return "Dot: " + dot;
                });
            }));

            // Matrix Multiplication Test
            results.add(testOperation(pName, "Matrix Multiplication (2x2 High-Prec)", () -> {
                return MathContext.exact().withJavaMathContext(new java.math.MathContext(40)).compute(() -> {
                    DenseMatrixStorage<Real> ms1 = new DenseMatrixStorage<>(2, 2, ring.zero());
                    ms1.set(0, 0, Real.of("1.0000000000000000000000000000000000000001"));
                    ms1.set(1, 1, Real.of(1));
                    Matrix<Real> m1 = new GenericMatrix<>(ms1, provider, ring);
                    
                    Matrix<Real> result = m1.multiply(m1);
                    Real topVal = result.get(0, 0);
                    assertTrue(topVal.toString().length() > 30, "Lost precision in matrix multiply: " + topVal);
                    return "M*M[0,0]: " + topVal;
                });
            }));
        }

        printMarkdownReport(results);
    }

    private ComplianceResult testOperation(String component, String name, java.util.function.Supplier<String> test) {
        ComplianceResult res = new ComplianceResult();
        res.component = component;
        res.testName = name;
        try {
            res.details = test.get();
            res.status = "✅ PASS";
        } catch (Throwable e) {
            res.status = "❌ FAIL";
            res.details = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        }
        return res;
    }

    private void printMarkdownReport(List<ComplianceResult> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(PROJECT_NAME).append(" High-Precision Compliance Report\n\n");
        sb.append("| Component | Test Case | Status | Details |\n");
        sb.append("| --- | --- | --- | --- |\n");

        for (ComplianceResult res : results) {
            sb.append("| ").append(res.component).append(" | ").append(res.testName).append(" | ").append(res.status).append(" | ").append(res.details).append(" |\n");
        }
        sb.append("\n*Generated by HighPrecisionComplianceTest on ").append(new Date()).append("*\n");

        String report = sb.toString();
        System.out.println(report);

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(REPORT_PATH);
            if (path.getParent() != null) {
                java.nio.file.Files.createDirectories(path.getParent());
            }
            java.nio.file.Files.writeString(path, report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
