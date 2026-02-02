/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.natural.chemistry.computational.quantum;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.linearalgebra.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Direct Inversion in the Iterative Subspace (DIIS) accelerator.
 * Improves SCF convergence by minimizing the error vector in a subspace of previous iterations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DIISSubspace {

    private final int maxSubspaceSize;
    private final List<Matrix<Real>> fockMatrices;
    private final List<Matrix<Real>> errorVectors;

    public DIISSubspace(int maxSubspaceSize) {
        this.maxSubspaceSize = maxSubspaceSize;
        this.fockMatrices = new ArrayList<>();
        this.errorVectors = new ArrayList<>();
    }

    /**
     * Adds an iteration to the subspace.
     * @param F The Fock matrix of the current iteration.
     * @param error The error vector (usually FDS - SDF).
     */
    public void add(Matrix<Real> F, Matrix<Real> error) {
        if (fockMatrices.size() >= maxSubspaceSize) {
            fockMatrices.remove(0);
            errorVectors.remove(0);
        }
        fockMatrices.add(F);
        errorVectors.add(error);
    }

    /**
     * Extrapolates a new Fock matrix using the DIIS equations.
     * Solve B * c = r for c.
     * New F = sum(c_i * F_i).
     */
    public Matrix<Real> extrapolate() {
        int n = fockMatrices.size();
        if (n < 2) {
            return fockMatrices.get(n - 1);
        }

        // Build B matrix: (n+1) x (n+1)
        Real[][] B_data = new Real[n + 1][n + 1];
        
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                Real dot = dotProduct(errorVectors.get(i), errorVectors.get(j));
                B_data[i][j] = dot;
                B_data[j][i] = dot;
            }
            B_data[i][n] = Real.ONE.negate();
            B_data[n][i] = Real.ONE.negate();
        }
        B_data[n][n] = Real.ZERO;

        Matrix<Real> B = DenseMatrix.of(B_data, Reals.getInstance());
        
        // Build RHS vector r = (0, 0, ... -1)
        Real[][] r_data = new Real[n + 1][1];
        for (int i = 0; i < n; i++) r_data[i][0] = Real.ZERO;
        r_data[n][0] = Real.ONE.negate();
        
        Matrix<Real> r = DenseMatrix.of(r_data, Reals.getInstance());

        // Solve c = B^-1 * r
        Matrix<Real> c_vec;
        try {
            c_vec = B.inverse().multiply(r);
        } catch (Exception e) {
            System.out.println("DIIS: Singular matrix, skipping extrapolation.");
            return fockMatrices.get(n - 1);
        }

        // Construct extrapolated F = sum(c_i * F_i)
        // Use manual scaling to avoid Matrix API ambiguity
        int dim = fockMatrices.get(0).rows();
        Real[][] F_new_data = new Real[dim][dim];
        
        // Initialize with Zero
        for(int i=0; i<dim; i++) {
            for(int j=0; j<dim; j++) {
                F_new_data[i][j] = Real.ZERO;
            }
        }
        
        for (int k = 0; k < n; k++) {
            Real coeff = c_vec.get(k, 0);
            Matrix<Real> F_k = fockMatrices.get(k);
            
            for(int i=0; i<dim; i++) {
                for(int j=0; j<dim; j++) {
                    Real val = F_k.get(i, j);
                    F_new_data[i][j] = F_new_data[i][j].add(val.multiply(coeff));
                }
            }
        }
        
        return DenseMatrix.of(F_new_data, Reals.getInstance());
    }

    private Real dotProduct(Matrix<Real> A, Matrix<Real> B) {
        // Trace(A^T * B) or sum(Aij * Bij)
        // Treated as vector dot product
        int rows = A.rows();
        int cols = A.cols();
        Real sum = Real.ZERO;
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                sum = sum.add(A.get(i, j).multiply(B.get(i, j)));
            }
        }
        return sum;
    }
}
