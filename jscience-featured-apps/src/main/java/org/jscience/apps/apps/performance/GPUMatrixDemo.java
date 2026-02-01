/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.apps.performance;

import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.BackendManager;
import java.nio.DoubleBuffer;
import java.util.Random;

/**
 * Demo showcasing GPU-accelerated matrix multiplication.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class GPUMatrixDemo {

    public static void main(String[] args) {
        System.out.println("=== JScience GPU Matrix Multiplication Demo ===");

        GPUBackend gpu = null;
        try {
            gpu = (GPUBackend) BackendManager.getAllBackends().stream()
                    .filter(b -> b instanceof GPUBackend && b.isAvailable())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.err.println("Error discovering GPU backends: " + e.getMessage());
        }

        if (gpu == null) {
            System.out.println("No GPU acceleration available (neither CUDA nor OpenCL).");
            System.out.println("Available backends: " + BackendManager.getAvailableBackendNames());
            return;
        }

        System.out.println("Selected GPU backend: " + gpu.getName());
        GPUBackend.DeviceInfo[] devices = gpu.getDevices();
        for (int i = 0; i < devices.length; i++) {
            System.out.println("  Device [" + i + "]: " + devices[i]);
        }

        int N = 1024;
        System.out.println("\nPerforming " + N + "x" + N + " matrix multiplication...");

        DoubleBuffer A = DoubleBuffer.allocate(N * N);
        DoubleBuffer B = DoubleBuffer.allocate(N * N);
        DoubleBuffer C = DoubleBuffer.allocate(N * N);

        Random rand = new Random();
        for (int i = 0; i < N * N; i++) {
            A.put(i, rand.nextDouble());
            B.put(i, rand.nextDouble());
        }

        System.out.println("Running on GPU...");
        long start = System.currentTimeMillis();
        try {
            gpu.matrixMultiply(A, B, C, N, N, N);
            long end = System.currentTimeMillis();
            System.out.println("GPU Time: " + (end - start) + " ms");
        } catch (Exception e) {
            System.err.println("GPU execution failed: " + e.getMessage());
        }

        System.out.println("\nDemo completed.");
    }
}
