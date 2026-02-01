/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.gpu.demo;

import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLBackend;
import org.jscience.core.technical.backend.gpu.cuda.CUDABackend;
import java.nio.DoubleBuffer;
import java.util.Random;

/**
 * Demo for GPU-accelerated matrix multiplication using CUDA or OpenCL.
 */
public class GPUMatrixDemo {

    public static void main(String[] args) {
        int n = 1024;
        System.out.println("Initializing " + n + "x" + n + " matrices...");
        
        DoubleBuffer A = createRandomBuffer(n * n);
        DoubleBuffer B = createRandomBuffer(n * n);
        DoubleBuffer C = DoubleBuffer.allocate(n * n);
        
        GPUBackend gpu = null;
        
        // Try CUDA first
        CUDABackend cuda = new CUDABackend();
        if (cuda.isAvailable()) {
            System.out.println("Using CUDA Backend");
            gpu = cuda;
        } else {
            OpenCLBackend ocl = new OpenCLBackend();
            if (ocl.isAvailable()) {
                System.out.println("Using OpenCL Backend");
                gpu = ocl;
            }
        }
        
        if (gpu == null) {
            System.out.println("No GPU backend available. Falling back to CPU...");
            // Standard CPU implementation demo...
            return;
        }
        
        GPUBackend.DeviceInfo[] devices = gpu.getDevices();
        if (devices.length > 0) {
            System.out.println("Found devices:");
            for (int i = 0; i < devices.length; i++) {
                System.out.println(i + ": " + devices[i]);
            }
            gpu.selectDevice(0);
        }
        
        long start = System.nanoTime();
        gpu.matrixMultiply(A, B, C, n, n, n);
        gpu.synchronize();
        long end = System.nanoTime();
        
        System.out.printf("GPU Matrix Multiply (%dx%d) took: %.3f ms\n", 
            n, n, (end - start) / 1e6);
            
        // Verification (checksum)
        double sum = 0;
        for (int i = 0; i < 100; i++) sum += C.get(i);
        System.out.println("Result checksum (first 100): " + sum);
    }

    private static DoubleBuffer createRandomBuffer(int size) {
        DoubleBuffer db = DoubleBuffer.allocate(size);
        Random r = new Random(42);
        for (int i = 0; i < size; i++) db.put(i, r.nextDouble());
        return db;
    }
}
