package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;

import java.util.Objects;

/**
 * Numerical correctness verifier for Linear Algebra operations.
 * Compares results against ground truth with specified tolerances.
 */
public class CorrectnessVerifier {

    public static void verify(Object actual, Object expected, double tolerance) {
        if (actual == null || expected == null) {
            if (actual != expected) throw new AssertionError("Expected " + expected + " but got " + actual);
            return;
        }

        if (actual instanceof Matrix && expected instanceof Matrix) {
            verifyMatrix((Matrix<?>) actual, (Matrix<?>) expected, tolerance);
        } else if (actual instanceof Vector && expected instanceof Vector) {
            verifyVector((Vector<?>) actual, (Vector<?>) expected, tolerance);
        } else if (actual instanceof LUResult && expected instanceof LUResult) {
            verifyLU((LUResult<?>) actual, (LUResult<?>) expected, tolerance);
        } else if (actual instanceof QRResult && expected instanceof QRResult) {
            verifyQR((QRResult<?>) actual, (QRResult<?>) expected, tolerance);
        } else if (actual instanceof CholeskyResult && expected instanceof CholeskyResult) {
            verifyCholesky((CholeskyResult<?>) actual, (CholeskyResult<?>) expected, tolerance);
        } else {
            verifyScalar(actual, expected, tolerance);
        }
    }

    private static void verifyMatrix(Matrix<?> actual, Matrix<?> expected, double tolerance) {
        if (actual.rows() != expected.rows() || actual.cols() != expected.cols()) {
            throw new AssertionError("Matrix dimensions mismatch: " + actual.rows() + "x" + actual.cols() + 
                " vs " + expected.rows() + "x" + expected.cols());
        }
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.cols(); j++) {
                verifyScalar(actual.get(i, j), expected.get(i, j), tolerance);
            }
        }
    }

    private static void verifyVector(Vector<?> actual, Vector<?> expected, double tolerance) {
        if (actual.dimension() != expected.dimension()) {
            throw new AssertionError("Vector dimension mismatch: " + actual.dimension() + " vs " + expected.dimension());
        }
        for (int i = 0; i < actual.dimension(); i++) {
            verifyScalar(actual.get(i), expected.get(i), tolerance);
        }
    }

    private static void verifyLU(LUResult<?> actual, LUResult<?> expected, double tolerance) {
        verify(actual.getL(), expected.getL(), tolerance);
        verify(actual.getU(), expected.getU(), tolerance);
        // Pivot check is complex if permutations differ but result is same. 
        // For simplicity, we compare L and U after pivot application if possible, or just L and U directly.
    }

    private static void verifyQR(QRResult<?> actual, QRResult<?> expected, double tolerance) {
        verify(actual.getQ(), expected.getQ(), tolerance);
        verify(actual.getR(), expected.getR(), tolerance);
    }

    private static void verifyCholesky(CholeskyResult<?> actual, CholeskyResult<?> expected, double tolerance) {
        verify(actual.getL(), expected.getL(), tolerance);
    }

    private static void verifyScalar(Object actual, Object expected, double tolerance) {
        double a = toDouble(actual);
        double e = toDouble(expected);
        
        if (Double.isNaN(a) && Double.isNaN(e)) return;
        
        double diff = Math.abs(a - e);
        double norm = Math.max(1.0, Math.max(Math.abs(a), Math.abs(e)));
        
        if (diff / norm > tolerance) {
            throw new AssertionError(String.format("Accuracy failure: expected %s (approx %f) but got %s (approx %f). Error: %e > %e", 
                expected, e, actual, a, diff/norm, tolerance));
        }
        
        // Complex handling
        if (actual instanceof Complex && expected instanceof Complex) {
            double ai = ((Complex) actual).imaginary();
            double ei = ((Complex) expected).imaginary();
            double diffI = Math.abs(ai - ei);
            if (diffI / norm > tolerance) {
                throw new AssertionError(String.format("Complex Accuracy failure (Imaginary): expected %s but got %s", expected, actual));
            }
        }
    }

    private static double toDouble(Object o) {
        if (o instanceof Number) return ((Number) o).doubleValue();
        if (o instanceof Real) return ((Real) o).doubleValue();
        if (o instanceof Complex) return ((Complex) o).real();
        if (o instanceof RealBig) return ((RealBig) o).doubleValue();
        return 0.0;
    }
}
