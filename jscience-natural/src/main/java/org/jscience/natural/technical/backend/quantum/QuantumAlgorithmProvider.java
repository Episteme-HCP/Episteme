/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.technical.backend.quantum;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.natural.technical.backend.quantum.QuantumBackend.QuantumCircuit;
import org.jscience.natural.technical.backend.quantum.QuantumBackend.QuantumResult;

/**
 * Algorithm provider for quantum computing algorithms.
 * <p>
 * Provides high-level quantum algorithms (VQE, QAOA, Grover, Shor, QPE)
 * that use a {@link QuantumBackend} for circuit execution. This separation
 * allows algorithm implementations to target different hardware backends.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface QuantumAlgorithmProvider extends AlgorithmProvider {

    @Override
    default String getAlgorithmType() {
        return "Quantum Algorithms";
    }

    @Override
    default String getName() {
        return "Quantum Algorithm Provider";
    }

    /**
     * Performs Variational Quantum Eigensolver (VQE) to find ground state energy.
     *
     * @param hamiltonian Hamiltonian matrix
     * @param ansatz Parameterized quantum circuit
     * @param optimizer Classical optimizer ("COBYLA", "SPSA", etc.)
     * @return Ground state energy
     */
    double vqe(Matrix<Complex> hamiltonian, QuantumCircuit ansatz, String optimizer);

    /**
     * Performs Quantum Approximate Optimization Algorithm (QAOA).
     *
     * @param costHamiltonian Cost Hamiltonian for the optimization problem
     * @param layers Number of QAOA layers (p parameter)
     * @return Optimal parameters and final state
     */
    QuantumResult qaoa(Matrix<Complex> costHamiltonian, int layers);

    /**
     * Performs Quantum Phase Estimation (QPE).
     *
     * @param unitary Unitary operator
     * @param eigenstate Initial eigenstate
     * @param precision Number of precision qubits
     * @return Estimated phase
     */
    double quantumPhaseEstimation(Matrix<Complex> unitary, Vector<Complex> eigenstate, int precision);

    /**
     * Performs Grover's search algorithm.
     *
     * @param oracle Oracle function (marks solution states)
     * @param numQubits Number of qubits in search space
     * @return Measurement result (solution state)
     */
    QuantumResult groverSearch(QuantumCircuit oracle, int numQubits);

    /**
     * Performs Shor's factoring algorithm.
     *
     * @param N Number to factor
     * @return Factors of N
     */
    int[] shorFactor(int N);

    /**
     * Converts a classical matrix to a quantum unitary operator.
     */
    QuantumCircuit matrixToUnitary(Matrix<Complex> matrix);

    /**
     * Performs quantum state tomography to reconstruct a density matrix.
     */
    Matrix<Complex> stateTomography(QuantumCircuit circuit, int shots);
}
