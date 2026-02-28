/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.vision.backends;

import org.episteme.core.media.vision.VisionAlgorithmBackend;
import org.episteme.core.media.vision.ImageOp;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import com.google.auto.service.AutoService;

import java.nio.DoubleBuffer;

/**
 * OpenCL-accelerated Vision Backend.
 * <p>
 * This class handles GPU-accelerated image processing using OpenCL.
 * Implements {@link GPUBackend} and {@link NativeBackend}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class, GPUBackend.class, NativeBackend.class, VisionAlgorithmBackend.class})
public class NativeOpenCLVisionBackend implements VisionAlgorithmBackend<Object>, GPUBackend, NativeBackend {

    @Override
    public boolean isLoaded() {
        return isAvailable();
    }

    @Override
    public String getNativeLibraryName() {
        return "opencl";
    }

    @Override public String getType() { return "vision"; }
    @Override public String getId() { return "native-opencl-vision"; }
    @Override public String getDescription() { return "GPU-accelerated image processing using OpenCL."; }
    @Override public boolean isAvailable() {
        try {
            Class.forName("org.jocl.CL");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private org.jocl.cl_context context;

    @Override
    public Object apply(Object image, ImageOp<Object> op) {
        if (!(image instanceof org.jocl.cl_mem)) {
            throw new IllegalArgumentException("Expected cl_mem for NativeOpenCLVisionBackend");
        }
        return op.process(image);
    }

    @Override
    public Object createImage(Object data, int width, int height) {
        if (data instanceof int[]) {
            int[] pixels = (int[]) data;
            org.jocl.cl_mem mem = org.jocl.CL.clCreateBuffer(context, 
                org.jocl.CL.CL_MEM_READ_WRITE | org.jocl.CL.CL_MEM_COPY_HOST_PTR, 
                (long) pixels.length * 4, org.jocl.Pointer.to(pixels), null);
            return mem;
        }
        throw new UnsupportedOperationException("OpenCL data upload only supported for int arrays for now.");
    }
    
    @Override
    public String getName() {
        return "OpenCL Vision Backend";
    }

    @Override
    public int getPriority() {
        return 15; 
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
