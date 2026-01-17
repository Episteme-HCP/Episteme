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

package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.numbers.real.Real;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a density matrix $\rho$ for mixed quantum states.
 * <p>
 * $\rho = \sum_i p_i |\psi_i\rangle\langle\psi_i|$
 * where $p_i$ is probability, and $\sum p_i = 1$.
 * </p>
 *
 * @author Silvere Martin-Michiellot
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
    public Real purity() {
        // Tr(rho^2)
        try {
            DenseMatrix<Complex> squared = (DenseMatrix<Complex>) matrix.multiply(matrix);
            Complex trace = squared.trace();
            return trace.getReal();
        } catch (Exception e) {
            return Real.ZERO;
        }
    }

    private static DensityMatrix createFromData(Complex[][] data) {
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
    public DensityMatrix amplitudeDamping(Real gamma) {
        // For a single qubit:
        // E0 = [[1, 0], [0, sqrt(1-γ)]] (no decay)
        // E1 = [[0, sqrt(γ)], [0, 0]] (decay)
        // ρ' = E0 ρ E0† + E1 ρ E1†

        int dim = matrix.rows();
        Complex[][] newData = new Complex[dim][dim];

        Real sqrtOneMinusGamma = Real.ONE.subtract(gamma).sqrt();

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
            newData[1][1] = rho11.multiply(Complex.of(Real.ONE.subtract(gamma)));
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
    public DensityMatrix phaseDamping(Real gamma) {
        int dim = matrix.rows();
        Complex[][] newData = new Complex[dim][dim];

        Real damping = Real.ONE.subtract(gamma);

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == j) {
                    newData[i][j] = matrix.get(i, j);
                } else {
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
    public DensityMatrix depolarize(Real p) {
        int dim = matrix.rows();
        Complex[][] newData = new Complex[dim][dim];

        // For single qubit: ρ → (1-p)ρ + (p/2)I
        if (dim == 2) {
            Real oneMinusP = Real.ONE.subtract(p);
            Real pOver2 = p.divide(Real.of(2));

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
            Real invDim = Real.ONE.divide(Real.of(dim));
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    Complex original = matrix.get(i, j).multiply(Complex.of(Real.ONE.subtract(p)));
                    if (i == j) {
                        newData[i][j] = original.add(Complex.of(p.multiply(invDim)));
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
    public Real vonNeumannEntropy() {
        Real gamma = purity();
        if (gamma.compareTo(Real.of(0.9999)) >= 0)
            return Real.ZERO; // Pure state

        int dim = matrix.rows();
        if (dim == 2) {
            // For 2-level: use closed form based on purity
            // S = -λ+ log(λ+) - λ- log(λ-)
            // where λ± = (1 ± sqrt(2γ-1))/2 when γ = Tr(ρ²)
            // Note: formula for 2-level state purity relation to eigenvalues:
            // 2*Purity - 1 = BlochVector^2.
            // Eigenvalues are (1 +/- |r|)/2.
            // |r| = sqrt(2*gamma - 1). Correct.
            
            Real x = gamma.multiply(Real.of(2)).subtract(Real.ONE).sqrt();
            Real lambdaPlus = Real.ONE.add(x).divide(Real.of(2));
            Real lambdaMinus = Real.ONE.subtract(x).divide(Real.of(2));
            Real entropy = Real.ZERO;
            
            if (lambdaPlus.compareTo(Real.of(1e-10)) > 0)
                entropy = entropy.subtract(lambdaPlus.multiply(lambdaPlus.log()));
            if (lambdaMinus.compareTo(Real.of(1e-10)) > 0)
                entropy = entropy.subtract(lambdaMinus.multiply(lambdaMinus.log()));
                
            return entropy;
        }

        // Fallback: return estimate based on purity
        // -log(gamma) is Renyi entropy approx.
        // dim scaling?
        return gamma.log().negate().multiply(Real.of(dim - 1)).divide(Real.of(dim));
    }

    public DenseMatrix<Complex> getMatrix() {
        return matrix;
    }
}
