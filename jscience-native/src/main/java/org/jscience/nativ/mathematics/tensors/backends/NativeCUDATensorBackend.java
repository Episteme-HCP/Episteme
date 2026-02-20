/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.tensors.backends;

import org.jscience.core.mathematics.linearalgebra.Tensor;
import org.jscience.nativ.mathematics.linearalgebra.tensors.CUDATensor;
import org.jscience.nativ.technical.backend.nativ.NativeBackend;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.mathematics.linearalgebra.tensors.providers.TensorProvider;
import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import com.google.auto.service.AutoService;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.JCudaDriver;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaMemcpyKind;

import java.nio.DoubleBuffer;
import java.util.Arrays;

/**
 * CUDA-accelerated Tensor Backend.
 * <p>
 * Implements Tensor operations using JCuda, producing
 * {@link CUDATensor} instances backed by GPU device memory.
 * Implements {@link GPUBackend} and {@link NativeBackend}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class, GPUBackend.class, NativeBackend.class, TensorProvider.class, AlgorithmProvider.class})
public class NativeCUDATensorBackend implements TensorProvider, GPUBackend, NativeBackend {

    @Override
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public String getNativeLibraryName() {
        return "cuda";
    }

    @Override
    public String getId() {
        return "cuda-tensor";
    }

    @Override
    public String getName() {
        return "CUDA GPU Tensor Backend";
    }

    @Override
    public String getDescription() {
        return "Native Tensor Backend using CUDA on GPU.";
    }

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
            throw new UnsupportedOperationException("Only Float and Double supported, got: " + type.getName());
        }
    }

    private int getSizeOf(Class<?> type) {
        return (type == Float.class) ? Sizeof.FLOAT : Sizeof.DOUBLE;
    }

    @Override
    public int getPriority() {
        return 80;
    }

    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1;
        double base = getPriority();
        if (context.getDataSize() < 1024) base -= 100;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 30;
        return base;
    }

    @Override
    public org.jscience.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.jscience.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override
    public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return null; // TODO: CUDA Context
    }

    @Override public DeviceInfo[] getDevices() { return new DeviceInfo[0]; }
    @Override public void selectDevice(int deviceId) { }
    @Override public long allocateGPUMemory(long sizeBytes) { return 0; }
    @Override public void copyToGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) { }
    @Override public void copyFromGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) { }
    @Override public void freeGPUMemory(long gpuHandle) { }
    @Override public void synchronize() { }
    @Override public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) { }
}
