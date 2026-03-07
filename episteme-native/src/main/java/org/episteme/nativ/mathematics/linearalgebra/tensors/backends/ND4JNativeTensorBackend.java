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
            boolean isCpu = backend != null && backend.getClass().getName().contains("CpuBackend");
            if (!isCpu && backend != null) {
                System.out.println("[INFO] ND4J Native (CPU) found GPU backend instead: " + backend.getClass().getSimpleName() + ". Fallback to CPU Dense.");
            }
            return isCpu;
        } catch (Throwable t) {
            String msg = t.getMessage() != null ? t.getMessage() : t.toString();
            if (msg.contains("cuda") || msg.contains("cublas")) {
                System.err.println("[WARN] ND4J Native (CPU) initialization failed due to CUDA conflict: " + msg + ". This happens when nd4j-cuda is on the classpath but the CUDA Toolkit (likely 12.x) is missing.");
            } else {
                System.err.println("[WARN] ND4J Native (CPU) initialization failed: " + msg);
            }
            return false;
        }
    }

    @Override
    protected org.episteme.core.technical.backend.ExecutionContext doGetContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
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
