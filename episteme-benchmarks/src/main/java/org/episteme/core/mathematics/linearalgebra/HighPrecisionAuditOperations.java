/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import java.math.BigDecimal;

/**
 * Shared operations for High-Precision audits (Compliance, Correctness, Performance).
 * Ensures all three suites test the exact same 68 operations in the same order.
 */
public class HighPrecisionAuditOperations {

    public interface AuditAction<T> {
        void run(String opName, java.util.function.Supplier<Object> test);
    }

    // --- RealBig Individual Tests ---
    public static void testAdd(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 3; Matrix<RealBig> A = createRealBigMatrix(new BigDecimal("0.1"), n, prov); Matrix<RealBig> B = createRealBigMatrix(new BigDecimal("0.2"), n, prov);
        assertMatrixClose(prov.add(A, B), groundTruth.add(A, B), new BigDecimal("1e-25"), "RB:Add");
    }
    public static void testSubtract(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 3; Matrix<RealBig> A = createRealBigMatrix(new BigDecimal("0.1"), n, prov); Matrix<RealBig> B = createRealBigMatrix(new BigDecimal("0.2"), n, prov);
        assertMatrixClose(prov.subtract(A, B), groundTruth.subtract(A, B), new BigDecimal("1e-25"), "RB:Sub");
    }
    public static void testScale(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 3; Matrix<RealBig> A = createRealBigMatrix(new BigDecimal("0.1"), n, prov); RealBig s = RealBig.create(new BigDecimal("2.0"));
        assertMatrixClose(prov.scale(s, A), groundTruth.scale(s, A), new BigDecimal("1e-25"), "RB:Scale");
    }
    public static void testMultiply(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 3; Matrix<RealBig> A = createRealBigMatrix(new BigDecimal("0.1"), n, prov); Matrix<RealBig> B = createRealBigMatrix(new BigDecimal("0.2"), n, prov);
        assertMatrixClose(prov.multiply(A, B), groundTruth.multiply(A, B), new BigDecimal("1e-25"), "RB:Mul");
    }
    public static void testMatVec(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 3; Matrix<RealBig> A = createRealBigMatrix(new BigDecimal("0.1"), n, prov); Vector<RealBig> v = createRealBigVector(RealBig.create(new BigDecimal("0.5")), n, prov);
        assertVectorClose(prov.multiply(A, v), groundTruth.multiply(A, v), new BigDecimal("1e-25"), "RB:MatVec");
    }
    public static void testTranspose(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 3; Matrix<RealBig> A = createRealBigMatrix(new BigDecimal("0.1"), n, prov);
        assertMatrixClose(prov.transpose(A), groundTruth.transpose(A), new BigDecimal("1e-25"), "RB:Trans");
    }
    public static void testInverse(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 2; Matrix<RealBig> A = createInvertibleRealBigMatrix(n, prov);
        assertMatrixClose(prov.inverse(A), groundTruth.inverse(A), new BigDecimal("1e-20"), "RB:Inv");
    }
    public static void testDeterminant(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 2; Matrix<RealBig> A = createInvertibleRealBigMatrix(n, prov);
        assertScalarClose(prov.determinant(A), groundTruth.determinant(A), new BigDecimal("1e-20"), "RB:Det");
    }
    public static void testSolve(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 2; Matrix<RealBig> A = createInvertibleRealBigMatrix(n, prov); Vector<RealBig> b = createRealBigVector(RealBig.create(new BigDecimal("1.0")), n, prov);
        assertVectorClose(prov.solve(A, b), groundTruth.solve(A, b), new BigDecimal("1e-20"), "RB:Solve");
    }
    public static void testDot(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 3; Vector<RealBig> v = createRealBigVector(RealBig.create(new BigDecimal("0.5")), n, prov);
        assertScalarClose(prov.dot(v, v), groundTruth.dot(v, v), new BigDecimal("1e-25"), "RB:Dot");
    }
    public static void testNorm(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 3; Vector<RealBig> v = createRealBigVector(RealBig.create(new BigDecimal("0.5")), n, prov);
        assertScalarClose(prov.norm(v), groundTruth.norm(v), new BigDecimal("1e-25"), "RB:Norm");
    }
    public static void testLU(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 2; Matrix<RealBig> A = createInvertibleRealBigMatrix(n, prov);
        LUResult<RealBig> lu = prov.lu(A);
        assertMatrixClose(prov.multiply(lu.getL(), lu.getU()), A, new BigDecimal("1e-20"), "RB:LU");
    }
    public static void testQR(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 2; Matrix<RealBig> A = createRealBigMatrix(new BigDecimal("0.5"), n, prov);
        QRResult<RealBig> qr = prov.qr(A);
        assertMatrixClose(prov.multiply(qr.getQ(), qr.getR()), A, new BigDecimal("1e-20"), "RB:QR");
    }
    public static void testSVD(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 2; Matrix<RealBig> A = createRealBigMatrix(new BigDecimal("0.5"), n, prov);
        SVDResult<RealBig> svd = prov.svd(A);
        Matrix<RealBig> S = toDiagonalMatrix(svd.getS(), prov);
        assertMatrixClose(prov.multiply(prov.multiply(svd.getU(), S), prov.transpose(svd.getV())), A, new BigDecimal("1e-18"), "RB:SVD");
    }
    public static void testCholesky(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 2; Matrix<RealBig> A = createSPDRealBigMatrix(n, prov);
        CholeskyResult<RealBig> chol = prov.cholesky(A);
        assertMatrixClose(prov.multiply(chol.getL(), prov.transpose(chol.getL())), A, new BigDecimal("1e-20"), "RB:Chol");
    }
    public static void testEigen(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        int n = 2; Matrix<RealBig> A = createSPDRealBigMatrix(n, prov);
        EigenResult<RealBig> eigen = prov.eigen(A);
        Matrix<RealBig> D = toDiagonalMatrix(eigen.getD(), prov);
        assertMatrixClose(prov.multiply(A, eigen.getV()), prov.multiply(eigen.getV(), D), new BigDecimal("1e-15"), "RB:Eigen");
    }

