package org.episteme.benchmarks.audit.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.GenericVector;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.OperationContext;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Universal Audit Logic for Linear Algebra operations.
 * Covers Square, Rectangular, and Triangular matrices across 68+ operations.
 * Includes Ground Truth verification.
 */
public class LinearAlgebraAuditSuite {

    public interface AuditAction {
        void run(String opName, Supplier<?> test);
    }

    public static <E> void runFullAudit(LinearAlgebraProvider<E> p, LinearAlgebraProvider<E> ref, int n, AuditAction action, Ring<E> ring, String prefix, double tolerance) {
        System.out.println("[LinearAlgebraAudit] Running full audit for " + p.getName() + " (Domain: " + prefix + ")...");
        try {
            Random rand = new Random(42);
        
        // --- 1. SQUARE MATRIX OPERATIONS (N x N) ---
        System.out.println("[LinearAlgebraAudit] Generating random matrix A...");
        Matrix<E> A = randomMatrix(n, n, ref, ring, rand);
        System.out.println("[LinearAlgebraAudit] Generating random matrix B...");
        Matrix<E> B = randomMatrix(n, n, ref, ring, rand);
        System.out.println("[LinearAlgebraAudit] Generating random vector v...");
        Vector<E> v = randomVector(n, ref, ring, rand);
        System.out.println("[LinearAlgebraAudit] Generating random invertible matrix invA...");
        Matrix<E> invA = randomInvertibleMatrix(n, ring, rand); 
        System.out.println("[LinearAlgebraAudit] Generating random SPD matrix spdA...");
        Matrix<E> spdA = randomSPDMatrix(n, ref, ring, rand);
        E scalar = ring.add(ring.one(), ring.one()); // 2.0
        System.out.println("[LinearAlgebraAudit] Starting operations...");

        action.run(prefix + "Add", () -> verify(p.add(A, B), ref.add(A, B), tolerance));
        action.run(prefix + "Sub", () -> verify(p.subtract(A, B), ref.subtract(A, B), tolerance));
        action.run(prefix + "Scale", () -> verify(p.scale(scalar, A), ref.scale(scalar, A), tolerance));
        action.run(prefix + "Mul", () -> verify(p.multiply(A, B), ref.multiply(A, B), tolerance));
        action.run(prefix + "MatVec", () -> verify(p.multiply(A, v), ref.multiply(A, v), tolerance));
        action.run(prefix + "Trans", () -> verify(p.transpose(A), ref.transpose(A), tolerance));
        action.run(prefix + "Inv", () -> verify(p.inverse(invA), ref.inverse(invA), tolerance));
        action.run(prefix + "Det", () -> verify(p.determinant(invA), ref.determinant(invA), tolerance));
        action.run(prefix + "Solve", () -> verify(p.solve(invA, v), ref.solve(invA, v), tolerance));
        action.run(prefix + "Trace", () -> verify(p.trace(A), ref.trace(A), tolerance));

        // Decompositions
        action.run(prefix + "LU", () -> verify(p.lu(invA), ref.lu(invA), tolerance));
        action.run(prefix + "QR", () -> verify(p.qr(A), ref.qr(A), tolerance));
        action.run(prefix + "SVD", () -> verify(p.svd(A), ref.svd(A), tolerance)); 
        action.run(prefix + "Chol", () -> verify(p.cholesky(spdA), ref.cholesky(spdA), tolerance));
        action.run(prefix + "Eigen", () -> verify(p.eigen(invA), ref.eigen(invA), tolerance));

        // --- 2. RECTANGULAR MATRIX OPERATIONS (M x N) ---
        int m = n + 2;
        int k = n - 1; if (k < 1) k = 1;
        Matrix<E> R1 = randomMatrix(m, n, ref, ring, rand);
        Matrix<E> R2 = randomMatrix(n, k, ref, ring, rand);
        Vector<E> vN = randomVector(n, ref, ring, rand);

        action.run(prefix + "Rect:Mul", () -> verify(p.multiply(R1, R2), ref.multiply(R1, R2), tolerance));
        action.run(prefix + "Rect:Trans", () -> verify(p.transpose(R1), ref.transpose(R1), tolerance));
        action.run(prefix + "Rect:MatVec", () -> verify(p.multiply(R1, vN), ref.multiply(R1, vN), tolerance));
        action.run(prefix + "Rect:SVD", () -> verify(p.svd(R1), ref.svd(R1), tolerance));
        action.run(prefix + "Rect:QR", () -> verify(p.qr(R1), ref.qr(R1), tolerance));

        // --- 3. TRIANGULAR MATRIX OPERATIONS ---
        LUResult<E> luRef = ref.lu(invA);
        Matrix<E> L = luRef.getL();
        Matrix<E> U = luRef.getU();
        
        action.run(prefix + "Tri:LowerSolve", () -> verify(p.solve(L, v), ref.solve(L, v), tolerance));
        action.run(prefix + "Tri:UpperSolve", () -> verify(p.solve(U, v), ref.solve(U, v), tolerance));
        action.run(prefix + "Tri:Mul", () -> verify(p.multiply(L, U), ref.multiply(L, U), tolerance));

        // --- 4. VECTOR GEOMETRY & OPERATIONS ---
        action.run(prefix + "Vec:Dot", () -> verify(p.dot(v, v), ref.dot(v, v), tolerance));
        action.run(prefix + "Vec:Norm", () -> verify(p.norm(v), ref.norm(v), tolerance));
        action.run(prefix + "Vec:Normalize", () -> verify(p.normalize(v), ref.normalize(v), tolerance));
        
        Vector<E> v3a = randomVector(3, ref, ring, rand);
        Vector<E> v3b = randomVector(3, ref, ring, rand);
        action.run(prefix + "Vec:Cross", () -> verify(p.cross(v3a, v3b), ref.cross(v3a, v3b), tolerance));
        
        action.run(prefix + "Vec:Angle", () -> verify(p.angle(v, v), ref.angle(v, v), tolerance));
        action.run(prefix + "Vec:Proj", () -> verify(p.projection(v, v), ref.projection(v, v), tolerance));

        // --- 5. MATRIX FUNCTIONS & TRANSCENDENTALS ---
        Matrix<E> single = randomMatrix(1, 1, ref, ring, rand);
        action.run(prefix + "Func:Exp", () -> verify(p.exp(single), ref.exp(single), tolerance));
        action.run(prefix + "Func:Log", () -> verify(p.log(single), ref.log(single), tolerance));
        action.run(prefix + "Func:Sin", () -> verify(p.sin(single), ref.sin(single), tolerance));
        action.run(prefix + "Func:Cos", () -> verify(p.cos(single), ref.cos(single), tolerance));
        action.run(prefix + "Func:Tan", () -> verify(p.tan(single), ref.tan(single), tolerance));
        action.run(prefix + "Func:Sinh", () -> verify(p.sinh(single), ref.sinh(single), tolerance));
        action.run(prefix + "Func:Cosh", () -> verify(p.cosh(single), ref.cosh(single), tolerance));
        action.run(prefix + "Func:Tanh", () -> verify(p.tanh(single), ref.tanh(single), tolerance));
        action.run(prefix + "Func:Sqrt", () -> verify(p.sqrt(single), ref.sqrt(single), tolerance));
        action.run(prefix + "Func:Cbrt", () -> verify(p.cbrt(single), ref.cbrt(single), tolerance));
        action.run(prefix + "Func:Pow", () -> verify(p.pow(single, ring.one()), ref.pow(single, ring.one()), tolerance));

        // --- 6. SPARSE ITERATIVE ---
        if (p instanceof SparseLinearAlgebraProvider<E> sp) {
            Vector<E> x0 = randomVector(n, ref, ring, rand);
            E solveTolerance = getSmallTolerance(ring);
            action.run(prefix + "Sparse:BiCGSTAB", () -> verify(sp.bicgstab(invA, v, x0, solveTolerance, 100), ref.solve(invA, v), tolerance * 1000)); 
            action.run(prefix + "Sparse:ConjGrad", () -> verify(sp.conjugateGradient(spdA, v, x0, solveTolerance, 100), ref.solve(spdA, v), tolerance * 1000));
            action.run(prefix + "Sparse:GMRES", () -> verify(sp.gmres(invA, v, x0, solveTolerance, 100, 2), ref.solve(invA, v), tolerance * 1000));
        }
        } catch (Throwable t) {
            System.err.println("[LinearAlgebraAudit] CRITICAL FAILURE during audit of " + p.getName() + " (" + prefix + "): " + t.toString());
            t.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static <E> E getSmallTolerance(Ring<E> ring) {
        E zero = ring.zero();
        if (zero instanceof Complex) {
            Real rVal;
            if (((Complex) zero).getReal() instanceof RealBig) {
                rVal = RealBig.create(new java.math.BigDecimal("1e-20"));
            } else {
                rVal = Real.of(1e-10);
            }
            return (E) Complex.of(rVal);
        }
        if (zero instanceof RealBig) {
            return (E) RealBig.create(new java.math.BigDecimal("1e-20"));
        }
        if (zero instanceof Real) {
            return (E) Real.of(1e-10);
        }
        return zero;
    }

    private static Object verify(Object actual, Object expected, double tolerance) {
        CorrectnessVerifier.verify(actual, expected, tolerance);
        return actual;
    }

    private static <E> Matrix<E> randomMatrix(int rows, int cols, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(rows, cols, ring, 1.0);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                storage.set(i, j, randomElement(ring.zero(), rand));
            }
        }
        return new GenericMatrix<>(storage, p, ring);
    }

