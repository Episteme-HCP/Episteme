package org.jscience.physics.quantum;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Quantum Circuit / Context.
 * Holds registers and the sequence of gates.
 */
public class QuantumContext {
    private final List<QuantumRegister> registers = new ArrayList<>();
    private final List<QuantumGate> gates = new ArrayList<>();

    public void addRegister(QuantumRegister register) {
        registers.add(register);
    }

    public void addGate(QuantumGate gate) {
        gates.add(gate);
    }

    public List<QuantumRegister> getRegisters() {
        return registers;
    }

    public List<QuantumGate> getGates() {
        return gates;
    }
    
    /**
     * Helper to generate OpenQASM 2.0 representation.
     */
    public String toOpenQASM() {
        StringBuilder sb = new StringBuilder();
        sb.append("OPENQASM 2.0;\n");
        sb.append("include \"qelib1.inc\";\n");
        
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
        String qreg = "q"; // Assuming single register for simplicity or name mapping needed?
        // Ideally we need to map qubit index to register name.
        // For simplicity, let's assume the first register is used or flattened.
        // Or specific logic.
        // Let's assume register name is passed or we default to registers.get(0).
        String rName = registers.isEmpty() ? "q" : registers.get(0).getName();
        
        switch (gate.getType()) {
            case H: return "h " + rName + "[" + q[0] + "]";
            case X: return "x " + rName + "[" + q[0] + "]";
            case Y: return "y " + rName + "[" + q[0] + "]";
            case Z: return "z " + rName + "[" + q[0] + "]";
            case CX: return "cx " + rName + "[" + q[0] + "]," + rName + "[" + q[1] + "]";
            case MEASURE: return "measure " + rName + "[" + q[0] + "] -> c_" + rName + "[" + q[0] + "]";
            default: return "// unknown gate " + gate.getType();
        }
    }
}
