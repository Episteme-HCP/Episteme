package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeMPFRDenseLinearAlgebraBackend;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeFFMBLASBackend;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeMPFRSparseLinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.io.IOException;



public class IsolatedComplianceTest {

    private static final int SIZE = 20;

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }

    @Test
    public void generateComplianceReport() {
        List<LinearAlgebraProvider<Real>> providers = new ArrayList<>();
        
        // Explicitly test our target backends
        providers.add(new NativeFFMBLASBackend<Real>());
        providers.add(new NativeMPFRDenseLinearAlgebraBackend<Real>());
        providers.add(new NativeMPFRSparseLinearAlgebraBackend<Real>());

        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<Real> provider : providers) {
            System.out.println("Testing provider: " + provider.getName());
            ComplianceResult res = new ComplianceResult();
            res.providerName = provider.getName();
            res.environment = provider.getEnvironmentInfo();
            
            testOperation(res, "Transpose", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                provider.transpose(a);
            });

            testOperation(res, "Multiply", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                double[][] bData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                RealDoubleMatrix b = RealDoubleMatrix.of(bData);
                provider.multiply(a, b);
            });

            testOperation(res, "Inverse", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                provider.inverse(a);
            });

            testOperation(res, "LU", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                provider.lu(a);
            });

            testOperation(res, "QR", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                provider.qr(a);
            });

            testOperation(res, "SVD", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                provider.svd(a);
            });

            testOperation(res, "Eigen", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                // Make symmetric for SYEV/HEEV
                for (int i = 0; i < SIZE; i++)
                    for (int j = i+1; j < SIZE; j++)
                        aData[j][i] = aData[i][j];
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                provider.eigen(a);
            });

            testOperation(res, "Solve", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                double[] bData = new double[SIZE];
                for (int i = 0; i < SIZE; i++) bData[i] = 1.0;
                org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector b = org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(bData);
                provider.solve(a, b);
            });

            results.add(res);
        }

        printMarkdownReport(results);
    }

    private void testOperation(ComplianceResult res, String opName, Runnable test) {
        try {
            test.run();
            res.status.put(opName, "✅ PASS");
        } catch (UnsupportedOperationException e) {
            res.status.put(opName, "❌ N/A");
        } catch (Throwable e) {
            res.status.put(opName, "⚠️ FAIL (" + e.getClass().getSimpleName() + ")");
            System.err.println("FAIL " + opName + " on " + res.providerName + ": " + e.getMessage());
        }
    }

    private double[][] randomData(int rows, int cols) {
        Random rand = new Random(42);
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = rand.nextDouble() * 2 - 1;
            }
        }
        return data;
    }

    private void printMarkdownReport(List<ComplianceResult> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Episteme Linear Algebra Isolated Compliance Report\n\n");
        
        Set<String> ops = results.get(0).status.keySet();
        sb.append("| Provider | Environment |");
        for (String op : ops) sb.append(" ").append(op).append(" |");
        sb.append("\n| --- | --- |").append(" --- |".repeat(ops.size())).append("\n");

        for (ComplianceResult res : results) {
            sb.append("| ").append(res.providerName).append(" | ").append(res.environment).append(" |");
            for (String op : ops) sb.append(" ").append(res.status.get(op)).append(" |");
            sb.append("\n");
        }
        
        String report = sb.toString();
        System.out.println(report);
        
        try {
            java.nio.file.Files.writeString(java.nio.file.Paths.get("../docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT_ISOLATED.md"), report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
