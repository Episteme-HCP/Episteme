/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.economics.models;

import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
/**
 * Implementation of the Leontief Input-Output model using high-performance backends.
 * <p>
 * This model calculates the total output required to satisfy a given final demand,
 * taking into account inter-industry dependencies.
 * Formula: X = (I - A)^-1 * d
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class LeontiefModel {

    public LeontiefModel() {
    }

    /**
     * Solves the Leontief system for a given technical coefficient matrix and final demand.
     * 
     * @param A the technical coefficient matrix (n x n)
     * @param d the final demand vector (n x 1)
     * @return the total output vector (n x 1)
     */
    public RealDoubleMatrix solve(RealDoubleMatrix A, RealDoubleMatrix d) {
        int n = A.rows();
        if (A.cols() != n || d.rows() != n || d.cols() != 1) {
            throw new IllegalArgumentException("Matrix dimensions do not match Leontief requirements");
        }

        // I - A
        RealDoubleMatrix I_minus_A = RealDoubleMatrix.direct(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double val = (i == j ? 1.0 : 0.0) - A.toDoubleArray()[i * n + j]; // Simplified for demonstration
                I_minus_A.set(i, j, val);
            }
        }

        // X = d + Ad + A^2d + ... (valid if spectral radius of A < 1)
        RealDoubleMatrix X = RealDoubleMatrix.of(d.toDoubleArray(), n, 1);
        RealDoubleMatrix currentTerm = RealDoubleMatrix.of(d.toDoubleArray(), n, 1);

        // 50 iterations for convergence
        for (int k = 0; k < 50; k++) {
            currentTerm = A.multiply(currentTerm);
            X = (RealDoubleMatrix) X.add(currentTerm);
        }

        return X;
    }
}

