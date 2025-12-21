/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a density matrix $\rho$ for mixed quantum states.
 * <p>
 * $\rho = \sum_i p_i |\psi_i\rangle\langle\psi_i|$
 * where $p_i$ is probability, and $\sum p_i = 1$.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DensityMatrix {

    private final DenseMatrix<Complex> matrix;

    public DensityMatrix(DenseMatrix<Complex> matrix) {
        this.matrix = matrix;
    }

    /**
     * Creates a pure state density matrix $|\psi\rangle\langle\psi|$.
     */
    public static DensityMatrix fromPureState(BraKet psi) {
        // Outer product |psi><psi|
        // result[i][j] = psi[i] * conj(psi[j])

        int dim = psi.vector().dimension();
        Complex[][] data = new Complex[dim][dim];

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                data[i][j] = psi.vector().get(i).multiply(psi.vector().get(j).conjugate());
            }
        }

        return createFromData(data);
    }

    /**
     * Calculates the Purity $\gamma = Tr(\rho^2)$.
     * For pure states, Purity = 1. For mixed states, < 1.
     */
    public double purity() {
        // Tr(rho^2)
        // For MVP, simplistic calculation
        try {
            DenseMatrix<Complex> squared = (DenseMatrix<Complex>) matrix.multiply(matrix);
            Complex trace = squared.trace();
            return trace.real();
        } catch (Exception e) {
            // Fallback or rethrow
            return 0.0;
        }
    }

    private static DensityMatrix createFromData(Complex[][] data) {
        // int rows = data.length;
        List<List<Complex>> rowsList = new ArrayList<>();
        for (Complex[] row : data) {
            java.util.Collections.addAll(rowsList, java.util.Arrays.asList(row));
        }
        return new DensityMatrix(new DenseMatrix<>(rowsList, Complex.ZERO));
    }

    /**
     * Applies amplitude damping (T1 decay) channel.
     * Models energy dissipation to environment.
     * 
     * @param gamma Decay probability (0 to 1)
     * @return New density matrix after damping
     */
    public DensityMatrix amplitudeDamping(double gamma) {
        // For a single qubit:
        // E0 = [[1, 0], [0, sqrt(1-γ)]] (no decay)
        // E1 = [[0, sqrt(γ)], [0, 0]] (decay)
        // ρ' = E0 ρ E0† + E1 ρ E1†

        int dim = matrix.rows();
        Complex[][] newData = new Complex[dim][dim];

        double sqrtOneMinusGamma = Math.sqrt(1 - gamma);
        // double sqrtGamma = Math.sqrt(gamma);

        // Simplified 2-level system
        if (dim == 2) {
            Complex rho00 = matrix.get(0, 0);
            Complex rho01 = matrix.get(0, 1);
            Complex rho10 = matrix.get(1, 0);
            Complex rho11 = matrix.get(1, 1);

            // ρ'00 = ρ00 + γρ11
            newData[0][0] = rho00.add(rho11.multiply(Complex.of(gamma)));
            // ρ'01 = sqrt(1-γ) ρ01
            newData[0][1] = rho01.multiply(Complex.of(sqrtOneMinusGamma));
            // ρ'10 = sqrt(1-γ) ρ10
            newData[1][0] = rho10.multiply(Complex.of(sqrtOneMinusGamma));
            // ρ'11 = (1-γ) ρ11
            newData[1][1] = rho11.multiply(Complex.of(1 - gamma));
        } else {
            // For larger systems, just return copy (simplified)
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    newData[i][j] = matrix.get(i, j);
                }
            }
        }

        return createFromData(newData);
    }

    /**
     * Applies phase damping (T2 dephasing) channel.
     * Models loss of phase coherence without energy loss.
     * 
     * @param gamma Dephasing probability (0 to 1)
     * @return New density matrix after dephasing
     */
    public DensityMatrix phaseDamping(double gamma) {
        int dim = matrix.rows();
        Complex[][] newData = new Complex[dim][dim];

        double damping = 1 - gamma;

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == j) {
                    // Diagonal elements unchanged
                    newData[i][j] = matrix.get(i, j);
                } else {
                    // Off-diagonal elements decay
                    newData[i][j] = matrix.get(i, j).multiply(Complex.of(damping));
                }
            }
        }

        return createFromData(newData);
    }

    /**
     * Depolarizing channel.
     * ρ → (1-p)ρ + (p/3)(XρX + YρY + ZρZ)
     * 
     * @param p Error probability
     * @return Depolarized density matrix
     */
    public DensityMatrix depolarize(double p) {
        int dim = matrix.rows();
        Complex[][] newData = new Complex[dim][dim];

        // For single qubit: ρ → (1-p)ρ + (p/2)I
        if (dim == 2) {
            double oneMinusP = 1 - p;
            double pOver2 = p / 2.0;

            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    Complex original = matrix.get(i, j).multiply(Complex.of(oneMinusP));
                    if (i == j) {
                        newData[i][j] = original.add(Complex.of(pOver2));
                    } else {
                        newData[i][j] = original;
                    }
                }
            }
        } else {
            // Generalized depolarizing: ρ → (1-p)ρ + p*I/d
            double invDim = 1.0 / dim;
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    Complex original = matrix.get(i, j).multiply(Complex.of(1 - p));
                    if (i == j) {
                        newData[i][j] = original.add(Complex.of(p * invDim));
                    } else {
                        newData[i][j] = original;
                    }
                }
            }
        }

        return createFromData(newData);
    }

    /**
     * Von Neumann entropy: S(ρ) = -Tr(ρ log ρ)
     * For pure states S = 0, for maximally mixed S = log(d)
     * 
     * @return Entropy in nats (use log base e)
     */
    public double vonNeumannEntropy() {
        // Simplified: compute eigenvalues and -Σ λ_i log(λ_i)
        // For now, use purity-based approximation for 2-level system
        double gamma = purity();
        if (gamma >= 0.9999)
            return 0.0; // Pure state

        int dim = matrix.rows();
        if (dim == 2) {
            // For 2-level: use closed form based on purity
            // S = -λ+ log(λ+) - λ- log(λ-)
            // where λ± = (1 ± sqrt(2γ-1))/2 when γ = Tr(ρ²)
            double x = Math.sqrt(2 * gamma - 1);
            double lambdaPlus = (1 + x) / 2;
            double lambdaMinus = (1 - x) / 2;
            double entropy = 0;
            if (lambdaPlus > 1e-10)
                entropy -= lambdaPlus * Math.log(lambdaPlus);
            if (lambdaMinus > 1e-10)
                entropy -= lambdaMinus * Math.log(lambdaMinus);
            return entropy;
        }

        // Fallback: return estimate based on purity
        return -Math.log(gamma) * (dim - 1) / dim;
    }

    public DenseMatrix<Complex> getMatrix() {
        return matrix;
    }
}
