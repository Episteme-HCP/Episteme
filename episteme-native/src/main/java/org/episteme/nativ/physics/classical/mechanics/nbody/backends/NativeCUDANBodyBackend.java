/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.physics.classical.mechanics.nbody.backends;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.natural.physics.classical.mechanics.nbody.NBodyProvider;
import org.episteme.core.technical.algorithm.OperationContext;
import com.google.auto.service.AutoService;
import jcuda.driver.JCudaDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.Backend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import java.nio.DoubleBuffer;

/**
 * CUDA-accelerated N-Body simulation backend using JCuda.
 */
@AutoService({ComputeBackend.class, GPUBackend.class, Backend.class, NativeBackend.class})
public class NativeCUDANBodyBackend implements NBodyProvider, GPUBackend, NativeBackend {

    private static final int GPU_THRESHOLD = 1000;
    private static volatile Boolean cudaAvailable;

    @Override
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public String getNativeLibraryName() {
        return "cuda";
    }

    @Override
    public String getName() {
        return "Native CUDA N-Body";
    }

    @Override
    public String getDescription() {
        return "CUDA-accelerated N-Body simulation (O(N^2))";
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
    }

    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1;
        double base = getPriority();
        if (context.getDataSize() < GPU_THRESHOLD) base -= 100;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 30;
        if (context.hasHint(OperationContext.Hint.BATCH)) base += 20;
        if (context.hasHint(OperationContext.Hint.LOW_LATENCY)) base -= 50;
        return base;
    }

    @Override
    public boolean isAvailable() {
        if (cudaAvailable == null) {
            synchronized (NativeCUDANBodyBackend.class) {
                if (cudaAvailable == null) {
                    cudaAvailable = detectCuda();
                }
            }
        }
        return cudaAvailable;
    }

    @Override
    public void shutdown() {
    }

    private static boolean detectCuda() {
        try {
            JCudaDriver.setExceptionsEnabled(true);
            JCudaDriver.cuInit(0);
            int[] count = new int[1];
            JCudaDriver.cuDeviceGetCount(count);
            return count[0] > 0;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int numParticles = masses.length;
        if (!isAvailable()) {
            throw new IllegalStateException("CUDA is not available.");
        }
        if (numParticles < GPU_THRESHOLD) {
            throw new IllegalStateException("Problem size too small.");
        }
        computeForcesCUDA(positions, masses, forces, G, softening);
    }

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        double[] posD = new double[positions.length];
        double[] massD = new double[masses.length];
        double[] forceD = new double[forces.length];

        for (int i = 0; i < positions.length; i++) posD[i] = positions[i].doubleValue();
        for (int i = 0; i < masses.length; i++) massD[i] = masses[i].doubleValue();

        computeForces(posD, massD, forceD, G.doubleValue(), softening.doubleValue());

        for (int i = 0; i < forces.length; i++) forces[i] = Real.of(forceD[i]);
    }

    private void computeForcesCUDA(double[] positions, double[] masses, double[] forces, double G, double softening) {
        throw new UnsupportedOperationException("CUDA implementation missing (NVRTC dependency).");
    }

    @Override
    public DeviceInfo[] getDevices() {
        if (!isAvailable()) return new DeviceInfo[0];
        int[] count = new int[1];
        JCudaDriver.cuDeviceGetCount(count);
        DeviceInfo[] devices = new DeviceInfo[count[0]];
        for (int i = 0; i < count[0]; i++) {
            byte[] name = new byte[256];
            jcuda.driver.CUdevice device = new jcuda.driver.CUdevice();
            JCudaDriver.cuDeviceGet(device, i);
            JCudaDriver.cuDeviceGetName(name, 256, device);
            devices[i] = new DeviceInfo(new String(name).trim(), 0, 0, "NVIDIA GPU");
        }
        return devices;
    }

    @Override
    public void selectDevice(int deviceId) {
        if (!isAvailable()) throw new IllegalStateException("CUDA not available");
        jcuda.driver.CUdevice device = new jcuda.driver.CUdevice();
        JCudaDriver.cuDeviceGet(device, deviceId);
        jcuda.driver.CUcontext context = new jcuda.driver.CUcontext();
        JCudaDriver.cuCtxCreate(context, 0, device);
    }

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        jcuda.driver.CUdeviceptr devicePtr = new jcuda.driver.CUdeviceptr();
        JCudaDriver.cuMemAlloc(devicePtr, sizeBytes);
        return getNativePointer(devicePtr);
    }

    private static long getNativePointer(jcuda.NativePointerObject obj) {
        try {
            java.lang.reflect.Field field = jcuda.NativePointerObject.class.getDeclaredField("nativePointer");
            field.setAccessible(true);
            return field.getLong(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access JCuda native pointer", e);
        }
    }

    private static jcuda.driver.CUdeviceptr createPointer(long handle) {
        try {
            java.lang.reflect.Constructor<jcuda.driver.CUdeviceptr> ctor = 
                jcuda.driver.CUdeviceptr.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            jcuda.driver.CUdeviceptr ptr = ctor.newInstance();
            
            java.lang.reflect.Field field = jcuda.NativePointerObject.class.getDeclaredField("nativePointer");
            field.setAccessible(true);
            field.setLong(ptr, handle);
            return ptr;
        } catch (Exception e) {
            throw new RuntimeException("Failed to wrap native CUDA handle", e);
        }
    }

    @Override
    public void copyToGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {
        JCudaDriver.cuMemcpyHtoD(createPointer(gpuHandle), jcuda.Pointer.to(hostBuffer), sizeBytes);
    }

    @Override
    public void copyFromGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {
        JCudaDriver.cuMemcpyDtoH(jcuda.Pointer.to(hostBuffer), createPointer(gpuHandle), sizeBytes);
    }

    @Override
    public void freeGPUMemory(long gpuHandle) {
        JCudaDriver.cuMemFree(createPointer(gpuHandle));
    }

    @Override
    public void synchronize() {
        JCudaDriver.cuCtxSynchronize();
    }

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        throw new UnsupportedOperationException("Matrix multiplication not implemented in this N-Body specialized backend.");
    }
}
