package org.jscience.quantum;

import java.util.Arrays;

/**
 * Represents a Quantum Gate operation on specific qubits.
 */
public class QuantumGate {
    private final QuantumGateType type;
    private final int[] targetQubits;

    public QuantumGate(QuantumGateType type, int... targetQubits) {
        this.type = type;
        this.targetQubits = targetQubits;
    }

    public QuantumGateType getType() {
        return type;
    }

    public int[] getTargetQubits() {
        return targetQubits;
    }

    @Override
    public String toString() {
        return type + " " + Arrays.toString(targetQubits);
    }
}
