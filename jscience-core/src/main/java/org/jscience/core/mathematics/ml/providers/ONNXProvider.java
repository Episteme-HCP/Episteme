/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.mathematics.ml.providers;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;

/**
 * ONNX Inference Provider.
 * Wraps ONNX Runtime for inference benchmarks.
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
@AutoService(AlgorithmProvider.class)
public class ONNXProvider implements AlgorithmProvider {

    @Override
    public String getName() {
        return "ONNX Runtime Inference";
    }

    @Override
    public String getAlgorithmType() {
        return "inference";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("ai.onnxruntime.OrtEnvironment");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