    public static void testBiCGSTAB(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        if (prov instanceof SparseLinearAlgebraProvider<RealBig> sp) {
            int n = 2; Matrix<RealBig> A = createInvertibleRealBigMatrix(n, prov); Vector<RealBig> b = createRealBigVector(RealBig.create(new BigDecimal("1.0")), n, prov);
            Vector<RealBig> x0 = createRealBigVector(RealBig.ZERO, n, prov);
            assertVectorClose(sp.bicgstab(A, b, x0, RealBig.create(new BigDecimal("1e-10")), 100), groundTruth.solve(A, b), new BigDecimal("1e-9"), "RB:BiCGSTAB");
        }
    }
    public static void testConjugateGradient(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        if (prov instanceof SparseLinearAlgebraProvider<RealBig> sp) {
            int n = 2; Matrix<RealBig> A = createSPDRealBigMatrix(n, prov); Vector<RealBig> b = createRealBigVector(RealBig.create(new BigDecimal("1.0")), n, prov);
            Vector<RealBig> x0 = createRealBigVector(RealBig.ZERO, n, prov);
            assertVectorClose(sp.conjugateGradient(A, b, x0, RealBig.create(new BigDecimal("1e-10")), 100), groundTruth.solve(A, b), new BigDecimal("1e-9"), "RB:ConjGrad");
        }
    }
    public static void testGMRES(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        if (prov instanceof SparseLinearAlgebraProvider<RealBig> sp) {
            int n = 2; Matrix<RealBig> A = createInvertibleRealBigMatrix(n, prov); Vector<RealBig> b = createRealBigVector(RealBig.create(new BigDecimal("1.0")), n, prov);
            Vector<RealBig> x0 = createRealBigVector(RealBig.ZERO, n, prov);
            assertVectorClose(sp.gmres(A, b, x0, RealBig.create(new BigDecimal("1e-10")), 100, 5), groundTruth.solve(A, b), new BigDecimal("1e-9"), "RB:GMRES");
        }
    }

