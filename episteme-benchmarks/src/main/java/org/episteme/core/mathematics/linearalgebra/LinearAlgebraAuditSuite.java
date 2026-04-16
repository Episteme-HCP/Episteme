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
 * Covers Square, Rectangular, and Triangular matrices across 68+ operations.
 */
public class LinearAlgebraAuditSuite {

    public interface AuditAction {
        void run(String opName, Supplier<?> test);
    }

    public static <E> void runFullAudit(LinearAlgebraProvider<E> p, int n, AuditAction action, Ring<E> ring, String prefix) {
        Random rand = new Random(42);
        
        // --- 1. SQUARE MATRIX OPERATIONS (N x N) ---
        Matrix<E> A = randomMatrix(n, n, p, ring, rand);
        Matrix<E> B = randomMatrix(n, n, p, ring, rand);
        Vector<E> v = randomVector(n, p, ring, rand);
        Matrix<E> invA = randomInvertibleMatrix(n, ring, rand);
        Matrix<E> spdA = randomSPDMatrix(n, p, ring, rand);
        E scalar = ring.add(ring.one(), ring.one()); // 2.0

        action.run(prefix + "Add", () -> p.add(A, B));
        action.run(prefix + "Sub", () -> p.subtract(A, B));
        action.run(prefix + "Scale", () -> p.scale(scalar, A));
        action.run(prefix + "Mul", () -> p.multiply(A, B));
        action.run(prefix + "MatVec", () -> p.multiply(A, v));
        action.run(prefix + "Trans", () -> p.transpose(A));
        action.run(prefix + "Inv", () -> p.inverse(invA));
        action.run(prefix + "Det", () -> p.determinant(invA));
        action.run(prefix + "Solve", () -> p.solve(invA, v));
        action.run(prefix + "Trace", () -> invA.trace());

        // Decompositions
        action.run(prefix + "LU", () -> p.lu(invA));
        action.run(prefix + "QR", () -> p.qr(A));
        action.run(prefix + "SVD", () -> p.svd(A)); 
        action.run(prefix + "Chol", () -> p.cholesky(spdA));
        action.run(prefix + "Eigen", () -> p.eigen(invA));

        // --- 2. RECTANGULAR MATRIX OPERATIONS (M x N) ---
        int m = n + 2;
        int k = n - 1; if (k < 1) k = 1;
        Matrix<E> R1 = randomMatrix(m, n, p, ring, rand);
        Matrix<E> R2 = randomMatrix(n, k, p, ring, rand);
        Vector<E> vN = randomVector(n, p, ring, rand);

        action.run(prefix + "Rect:Mul", () -> p.multiply(R1, R2));
        action.run(prefix + "Rect:Trans", () -> p.transpose(R1));
        action.run(prefix + "Rect:MatVec", () -> p.multiply(R1, vN));
        action.run(prefix + "Rect:SVD", () -> p.svd(R1));
        action.run(prefix + "Rect:QR", () -> p.qr(R1));

        // --- 3. TRIANGULAR MATRIX OPERATIONS ---
        Matrix<E> L = createTriangular(n, false, ring, rand);
        Matrix<E> U = createTriangular(n, true, ring, rand);
        
        LUResult<E> lu = p.lu(invA);
        action.run(prefix + "Tri:LowerSolve", () -> p.solve(lu.getL(), v));
        action.run(prefix + "Tri:UpperSolve", () -> p.solve(lu.getU(), v));
        action.run(prefix + "Tri:Mul", () -> p.multiply(L, U));

        // --- 4. VECTOR GEOMETRY & OPERATIONS ---
        action.run(prefix + "Vec:Dot", () -> p.dot(v, v));
        action.run(prefix + "Vec:Norm", () -> p.norm(v));
        action.run(prefix + "Vec:Normalize", () -> p.normalize(v));
        
        // Specific 3D test for Cross Product
        Vector<E> v3a = randomVector(3, p, ring, rand);
        Vector<E> v3b = randomVector(3, p, ring, rand);
        action.run(prefix + "Vec:Cross", () -> p.cross(v3a, v3b));
        
        action.run(prefix + "Vec:Angle", () -> p.angle(v, v));
        action.run(prefix + "Vec:Proj", () -> p.projection(v, v));

        // --- 5. MATRIX FUNCTIONS & TRANSCENDENTALS ---
        Matrix<E> single = randomMatrix(1, 1, p, ring, rand);
        action.run(prefix + "Func:Exp", () -> p.exp(single));
        action.run(prefix + "Func:Log", () -> p.log(single));
        action.run(prefix + "Func:Sin", () -> p.sin(single));
        action.run(prefix + "Func:Cos", () -> p.cos(single));
        action.run(prefix + "Func:Tan", () -> p.tan(single));
        action.run(prefix + "Func:Sinh", () -> p.sinh(single));
        action.run(prefix + "Func:Cosh", () -> p.cosh(single));
        action.run(prefix + "Func:Tanh", () -> p.tanh(single));
        action.run(prefix + "Func:Sqrt", () -> p.sqrt(single));
        action.run(prefix + "Func:Cbrt", () -> p.cbrt(single));
        action.run(prefix + "Func:Pow", () -> p.pow(single, ring.one()));

        // --- 6. SPARSE ITERATIVE (If applicable) ---
        if (p instanceof SparseLinearAlgebraProvider<E> sp) {
            Vector<E> x0 = randomVector(n, p, ring, rand);
            action.run(prefix + "Sparse:BiCGSTAB", () -> sp.bicgstab(invA, v, x0, ring.one(), 5));
            action.run(prefix + "Sparse:ConjGrad", () -> sp.conjugateGradient(spdA, v, x0, ring.one(), 5));
            action.run(prefix + "Sparse:GMRES", () -> sp.gmres(invA, v, x0, ring.one(), 5, 2));
        }
    }

