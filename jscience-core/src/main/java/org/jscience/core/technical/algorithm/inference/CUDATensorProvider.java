/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.inference;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.core.technical.algorithm.TensorProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.algorithm.OperationContext;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.JCudaDriver;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaMemcpyKind;

import java.util.Arrays;

/**
 * CUDA-accelerated Neural Network provider.
 * <p>
 * Implements Tensor operations using JCuda.
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
            // Check if we can initialize CUDA Driver API
            JCudaDriver.setExceptionsEnabled(true);
            JCudaDriver.cuInit(0);
            int[] deviceCount = new int[1];
            JCudaDriver.cuDeviceGetCount(deviceCount);
            return deviceCount[0] > 0;
        } catch (Throwable t) {
            // Log warning?
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
        JCuda.cudaMemset(ptr, 0, byteSize); // 0 byte pattern works for float/double 0.0
        
        return new CUDATensor<>(ptr, shape, elementType);
    }

    @Override
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        ensureAvailable();
        validateType(elementType);
        
        long totalElements = Arrays.stream(shape).asLongStream().reduce(1, (a, b) -> a * b);
        
        // Host allocation
        if (elementType == Float.class) {
            float[] hostData = new float[(int)totalElements];
            Arrays.fill(hostData, 1.0f);
            return createFromPrimitive(hostData, shape, elementType);
        } else { // Double
            double[] hostData = new double[(int)totalElements];
            Arrays.fill(hostData, 1.0d);
            return createFromPrimitive(hostData, shape, elementType);
        }
    }

    @Override
    public <T> Tensor<T> create(T[] data, int... shape) {
        ensureAvailable();
        if (data.length == 0) throw new IllegalArgumentException("Empty data");
        Class<?> componentType = data.getClass().getComponentType();
        // Since T is erased, we rely on array component type or passed implicit knowledge
        // But create signature is create(T[] data...).
        // We need to unbox.
        
        // This Cast is unchecked but necessary
        @SuppressWarnings("unchecked")
        Class<T> elementType = (Class<T>) componentType; 
        validateType(elementType);
        
        long expectedSize = Arrays.stream(shape).asLongStream().reduce(1, (a, b) -> a * b);
        if (data.length != expectedSize) {
            throw new IllegalArgumentException("Data length " + data.length + " does not match shape size " + expectedSize);
        }

        if (elementType == Float.class) {
            float[] primitives = new float[data.length];
            for(int i=0; i<data.length; i++) primitives[i] = ((Number)data[i]).floatValue();
            return createFromPrimitive(primitives, shape, elementType);
        } else {
            double[] primitives = new double[data.length];
            for(int i=0; i<data.length; i++) primitives[i] = ((Number)data[i]).doubleValue();
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
            throw new UnsupportedOperationException("Native CUDA Tensor only supports Float and Double, got: " + type.getName());
        }
    }

    private int getSizeOf(Class<?> type) {
        return (type == Float.class) ? Sizeof.FLOAT : Sizeof.DOUBLE;
    }

    @Override
    public String getName() {
        return "CUDA GPU Neural";
    }

    @Override
    public int getPriority() {
        return 80;
    }

    /** Minimum tensor element count where CUDA outperforms CPU. */
    private static final int GPU_TENSOR_THRESHOLD = 1024;

    /**
     * Context-aware scoring that accounts for GPU data transfer overhead.
     * <p>
     * Tensor operations are highly parallelizable, so GPU benefits
     * appear at relatively small tensor sizes.
     * </p>
     */
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
