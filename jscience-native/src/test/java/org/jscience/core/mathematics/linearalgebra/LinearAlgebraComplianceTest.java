package org.jscience.core.mathematics.linearalgebra;

import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.matrices.solvers.*;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.junit.jupiter.api.Test;
import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Systematic compliance test for all LinearAlgebraProvider implementations.
 * Generates a markdown report of supported features and correctness.
 */
public class LinearAlgebraComplianceTest {

    private static final double TOLERANCE = 1e-8;
    private static final int SIZE = 50;

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }

    @Test
    public void generateComplianceReport() {
        @SuppressWarnings("unchecked")
        List<LinearAlgebraProvider<Real>> rawProviders = AlgorithmManager.getProviders(LinearAlgebraProvider.class)
                .stream()
                .filter(p -> p.isCompatible(org.jscience.core.mathematics.sets.Reals.getInstance()))
                .map(p -> (LinearAlgebraProvider<Real>) p)
                .toList();
                
        List<LinearAlgebraProvider<Real>> providers = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (LinearAlgebraProvider<Real> p : rawProviders) {
            if (seen.add(p.getName())) {
                providers.add(p);
            }
        }

        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<Real> provider : providers) {
            ComplianceResult res = new ComplianceResult();
            res.providerName = provider.getName();
            res.environment = provider.getEnvironmentInfo();
            
            testOperation(res, "Transpose", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                Matrix<Real> result = provider.transpose(a);
                SimpleMatrix expected = new SimpleMatrix(aData).transpose();
                verifyMatrix(expected, result, TOLERANCE);
            });

            testOperation(res, "Add", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                double[][] bData = randomData(SIZE, SIZE, rand);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                RealDoubleMatrix b = RealDoubleMatrix.of(bData);
                Matrix<Real> result = provider.add(a, b);
                SimpleMatrix expected = new SimpleMatrix(aData).plus(new SimpleMatrix(bData));
                verifyMatrix(expected, result, TOLERANCE);
            });

            testOperation(res, "Subtract", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                double[][] bData = randomData(SIZE, SIZE, rand);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                RealDoubleMatrix b = RealDoubleMatrix.of(bData);
                Matrix<Real> result = provider.subtract(a, b);
                SimpleMatrix expected = new SimpleMatrix(aData).minus(new SimpleMatrix(bData));
                verifyMatrix(expected, result, TOLERANCE);
            });

            testOperation(res, "Scale", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                double scale = 3.14159;
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                Matrix<Real> result = provider.scale(Real.of(scale), a);
                SimpleMatrix expected = new SimpleMatrix(aData).scale(scale);
                verifyMatrix(expected, result, TOLERANCE);
            });

            testOperation(res, "Multiply", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                double[][] bData = randomData(SIZE, SIZE, rand);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                RealDoubleMatrix b = RealDoubleMatrix.of(bData);
                Matrix<Real> result = provider.multiply(a, b);
                SimpleMatrix expected = new SimpleMatrix(aData).mult(new SimpleMatrix(bData));
                verifyMatrix(expected, result, TOLERANCE);
            });

            testOperation(res, "Inverse", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                Matrix<Real> result = provider.inverse(a);
                SimpleMatrix expected = new SimpleMatrix(aData).invert();
                verifyMatrix(expected, result, 1e-7);
            });

            testOperation(res, "LU", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                LUResult<Real> result = provider.lu(a);
                verifyLU(a, result);
            });

            testOperation(res, "QR", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                QRResult<Real> result = provider.qr(a);
                verifyQR(a, result);
            });

            testOperation(res, "SVD", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(50, 40, rand);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                SVDResult<Real> result = provider.svd(a);
                verifySVD(a, result);
            });

            testOperation(res, "Cholesky", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                SimpleMatrix mat = new SimpleMatrix(aData);
                SimpleMatrix posDef = mat.transpose().mult(mat).plus(SimpleMatrix.identity(SIZE));
                double[][] pdData = toArray(posDef);
                RealDoubleMatrix a = RealDoubleMatrix.of(pdData);
                CholeskyResult<Real> result = provider.cholesky(a);
                verifyCholesky(a, result);
            });

            testOperation(res, "Eigen", () -> {
                Random rand = new Random(42);
                double[][] aData = randomData(SIZE, SIZE, rand);
                // Symmetric for easier verification
                SimpleMatrix mat = new SimpleMatrix(aData);
                SimpleMatrix sym = mat.plus(mat.transpose());
                double[][] symData = toArray(sym);
                RealDoubleMatrix a = RealDoubleMatrix.of(symData);
                EigenResult<Real> result = provider.eigen(a);
                verifyEigen(a, result);
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
            e.printStackTrace();
            res.status.put(opName, "⚠️ FAIL (" + e.getClass().getSimpleName() + ")");
        }
    }

    private void verifyMatrix(SimpleMatrix expected, Matrix<Real> actual, double tol) {
        assertEquals(expected.getNumRows(), actual.rows());
        assertEquals(expected.getNumCols(), actual.cols());
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.cols(); j++) {
                assertEquals(expected.get(i, j), actual.get(i, j).doubleValue(), tol);
            }
        }
    }

    private void verifyLU(Matrix<Real> a, LUResult<Real> res) {
        Matrix<Real> lu = res.L().multiply(res.U());
        int n = a.rows();
        Real[][] paData = new Real[n][n];
        for (int i = 0; i < n; i++) {
            int pivot = (int) res.P().get(i).doubleValue();
            for (int j = 0; j < n; j++) paData[i][j] = a.get(pivot, j);
        }
        Matrix<Real> PA = Matrix.of(paData, org.jscience.core.mathematics.sets.Reals.getInstance());
        verifyMatrix(new SimpleMatrix(toDoubleArray(PA)), lu, 1e-8);
    }

    private void verifyQR(Matrix<Real> a, QRResult<Real> res) {
        Matrix<Real> reconstructed = res.Q().multiply(res.R());
        verifyMatrix(new SimpleMatrix(toDoubleArray(a)), reconstructed, 1e-8);
    }

    private void verifySVD(Matrix<Real> a, SVDResult<Real> res) {
        int k = res.S().dimension();
        Real[][] sMatrix = new Real[res.U().cols()][res.V().cols()];
        for (int i = 0; i < sMatrix.length; i++) {
            for (int j = 0; j < sMatrix[0].length; j++) {
                sMatrix[i][j] = (i == j && i < k) ? res.S().get(i) : Real.ZERO;
            }
        }
        Matrix<Real> S = Matrix.of(sMatrix, org.jscience.core.mathematics.sets.Reals.getInstance());
        Matrix<Real> reconstructed = res.U().multiply(S).multiply(res.V().transpose());
        verifyMatrix(new SimpleMatrix(toDoubleArray(a)), reconstructed, 1e-8);
    }

    private void verifyCholesky(Matrix<Real> a, CholeskyResult<Real> res) {
        Matrix<Real> reconstructed = res.L().multiply(res.L().transpose());
        verifyMatrix(new SimpleMatrix(toDoubleArray(a)), reconstructed, 1e-8);
    }

    private void verifyEigen(Matrix<Real> a, EigenResult<Real> res) {
        // A * v = lambda * v
        for (int i = 0; i < res.D().dimension(); i++) {
            Real lambda = res.D().get(i);
            Vector<Real> v = res.V().transpose().getRow(i); // Assuming row vectors
            
            Vector<Real> Av = a.multiply(v);
            Vector<Real> lv = v.multiply(lambda);
            
            for (int j = 0; j < Av.dimension(); j++) {
                assertEquals(Av.get(j).doubleValue(), lv.get(j).doubleValue(), 1e-7);
            }
        }
    }

    private double[][] randomData(int rows, int cols, Random rand) {
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (rand.nextDouble() < 0.2) {
                    data[i][j] = 0.0;
                } else {
                    data[i][j] = rand.nextDouble() * 2 - 1;
                }
            }
        }
        return data;
    }

    private double[][] toDoubleArray(Matrix<Real> m) {
        double[][] d = new double[m.rows()][m.cols()];
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.cols(); j++) d[i][j] = m.get(i, j).doubleValue();
        }
        return d;
    }

    private double[][] toArray(SimpleMatrix m) {
        double[][] d = new double[m.getNumRows()][m.getNumCols()];
        for (int i = 0; i < m.getNumRows(); i++) {
            for (int j = 0; j < m.getNumCols(); j++) d[i][j] = m.get(i, j);
        }
        return d;
    }

    private void printMarkdownReport(List<ComplianceResult> results) {
        if (results.isEmpty()) return;
        
        StringBuilder sb = new StringBuilder();
        sb.append("# Linear Algebra Provider Compliance Report\n\n");
        
        Set<String> ops = results.get(0).status.keySet();
        
        sb.append("| Provider | Environment |");
        for (String op : ops) sb.append(" ").append(op).append(" |");
        sb.append("\n| --- | --- |").append(" --- |".repeat(ops.size())).append("\n");

        for (ComplianceResult res : results) {
            sb.append("| ").append(res.providerName).append(" | ").append(res.environment).append(" |");
            for (String op : ops) {
                sb.append(" ").append(res.status.get(op)).append(" |");
            }
            sb.append("\n");
        }
        sb.append("\n*Generated by LinearAlgebraComplianceTest on ").append(new Date()).append("*\n");
        
        String report = sb.toString();
        System.out.println(report);
        
        try {
            Files.createDirectories(Paths.get("../docs"));
            Files.writeString(Paths.get("../docs", "LinearAlgebraComplianceReport.md"), report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
