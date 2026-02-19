/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.natural.technical.backend.quantum.QuantumBackend;
import org.jscience.natural.technical.backend.quantum.QuantumAlgorithmProvider;

/**
 * Benchmark for Quantum Circuit Simulation.
 * Simulates a Quantum Fourier Transform (QFT) circuit to measure backend performance.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class QuantumCircuitBenchmark implements SystematicBenchmark<QuantumAlgorithmProvider> {

    private static final int NUM_QUBITS = 10;
    private QuantumBackend provider;
    private QuantumBackend.QuantumCircuit circuit;

    @Override public Class<QuantumAlgorithmProvider> getProviderClass() { return QuantumAlgorithmProvider.class; }
    @Override public String getIdPrefix() { return "quantum-qft-" + NUM_QUBITS; }
    @Override public String getNameBase() { return "Quantum Circuit Simulation (QFT)"; }

    @Override public String getId() { return getIdPrefix() + "-default"; }
    @Override public String getName() { return getNameBase(); }
    @Override public String getDescription() { return "Simulates a " + NUM_QUBITS + "-qubit Quantum Fourier Transform circuit using " + (provider != null ? provider.getName() : "default backend"); }
    @Override public String getDomain() { return "Quantum Computing"; }
    @Override public String getAlgorithmType() { return "Quantum Simulation"; }
    @Override public String getAlgorithmProvider() { return provider != null ? provider.getName() : "None"; }

    @Override
    public void setProvider(QuantumAlgorithmProvider provider) {
        if (provider instanceof QuantumBackend) {
            this.provider = (QuantumBackend) provider;
        }
    }

    @Override
    public boolean isAvailable() {
        return provider != null && provider.isAvailable();
    }

    @Override
    public void setup() {
        if (provider == null) throw new IllegalStateException("Provider not set");
        
        // Construct QFT Circuit via Backend API
        circuit = provider.createCircuit(NUM_QUBITS, NUM_QUBITS);
        
        for (int j = 0; j < NUM_QUBITS; j++) {
            // Apply Hadamard to qubit j
            circuit.hadamard(j);
            
            // Controlled Phase Rotations
            for (int k = j + 1; k < NUM_QUBITS; k++) {
                // Controlled-Phase(theta) is equivalent to CNOT -> Rz -> CNOT structure or similar decomp
                // Here we use CNOT + Rz + CNOT as a stress test pattern
                
                circuit.cnot(k, j); 
                circuit.rz(k, Math.PI / Math.pow(2, k - j)); // CPhase angle
                circuit.cnot(k, j);
            }
        }
        
        // Swaps at the end
        for (int i = 0; i < NUM_QUBITS / 2; i++) {
            // Swap using CNOTs
            int q1 = i;
            int q2 = NUM_QUBITS - 1 - i;
            circuit.cnot(q1, q2);
            circuit.cnot(q2, q1);
            circuit.cnot(q1, q2);
        }
    }

    @Override
    public void run() {
        if (circuit != null && provider != null) {
            provider.executeSimulator(circuit, 100);
        }
    }

    @Override
    public void teardown() {
        circuit = null;
    }

    @Override
    public int getSuggestedIterations() {
        return 20; // 10 qubits with 20 iterations
    }
}
