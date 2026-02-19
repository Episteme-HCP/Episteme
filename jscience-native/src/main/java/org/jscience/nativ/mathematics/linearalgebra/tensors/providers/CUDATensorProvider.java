/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.tensors.providers;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.nativ.mathematics.linearalgebra.tensors.CUDATensor;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.mathematics.linearalgebra.tensors.providers.TensorProvider;
import com.google.auto.service.AutoService;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.JCudaDriver;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaMemcpyKind;

import java.util.Arrays;

/**
 * CUDA-accelerated Tensor provider.
 * <p>
 * Implements Tensor factory operations using JCuda, producing
 * {@link CUDATensor} instances backed by GPU device memory.
 * Supports Float and Double element types.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService(AlgorithmProvider.class)
public class CUDATensorProvider implements TensorProvider {

    @Override
    public boolean isAvailable() {
        try {
            JCudaDriver.setExceptionsEnabled(true);
            JCudaDriver.cuInit(0);
            int[] deviceCount = new int[1];
            JCudaDriver.cuDeviceGetCount(deviceCount);
            return deviceCount[0] > 0;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        ensureAvailable();
        validateType(elementType);
        long totalElements = Arrays.stream(shape).asLongStream().reduce(1, (a, b) -> a * b);
        long byteSize = totalElements * getSizeOf(elementType);
        Pointer ptr = new Pointer();
        JCuda.cudaMalloc(ptr, byteSize);
        JCuda.cudaMemset(ptr, 0, byteSize);
        return new CUDATensor<>(ptr, shape, elementType);
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        ensureAvailable();
        validateType(elementType);
        long totalElements = Arrays.stream(shape).asLongStream().reduce(1, (a, b) -> a * b);
        if (elementType == Float.class) {
            float[] hostData = new float[(int)totalElements];
            Arrays.fill(hostData, 1.0f);
            return createFromPrimitive(hostData, shape, elementType);
        } else {
            double[] hostData = new double[(int)totalElements];
            Arrays.fill(hostData, 1.0d);
            return createFromPrimitive(hostData, shape, elementType);
        }
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        ensureAvailable();
        if (data.length == 0) throw new IllegalArgumentException("Empty data");
        @SuppressWarnings("unchecked")
        Class<T> elementType = (Class<T>) data.getClass().getComponentType();
        validateType(elementType);
        long expectedSize = Arrays.stream(shape).asLongStream().reduce(1, (a, b) -> a * b);
        if (data.length != expectedSize) {
            throw new IllegalArgumentException("Data length " + data.length + " does not match shape size " + expectedSize);
        }
        if (elementType == Float.class) {
            float[] primitives = new float[data.length];
            for (int i = 0; i < data.length; i++) primitives[i] = ((Number)data[i]).floatValue();
            return createFromPrimitive(primitives, shape, elementType);
        } else {
            double[] primitives = new double[data.length];
            for (int i = 0; i < data.length; i++) primitives[i] = ((Number)data[i]).doubleValue();
            return createFromPrimitive(primitives, shape, elementType);
        }
    }

    private <T> Tensor<T> createFromPrimitive(Object primitiveArray, int[] shape, Class<T> type) {
        long byteSize;
        Pointer hostPtr;
        if (type == Float.class) {
            float[] arr = (float[]) primitiveArray;
            byteSize = (long)arr.length * Sizeof.FLOAT;
            hostPtr = Pointer.to(arr);
        } else {
            double[] arr = (double[]) primitiveArray;
            byteSize = (long)arr.length * Sizeof.DOUBLE;
            hostPtr = Pointer.to(arr);
        }
        Pointer devicePtr = new Pointer();
        JCuda.cudaMalloc(devicePtr, byteSize);
        JCuda.cudaMemcpy(devicePtr, hostPtr, byteSize, cudaMemcpyKind.cudaMemcpyHostToDevice);
        return new CUDATensor<>(devicePtr, shape, type);
    }

    private void ensureAvailable() {
        if (!isAvailable()) throw new UnsupportedOperationException("CUDA is not available on this system.");
    }

    private void validateType(Class<?> type) {
        if (type != Float.class && type != Double.class) {
            throw new UnsupportedOperationException("CUDATensorProvider only supports Float and Double, got: " + type.getName());
        }
    }

    private int getSizeOf(Class<?> type) {
        return (type == Float.class) ? Sizeof.FLOAT : Sizeof.DOUBLE;
    }

    @Override
    public String getName() {
        return "CUDA GPU Tensor";
    }

    @Override
    public int getPriority() {
        return 80;
    }

    /** Minimum tensor element count where CUDA outperforms CPU. */
    private static final int GPU_TENSOR_THRESHOLD = 1024;

    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1;
        double base = getPriority();
        if (context.getDataSize() < GPU_TENSOR_THRESHOLD) base -= 100;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 30;
        if (context.hasHint(OperationContext.Hint.BATCH)) base += 20;
        if (context.hasHint(OperationContext.Hint.LOW_LATENCY)) base -= 50;
        if (context.hasHint(OperationContext.Hint.FLOAT32_OK)) base += 20;
        return base;
    }
}
