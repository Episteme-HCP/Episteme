/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.tensors.backends;

/**
 * ND4J Native (CPU) Tensor Provider.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ND4JNativeTensorProvider extends ND4JBaseTensorProvider {

    @Override
    protected boolean checkAvailability() {
        if (!checkCommonClasses()) return false;
        try {
            // Check specifically for Native backend being active
            // Note: ND4J usually loads one backend. We just check if it's NOT CUDA.
            // Or explicitly check for NativeBackend class presence
            Class.forName("org.nd4j.linalg.cpu.nativecpu.CpuBackend");
            return true;
        } catch (ClassNotFoundException e) {
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
        return "ND4J Tensor Provider using Native CPU Backend (AVX/AVX2/AVX512)";
    }

    @Override
    public int getPriority() {
        return isAvailable() ? 80 : 0;
    }

    @Override
    public boolean supportsGPU() {
        return false;
    }
}
