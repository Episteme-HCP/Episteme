/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.quantum;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.technical.backend.ComputeBackend;

/**
 * Quantum computing integration interface for hybrid classical-quantum workflows.
 * <p>
 * This interface provides a bridge between JScience's classical numerical computing
 * and quantum computing frameworks like Qiskit (IBM) and Cirq (Google).
 * </p>
 * <p>
 * <b>Implementation Strategies:</b>
 * <ul>
 * <li><b>Qiskit</b>: Use Jython or GraalVM Python to call Qiskit directly</li>
 * <li><b>Cirq</b>: Similar approach with Python integration</li>
 * <li><b>ProjectQ</b>: Pure Python quantum simulator</li>
 * <li><b>Q#</b>: Microsoft Quantum Development Kit via .NET interop</li>
 * <li><b>Pennylane</b>: Quantum machine learning integration</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface QuantumBackend extends ComputeBackend {

    /**
     * Quantum circuit representation.
     */
    interface QuantumCircuit {
        /**
         * Adds a Hadamard gate to the specified qubit.
         */
        void hadamard(int qubit);

        /**
         * Adds a CNOT (controlled-NOT) gate.
         */
        void cnot(int control, int target);

        /**
         * Adds a rotation gate around the X axis.
         */
        void rx(int qubit, double angle);

        /**
         * Adds a rotation gate around the Y axis.
         */
        void ry(int qubit, double angle);

        /**
         * Adds a rotation gate around the Z axis.
         */
        void rz(int qubit, double angle);

        /**
         * Adds a measurement operation.
         */
        void measure(int qubit, int classicalBit);

        /**
         * Returns the number of qubits in the circuit.
         */
        int getNumQubits();

        /**
         * Returns a string representation of the circuit (e.g., QASM).
         */
        /**
         * Returns a string representation of the circuit (e.g., QASM).
         */
        String toQASM();

        /**
         * Appends another circuit to this circuit.
         */
        void append(QuantumCircuit other);
    }

    /**
     * Quantum execution result.
     */
    interface QuantumResult {
        /**
         * Returns the measurement counts for each basis state.
         * 
         * @return Map from bit string (e.g., "0101") to count
         */
        java.util.Map<String, Integer> getCounts();

        /**
         * Returns the final statevector (if available from simulator).
         */
        Vector<Complex> getStatevector();

        /**
         * Returns the execution time in milliseconds.
         */
        long getExecutionTimeMs();
    }

    /**
     * Creates a new quantum circuit with the specified number of qubits.
     *
     * @param numQubits Number of qubits
     * @param numClassicalBits Number of classical bits for measurement
     * @return New quantum circuit
     */
    QuantumCircuit createCircuit(int numQubits, int numClassicalBits);

    /**
     * Executes a quantum circuit on a simulator.
     *
     * @param circuit The circuit to execute
     * @param shots Number of measurement shots
     * @return Execution result
     */
    QuantumResult executeSimulator(QuantumCircuit circuit, int shots);

    /**
     * Executes a quantum circuit.
     * @param circuit The quantum circuit to execute.
     * @return A map of measurement results (e.g., bitstring -> count).
     */
    default java.util.Map<String, Integer> execute(QuantumCircuit circuit) {
        return executeSimulator(circuit, 1024).getCounts();
    }

    /**
     * Executes a quantum circuit on real quantum hardware.
     *
     * @param circuit The circuit to execute
     * @param shots Number of measurement shots
     * @param backend Backend name (e.g., "ibmq_qasm_simulator", "ionq.qpu")
     * @return Execution result
     */
    QuantumResult executeHardware(QuantumCircuit circuit, int shots, String backend);

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
     *
     * @param matrix Classical matrix
     * @return Quantum circuit implementing the unitary
     */
    QuantumCircuit matrixToUnitary(Matrix<Complex> matrix);

    /**
     * Performs quantum state tomography to reconstruct a density matrix.
     *
     * @param circuit Circuit preparing the state
     * @param shots Number of measurement shots per basis
     * @return Reconstructed density matrix
     */
    Matrix<Complex> stateTomography(QuantumCircuit circuit, int shots);

    /**
     * Returns available quantum backends (simulators and hardware).
     */
    String[] getAvailableBackends();

    /**
     * Returns backend information (num_qubits, connectivity, etc.).
     */
    java.util.Map<String, Object> getBackendInfo(String backendName);
}

