package org.episteme.natural.physics.quantum.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.natural.technical.backend.quantum.QuantumBackend;

@AutoService({AlgorithmProvider.class, QuantumBackend.class, ComputeBackend.class, Backend.class})
public class QiskitAerBackend extends QiskitBackend {

    @Override public String getId() { return "qiskit_aer"; }
    @Override public String getName() { return "Qiskit Aer Simulator"; }
    @Override public String getDescription() { return "High performance C++ simulator for Qiskit circuits."; }
    @Override public int getPriority() { return 110; }

    @Override
    public boolean isAvailable() {
        try {
            Process p = new ProcessBuilder("python", "-c", "import qiskit_aer").start();
            return p.waitFor() == 0;
        } catch (Exception e) { return false; }
    }
}
