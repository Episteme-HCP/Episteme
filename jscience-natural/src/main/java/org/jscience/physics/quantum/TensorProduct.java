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
import java.util.Arrays;

/**
 * Tensor product operations for multi-qubit systems.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TensorProduct {

    /**
     * Computes the tensor (Kronecker) product of two matrices.
     * (A ⊗ B)_{(i,k),(j,l)} = A_{i,j} * B_{k,l}
     * 
     * @param A First matrix
     * @param B Second matrix
     * @return A ⊗ B
     */
    public static DenseMatrix<Complex> kronecker(DenseMatrix<Complex> A, DenseMatrix<Complex> B) {
        int aRows = A.rows();
        int aCols = A.cols();
        int bRows = B.rows();
        int bCols = B.cols();

        int resultRows = aRows * bRows;
        int resultCols = aCols * bCols;

        Complex[][] data = new Complex[resultRows][resultCols];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aCols; j++) {
                Complex aij = A.get(i, j);
                for (int k = 0; k < bRows; k++) {
                    for (int l = 0; l < bCols; l++) {
                        data[i * bRows + k][j * bCols + l] = aij.multiply(B.get(k, l));
                    }
                }
            }
        }

        List<List<Complex>> rowsList = new ArrayList<>();
        for (Complex[] row : data) {
            rowsList.add(Arrays.asList(row));
        }
        return new DenseMatrix<>(rowsList, org.jscience.mathematics.numbers.complex.Complex.ZERO);
    }

    /**
     * Tensor product of two quantum gates.
     * 
     * @param gate1 First gate
     * @param gate2 Second gate
     * @return Combined gate acting on gate1.qubits + gate2.qubits qubits
     */
    public static QuantumGate tensorGates(QuantumGate gate1, QuantumGate gate2) {
        DenseMatrix<Complex> result = kronecker(gate1.getMatrix(), gate2.getMatrix());
        return new QuantumGate(result);
    }

    /**
     * Tensor product of multiple gates.
     * 
     * @param gates Gates in order (first gate is applied to highest-order qubits)
     * @return Combined gate
     */
    public static QuantumGate tensorMultiple(QuantumGate... gates) {
        if (gates.length == 0) {
            throw new IllegalArgumentException("Need at least one gate");
        }

        QuantumGate result = gates[0];
        for (int i = 1; i < gates.length; i++) {
            result = tensorGates(result, gates[i]);
        }
        return result;
    }

    /**
     * Creates an n-qubit Hadamard gate: H ⊗ H ⊗ ... ⊗ H
     * 
     * @param numQubits Number of qubits
     * @return n-fold tensor product of Hadamard
     */
    public static QuantumGate nQubitHadamard(int numQubits) {
        QuantumGate h = QuantumGate.hadamard();
        QuantumGate result = h;
        for (int i = 1; i < numQubits; i++) {
            result = tensorGates(result, h);
        }
        return result;
    }

    /**
     * Creates an identity gate for n qubits.
     * 
     * @param numQubits Number of qubits
     * @return 2^n × 2^n identity matrix as gate
     */
    public static QuantumGate identity(int numQubits) {
        int dim = 1 << numQubits;
        Complex[][] data = new Complex[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                data[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }

        List<List<Complex>> rowsList = new ArrayList<>();
        for (Complex[] row : data) {
            rowsList.add(Arrays.asList(row));
        }
        return new QuantumGate(new DenseMatrix<>(rowsList, org.jscience.mathematics.numbers.complex.Complex.ZERO));
    }

    /**
     * Tensor product of quantum states.
     * |ψ⟩ ⊗ |φ⟩
     * 
     * @param psi First state
     * @param phi Second state
     * @return Combined state
     */
    public static BraKet tensorStates(BraKet psi, BraKet phi) {
        int dimPsi = psi.vector().dimension();
        int dimPhi = phi.vector().dimension();
        int dimResult = dimPsi * dimPhi;

        Complex[] result = new Complex[dimResult];
        for (int i = 0; i < dimPsi; i++) {
            for (int j = 0; j < dimPhi; j++) {
                result[i * dimPhi + j] = psi.vector().get(i).multiply(phi.vector().get(j));
            }
        }

        return new BraKet(result);
    }
}
