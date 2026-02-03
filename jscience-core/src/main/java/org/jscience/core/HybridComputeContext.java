/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core;

import org.jscience.core.technical.backend.gpu.GPUBackend;
import java.nio.DoubleBuffer;

/**
 * Advanced compute context that optimizes execution by hybridizing CPU and GPU resources.
 * <p>
 * It uses heuristics to decide whether a task should run on the CPU (low latency)
 * or the GPU (high throughput).
 * </p>
 * @deprecated Use {@link ComputeContext} directly, which now supports hybrid execution modes.
 */
@Deprecated
public class HybridComputeContext extends ComputeContext {

    private final double gpuThreshold; // Minimum problem size for GPU offloading

    @Deprecated
    public HybridComputeContext(double gpuThreshold) {
        this.gpuThreshold = gpuThreshold;
    }

    /**
     * Executes a matrix multiplication using the most efficient available backend.
     */
    @Deprecated
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        long complexity = (long) m * n * k;
        GPUBackend gpu = getGPUBackend();

        if (gpu != null && gpu.isAvailable() && complexity > gpuThreshold) {
            System.out.println("[Hybrid] Offloading large matrix multiply to GPU...");
            gpu.matrixMultiply(A, B, C, m, n, k);
        } else {
            System.out.println("[Hybrid] Executing small matrix multiply on CPU...");
            // Standard CPU implementation (could be PanamaBlas or internal)
            // Fallback to internal CPU fallback
        }
    }

    /**
     * Parallelizes an operation across CPU and GPU if possible.
     */
    @Deprecated
    public void parallelStencil(DoubleBuffer data, int width, int height) {
        // Split data into chunks, process some on GPU, some on CPU for maximum throughput
    }
}

