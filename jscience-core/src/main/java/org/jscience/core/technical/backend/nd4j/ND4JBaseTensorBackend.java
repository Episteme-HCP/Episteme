/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.nd4j;

import org.jscience.core.mathematics.linearalgebra.tensors.TensorBackend;
import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.gpu.cuda.CUDAExecutionContext;
import org.jscience.core.technical.backend.cpu.CPUExecutionContext;
import org.jscience.core.mathematics.linearalgebra.tensors.backends.CPUDenseTensorBackend;

/**
 * Abstract Base for ND4J-backed Tensor Providers.
 */
public abstract class ND4JBaseTensorBackend implements TensorBackend {

    protected static final CPUDenseTensorBackend fallback = new CPUDenseTensorBackend();
    protected final boolean isAvailable;

    protected ND4JBaseTensorBackend() {
        this.isAvailable = checkAvailability();
    }

    protected abstract boolean checkAvailability();
    public abstract boolean supportsGPU();

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean supportsParallelOps() {
        return true;
    }

    protected boolean checkCommonClasses() {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            Class.forName("org.nd4j.linalg.api.ndarray.INDArray");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    
    public ExecutionContext createContext() {
        if (!isAvailable) {
            throw new IllegalStateException("ND4J backend not available");
        }
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
