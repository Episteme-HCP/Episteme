/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Shared operations for High-Precision audits (Compliance, Correctness, Performance).
 * Ensures all three suites test the exact same 68 operations in the same order.
 */
public class HighPrecisionAuditOperations {

    public interface AuditAction<T> {
        void run(String opName, java.util.function.Supplier<Object> test);
    }

    public static void runRealBigAudit(LinearAlgebraProvider<RealBig> p, int n, AuditAction<RealBig> action) {
        RealBig val = RealBig.create(new BigDecimal("0.12345678901234567890123456789012345678901234567890"));
        RealBig val2 = RealBig.create(new BigDecimal("2.71828182845904523536028747135266249775724709369995"));
        
        Matrix<RealBig> A = createRealBigMatrix(val, n, p);
        Matrix<RealBig> B = createRealBigMatrix(val2, n, p);
        Vector<RealBig> v = createRealBigVector(val, n, p);
        Matrix<RealBig> invA = createInvertibleRealBigMatrix(n, p);
        Matrix<RealBig> spdA = createSPDRealBigMatrix(n, p);

        action.run("RB:Add", () -> p.add(A, B));
        action.run("RB:Sub", () -> p.subtract(A, B));
        action.run("RB:Scale", () -> p.scale(val, A));
        action.run("RB:Mul", () -> p.multiply(A, B));
        action.run("RB:MatVec", () -> p.multiply(A, v));
        action.run("RB:Trans", () -> p.transpose(A));
        action.run("RB:Inv", () -> p.inverse(invA));
        action.run("RB:Det", () -> p.determinant(invA));
        action.run("RB:Solve", () -> p.solve(invA, v));
        action.run("RB:Dot", () -> p.dot(v, v));
        action.run("RB:Norm", () -> p.norm(v));
        action.run("RB:LU", () -> p.lu(invA));
        action.run("RB:QR", () -> p.qr(A));
        action.run("RB:SVD", () -> p.svd(A)); 
        action.run("RB:Chol", () -> p.cholesky(spdA));
        action.run("RB:Eigen", () -> p.eigen(invA));

        // Iterative Solvers (Sparse)
        action.run("RB:BiCGSTAB", () -> {
            if (p instanceof SparseLinearAlgebraProvider<RealBig> sp) {
                return sp.bicgstab(invA, v, createRealBigVector(RealBig.create(BigDecimal.ZERO), n, p), RealBig.create(new BigDecimal("1e-8")), 100);
            } else throw new UnsupportedOperationException("Not sparse");
        });
        action.run("RB:ConjGrad", () -> {
            if (p instanceof SparseLinearAlgebraProvider<RealBig> sp) {
                return sp.conjugateGradient(spdA, v, createRealBigVector(RealBig.create(BigDecimal.ZERO), n, p), RealBig.create(new BigDecimal("1e-8")), 100);
            } else throw new UnsupportedOperationException("Not sparse");
        });
        action.run("RB:GMRES", () -> {
            if (p instanceof SparseLinearAlgebraProvider<RealBig> sp) {
                return sp.gmres(invA, v, createRealBigVector(RealBig.create(BigDecimal.ZERO), n, p), RealBig.create(new BigDecimal("1e-8")), 100, 5);
            } else throw new UnsupportedOperationException("Not sparse");
        });

        // Transcendentals
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, p);
        action.run("RB:Exp", () -> p.exp(T));
        action.run("RB:Sin", () -> p.sin(T));
        action.run("RB:Cos", () -> p.cos(T));
        action.run("RB:Tan", () -> p.tan(T));
        action.run("RB:Log", () -> p.log(createRealBigMatrix(new BigDecimal("2.0"), 1, p)));
        action.run("RB:Log10", () -> p.log10(createRealBigMatrix(new BigDecimal("2.0"), 1, p)));
        action.run("RB:Asin", () -> p.asin(T));
        action.run("RB:Acos", () -> p.acos(T));
        action.run("RB:Atan", () -> p.atan(T));
        action.run("RB:Sinh", () -> p.sinh(T));
        action.run("RB:Cosh", () -> p.cosh(T));
        action.run("RB:Tanh", () -> p.tanh(T));
        action.run("RB:Sqrt", () -> p.sqrt(createRealBigMatrix(new BigDecimal("2.0"), 1, p)));
        action.run("RB:Cbrt", () -> p.cbrt(createRealBigMatrix(new BigDecimal("2.0"), 1, p)));
        action.run("RB:Pow", () -> p.pow(createRealBigMatrix(new BigDecimal("2.0"), 1, p), RealBig.create(new BigDecimal("3"))));
    }

    public static void runComplexAudit(LinearAlgebraProvider<Complex> p, int n, AuditAction<Complex> action) {
        Complex z1 = Complex.of(1.5, 2.3);
        Complex z2 = Complex.of(0.7, -1.1);
        
        Matrix<Complex> A = createComplexMatrix(z1, n, p);
        Matrix<Complex> B = createComplexMatrix(z2, n, p);
        Vector<Complex> v = createComplexVector(z1, n, p);
        Matrix<Complex> invA = createInvertibleComplexMatrix(n, p);
        Matrix<Complex> spdA = createSPDComplexMatrix(n, p);

        action.run("C:Add", () -> p.add(A, B));
        action.run("C:Sub", () -> p.subtract(A, B));
        action.run("C:Scale", () -> p.scale(z2, A));
        action.run("C:Mul", () -> p.multiply(A, B));
        action.run("C:MatVec", () -> p.multiply(A, v));
        action.run("C:Trans", () -> p.transpose(A));
        action.run("C:Inv", () -> p.inverse(invA));
        action.run("C:Det", () -> p.determinant(invA));
        action.run("C:Solve", () -> p.solve(invA, v));
        action.run("C:Dot", () -> p.dot(v, v));
        action.run("C:Norm", () -> p.norm(v));
        action.run("C:LU", () -> p.lu(invA));
        action.run("C:QR", () -> p.qr(A));
        action.run("C:SVD", () -> p.svd(A)); 
        action.run("C:Chol", () -> p.cholesky(spdA));
        action.run("C:Eigen", () -> p.eigen(invA));

        // Iterative
        action.run("C:BiCGSTAB", () -> {
            if (p instanceof SparseLinearAlgebraProvider<Complex> sp) {
                return sp.bicgstab(invA, v, createComplexVector(Complex.of(0, 0), n, p), Complex.of(1e-8, 0), 100);
            } else throw new UnsupportedOperationException("Not sparse");
        });
        action.run("C:ConjGrad", () -> {
            if (p instanceof SparseLinearAlgebraProvider<Complex> sp) {
                return sp.conjugateGradient(spdA, v, createComplexVector(Complex.of(0, 0), n, p), Complex.of(1e-8, 0), 100);
            } else throw new UnsupportedOperationException("Not sparse");
        });
        action.run("C:GMRES", () -> {
            if (p instanceof SparseLinearAlgebraProvider<Complex> sp) {
                return sp.gmres(invA, v, createComplexVector(Complex.of(0, 0), n, p), Complex.of(1e-8, 0), 100, 5);
            } else throw new UnsupportedOperationException("Not sparse");
        });

        // Transcendentals
        Matrix<Complex> T = createComplexMatrix(Complex.of(0.5, 0.1), 1, p);
        action.run("C:Exp", () -> p.exp(T));
        action.run("C:Log", () -> p.log(createComplexMatrix(Complex.of(2.0, 0.5), 1, p)));
        action.run("C:Log10", () -> p.log10(createComplexMatrix(Complex.of(2.0, 0.5), 1, p)));
        action.run("C:Sin", () -> p.sin(T));
        action.run("C:Cos", () -> p.cos(T));
        action.run("C:Tan", () -> p.tan(T));
        action.run("C:Asin", () -> p.asin(T));
        action.run("C:Acos", () -> p.acos(T));
        action.run("C:Atan", () -> p.atan(T));
        action.run("C:Sinh", () -> p.sinh(T));
        action.run("C:Cosh", () -> p.cosh(T));
        action.run("C:Tanh", () -> p.tanh(T));
        action.run("C:Sqrt", () -> p.sqrt(createComplexMatrix(Complex.of(2.0, 0.5), 1, p)));
        action.run("C:Cbrt", () -> p.cbrt(createComplexMatrix(Complex.of(2.0, 0.5), 1, p)));
        action.run("C:Pow", () -> p.pow(createComplexMatrix(Complex.of(2.0, 0.5), 1, p), Complex.of(3, 0)));
    }

    // --- Helpers ---
    @SuppressWarnings("unchecked")
    private static Matrix<RealBig> createRealBigMatrix(RealBig val, int n, LinearAlgebraProvider<RealBig> p) {
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(val.bigDecimalValue().add(new BigDecimal(i + j)));
        return Matrix.of(data, (Ring<RealBig>) (Object) org.episteme.core.mathematics.numbers.real.Real.ZERO.getScalarRing());
    }
    private static Matrix<RealBig> createRealBigMatrix(BigDecimal val, int n, LinearAlgebraProvider<RealBig> p) {
        return createRealBigMatrix(RealBig.create(val), n, p);
    }
    @SuppressWarnings("unchecked")
    private static Matrix<RealBig> createInvertibleRealBigMatrix(int n, LinearAlgebraProvider<RealBig> p) {
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(i == j ? new BigDecimal(n + i) : new BigDecimal("0.1"));
        return Matrix.of(data, (Ring<RealBig>) (Object) org.episteme.core.mathematics.numbers.real.Real.ZERO.getScalarRing());
    }
    @SuppressWarnings("unchecked")
    private static Matrix<RealBig> createSPDRealBigMatrix(int n, LinearAlgebraProvider<RealBig> p) {
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(new BigDecimal(i == j ? n * 3.0 : 0.1));
        return Matrix.of(data, (Ring<RealBig>) (Object) org.episteme.core.mathematics.numbers.real.Real.ZERO.getScalarRing());
    }
    @SuppressWarnings("unchecked")
    private static Vector<RealBig> createRealBigVector(RealBig val, int n, LinearAlgebraProvider<RealBig> p) {
        RealBig[] data = new RealBig[n];
        for (int i = 0; i < n; i++) data[i] = RealBig.create(val.bigDecimalValue().add(new BigDecimal(i)));
        return Vector.of(data, (Ring<RealBig>) (Object) org.episteme.core.mathematics.numbers.real.Real.ZERO.getScalarRing());
    }

    @SuppressWarnings("unchecked")
    private static Matrix<Complex> createComplexMatrix(Complex z, int n, LinearAlgebraProvider<Complex> p) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = Complex.of(z.getReal().doubleValue() + i, z.getImaginary().doubleValue() + j);
        return Matrix.of(data, (Ring<Complex>) (Object) Complex.of(0, 0).getScalarRing());
    }
    @SuppressWarnings("unchecked")
    private static Matrix<Complex> createInvertibleComplexMatrix(int n, LinearAlgebraProvider<Complex> p) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = Complex.of(i == j ? n + i : 0.1, 0.1);
        return Matrix.of(data, (Ring<Complex>) (Object) Complex.of(0, 0).getScalarRing());
    }
    @SuppressWarnings("unchecked")
    private static Matrix<Complex> createSPDComplexMatrix(int n, LinearAlgebraProvider<Complex> p) {
        Complex[][] data = new Complex[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = Complex.of(i == j ? n * 3.0 : 0.1, 0);
        return Matrix.of(data, (Ring<Complex>) (Object) Complex.of(0, 0).getScalarRing());
    }
    @SuppressWarnings("unchecked")
    private static Vector<Complex> createComplexVector(Complex z, int n, LinearAlgebraProvider<Complex> p) {
        Complex[] data = new Complex[n];
        for (int i = 0; i < n; i++) data[i] = Complex.of(z.getReal().doubleValue() + i, z.getImaginary().doubleValue());
        return Vector.of(data, (Ring<Complex>) (Object) Complex.of(0, 0).getScalarRing());
    }
}
