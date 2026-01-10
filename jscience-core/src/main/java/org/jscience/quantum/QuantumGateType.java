package org.jscience.quantum;

/**
 * Supported Quantum Gate Types.
 */
public enum QuantumGateType {
    H,  // Hadamard
    X,  // Pauli-X
    Y,  // Pauli-Y
    Z,  // Pauli-Z
    CX, // Controlled-NOT (CNOT)
    MEASURE // Measurement
}
