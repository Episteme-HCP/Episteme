package org.jscience.natural.physics.quantum.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.technical.backend.quantum.QuantumBackend;
import org.jscience.core.technical.backend.quantum.QuantumAlgorithmProvider;
import org.jscience.natural.physics.quantum.QuantumContext;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.numbers.complex.Complex;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Quantum Backend that delegates to Qiskit (Python).
 */
@AutoService(QuantumBackend.class)
public class QiskitBackendProvider implements QuantumBackend, QuantumAlgorithmProvider {

    @Override public String getId() { return "qiskit"; }
    @Override public String getName() { return "Qiskit (Python)"; }
    @Override public String getDescription() { return "Executes quantum circuits via Python/Qiskit bridge."; }
    @Override public int getPriority() { return 100; }

    @Override
    public boolean isAvailable() {
        try {
            Process p = new ProcessBuilder("python", "-c", "import qiskit").start();
            return p.waitFor() == 0;
        } catch (Exception e) { return false; }
    }

    @Override public ExecutionContext createContext() { return null; }

    @Override
    public Map<String, Integer> execute(QuantumBackend.QuantumCircuit circuit) {
        return executeSimulator(circuit, 1024).getCounts();
    }

    @Override
    public QuantumBackend.QuantumResult executeSimulator(QuantumBackend.QuantumCircuit circuit, int shots) {
        Map<String, Integer> results = new HashMap<>();
        try {
            String qasm = circuit.toQASM();
            Path qasmFile = Files.createTempFile("circuit", ".qasm");
            Files.write(qasmFile, qasm.getBytes());
 
            File runnerScript = new File("tools/qiskit_runner.py");
            if (!runnerScript.exists()) {
                return new SimpleQuantumResult(results, 0);
            }
 
            ProcessBuilder pb = new ProcessBuilder("python", runnerScript.getAbsolutePath(), qasmFile.toAbsolutePath().toString());
            pb.redirectErrorStream(true);
            Process p = pb.start();
 
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) output.append(line);
            p.waitFor();
 
            String json = output.toString().trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                String content = json.substring(1, json.length() - 1);
                if (!content.isEmpty()) {
                    String[] pairs = content.split(",");
                    for (String pair : pairs) {
                        String[] kv = pair.split(":");
                        String key = kv[0].trim().replace("\"", "").replace("'", "");
                        int val = Integer.parseInt(kv[1].trim());
                        results.put(key, val);
                    }
                }
            }
            Files.deleteIfExists(qasmFile);
        } catch (Exception e) { e.printStackTrace(); }
        return new SimpleQuantumResult(results, 0);
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
    @Override public String[] getAvailableBackends() { return new String[]{"qiskit_sim"}; }
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
