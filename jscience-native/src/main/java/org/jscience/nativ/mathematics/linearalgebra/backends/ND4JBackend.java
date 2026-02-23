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

package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.cpu.CPUBackend;
import org.jscience.core.technical.backend.cpu.CPUExecutionContext;
import com.google.auto.service.AutoService;

/**
 * CPU compute backend for ND4J (N-Dimensional Arrays for Java).
 * <p>
 * ND4J provides scientific computing for the JVM with N-dimensional arrays.
 * This backend auto-selects the best ND4J backend (CUDA if available,
 * otherwise native CPU).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, CPUBackend.class})
public class ND4JBackend implements CPUBackend {

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
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 90;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public Object createBackend() {
        if (isAvailable()) {
            // Prefer CUDA if available, otherwise Native
            try {
                Class<?> cudaClass = Class.forName("org.jscience.nativ.mathematics.linearalgebra.tensors.backends.ND4JCUDATensorBackend");
                Object cudaProvider = cudaClass.getDeclaredConstructor().newInstance();
                if ((boolean) cudaClass.getMethod("isAvailable").invoke(cudaProvider)) {
                    return cudaProvider;
                }
            } catch (Exception e) {
                // Ignore
            }

            try {
                Class<?> nativeClass = Class.forName("org.jscience.nativ.mathematics.linearalgebra.tensors.backends.ND4JNativeTensorBackend");
                return nativeClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
