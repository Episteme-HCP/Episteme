package org.jscience.natural.physics.quantum.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import java.util.HashMap;
import java.util.Map;

import org.jscience.natural.technical.backend.quantum.QuantumBackend;
import org.jscience.natural.technical.backend.quantum.QuantumAlgorithmProvider;
import org.jscience.natural.physics.quantum.QuantumContext;
import org.jscience.natural.physics.quantum.QuantumGate;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.complex.Complex;

import org.redfx.strange.Program;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.Gate;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.X;
import org.redfx.strange.gate.Y;
import org.redfx.strange.gate.Z;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

/**
 * Quantum Backend implementation using the Strange pure Java library.
 */
@AutoService({AlgorithmProvider.class, QuantumBackend.class, ComputeBackend.class, Backend.class})
public class StrangeBackend implements QuantumBackend, QuantumAlgorithmProvider {

    @Override public String getName() { return "Strange (Local)"; }
    @Override public String getId() { return "strange"; }
    @Override public String getDescription() { return "Pure Java quantum simulator relying on Strange."; }
    @Override public int getPriority() { return 50; }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.redfx.strange.Program");
            return true;
        } catch (ClassNotFoundException e) { return false; }
    }

    @Override
    public ExecutionContext createContext() {
        return null; // Placeholder
    }

    @Override
    public Map<String, Integer> execute(QuantumBackend.QuantumCircuit circuit) {
        return executeSimulator(circuit, 1024).getCounts();
    }

    @Override
    public QuantumBackend.QuantumResult executeSimulator(QuantumBackend.QuantumCircuit circuit, int shots) {
        if (!(circuit instanceof QuantumContext)) throw new IllegalArgumentException("Context required");
        QuantumContext context = (QuantumContext) circuit;
        int numQubits = context.getRegisters().stream().mapToInt(r -> r.getSize()).sum();
        
        Program p = new Program(numQubits);
        for (QuantumGate gate : context.getGates()) {
            Step s = new Step();
            Gate strangeGate = convertGate(gate);
            if (strangeGate != null) {
                s.addGate(strangeGate);
                p.addStep(s);
            }
        }
        
        try {
            SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
            Result res = sqee.runProgram(p);
            org.redfx.strange.Complex[] probabilities = res.getProbability();
            
            Map<String, Integer> counts = new HashMap<>();
            java.util.Random rand = new java.util.Random();
            
            for (int i = 0; i < shots; i++) {
                double r = rand.nextDouble();
                double cumulativeProbability = 0.0;
                for (int j = 0; j < probabilities.length; j++) {
                    cumulativeProbability += probabilities[j].abssqr();
                    if (r <= cumulativeProbability) {
                        String binaryString = Integer.toBinaryString(j);
                        while (binaryString.length() < numQubits) binaryString = "0" + binaryString;
                        counts.put(binaryString, counts.getOrDefault(binaryString, 0) + 1);
                        break;
                    }
                }
            }
            return new SimpleQuantumResult(counts, 0);
        } catch (Exception e) {
            throw new RuntimeException("Strange execution failed", e);
        }
    }

    private Gate convertGate(QuantumGate gate) {
        int[] targets = gate.getTargetQubits();
        switch (gate.getType()) {
            case H: return new Hadamard(targets[0]);
            case X: return new X(targets[0]);
            case Y: return new Y(targets[0]);
            case Z: return new Z(targets[0]);
            case CX: return new Cnot(targets[0], targets[1]);
            default: return null;
        }
    }

    @Override public QuantumBackend.QuantumCircuit createCircuit(int q, int c) { return new QuantumContext(q); }
    @Override public QuantumBackend.QuantumResult executeHardware(QuantumBackend.QuantumCircuit c, int s, String b) { return executeSimulator(c, s); }
    @Override public double vqe(Matrix<Complex> h, QuantumBackend.QuantumCircuit a, String o) { return 0.0; }
    @Override public QuantumBackend.QuantumResult qaoa(Matrix<Complex> h, int l) { return null; }
    @Override public double quantumPhaseEstimation(Matrix<Complex> u, Vector<Complex> e, int p) { return 0.0; }
    @Override
    public QuantumBackend.QuantumResult groverSearch(QuantumBackend.QuantumCircuit oracle, int numQubits) {
        QuantumBackend.QuantumCircuit groverCircuit = org.jscience.natural.physics.quantum.QuantumAlgorithms.createGroverCircuit(this, oracle, numQubits);
        return executeSimulator(groverCircuit, 1024);
    }
    @Override
    public int[] shorFactor(int N) {
        return org.jscience.natural.physics.quantum.QuantumAlgorithms.shor(N);
    }
    @Override public QuantumBackend.QuantumCircuit matrixToUnitary(Matrix<Complex> m) { return null; }
    @Override public Matrix<Complex> stateTomography(QuantumBackend.QuantumCircuit c, int s) { return null; }
    @Override public String[] getAvailableBackends() { return new String[]{"strange"}; }
    @Override public Map<String, Object> getBackendInfo(String b) { return Map.of(); }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.QUANTUM;
    }

    private static class SimpleQuantumResult implements QuantumBackend.QuantumResult {
        private final Map<String, Integer> counts;
        private final long time;
        public SimpleQuantumResult(Map<String, Integer> c, long t) { this.counts = c; this.time = t; }
        @Override public Map<String, Integer> getCounts() { return counts; }
        @Override public Vector<Complex> getStatevector() { return null; }
        @Override public long getExecutionTimeMs() { return time; }
    }
}


