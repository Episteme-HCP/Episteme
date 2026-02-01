/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.quantum;

/**
 * Represents a Register of Qubits.
 * Re-located to core for unified backend access.
 */
public class QuantumRegister {
    private final String name;
    private final int size;

    public QuantumRegister(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() { return name; }
    public int getSize() { return size; }
}
