package org.jscience.physics.quantum;

/**
 * Represents a Register of Qubits.
 */
public class QuantumRegister {
    private final String name;
    private final int size;

    public QuantumRegister(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
