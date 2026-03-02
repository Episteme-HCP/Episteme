/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.tensors.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.tensors.TensorBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;

import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * ND4J CUDA (GPU) Tensor backend.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({TensorBackend.class, AlgorithmProvider.class})
public class ND4JCUDATensorBackend extends ND4JBaseTensorBackend {

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.GPU;
    }

    @Override
    protected boolean checkAvailability() {
        if (!checkCommonClasses()) return false;
        try {
            Class.forName("org.nd4j.linalg.jcublas.JCublasBackend");
            return true;
        } catch (ClassNotFoundException e) {
            // Log that the dependency is missing entirely
            // System.out.println("[INFO] ND4J CUDA (GPU) skipped: nd4j-cuda-platform dependency not found on classpath.");
            return false;
        } catch (Throwable t) {
            System.err.println("[WARN] ND4J CUDA (GPU) initialization failed: " + t.toString());
            return false;
        }
    }

    @Override
    public String getId() {
        return "nd4j-cuda";
    }

    @Override
    public String getName() {
        return "ND4J CUDA (GPU)";
    }

    @Override
    public String getDescription() {
        return "ND4J Tensor backend using CUDA GPU";
    }

    @Override
    public int getPriority() {
        return isAvailable() ? 100 : 0;
    }

    @Override
    public boolean supportsGPU() {
        return true;
    }
}
