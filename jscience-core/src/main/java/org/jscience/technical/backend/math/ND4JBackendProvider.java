/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
