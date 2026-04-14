package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeMPFRDenseLinearAlgebraBackend;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeFFMBLASBackend;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeMPFRSparseLinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
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
        List<LinearAlgebraProvider<?>> providers = new ArrayList<>();
        
        // Explicitly test our target backends
        providers.add(new NativeFFMBLASBackend<Real>());
        providers.add(new NativeMPFRDenseLinearAlgebraBackend<Real>());
        providers.add(new NativeMPFRSparseLinearAlgebraBackend<Real>());

        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<?> provider : providers) {
            System.out.println("Testing provider: " + provider.getName());
            ComplianceResult res = new ComplianceResult();
            res.providerName = provider.getName();
            res.environment = provider.getEnvironmentInfo();
            
            // Cast to Real for standard tests
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<Real> realProvider = (LinearAlgebraProvider<Real>) provider;
            
            testOperation(res, "Transpose", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                realProvider.transpose(a);
            });

            testOperation(res, "Multiply", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                double[][] bData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                RealDoubleMatrix b = RealDoubleMatrix.of(bData);
                realProvider.multiply(a, b);
            });

            testOperation(res, "Inverse", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                realProvider.inverse(a);
            });

            testOperation(res, "LU", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                realProvider.lu(a);
            });

            testOperation(res, "QR", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                realProvider.qr(a);
            });

            testOperation(res, "SVD", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                realProvider.svd(a);
            });

            testOperation(res, "Eigen", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                // Make symmetric for SYEV/HEEV
                for (int i = 0; i < SIZE; i++)
                    for (int j = i+1; j < SIZE; j++)
                        aData[j][i] = aData[i][j];
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                realProvider.eigen(a);
            });

            testOperation(res, "Solve", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                RealDoubleMatrix a = RealDoubleMatrix.of(aData);
                double[] bData = new double[SIZE];
                for (int i = 0; i < SIZE; i++) bData[i] = 1.0;
                org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector b = org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(bData);
                realProvider.solve(a, b);
            });

            testOperation(res, "SpMV", () -> {
                double[][] aData = randomData(SIZE, SIZE);
                Real[][] aReal = new Real[SIZE][SIZE];
                for(int i=0; i<SIZE; i++) for(int j=0; j<SIZE; j++) aReal[i][j] = Real.of(aData[i][j]);
                SparseMatrix<Real> a = new SparseMatrix<>(aReal, org.episteme.core.mathematics.sets.Reals.getInstance());
                double[] bData = new double[SIZE];
                for (int i = 0; i < SIZE; i++) bData[i] = 1.0;
                RealDoubleVector b = RealDoubleVector.of(bData);
                realProvider.multiply(a, b);
            });

            testOperation(res, "CG", () -> {
                if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException();
                SparseLinearAlgebraProvider<Real> sparseProvider = (SparseLinearAlgebraProvider<Real>) provider;
                double[][] aData = randomData(SIZE, SIZE);
                // Symmetric positive definite for CG
                for(int i=0; i<SIZE; i++) {
                    for(int j=i+1; j<SIZE; j++) aData[j][i] = aData[i][j];
                    aData[i][i] = 20.0;
                }
                Real[][] aReal = new Real[SIZE][SIZE];
                for(int i=0; i<SIZE; i++) for(int j=0; j<SIZE; j++) aReal[i][j] = Real.of(aData[i][j]);
                SparseMatrix<Real> a = new SparseMatrix<>(aReal, org.episteme.core.mathematics.sets.Reals.getInstance());
                double[] bData = new double[SIZE];
                for (int i = 0; i < SIZE; i++) bData[i] = 1.0;
                RealDoubleVector b = RealDoubleVector.of(bData);
                RealDoubleVector x0 = RealDoubleVector.of(new double[SIZE]);
                sparseProvider.conjugateGradient(a, b, x0, Real.of(1e-10), 100);
            });

            testOperation(res, "BiCGSTAB", () -> {
                if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException();
                SparseLinearAlgebraProvider<Real> sparseProvider = (SparseLinearAlgebraProvider<Real>) provider;
                double[][] aData = randomData(SIZE, SIZE);
                for(int i=0; i<SIZE; i++) aData[i][i] = 10.0;
                Real[][] aReal = new Real[SIZE][SIZE];
                for(int i=0; i<SIZE; i++) for(int j=0; j<SIZE; j++) aReal[i][j] = Real.of(aData[i][j]);
                SparseMatrix<Real> a = new SparseMatrix<>(aReal, org.episteme.core.mathematics.sets.Reals.getInstance());
                double[] bData = new double[SIZE];
                for (int i = 0; i < SIZE; i++) bData[i] = 1.0;
                RealDoubleVector b = RealDoubleVector.of(bData);
                RealDoubleVector x0 = RealDoubleVector.of(new double[SIZE]);
                sparseProvider.bicgstab(a, b, x0, Real.of(1e-10), 100);
            });

            testOperation(res, "GMRES", () -> {
                if (!(provider instanceof SparseLinearAlgebraProvider)) throw new UnsupportedOperationException();
                @SuppressWarnings("unchecked")
                SparseLinearAlgebraProvider<Real> sparseProvider = (SparseLinearAlgebraProvider<Real>) provider;
                double[][] aData = randomData(SIZE, SIZE);
                for(int i=0; i<SIZE; i++) aData[i][i] = 10.0;
                Real[][] aReal = new Real[SIZE][SIZE];
                for(int i=0; i<SIZE; i++) for(int j=0; j<SIZE; j++) aReal[i][j] = Real.of(aData[i][j]);
                SparseMatrix<Real> a = new SparseMatrix<>(aReal, org.episteme.core.mathematics.sets.Reals.getInstance());
                double[] bData = new double[SIZE];
                for (int i = 0; i < SIZE; i++) bData[i] = 1.0;
                RealDoubleVector b = RealDoubleVector.of(bData);
                RealDoubleVector x0 = RealDoubleVector.of(new double[SIZE]);
                sparseProvider.gmres(a, b, x0, Real.of(1e-10), 10, 5);
            });

            testOperation(res, "SpMV (Complex)", () -> {
                if (!(provider instanceof NativeMPFRSparseLinearAlgebraBackend)) throw new UnsupportedOperationException();
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Complex> complexProvider = (LinearAlgebraProvider<Complex>) provider;
                Complex[][] aComplex = new Complex[SIZE][SIZE];
                for(int i=0; i<SIZE; i++) {
                    for(int j=0; j<SIZE; j++) {
                        if (i==j || Math.random() < 0.1) aComplex[i][j] = Complex.of(Real.of(Math.random()), Real.of(Math.random()));
                        else aComplex[i][j] = Complex.ZERO;
                    }
                }
                SparseMatrix<Complex> a = new SparseMatrix<>(aComplex, Complex.ring());
                Complex[] bData = new Complex[SIZE];
                for (int i = 0; i < SIZE; i++) bData[i] = Complex.of(Real.ONE, Real.ONE);
                Vector<Complex> b = Vector.of(bData, Complex.ring());
                complexProvider.multiply(a, b);
            });

            testOperation(res, "BiCGSTAB (Complex)", () -> {
                if (!(provider instanceof NativeMPFRSparseLinearAlgebraBackend)) throw new UnsupportedOperationException();
                @SuppressWarnings("unchecked")
                SparseLinearAlgebraProvider<Complex> sparseProvider = (SparseLinearAlgebraProvider<Complex>) provider;
                Complex[][] aComplex = new Complex[SIZE][SIZE];
                for(int i=0; i<SIZE; i++) {
                    for(int j=0; j<SIZE; j++) {
                        if (i==j) aComplex[i][j] = Complex.of(Real.of(20.0), Real.ZERO);
                        else if (Math.random() < 0.1) aComplex[i][j] = Complex.of(Real.of(Math.random()), Real.of(Math.random()));
                        else aComplex[i][j] = Complex.ZERO;
                    }
                }
                SparseMatrix<Complex> a = new SparseMatrix<>(aComplex, Complex.ring());
                Complex[] bData = new Complex[SIZE];
                for (int i = 0; i < SIZE; i++) bData[i] = Complex.of(Real.ONE, Real.ZERO);
                Vector<Complex> b = Vector.of(bData, Complex.ring());
                Vector<Complex> x0 = Vector.zeros(SIZE, Complex.ring());
                sparseProvider.bicgstab(a, b, x0, Complex.of(Real.of(1e-10), Real.ZERO), 100);
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