    @SuppressWarnings("unchecked")
    private static <E> E randomElement(E zero, java.util.Random rand) {
        if (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
            Real rVal, iVal;
            if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
                java.math.BigInteger rUnscaled = new java.math.BigInteger(128, rand);
                java.math.BigInteger iUnscaled = new java.math.BigInteger(128, rand);
                rVal = RealBig.create(new java.math.BigDecimal(rUnscaled, 40));
                iVal = RealBig.create(new java.math.BigDecimal(iUnscaled, 40));
            } else {
                rVal = Real.of(rand.nextDouble());
                iVal = Real.of(rand.nextDouble());
            }
            return (E) org.episteme.core.mathematics.numbers.complex.Complex.of(rVal, iVal);
        }
        
        if (zero instanceof RealBig) {
            java.math.BigInteger unscaled = new java.math.BigInteger(128, rand);
            return (E) RealBig.create(new java.math.BigDecimal(unscaled, 40));
        }
        if (zero instanceof Real) return (E) Real.of(rand.nextDouble());
        return zero;
    }

    @SuppressWarnings("unchecked")

    private static <E> Matrix<E> randomInvertibleMatrix(int n, Ring<E> ring, Random rand) {
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(n, n, ring, 1.0);
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        E large;
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
            E casted = (E) org.episteme.core.mathematics.numbers.real.RealFloat.of(16.0f);
            large = casted;
        } else if (ring.zero() instanceof RealBig) {
            E casted = (E) RealBig.create(new java.math.BigDecimal("16.0"));
            large = casted;
        } else if (isComplex) {
            Real rVal;
            if (ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex c && c.getReal() instanceof RealBig) {
                rVal = RealBig.create(new java.math.BigDecimal("16.0"));
            } else {
                rVal = Real.of(16.0);
            }
            E casted = (E) Complex.of(rVal);
            large = casted;
        } else {
            E casted = (E) Real.of(16.0);
            large = casted;
        }
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                storage.set(i, j, i == j ? large : randomElement(ring.zero(), rand));
            }
        }
        LinearAlgebraProvider<E> p = AlgorithmManager.getRegistry().selectLinearAlgebraProvider(OperationContext.DEFAULT, ring);
        return new GenericMatrix<>(storage, p, ring);
    }

    private static <E> Matrix<E> randomSPDMatrix(int n, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        Matrix<E> A = randomMatrix(n, n, p, ring, rand);
        Matrix<E> At = adjoint(A, p, ring);
        Matrix<E> res = p.multiply(A, At);
        // Diagonal boost to ensure SPD
        E boost = ring.add(ring.one(), ring.one());
        for (int i = 0; i < 4; i++) boost = ring.add(boost, boost); // 32.0
        
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(n, n, ring, 1.0);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                storage.set(i, j, i == j ? ring.add(res.get(i, j), boost) : res.get(i, j));
            }
        }
        return new GenericMatrix<>(storage, p, ring);
    }

    private static <E> Vector<E> randomVector(int n, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        VectorStorage<E> storage = AlgorithmManager.getRegistry().createVectorStorage(n, ring, 1.0);
        for (int i = 0; i < n; i++) {
            storage.set(i, randomElement(ring.zero(), rand));
        }
        return new GenericVector<>(storage, p, ring);
    }

    private static <E> Matrix<E> adjoint(Matrix<E> A, LinearAlgebraProvider<E> p, Ring<E> ring) {
        Matrix<E> T = p.transpose(A);
        if (!(ring instanceof org.episteme.core.mathematics.sets.Complexes)) return T;
        
        MatrixStorage<E> storage = AlgorithmManager.getRegistry().createStorage(T.rows(), T.cols(), ring, 1.0);
        for (int i = 0; i < T.rows(); i++) {
            for (int j = 0; j < T.cols(); j++) {
                E val = T.get(i, j);
                if (val instanceof Complex c) {
                    @SuppressWarnings("unchecked")
                    E conj = (E) c.conjugate();
                    storage.set(i, j, conj);
                } else {
                    storage.set(i, j, val);
                }
            }
        }
        return new GenericMatrix<>(storage, p, ring);
    }


}
