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

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.jcublas.JCublas;
import java.lang.ref.Cleaner;

/**
 * Manages GPU memory for CUDA operations.
 * <p>
 * Holds a pointer to device memory and handles allocation/deallocation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CUDAStorage implements AutoCloseable {

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

    /**
     * Allocates memory on the GPU.
     * 
     * @param size number of double elements to allocate
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
     * 
     * @param devicePointer the pointer to device memory
     * @param size          number of elements
     */
    public CUDAStorage(Pointer devicePointer, int size) {
        this.devicePointer = devicePointer;
        this.size = size;
        // For wrapped pointers, we typically don't own them,
        // but if we do, we should register. Assuming we DON'T own wrapped pointers for
        // now,
        // or we need a flag? The original code didn't set 'freed' here but close()
        // works.
        // Let's assume we own it if we wrap it, effectively taking ownership?
        // Original close() frees it. So yes, we check.
        this.cleanable = CLEANER.register(this, new State(devicePointer));
    }

    public Pointer getPointer() {
        return devicePointer;
    }

    public int getSize() {
        return size;
    }

    /**
     * Uploads data from host (CPU) to device (GPU).
     * 
     * @param hostData array of doubles
     */
    public void upload(double[] hostData) {
        if (hostData.length != size) {
            throw new IllegalArgumentException("Data size mismatch");
        }
        JCublas.cublasSetVector(size, Sizeof.DOUBLE, Pointer.to(hostData), 1, devicePointer, 1);
    }

    /**
     * Downloads data from device (GPU) to host (CPU).
     * 
     * @return array of doubles
     */
    public double[] download() {
        double[] hostData = new double[size];
        JCublas.cublasGetVector(size, Sizeof.DOUBLE, devicePointer, 1, Pointer.to(hostData), 1);
        return hostData;
    }

    /**
     * Frees the GPU memory.
     */
    @Override
    public void close() {
        cleanable.clean();
    }

}
