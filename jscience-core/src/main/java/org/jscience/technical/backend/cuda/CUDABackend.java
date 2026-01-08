/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.technical.backend.cuda;

import org.jscience.technical.backend.ComputeBackend;
import org.jscience.technical.backend.ExecutionContext;
import jcuda.driver.JCudaDriver;

/**
 * CUDA implementation of ComputeBackend for GPU acceleration.
 * <p>
 * Uses JCuda to execute computations on NVIDIA GPUs.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CUDABackend implements ComputeBackend {

    private static boolean available;

    static {
        try {
            // Check if JCuda is available
            JCudaDriver.setExceptionsEnabled(false);
            // Just accessing the class might throw if library not found
            Class.forName("jcuda.driver.JCudaDriver");
            available = true;
        } catch (Throwable t) {
            available = false;
        }
    }

    @Override
    public String getId() {
        return "cuda";
    }

    @Override
    public String getName() {
        return "GPU (CUDA)";
    }

    @Override
    public String getDescription() {
        return "Hardware acceleration via NVIDIA CUDA cores for high-performance computing.";
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public ExecutionContext createContext() {
        if (!available) {
            throw new IllegalStateException("CUDA backend is not available");
        }
        return new CUDAExecutionContext();
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public int getPriority() {
        return 20; // Higher priority than OpenCL if available
    }
}
