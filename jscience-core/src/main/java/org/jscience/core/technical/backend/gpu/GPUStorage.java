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

package org.jscience.core.technical.backend.gpu;

/**
 * Generic interface for GPU memory storage.
 * <p>
 * Abstracts GPU memory management across different backends (CUDA, OpenCL, Vulkan, etc.).
 * Implementations handle allocation, data transfer, and deallocation of GPU memory.
 * </p>
 * <p>
 * <b>Implementations:</b>
 * <ul>
 * <li><b>CUDA</b>: {@code CUDAStorage} - NVIDIA GPU memory via JCuda</li>
 * <li><b>OpenCL</b>: {@code OpenCLStorage} - Cross-platform GPU memory via JOCL</li>
 * <li><b>Vulkan</b>: {@code VulkanStorage} - Modern GPU memory via LWJGL</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface GPUStorage extends AutoCloseable {

    /**
     * Returns the size of the allocated GPU memory in elements.
     * 
     * @return number of elements (typically doubles or floats)
     */
    int getSize();

    /**
     * Uploads data from host (CPU) memory to device (GPU) memory.
     * 
     * @param hostData array of data to upload
     * @throws IllegalArgumentException if data size doesn't match allocated size
     */
    void upload(double[] hostData);

    /**
     * Downloads data from device (GPU) memory to host (CPU) memory.
     * 
     * @return array of data from GPU
     */
    double[] download();

    /**
     * Returns the native GPU memory handle.
     * <p>
     * The type of this handle is backend-specific:
     * <ul>
     * <li>CUDA: {@code jcuda.Pointer}</li>
     * <li>OpenCL: {@code org.jocl.cl_mem}</li>
     * <li>Vulkan: {@code long} (VkDeviceMemory handle)</li>
     * </ul>
     * </p>
     * 
     * @return native GPU memory handle (type varies by backend)
     */
    Object getNativeHandle();

    /**
     * Frees the GPU memory.
     * <p>
     * This method is called automatically when using try-with-resources.
     * Implementations should be idempotent (safe to call multiple times).
     * </p>
     */
    @Override
    void close();

    /**
     * Checks if the GPU memory has been freed.
     * 
     * @return true if memory has been freed, false otherwise
     */
    default boolean isClosed() {
        return false; // Implementations should override
    }
}
