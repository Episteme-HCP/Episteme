package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Universal Audit Logic for Linear Algebra operations.
 * Ported from HighPrecisionAuditOperations to support all numeric domains.
 */
public class LinearAlgebraAuditSuite {

    public interface AuditAction {
        void run(String opName, Supplier<?> test);
    }

    public static <E> void runAudit(LinearAlgebraProvider<E> p, int n, AuditAction action, Ring<E> ring, String prefix) {
        Random rand = new Random(42);
        
        // --- Setup Data ---
        Matrix<E> A = randomMatrix(n, n, p, ring, rand);
        Matrix<E> B = randomMatrix(n, n, p, ring, rand);
        Vector<E> v = randomVector(n, p, ring, rand);
        Matrix<E> invA = randomInvertibleMatrix(n, p, ring, rand);
        Matrix<E> spdA = randomSPDMatrix(n, p, ring, rand);
        E s = ring.add(ring.one(), ring.one()); // 2.0

        // --- 1. Basic Arithmetic ---
        action.run(prefix + "Add", () -> p.add(A, B));
        action.run(prefix + "Sub", () -> p.subtract(A, B));
        action.run(prefix + "Scale", () -> p.scale(s, A));
        action.run(prefix + "Mul", () -> p.multiply(A, B));
        action.run(prefix + "MatVec", () -> p.multiply(A, v));
        action.run(prefix + "Trans", () -> p.transpose(A));
        action.run(prefix + "SubMat", () -> A.subMatrix(0, n/2, 0, n/2));

        // --- 2. Solvers & Inversion ---
        action.run(prefix + "Inv", () -> p.inverse(invA));
        action.run(prefix + "Det", () -> p.determinant(invA));
        action.run(prefix + "Solve", () -> p.solve(invA, v));
        action.run(prefix + "Trace", () -> invA.trace());

        // --- 3. Vector Operations ---
        action.run(prefix + "Dot", () -> p.dot(v, v));
        action.run(prefix + "Norm", () -> p.norm(v));
        action.run(prefix + "Normalize", () -> v.normalize());
        if (n >= 3) {
            action.run(prefix + "Cross", () -> v.cross(v));
        }

        // --- 4. Decompositions ---
        action.run(prefix + "LU", () -> p.lu(invA));
        action.run(prefix + "QR", () -> p.qr(A));
        action.run(prefix + "SVD", () -> p.svd(A)); 
        action.run(prefix + "Chol", () -> p.cholesky(spdA));
        action.run(prefix + "Eigen", () -> p.eigen(invA));

        // --- 5. Iterative Solvers ---
        if (p instanceof SparseLinearAlgebraProvider<E> sp) {
            action.run(prefix + "BiCGSTAB", () -> sp.bicgstab(invA, v, randomVector(n, p, ring, rand), ring.one(), 10));
            action.run(prefix + "ConjGrad", () -> sp.conjugateGradient(spdA, v, randomVector(n, p, ring, rand), ring.one(), 10));
            action.run(prefix + "GMRES", () -> sp.gmres(invA, v, randomVector(n, p, ring, rand), ring.one(), 10, 5));
        }

        // --- 6. Matrix Functions (Element-wise) ---
        Matrix<E> small = randomMatrix(1, 1, p, ring, rand);
        action.run(prefix + "Exp", () -> p.exp(small));
        action.run(prefix + "Log", () -> p.log(small));
        action.run(prefix + "Sin", () -> p.sin(small));
        action.run(prefix + "Cos", () -> p.cos(small));
        action.run(prefix + "Tan", () -> p.tan(small));
        action.run(prefix + "Sqrt", () -> p.sqrt(small));
    }

    private static <E> Matrix<E> randomMatrix(int rows, int cols, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), rows, cols);
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isComplex) {
                    data[i][j] = (E) Complex.of(rand.nextDouble(), rand.nextDouble());
                } else if (ring.zero() instanceof Real) {
                    data[i][j] = (E) Real.of(rand.nextDouble());
                } else if (ring.zero() instanceof RealBig) {
                    data[i][j] = (E) RealBig.create(java.math.BigDecimal.valueOf(rand.nextDouble()));
                } else {
                    data[i][j] = ring.zero();
                }
            }
        }
        return Matrix.of(data, ring);
    }

    private static <E> Matrix<E> randomInvertibleMatrix(int n, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        Matrix<E> m = randomMatrix(n, n, p, ring, rand);
        // Boost diagonal to ensure invertibility
        for (int i = 0; i < n; i++) {
            m.set(i, i, ring.add(m.get(i, i), ring.scale(10.0, ring.one())));
        }
        return m;
    }

    private static <E> Matrix<E> randomSPDMatrix(int n, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        Matrix<E> A = randomMatrix(n, n, p, ring, rand);
        Matrix<E> AT = p.transpose(A);
        Matrix<E> result = p.multiply(A, AT);
        for (int i = 0; i < n; i++) {
            result.set(i, i, ring.add(result.get(i, i), ring.scale(5.0, ring.one())));
        }
        return result;
    }

    private static <E> Vector<E> randomVector(int n, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        @SuppressWarnings("unchecked")
        E[] data = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), n);
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        for (int i = 0; i < n; i++) {
            if (isComplex) {
                data[i] = (E) Complex.of(rand.nextDouble(), rand.nextDouble());
            } else if (ring.zero() instanceof Real) {
                data[i] = (E) Real.of(rand.nextDouble());
            } else if (ring.zero() instanceof RealBig) {
                data[i] = (E) RealBig.create(java.math.BigDecimal.valueOf(rand.nextDouble()));
            } else {
                data[i] = ring.zero();
            }
        }
        return Vector.of(data, ring);
    }
}
