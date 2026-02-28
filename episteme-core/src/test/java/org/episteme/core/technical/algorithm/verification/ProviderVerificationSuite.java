/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm.verification;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;

import java.util.Random;

/**
 * Utility for verifying mathematical correctness of algorithm providers.
 * Compares target provider results against a known high-precision reference.
 */
public class ProviderVerificationSuite {

    public static class AuditResult {
        public final boolean passed;
        public final double maxError;
        public final String details;

        public AuditResult(boolean passed, double maxError, String details) {
            this.passed = passed;
            this.maxError = maxError;
            this.details = details;
        }
    }

    /**
     * Verifies Matrix Multiplication: C = A * B
     */
    public static AuditResult verifyMultiply(LinearAlgebraProvider<Real> target, int size, double epsilon) {
        Random rnd = new Random(42);
        double[][] dataA = generateRandom(size, size, rnd);
        double[][] dataB = generateRandom(size, size, rnd);

        Matrix<Real> A = RealDoubleMatrix.of(dataA);
        Matrix<Real> B = RealDoubleMatrix.of(dataB);

        // 1. Reference result (using target's own multiply if it's the reference, 
        // or a known good one. Here we assume we compare against the standard Java impl)
        // For simple multiply, we can compute it manually with high precision if needed.
        double[][] expected = referenceMultiply(dataA, dataB);

        // 2. Target result
        Matrix<Real> C = target.multiply(A, B);

        // 3. Compare
        double maxErr = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double actual = C.get(i, j).doubleValue();
                maxErr = Math.max(maxErr, Math.abs(actual - expected[i][j]));
            }
        }

        boolean passed = maxErr <= epsilon;
        return new AuditResult(passed, maxErr, target.getName() + " Multiply " + size + "x" + size);
    }

    /**
     * Verifies Linear System Solving: A * X = B => solve(A, B) = X
     */
    public static AuditResult verifySolve(LinearAlgebraProvider<Real> target, int size, double epsilon) {
        Random rnd = new Random(42);
        // Generate a well-conditioned matrix if possible, or just random
        double[][] dataA = generateRandom(size, size, rnd);
        // Make diagonal dominant to ensure invertibility for simple test
        for (int i = 0; i < size; i++) dataA[i][i] += size;

        double[][] dataB = generateRandom(size, 1, rnd);

        Matrix<Real> A = RealDoubleMatrix.of(dataA);
        double[] flatB = new double[size];
        for(int i=0; i<size; i++) flatB[i] = dataB[i][0];
        Vector<Real> V_B = RealDoubleVector.of(flatB);
        Vector<Real> X = target.solve(A, V_B);

        // 2. Verification: Check if A * X approx B
        Vector<Real> B_check = target.multiply(A, X);

        double maxErr = 0;
        for (int i = 0; i < size; i++) {
            double actual = B_check.get(i).doubleValue();
            maxErr = Math.max(maxErr, Math.abs(actual - dataB[i][0]));
        }

        boolean passed = maxErr <= epsilon;
        return new AuditResult(passed, maxErr, target.getName() + " Solve " + size + "x" + size);
    }

    private static double[][] generateRandom(int rows, int cols, Random rnd) {
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = rnd.nextDouble();
            }
        }
        return data;
    }

    private static double[][] referenceMultiply(double[][] A, double[][] B) {
        int m = A.length;
        int k = A[0].length;
        int n = B[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int l = 0; l < k; l++) {
                    sum += A[i][l] * B[l][j];
                }
                C[i][j] = sum;
            }
        }
        return C;
    }
}
