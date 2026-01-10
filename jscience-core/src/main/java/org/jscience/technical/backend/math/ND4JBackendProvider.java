/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.math;

import org.jscience.technical.backend.BackendProvider;
import org.jscience.technical.backend.BackendDiscovery;

/**
 * BackendProvider for ND4J.
 */
public class ND4JBackendProvider implements BackendProvider {
    @Override
    public String getType() {
        return BackendDiscovery.TYPE_TENSOR;
    }

    @Override
    public String getId() {
        return "nd4j";
    }

    @Override
    public String getName() {
        return "ND4J";
    }

    @Override
    public String getDescription() {
        return "Scientific computing for the JVM with N-dimensional arrays.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 90;
    }

    @Override
    public Object createBackend() {
        if (isAvailable()) {
            // Prefer CUDA if available, otherwise Native
            // Since we split the providers, we can check them directly or just try to instantiate CUDA first
            // Note: The providers themselves check availability.
            try {
                // Try CUDA first
                Class<?> cudaClass = Class.forName("org.jscience.mathematics.linearalgebra.tensors.backends.ND4JCUDATensorProvider");
                Object cudaProvider = cudaClass.getDeclaredConstructor().newInstance();
                if ((boolean) cudaClass.getMethod("isAvailable").invoke(cudaProvider)) {
                    return cudaProvider;
                }
            } catch (Exception e) {
                // Ignore
            }
            
            try {
                // Fallback to Native
                Class<?> nativeClass = Class.forName("org.jscience.mathematics.linearalgebra.tensors.backends.ND4JNativeTensorProvider");
                return nativeClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
