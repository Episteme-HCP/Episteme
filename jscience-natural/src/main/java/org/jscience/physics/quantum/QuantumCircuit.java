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
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Complexes;
import java.util.ArrayList;

import java.util.List;

/**
 * Simulates a quantum circuit composed of qubits and gates.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantumCircuit {

    private final int numQubits;
    private final List<GateApplication> gates;

    /**
     * Inner class to store gate application details.
     */
    private static class GateApplication {
        final QuantumGate gate;
        final int[] targetQubits;

        GateApplication(QuantumGate gate, int... targetQubits) {
            this.gate = gate;
            this.targetQubits = targetQubits;
        }
    }

    public QuantumCircuit(int numQubits) {
        this.numQubits = numQubits;
        this.gates = new ArrayList<>();
    }

    /**
     * Adds a gate to the circuit.
     * 
     * @param gate         The quantum gate
     * @param targetQubits Indices of qubits to apply the gate to
     */
    public void addGate(QuantumGate gate, int... targetQubits) {
        if (targetQubits.length != gate.getQubits()) {
            throw new IllegalArgumentException("Gate expects " + gate.getQubits() +
                    " qubits, but got " + targetQubits.length);
        }
        for (int q : targetQubits) {
            if (q < 0 || q >= numQubits) {
                throw new IndexOutOfBoundsException("Qubit index out of range: " + q);
            }
        }
        gates.add(new GateApplication(gate, targetQubits));
    }

    /**
     * Runs the circuit starting from the |0...0> state.
     * 
     * @return Final state vector (Ket)
     */
    public BraKet run() {
        // Initialize state |0...0>
        // Dimension is 2^n
        int dim = 1 << numQubits;
        // Basic array for now, optimizing later
        Complex[] stateData = new Complex[dim];
        stateData[0] = Complex.ONE;
        for (int i = 1; i < dim; i++) {
            stateData[i] = Complex.ZERO;
        }
        BraKet currentState = new BraKet(stateData);

        for (GateApplication app : gates) {
            currentState = applyGate(currentState, app);
        }

        return currentState;
    }

    /**
     * Applies a gate to the full state vector.
     * This involves tensor products if the gate acts on a subset of qubits.
     * Note: This is an exponential simulation (classical simulation of quantum
     * systems).
     */
    private BraKet applyGate(BraKet state, GateApplication app) {
        // Efficient simulation for single-qubit gates without constructing full matrix
        if (app.targetQubits.length == 1) {
            return applySingleQubitGate(state, app.gate, app.targetQubits[0]);
        }

        // Fallback: Construct full matrix op
        Matrix<Complex> op = buildFullOperator(app);

        // Convert BraKet vector to Matrix-compatible vector
        Vector<Complex> stateVec = state.vector();

        // Perform multiplication
        Vector<Complex> resultVec = op.multiply(stateVec);

        return new BraKet(resultVec);
    }

    private BraKet applySingleQubitGate(BraKet state, QuantumGate gate, int targetQubit) {
        // Iterate over all basis states |i>
        // If i has 0 at targetQubit, it's paired with i | (1 << target)
        // Apply 2x2 gate matrix to this pair.

        int dim = 1 << numQubits;
        DenseVector<Complex> v = state.vector();
        Complex[] newData = new Complex[dim];
        for (int i = 0; i < dim; i++)
            newData[i] = v.get(i); // Copy

        int mask = 1 << targetQubit;

        Matrix<Complex> m = gate.getMatrix();
        Complex u00 = m.get(0, 0);
        Complex u01 = m.get(0, 1);
        Complex u10 = m.get(1, 0);
        Complex u11 = m.get(1, 1);

        for (int i = 0; i < dim; i++) {
            if ((i & mask) == 0) {
                // i is the |0> component (at target bit)
                // j is the |1> component
                int j = i | mask;

                Complex alpha = v.get(i);
                Complex beta = v.get(j);

                // [new_alpha] = [u00 u01] [alpha]
                // [new_beta ] [u10 u11] [beta ]

                newData[i] = u00.multiply(alpha).add(u01.multiply(beta));
                newData[j] = u10.multiply(alpha).add(u11.multiply(beta));
            }
        }

        // Create new DenseVector (requires list)
        List<Complex> dataList = new ArrayList<>(dim);
        for (Complex c : newData)
            dataList.add(c);

        return new BraKet(new DenseVector<>(dataList, Complexes.getInstance()));
    }

    private Matrix<Complex> buildFullOperator(GateApplication app) {
        // Construct full matrix by iterating over basis states (columns)
        int dim = 1 << numQubits;

        List<List<Complex>> rows = new ArrayList<>(dim);
        for (int i = 0; i < dim; i++) {
            List<Complex> row = new ArrayList<>(dim);
            for (int j = 0; j < dim; j++)
                row.add(Complex.ZERO);
            rows.add(row);
        }

        // Simpler: Apply gate to each basis vector |j> to get j-th column of Matrix.
        for (int j = 0; j < dim; j++) {
            int bits = 0;
            for (int k = 0; k < app.targetQubits.length; k++) {
                int bitPos = app.targetQubits[k];
                if ((j & (1 << bitPos)) != 0) {
                    bits |= (1 << k); // Set k-th bit of local index
                }
            }

            // Get column 'bits' from gate matrix
            Matrix<Complex> G = app.gate.getMatrix();
            int gSize = 1 << app.targetQubits.length;

            for (int r = 0; r < gSize; r++) {
                Complex val = G.get(r, bits);
                // If val != 0, we add to row 'k' of full matrix

                int k = j;
                // Clear target bits
                for (int p = 0; p < app.targetQubits.length; p++) {
                    k &= ~(1 << app.targetQubits[p]);
                }
                // Set target bits based on r
                for (int p = 0; p < app.targetQubits.length; p++) {
                    if ((r & (1 << p)) != 0) {
                        k |= (1 << app.targetQubits[p]);
                    }
                }

                rows.get(k).set(j, val);
            }
        }

        return new DenseMatrix<>(rows, Complexes.getInstance());
    }

}


