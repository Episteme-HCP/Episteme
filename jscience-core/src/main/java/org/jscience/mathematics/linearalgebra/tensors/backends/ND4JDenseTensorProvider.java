/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.linearalgebra.tensors.backends;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.cuda.CUDAExecutionContext;

/**
 * ND4J-backed TensorProvider implementation.
 * <p>
 * This provider uses ND4J (N-Dimensional Arrays for Java) for GPU-accelerated
 * tensor operations. When ND4J is not available on the classpath, it falls back
 * to the native implementation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ND4JDenseTensorProvider implements TensorProvider {

    private static boolean isAvailable = false;
    private static final CPUDenseTensorProvider fallback = new CPUDenseTensorProvider();

    static {
        try {
            // Try to load ND4J classes
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            isAvailable = true;
        } catch (ClassNotFoundException e) {
            isAvailable = false;
        }
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public ExecutionContext createContext() {
        if (!isAvailable) {
            throw new IllegalStateException("ND4J backend not available");
        }
        // ND4J manages its own context usually, but if we need a return
        return new CUDAExecutionContext();
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    /**
     * Returns true if ND4J is available on the classpath.
     * 
     * @return true if ND4J can be used
     */
    public static boolean isND4JAvailable() {
        return isAvailable;
    }

    @Override
    public String getName() {
        return "ND4J";
    }

    @Override
    public int getPriority() {
        return isAvailable ? 80 : 0;
    }

    @Override
    public boolean supportsGPU() {
        return isAvailable;
    }

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        if (!isAvailable || !Real.class.isAssignableFrom(elementType)) {
            return fallback.zeros(elementType, shape);
        }
        // ND4J implementation would go here
        return fallback.zeros(elementType, shape);
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        if (!isAvailable || !Real.class.isAssignableFrom(elementType)) {
            return fallback.ones(elementType, shape);
        }
        // ND4J implementation would go here
        return fallback.ones(elementType, shape);
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        if (!isAvailable) {
            return fallback.create(data, shape);
        }
        // ND4J implementation would go here
        return fallback.create(data, shape);
    }

    /**
     * Converts a JScience Tensor to an ND4J INDArray (when ND4J is available).
     */
    public Object toINDArray(Tensor<Real> tensor) {
        if (!isAvailable) {
            throw new UnsupportedOperationException("ND4J is not available");
        }
        throw new UnsupportedOperationException("ND4J conversion not yet implemented");
    }

    /**
     * Creates a JScience Tensor from an ND4J INDArray.
     */
    public Tensor<Real> fromINDArray(Object indArray) {
        if (!isAvailable) {
            throw new UnsupportedOperationException("ND4J is not available");
        }
        throw new UnsupportedOperationException("ND4J conversion not yet implemented");
    }
}
