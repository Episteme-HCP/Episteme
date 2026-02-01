/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.quantum;

import java.util.ArrayList;
import java.util.List;
import org.jscience.core.technical.backend.quantum.QuantumBackend;

/**
 * Represents a Quantum Circuit / Context.
 * Holds registers and the sequence of gates.
 * Located in natural for physics domain logic.
 */
public class QuantumContext implements QuantumBackend.QuantumCircuit {
    private final List<QuantumRegister> registers = new ArrayList<>();
    private final List<QuantumGate> gates = new ArrayList<>();
    private final int totalQubits;

    public QuantumContext(int qubits) {
        this.totalQubits = qubits;
        this.registers.add(new QuantumRegister("q", qubits));
    }

    public void addRegister(QuantumRegister register) {
        registers.add(register);
    }

    public void addGate(QuantumGate gate) {
        gates.add(gate);
    }

    public List<QuantumRegister> getRegisters() { return registers; }
    public List<QuantumGate> getGates() { return gates; }

    // QuantumCircuit implementation
    @Override public void hadamard(int qubit) { addGate(new QuantumGate(QuantumGateType.H, qubit)); }
    @Override public void cnot(int control, int target) { addGate(new QuantumGate(QuantumGateType.CX, control, target)); }
    @Override public void rx(int qubit, double angle) { 
        QuantumGate g = new QuantumGate(QuantumGateType.RX, qubit);
        g.setParameter(angle);
        addGate(g);
    }
    @Override public void ry(int qubit, double angle) {
        QuantumGate g = new QuantumGate(QuantumGateType.RY, qubit);
        g.setParameter(angle);
        addGate(g);
    }
    @Override public void rz(int qubit, double angle) {
        QuantumGate g = new QuantumGate(QuantumGateType.RZ, qubit);
        g.setParameter(angle);
        addGate(g);
    }
    @Override public void measure(int qubit, int classicalBit) { addGate(new QuantumGate(QuantumGateType.MEASURE, qubit)); }
    @Override public int getNumQubits() { return totalQubits; }

    @Override
    public String toQASM() {
        StringBuilder sb = new StringBuilder("OPENQASM 2.0;\ninclude \"qelib1.inc\";\n");
        for (QuantumRegister reg : registers) {
            sb.append("qreg ").append(reg.getName()).append("[").append(reg.getSize()).append("];\n");
            sb.append("creg c_").append(reg.getName()).append("[").append(reg.getSize()).append("];\n");
        }
        for (QuantumGate gate : gates) {
            sb.append(gateToQASM(gate)).append(";\n");
        }
        return sb.toString();
    }

    private String gateToQASM(QuantumGate gate) {
        int[] q = gate.getTargetQubits();
        String rName = registers.get(0).getName();
        switch (gate.getType()) {
            case H: return "h " + rName + "[" + q[0] + "]";
            case X: return "x " + rName + "[" + q[0] + "]";
            case Y: return "y " + rName + "[" + q[0] + "]";
            case Z: return "z " + rName + "[" + q[0] + "]";
            case RX: return "rx(" + gate.getParameter() + ") " + rName + "[" + q[0] + "]";
            case RY: return "ry(" + gate.getParameter() + ") " + rName + "[" + q[0] + "]";
            case RZ: return "rz(" + gate.getParameter() + ") " + rName + "[" + q[0] + "]";
            case CX: return "cx " + rName + "[" + q[0] + "]," + rName + "[" + q[1] + "]";
            case MEASURE: return "measure " + rName + "[" + q[0] + "] -> c_" + rName + "[" + q[0] + "]";
            default: return "// unknown";
        }
    }
}