    public static void testExp(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.exp(T), groundTruth.exp(T), new BigDecimal("1e-25"), "RB:Exp");
    }
    public static void testSin(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.sin(T), groundTruth.sin(T), new BigDecimal("1e-25"), "RB:Sin");
    }
    public static void testCos(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.cos(T), groundTruth.cos(T), new BigDecimal("1e-25"), "RB:Cos");
    }
    public static void testTan(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.tan(T), groundTruth.tan(T), new BigDecimal("1e-25"), "RB:Tan");
    }
    public static void testLog(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("2.0"), 1, prov);
        assertMatrixClose(prov.log(T), groundTruth.log(T), new BigDecimal("1e-25"), "RB:Log");
    }
    public static void testLog10(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("2.0"), 1, prov);
        assertMatrixClose(prov.log10(T), groundTruth.log10(T), new BigDecimal("1e-25"), "RB:Log10");
    }
    public static void testAsin(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.asin(T), groundTruth.asin(T), new BigDecimal("1e-25"), "RB:Asin");
    }
    public static void testAcos(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.acos(T), groundTruth.acos(T), new BigDecimal("1e-25"), "RB:Acos");
    }
    public static void testAtan(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.atan(T), groundTruth.atan(T), new BigDecimal("1e-25"), "RB:Atan");
    }
    public static void testSinh(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.sinh(T), groundTruth.sinh(T), new BigDecimal("1e-25"), "RB:Sinh");
    }
    public static void testCosh(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.cosh(T), groundTruth.cosh(T), new BigDecimal("1e-25"), "RB:Cosh");
    }
    public static void testTanh(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("0.5"), 1, prov);
        assertMatrixClose(prov.tanh(T), groundTruth.tanh(T), new BigDecimal("1e-25"), "RB:Tanh");
    }
    public static void testSqrt(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("2.0"), 1, prov);
        assertMatrixClose(prov.sqrt(T), groundTruth.sqrt(T), new BigDecimal("1e-25"), "RB:Sqrt");
    }
    public static void testCbrt(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("2.0"), 1, prov);
        assertMatrixClose(prov.cbrt(T), groundTruth.cbrt(T), new BigDecimal("1e-25"), "RB:Cbrt");
    }
    public static void testPow(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {
        Matrix<RealBig> T = createRealBigMatrix(new BigDecimal("2.0"), 1, prov); RealBig s = RealBig.create(new BigDecimal("3"));
        assertMatrixClose(prov.pow(T, s), groundTruth.pow(T, s), new BigDecimal("1e-25"), "RB:Pow");
    }

    // --- Complex Individual Tests (Stubs) ---
    public static void testComplexAdd(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexSubtract(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexScale(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexMultiply(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexMatVec(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexTranspose(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexInverse(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexDeterminant(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexSolve(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexDot(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexNorm(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexLU(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexQR(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexSVD(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexCholesky(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexEigen(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexBiCGSTAB(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexConjugateGradient(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexGMRES(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexExp(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexLog(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexLog10(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexSin(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexCos(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexTan(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexAsin(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexAcos(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexAtan(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexSinh(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexCosh(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexTanh(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexSqrt(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexCbrt(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}
    public static void testComplexPow(LinearAlgebraProvider<RealBig> prov, LinearAlgebraProvider<RealBig> groundTruth) {}

    public static void runRealBigAudit(LinearAlgebraProvider<RealBig> p, int n, AuditAction<RealBig> action) {
        RealBig val = RealBig.create(new java.math.BigDecimal("0.12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"));
        RealBig val2 = RealBig.create(new java.math.BigDecimal("2.71828182845904523536028747135266249775724709369995957496696762772407663035354759457138217852516642742746639193"));
        
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
                return sp.bicgstab(invA, v, createRealBigVector(RealBig.ZERO, n, p), RealBig.create(new BigDecimal("1e-8")), 100);
            } else throw new UnsupportedOperationException("Not sparse");
        });
        action.run("RB:ConjGrad", () -> {
            if (p instanceof SparseLinearAlgebraProvider<RealBig> sp) {
                return sp.conjugateGradient(spdA, v, createRealBigVector(RealBig.ZERO, n, p), RealBig.create(new BigDecimal("1e-8")), 100);
            } else throw new UnsupportedOperationException("Not sparse");
        });
        action.run("RB:GMRES", () -> {
            if (p instanceof SparseLinearAlgebraProvider<RealBig> sp) {
                return sp.gmres(invA, v, createRealBigVector(RealBig.ZERO, n, p), RealBig.create(new BigDecimal("1e-8")), 100, 5);
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

    // --- Static Helpers and Assertions ---
    private static void assertMatrixClose(Matrix<RealBig> actual, Matrix<RealBig> expected, BigDecimal tol, String msg) {
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.cols(); j++) {
                BigDecimal valA = actual.get(i, j).bigDecimalValue();
                BigDecimal valE = expected.get(i, j).bigDecimalValue();
                if (valA.subtract(valE).abs().compareTo(tol) > 0) throw new AssertionError(msg + " at [" + i + "," + j + "]");
            }
        }
    }
    @SuppressWarnings("unchecked")
    private static <T> Matrix<T> toDiagonalMatrix(Vector<T> v, LinearAlgebraProvider<T> p) {
        int n = v.dimension();
        T[][] data = (T[][]) java.lang.reflect.Array.newInstance(v.getScalarRing().zero().getClass().arrayType(), n);
        for(int i=0; i<n; i++) {
            data[i] = (T[]) java.lang.reflect.Array.newInstance(v.getScalarRing().zero().getClass(), n);
            for(int j=0; j<n; j++) {
                data[i][j] = (i == j) ? v.get(i) : (T) v.getScalarRing().zero();
            }
        }
        return Matrix.of(data, (Ring<T>) v.getScalarRing());
    }

    private static void assertVectorClose(Vector<RealBig> actual, Vector<RealBig> expected, BigDecimal tol, String msg) {
        for (int i = 0; i < actual.dimension(); i++) {
            BigDecimal valA = actual.get(i).bigDecimalValue();
            BigDecimal valE = expected.get(i).bigDecimalValue();
            if (valA.subtract(valE).abs().compareTo(tol) > 0) throw new AssertionError(msg + " at [" + i + "]");
        }
    }
    private static void assertScalarClose(Object actual, Object expected, BigDecimal tol, String msg) {
        BigDecimal valA = (actual instanceof RealBig) ? ((RealBig)actual).bigDecimalValue() : BigDecimal.valueOf(((Number)actual).doubleValue());
        BigDecimal valE = (expected instanceof RealBig) ? ((RealBig)expected).bigDecimalValue() : BigDecimal.valueOf(((Number)expected).doubleValue());
        if (valA.subtract(valE).abs().compareTo(tol) > 0) throw new AssertionError(msg);
    }

    @SuppressWarnings("unchecked")
    private static Matrix<RealBig> createRealBigMatrix(RealBig val, int n, LinearAlgebraProvider<RealBig> p) {
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(val.bigDecimalValue().add(new BigDecimal(i + j)));
        return Matrix.of(data, (Ring<RealBig>) (Object) org.episteme.core.mathematics.numbers.real.RealBig.ZERO.getScalarRing());
    }
    private static Matrix<RealBig> createRealBigMatrix(BigDecimal val, int n, LinearAlgebraProvider<RealBig> p) {
        return createRealBigMatrix(RealBig.create(val), n, p);
    }
    @SuppressWarnings("unchecked")
    private static Matrix<RealBig> createInvertibleRealBigMatrix(int n, LinearAlgebraProvider<RealBig> p) {
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(i == j ? new BigDecimal(n + i) : new BigDecimal("0.1"));
        return Matrix.of(data, (Ring<RealBig>) (Object) org.episteme.core.mathematics.numbers.real.RealBig.ZERO.getScalarRing());
    }
    @SuppressWarnings("unchecked")
    private static Matrix<RealBig> createSPDRealBigMatrix(int n, LinearAlgebraProvider<RealBig> p) {
        RealBig[][] data = new RealBig[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = RealBig.create(new BigDecimal(i == j ? n * 3.0 : 0.1));
        return Matrix.of(data, (Ring<RealBig>) (Object) org.episteme.core.mathematics.numbers.real.RealBig.ZERO.getScalarRing());
    }
    @SuppressWarnings("unchecked")
    private static Vector<RealBig> createRealBigVector(RealBig val, int n, LinearAlgebraProvider<RealBig> p) {
        RealBig[] data = new RealBig[n];
        for (int i = 0; i < n; i++) data[i] = RealBig.create(val.bigDecimalValue().add(new BigDecimal(i)));
        return Vector.of(data, (Ring<RealBig>) (Object) org.episteme.core.mathematics.numbers.real.RealBig.ZERO.getScalarRing());
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
