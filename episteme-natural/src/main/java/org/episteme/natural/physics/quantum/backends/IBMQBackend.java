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
            Process p = new ProcessBuilder("python", "-c", "import qiskit_ibm_runtime").start();
            return p.waitFor() == 0;
        } catch (Exception e) { return false; }
    }
}
