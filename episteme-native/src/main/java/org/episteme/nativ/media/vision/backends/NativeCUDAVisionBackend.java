/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.media.vision.backends;

import org.episteme.core.media.vision.ImageOp;
import org.episteme.core.media.vision.VisionAlgorithmBackend;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import com.google.auto.service.AutoService;

import java.nio.DoubleBuffer;

/**
 * CUDA-accelerated Vision Backend.
 * <p>
 * Implements GPU-accelerated image processing using NVIDIA CUDA.
 * Implements {@link GPUBackend} and {@link NativeBackend}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class, GPUBackend.class, NativeBackend.class, VisionAlgorithmBackend.class})
public class NativeCUDAVisionBackend implements VisionAlgorithmBackend<Object>, GPUBackend, NativeBackend {

    @Override
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public String getNativeLibraryName() {
        return "cuda";
    }

    @Override public String getType() { return "vision"; }
    @Override public String getId() { return "native-cuda-vision"; }
    @Override public String getDescription() { return "GPU-accelerated image processing using NVIDIA CUDA."; }
    @Override public boolean isAvailable() {
        try {
            Class.forName("jcuda.driver.JCudaDriver");
            return true; 
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public Object apply(Object image, ImageOp<Object> op) {
        if (!(image instanceof jcuda.driver.CUdeviceptr)) {
            throw new IllegalArgumentException("Expected CUdeviceptr for NativeCUDAVisionBackend");
        }
        return op.process(image);
    }

    @Override
    public Object createImage(Object data, int width, int height) {
        if (data instanceof int[]) {
            int[] pixels = (int[]) data;
            jcuda.driver.CUdeviceptr deviceData = new jcuda.driver.CUdeviceptr();
            jcuda.driver.JCudaDriver.cuMemAlloc(deviceData, (long) pixels.length * 4);
            jcuda.driver.JCudaDriver.cuMemcpyHtoD(deviceData, jcuda.Pointer.to(pixels), (long) pixels.length * 4);
            return deviceData;
        }
        throw new UnsupportedOperationException("CUDA data upload only supported for int arrays for now.");
    }
    
    @Override
    public String getName() {
        return "CUDA Vision Backend";
    }

    @Override
    public int getPriority() {
        return 20; 
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
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
