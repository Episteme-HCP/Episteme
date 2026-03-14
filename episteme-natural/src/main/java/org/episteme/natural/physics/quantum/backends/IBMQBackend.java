package org.episteme.natural.physics.quantum.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.natural.technical.backend.quantum.QuantumBackend;

@AutoService({AlgorithmProvider.class, QuantumBackend.class, ComputeBackend.class, Backend.class})
public class IBMQBackend extends QiskitBackend {

    @Override public String getId() { return "qiskit_ibmq"; }
    @Override public String getName() { return "IBM Quantum Hardware"; }
    @Override public String getDescription() { return "Executes quantum circuits on real IBM Quantum devices."; }
    @Override public int getPriority() { return 120; }

    @Override
    public boolean isAvailable() {
        try {
            String python = PythonResolver.resolve();
            Process p = new ProcessBuilder(python, "-c", "import qiskit_ibm_runtime").start();
            boolean success = p.waitFor() == 0;
            return success && !isExplicitlyDisabled();
        } catch (Exception e) { return false; }
    }

    @Override
    public String getStatusMessage() {
        if (isAvailable()) return "Ready (qiskit_ibm_runtime)";
        return "Missing 'qiskit-ibm-runtime' Python package. Try: python -m pip install qiskit-ibm-runtime";
    }
}
