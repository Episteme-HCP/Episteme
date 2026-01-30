/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.gpu;

import org.jscience.technical.backend.ComputeBackend;
import java.nio.DoubleBuffer;

/**
 * GPU acceleration backend using CUDA or OpenCL.
 * <p>
 * This interface provides a unified API for GPU-accelerated computations,
 * supporting both NVIDIA CUDA and cross-platform OpenCL backends.
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
     * Performs matrix multiplication on GPU: C = A × B
     *
     * @param A Left matrix (row-major, size m×k)
     * @param B Right matrix (row-major, size k×n)
     * @param C Result matrix (row-major, size m×n)
     * @param m Number of rows in A
     * @param n Number of columns in B
     * @param k Common dimension
     */
    void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, 
                       int m, int n, int k);

    /**
     * Performs element-wise operations on GPU.
     *
     * @param operation Operation type: "add", "mul", "sin", "exp", etc.
     * @param input Input buffer
     * @param output Output buffer
     * @param size Number of elements
     */
    void elementWise(String operation, DoubleBuffer input, DoubleBuffer output, int size);

    /**
     * Computes Fast Fourier Transform on GPU.
     *
     * @param real Real part of input
     * @param imag Imaginary part of input
     * @param realOut Real part of output
     * @param imagOut Imaginary part of output
     * @param n Transform size (must be power of 2)
     * @param inverse If true, compute inverse FFT
     */
    void fft(DoubleBuffer real, DoubleBuffer imag, 
            DoubleBuffer realOut, DoubleBuffer imagOut, 
            int n, boolean inverse);

    /**
     * Reduces an array to a single value (sum, max, min, etc.).
     *
     * @param operation Reduction operation: "sum", "max", "min", "prod"
     * @param input Input buffer
     * @param size Number of elements
     * @return Reduced value
     */
    double reduce(String operation, DoubleBuffer input, int size);

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
}
