/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.quantum;

/**
 * Supported Quantum Gate Types.
 * Re-located to core for unified backend access.
 */
public enum QuantumGateType {
    H,  // Hadamard
    X,  // Pauli-X
    Y,  // Pauli-Y
    Z,  // Pauli-Z
    CX, // Controlled-NOT (CNOT)
    RX, // Rotation X
    RY, // Rotation Y
    RZ, // Rotation Z
    MEASURE // Measurement
}
