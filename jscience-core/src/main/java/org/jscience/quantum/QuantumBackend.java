package org.jscience.quantum;

import java.util.Map;

/**
 * Interface for Quantum Execution Backends.
 * Implementations provide access to simulators (like Qiskit) or real hardware.
 */
public interface QuantumBackend {
    
    /**
     * Executes a quantum circuit defined by the context.
     * @param context The quantum context containing registers and gates.
     * @return A map of measurement results (e.g., bitstring -> count).
     */
    Map<String, Integer> execute(QuantumContext context);

    /**
     * Checks if this backend is available (e.g., Python/Qiskit is installed).
     * @return true if available.
     */
    boolean isAvailable();
}
