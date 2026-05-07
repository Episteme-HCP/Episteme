/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.backend.gpu;

import org.episteme.core.technical.backend.ComputeBackend;
import java.nio.DoubleBuffer;

/**
 * GPU device management backend using CUDA or OpenCL.
 * <p>
 * This interface provides GPU device discovery, selection, and memory management.
 * Algorithm-specific GPU operations (FFT, matrix multiply, reductions) are provided
 * by their respective AlgorithmProvider implementations.
 * </p>
 * <p>
 * <b>Implementation Strategy:</b>
 * <ul>
 * <li>Use JCuda for NVIDIA GPUs: http://www.jcuda.org/</li>
 * <li>Use JOCL for OpenCL devices: http://www.jocl.org/</li>
 * <li>Use Project Panama for direct native GPU library access</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface GPUBackend extends ComputeBackend {

    /**
     * GPU device information.
     */
    class DeviceInfo {
        public final String name;
        public final long totalMemory;
        public final int computeUnits;
        public final String vendor;
        
        public DeviceInfo(String name, long totalMemory, int computeUnits, String vendor) {
            this.name = name;
            this.totalMemory = totalMemory;
            this.computeUnits = computeUnits;
            this.vendor = vendor;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%s) - %d MB, %d CUs", 
                name, vendor, totalMemory / (1024*1024), computeUnits);
        }
    }

    /**
     * Returns information about available GPU devices.
     */
    DeviceInfo[] getDevices();

    /**
     * Selects a GPU device for computation.
     *
     * @param deviceId Device index (0-based)
     */
    void selectDevice(int deviceId);

    /**
     * Allocates GPU memory and returns a handle.
     *
     * @param sizeBytes Size in bytes
     * @return GPU memory handle (opaque pointer)
     */
    long allocateGPUMemory(long sizeBytes);

    /**
     * Copies data from host to GPU.
     *
     * @param gpuHandle GPU memory handle
     * @param hostBuffer Host buffer
     * @param sizeBytes Number of bytes to copy
     */
    void copyToGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes);

    /**
     * Copies data from GPU to host.
     *
     * @param gpuHandle GPU memory handle
     * @param hostBuffer Host buffer
     * @param sizeBytes Number of bytes to copy
     */
    void copyFromGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes);

    /**
     * Frees GPU memory.
     *
     * @param gpuHandle GPU memory handle
     */
    void freeGPUMemory(long gpuHandle);

    /**
     * Synchronizes GPU execution (waits for all kernels to complete).
     */
    void synchronize();

    /**
     * Performs matrix multiplication C = A * B on the GPU.
     * Dimensions: A(m x k), B(k x n), C(m x n).
     */
    void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k);
}

