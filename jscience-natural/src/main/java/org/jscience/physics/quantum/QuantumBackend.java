package org.jscience.physics.quantum;

import java.util.Map;

/**
 * Interface for Quantum Execution Backends.
 * Implementations provide access to simulators (like Qiskit) or real hardware.
 */
public interface QuantumBackend extends org.jscience.technical.backend.BackendProvider {
    
    /**
     * Executes a quantum circuit defined by the context.
     * @param context The quantum context containing registers and gates.
     * @return A map of measurement results (e.g., bitstring -> count).
     */
    Map<String, Integer> execute(QuantumContext context);

    @Override
    default String getType() {
        return "quantum";
    }

    @Override
    default Object createBackend() {
        return this;
    }
}
