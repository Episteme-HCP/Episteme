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

package org.jscience.core.technical.backend.gpu.cuda;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.jcublas.JCublas;
import org.jscience.core.technical.backend.gpu.GPUStorage;

import java.lang.ref.Cleaner;

/**
 * CUDA implementation of GPU memory storage.
 * <p>
 * Manages GPU memory for CUDA operations using JCublas.
 * This class should only be used when the CUDA backend is available.
 * </p>
 * <p>
 * <b>Usage:</b> Obtain instances through {@link CUDABackend} rather than
 * direct instantiation to ensure proper backend initialization.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CUDAStorage implements GPUStorage {

    private static final Cleaner CLEANER = Cleaner.create();

    private static class State implements Runnable {
        private final Pointer pointer;

        State(Pointer pointer) {
            this.pointer = pointer;
        }

        @Override
        public void run() {
            try {
                // Warning: Context might be gone, but this is best effort
                JCublas.cublasFree(pointer);
            } catch (Throwable t) {
                // Ignore errors during cleanup
            }
        }
    }

    private final Pointer devicePointer;
    private final int size; // Number of elements (doubles)
    private final Cleaner.Cleanable cleanable;
    private volatile boolean closed = false;

    /**
     * Allocates memory on the GPU.
     * <p>
     * <b>Note:</b> Prefer using {@link CUDABackend#allocateStorage(int)}
     * to ensure proper backend initialization.
     * </p>
     * 
     * @param size number of double elements to allocate
     * @throws RuntimeException if CUDA allocation fails
     */
    public CUDAStorage(int size) {
        this.size = size;
        this.devicePointer = new Pointer();
        int status = JCublas.cublasAlloc(size, Sizeof.DOUBLE, devicePointer);
        if (status != 0) { // JCublas status 0 is success (CUBLAS_STATUS_SUCCESS)
            throw new RuntimeException("CUDA allocation failed with status: " + status);
        }
        this.cleanable = CLEANER.register(this, new State(devicePointer));
    }

    /**
     * Wraps an existing device pointer.
     * <p>
     * Takes ownership of the pointer and will free it on close.
     * </p>
     * 
     * @param devicePointer the pointer to device memory
     * @param size          number of elements
     */
    public CUDAStorage(Pointer devicePointer, int size) {
        this.devicePointer = devicePointer;
        this.size = size;
        this.cleanable = CLEANER.register(this, new State(devicePointer));
    }

    /**
     * Returns the CUDA device pointer.
     * 
     * @return CUDA Pointer to device memory
     */
    public Pointer getPointer() {
        return devicePointer;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void upload(double[] hostData) {
        if (closed) {
            throw new IllegalStateException("Storage has been closed");
        }
        if (hostData.length != size) {
            throw new IllegalArgumentException("Data size mismatch: expected " + size + ", got " + hostData.length);
        }
        JCublas.cublasSetVector(size, Sizeof.DOUBLE, Pointer.to(hostData), 1, devicePointer, 1);
    }

    @Override
    public double[] download() {
        if (closed) {
            throw new IllegalStateException("Storage has been closed");
        }
        double[] hostData = new double[size];
        JCublas.cublasGetVector(size, Sizeof.DOUBLE, devicePointer, 1, Pointer.to(hostData), 1);
        return hostData;
    }

    @Override
    public Object getNativeHandle() {
        return devicePointer;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            cleanable.clean();
        }
    }

}