    private static <E> Matrix<E> randomMatrix(int rows, int cols, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(ring.one().getClass(), rows, cols);
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = randomElement(ring, rand, isComplex);
            }
        }
        return Matrix.of(data, ring);
    }

    private static <E> E randomElement(Ring<E> ring, Random rand, boolean isComplex) {
        if (isComplex) return (E) Complex.of(rand.nextDouble(), rand.nextDouble());
        if (ring.one() instanceof Real) return (E) Real.of(rand.nextDouble());
        if (ring.one() instanceof RealBig) return (E) RealBig.create(java.math.BigDecimal.valueOf(rand.nextDouble()));
        return ring.one();
    }

    private static <E> Matrix<E> createTriangular(int n, boolean upper, Ring<E> ring, Random rand) {
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(ring.one().getClass(), n, n);
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((upper && i <= j) || (!upper && i >= j)) {
                    data[i][j] = randomElement(ring, rand, isComplex);
                } else {
                    data[i][j] = ring.zero();
                }
            }
        }
        return Matrix.of(data, ring);
    }

    private static <E> Matrix<E> randomInvertibleMatrix(int n, Ring<E> ring, Random rand) {
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(ring.one().getClass(), n, n);
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        E large = ring.add(ring.one(), ring.one()); // 2.0
        for (int i = 0; i < 3; i++) large = ring.add(large, large); // 16.0
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = i == j ? large : randomElement(ring, rand, isComplex);
            }
        }
        return Matrix.of(data, ring);
    }

    private static <E> Matrix<E> randomSPDMatrix(int n, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        Matrix<E> A = randomMatrix(n, n, p, ring, rand);
        Matrix<E> At = p.transpose(A);
        Matrix<E> res = p.multiply(A, At);
        // Diagonal boost to ensure SPD
        E boost = ring.add(ring.one(), ring.one());
        for (int i = 0; i < 4; i++) boost = ring.add(boost, boost); // 32.0
        
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) java.lang.reflect.Array.newInstance(ring.one().getClass(), n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                data[i][j] = i == j ? ring.add(res.get(i, j), boost) : res.get(i, j);
            }
        }
        return Matrix.of(data, ring);
    }

    private static <E> Vector<E> randomVector(int n, LinearAlgebraProvider<E> p, Ring<E> ring, Random rand) {
        @SuppressWarnings("unchecked")
        E[] data = (E[]) java.lang.reflect.Array.newInstance(ring.one().getClass(), n);
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        for (int i = 0; i < n; i++) {
            data[i] = randomElement(ring, rand, isComplex);
        }
        return Vector.of(data, ring);
    }
}
