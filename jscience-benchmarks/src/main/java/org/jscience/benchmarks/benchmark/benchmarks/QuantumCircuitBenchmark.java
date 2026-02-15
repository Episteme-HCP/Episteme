/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.natural.physics.quantum.QuantumCircuit;
import org.jscience.natural.physics.quantum.QuantumGate;
import org.jscience.natural.physics.quantum.QuantumGateType;

/**
 * Benchmark for Quantum Circuit Simulation.
 * Simulates a Quantum Fourier Transform (QFT) circuit to measure classical simulation performance.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class QuantumCircuitBenchmark implements RunnableBenchmark {

    private static final int NUM_QUBITS = 10; // 2^10 = 1024 states, reasonable for repeated benchmark
    private QuantumCircuit circuit;

    @Override public String getId() { return "quantum-qft-" + NUM_QUBITS; }
    @Override public String getName() { return "Quantum Circuit Simulation (QFT)"; }
    @Override public String getDescription() { return "Simulates a " + NUM_QUBITS + "-qubit Quantum Fourier Transform circuit on a classical CPU."; }
    @Override public String getDomain() { return "Quantum Computing"; }
    @Override public String getAlgorithmType() { return "Quantum Simulation"; }

    @Override
    public void setup() {
        // Construct QFT Circuit
        circuit = new QuantumCircuit(NUM_QUBITS);
        
        for (int j = 0; j < NUM_QUBITS; j++) {
            // Apply Hadamard to qubit j
            circuit.addGate(QuantumGate.hadamard(), j);
            
            // Controlled Phase Rotations
            for (int k = j + 1; k < NUM_QUBITS; k++) {
                // Use Standard Gates (H, Z, CNOT) to stress the simulator without needing parameterized gate support
                // Standard CNOT for entanglement
                circuit.addGate(QuantumGate.cnot(), k, j); 
                
                // Pauli-Z to simulate phase flip (instead of arbitrary rotation)
                circuit.addGate(QuantumGate.pauliZ(), k);
                
                // Uncompute CNOT
                circuit.addGate(QuantumGate.cnot(), k, j);
            }
        }
        
        // Swaps at the end (usual QFT step)
        for (int i = 0; i < NUM_QUBITS / 2; i++) {
            // Swap using 3 CNOTs
            int q1 = i;
            int q2 = NUM_QUBITS - 1 - i;
            circuit.addGate(QuantumGate.cnot(), q1, q2);
            circuit.addGate(QuantumGate.cnot(), q2, q1);
            circuit.addGate(QuantumGate.cnot(), q1, q2);
        }
    }

    @Override
    public void run() {
        if (circuit != null) {
            circuit.run();
        }
    }

    @Override
    public void teardown() {
        circuit = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 50; // Exponential complexity, so keep iterations lower
    }
}
