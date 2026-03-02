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
 * ND4J Native (CPU) Tensor backend, using AVX/AVX2/AVX512 instructions when available.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({TensorBackend.class, AlgorithmProvider.class})
public class ND4JNativeTensorBackend extends ND4JBaseTensorBackend {

    @Override
    protected boolean checkAvailability() {
        if (!checkCommonClasses()) return false;
        try {
            // Try to load the CPU backend specifically
            org.nd4j.linalg.factory.Nd4jBackend backend = org.nd4j.linalg.factory.Nd4j.getBackend();
            return backend != null && backend.getClass().getName().contains("CpuBackend");
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public String getId() {
        return "nd4j-native";
    }

    @Override
    public String getName() {
        return "ND4J Native (CPU)";
    }

    @Override
    public String getDescription() {
        return "ND4J Tensor backend using native CPU (AVX/AVX2/AVX512)";
    }

    @Override
    public int getPriority() {
        return isAvailable() ? 80 : 0;
    }

    @Override
    public boolean supportsGPU() {
        return false;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }
}
