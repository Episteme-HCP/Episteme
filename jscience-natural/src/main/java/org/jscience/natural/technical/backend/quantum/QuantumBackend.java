/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.technical.backend.quantum;

import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.technical.backend.ComputeBackend;

/**
 * Hardware interface for quantum computing backends.
 * <p>
 * Provides circuit construction, simulator execution, and hardware execution.
 * Algorithm-level operations (VQE, QAOA, Grover, Shor) are in
 * {@link QuantumAlgorithmProvider}, which uses this backend for execution.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface QuantumBackend extends ComputeBackend {

    @Override
    default String getType() {
        return "quantum";
    }

    @Override
    boolean isAvailable();

    /**
     * Quantum circuit representation.
     */
    interface QuantumCircuit {
        void hadamard(int qubit);
        void cnot(int control, int target);
        void rx(int qubit, double angle);
        void ry(int qubit, double angle);
        void rz(int qubit, double angle);
        void measure(int qubit, int classicalBit);
        int getNumQubits();
        String toQASM();
        void append(QuantumCircuit other);
    }

    /**
     * Quantum execution result.
     */
    interface QuantumResult {
        java.util.Map<String, Integer> getCounts();
        Vector<Complex> getStatevector();
        long getExecutionTimeMs();
    }

    /**
     * Creates a new quantum circuit.
     */
    QuantumCircuit createCircuit(int numQubits, int numClassicalBits);

    /**
     * Executes a quantum circuit on a simulator.
     */
    QuantumResult executeSimulator(QuantumCircuit circuit, int shots);

    /**
     * Executes a quantum circuit on a simulator with default 1024 shots.
     */
    default java.util.Map<String, Integer> execute(QuantumCircuit circuit) {
        return executeSimulator(circuit, 1024).getCounts();
    }

    /**
     * Executes a quantum circuit on real quantum hardware.
     */
    QuantumResult executeHardware(QuantumCircuit circuit, int shots, String backend);

    /**
     * Returns available quantum backends (simulators and hardware).
     */
    String[] getAvailableBackends();

    /**
     * Returns backend information (num_qubits, connectivity, etc.).
     */
    java.util.Map<String, Object> getBackendInfo(String backendName);
}
