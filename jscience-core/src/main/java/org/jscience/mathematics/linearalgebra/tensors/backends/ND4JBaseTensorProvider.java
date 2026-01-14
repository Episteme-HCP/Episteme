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

package org.jscience.mathematics.linearalgebra.tensors.backends;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;
import org.jscience.technical.backend.cuda.CUDAExecutionContext;
import org.jscience.technical.backend.cpu.CPUExecutionContext;

/**
 * Abstract Base for ND4J-backed Tensor Providers.
 * <p>
 * Encapsulates common logic for ND4J (Native or CUDA).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class ND4JBaseTensorProvider implements TensorProvider {

    protected static final CPUDenseTensorProvider fallback = new CPUDenseTensorProvider();
    protected final boolean isAvailable;

    protected ND4JBaseTensorProvider() {
        this.isAvailable = checkAvailability();
    }

    /**
     * Checks if the specific backend implementation (Native/CUDA) is active and available.
     */
    protected abstract boolean checkAvailability();

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    /**
     * Helper to load common ND4J classes.
     */
    protected boolean checkCommonClasses() {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            Class.forName("org.nd4j.linalg.api.ndarray.INDArray");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    
    @Override
    public ExecutionContext createContext() {
        if (!isAvailable) {
            throw new IllegalStateException("ND4J backend not available");
        }
        // If GPU is supported by this provider, return CUDA context, else CPU
        if (supportsGPU()) {
            return new CUDAExecutionContext();
        }
        return new CPUExecutionContext();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        if (!isAvailable || !Real.class.isAssignableFrom(elementType)) {
            return fallback.zeros(elementType, shape);
        }
        try {
            long[] longShape = convertShape(shape);
            org.nd4j.linalg.api.ndarray.INDArray ndArray = org.nd4j.linalg.factory.Nd4j.zeros(longShape);
            return (Tensor<T>) fromINDArray(ndArray);
        } catch (Exception e) {
            return fallback.zeros(elementType, shape);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        if (!isAvailable || !Real.class.isAssignableFrom(elementType)) {
            return fallback.ones(elementType, shape);
        }
        try {
            long[] longShape = convertShape(shape);
            org.nd4j.linalg.api.ndarray.INDArray ndArray = org.nd4j.linalg.factory.Nd4j.ones(longShape);
            return (Tensor<T>) fromINDArray(ndArray);
        } catch (Exception e) {
            return fallback.ones(elementType, shape);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> create(T[] data, int... shape) {
        if (!isAvailable || data.length == 0 || !(data[0] instanceof Real)) {
            return fallback.create(data, shape);
        }
        try {
            double[] doubleData = new double[data.length];
            for (int i = 0; i < data.length; i++) {
                doubleData[i] = ((Real) data[i]).doubleValue();
            }
            long[] longShape = convertShape(shape);
            org.nd4j.linalg.api.ndarray.INDArray ndArray = org.nd4j.linalg.factory.Nd4j.create(doubleData, longShape);
            return (Tensor<T>) fromINDArray(ndArray);
        } catch (Exception e) {
            return fallback.create(data, shape);
        }
    }

    public org.nd4j.linalg.api.ndarray.INDArray toINDArray(Tensor<Real> tensor) {
        if (!isAvailable) throw new UnsupportedOperationException("ND4J is not available");
        
        int[] shape = tensor.shape();
        long[] longShape = convertShape(shape);
        int totalSize = 1;
        for (int dim : shape) totalSize *= dim;
        
        double[] data = new double[totalSize];
        int[] indices = new int[shape.length];
        
        for (int i = 0; i < totalSize; i++) {
            int remaining = i;
            for (int d = shape.length - 1; d >= 0; d--) {
                indices[d] = remaining % shape[d];
                remaining /= shape[d];
            }
            data[i] = tensor.get(indices).doubleValue();
        }
        return org.nd4j.linalg.factory.Nd4j.create(data, longShape);
    }

    public Tensor<Real> fromINDArray(org.nd4j.linalg.api.ndarray.INDArray indArray) {
        if (!isAvailable) throw new UnsupportedOperationException("ND4J is not available");
        
        long[] longShape = indArray.shape();
        int[] shape = new int[longShape.length];
        for (int i = 0; i < longShape.length; i++) shape[i] = (int) longShape[i];
        
        int totalSize = 1;
        for (int dim : shape) totalSize *= dim;
        
        Real[] data = new Real[totalSize];
        double[] ndData = indArray.data().asDouble();
        for (int i = 0; i < totalSize; i++) {
            data[i] = Real.of(ndData[i]);
        }
        return new DenseTensor<>(data, shape);
    }

    protected long[] convertShape(int[] shape) {
        long[] longShape = new long[shape.length];
        for (int i = 0; i < shape.length; i++) {
            longShape[i] = shape[i];
        }
        return longShape;
    }
}
