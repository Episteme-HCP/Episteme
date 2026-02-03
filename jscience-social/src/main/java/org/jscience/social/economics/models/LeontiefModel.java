/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.economics.models;

import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
/**
 * Implementation of the Leontief Input-Output model.
 * <p>
 * This model calculates the total output required to satisfy a given final demand,
 * taking into account inter-industry dependencies. Developed by Wassily Leontief
 * (Nobel Prize in Economics, 1973).
 * </p>
 * <p>
 * The model is defined by the fundamental equation:
 * <pre>
 * X = (I - A)^-1 * d
 * </pre>
 * where:
 * <ul>
 *   <li><b>X</b>: Total output vector (n x 1)</li>
 *   <li><b>A</b>: Technical coefficient matrix (n x n), where a<sub>ij</sub> 
 *       represents the input from industry i required to produce one unit of industry j</li>
 *   <li><b>d</b>: Final demand vector (n x 1)</li>
 * </ul>
 * </p>
 * <p>
 * This implementation uses a power series approximation (Neumann series) 
 * which converges if the spectral radius of A is less than 1.
 * </p>
 * 
 * @see <a href="https://doi.org/10.2307/1927392">Leontief, W. W. (1936). Quantitative Input and Output Relations in the Economic System of the United States.</a>
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

