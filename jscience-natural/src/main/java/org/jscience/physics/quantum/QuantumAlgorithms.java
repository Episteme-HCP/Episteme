/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import java.util.Arrays;

/**
 * Quantum algorithms (Grover's search, Shor's period finding).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantumAlgorithms {

    /**
     * Creates a Grover oracle for a specific marked state.
     * Oracle flips the sign of the marked state: O|xÃ¢Å¸Â© = -|xÃ¢Å¸Â© if x = marked, else
     * |xÃ¢Å¸Â©
     * 
     * @param numQubits   Number of qubits
     * @param markedState Index of the state to search for
     * @return Oracle gate
     */
    public static QuantumGate groverOracle(int numQubits, int markedState) {
        int dim = 1 << numQubits; // 2^n
        Complex[][] data = new Complex[dim][dim];

        // Initialize as identity
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                data[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }

        // Flip sign of marked state
        data[markedState][markedState] = Complex.ONE.negate();

        return createGate(data);
    }

    /**
     * Creates the Grover diffusion operator.
     * D = 2|sÃ¢Å¸Â©Ã¢Å¸Â¨s| - I where |sÃ¢Å¸Â© is the uniform superposition.
     * 
     * @param numQubits Number of qubits
     * @return Diffusion gate
     */
    public static QuantumGate groverDiffusion(int numQubits) {
        int dim = 1 << numQubits;
        Complex[][] data = new Complex[dim][dim];

        // 2/N - ÃŽÂ´_ij
        Complex twoOverN = Complex.of(2.0 / dim);

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == j) {
                    data[i][j] = twoOverN.subtract(Complex.ONE);
                } else {
                    data[i][j] = twoOverN;
                }
            }
        }

        return createGate(data);
    }

    /**
     * Optimal number of Grover iterations.
     * r Ã¢â€°Ë† (Ãâ‚¬/4) * sqrt(N)
     * 
     * @param numQubits Number of qubits (N = 2^n)
     * @return Optimal iteration count
     */
    public static int optimalGroverIterations(int numQubits) {
        int N = 1 << numQubits;
        return (int) Math.round(Math.PI / 4.0 * Math.sqrt(N));
    }

    /**
     * Runs Grover's search algorithm.
     * 
     * @param numQubits   Number of qubits
     * @param markedState State to search for
     * @return Final quantum state (should have high amplitude at markedState)
     */
    public static BraKet groverSearch(int numQubits, int markedState) {
        int dim = 1 << numQubits;

        // Start in uniform superposition |sÃ¢Å¸Â© = H^Ã¢Å â€”n |0Ã¢Å¸Â©^Ã¢Å â€”n
        Complex amp = Complex.of(1.0 / Math.sqrt(dim));
        Complex[] state = new Complex[dim];
        for (int i = 0; i < dim; i++) {
            state[i] = amp;
        }
        BraKet psi = new BraKet(state);

        // Create oracle and diffusion
        QuantumGate oracle = groverOracle(numQubits, markedState);
        QuantumGate diffusion = groverDiffusion(numQubits);

        // Apply Grover iterations
        int iterations = optimalGroverIterations(numQubits);
        for (int i = 0; i < iterations; i++) {
            psi = oracle.apply(psi);
            psi = diffusion.apply(psi);
        }

        return psi;
    }

    /**
     * Quantum Fourier Transform gate for n qubits.
     * QFT|jÃ¢Å¸Â© = (1/sqrt(N)) ÃŽÂ£_k exp(2Ãâ‚¬ijk/N)|kÃ¢Å¸Â©
     * 
     * @param numQubits Number of qubits
     * @return QFT gate
     */
    public static QuantumGate qft(int numQubits) {
        int N = 1 << numQubits;
        Complex[][] data = new Complex[N][N];

        Complex omega = Complex.of(Math.cos(2 * Math.PI / N), Math.sin(2 * Math.PI / N));
        Complex scale = Complex.of(1.0 / Math.sqrt(N));

        for (int j = 0; j < N; j++) {
            for (int k = 0; k < N; k++) {
                // Ãâ€°^(jk)
                int exponent = (j * k) % N;
                Complex entry = complexPow(omega, exponent).multiply(scale);
                data[j][k] = entry;
            }
        }

        return createGate(data);
    }

    /**
     * Inverse QFT.
     */
    public static QuantumGate inverseQft(int numQubits) {
        int N = 1 << numQubits;
        Complex[][] data = new Complex[N][N];

        Complex omega = Complex.of(Math.cos(-2 * Math.PI / N), Math.sin(-2 * Math.PI / N));
        Complex scale = Complex.of(1.0 / Math.sqrt(N));

        for (int j = 0; j < N; j++) {
            for (int k = 0; k < N; k++) {
                int exponent = (j * k) % N;
                Complex entry = complexPow(omega, exponent).multiply(scale);
                data[j][k] = entry;
            }
        }

        return createGate(data);
    }

    private static Complex complexPow(Complex base, int exp) {
        Complex result = Complex.ONE;
        for (int i = 0; i < exp; i++) {
            result = result.multiply(base);
        }
        return result;
    }

    private static QuantumGate createGate(Complex[][] data) {
        List<List<Complex>> rowsList = new ArrayList<>();
        for (Complex[] row : data) {
            rowsList.add(Arrays.asList(row));
        }
        return new QuantumGate(new DenseMatrix<>(rowsList, Complex.ZERO));
    }
}


