/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.backend;

import java.util.logging.Logger;

/**
 * Centralized Compute Backend Manager.
 * <p>
 * Controls GPU, Distributed (Spark/Flink), and CPU execution with transparent
 * fallback.
 * Users can query current backend status and fallback modes.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class ComputeBackendManager {

    private static final Logger LOG = Logger.getLogger(ComputeBackendManager.class.getName());

    private static ComputeBackendManager INSTANCE = new ComputeBackendManager();

    private BackendType preferredBackend = BackendType.CPU_PURE_JAVA;
    private BackendType currentBackend = BackendType.CPU_PURE_JAVA;
    private boolean gpuAvailable = false;
    private boolean sparkAvailable = false;
    private boolean nativeBlasAvailable = false;
    private boolean inFallbackMode = false;
    private String fallbackReason = null;

    public enum BackendType {
        GPU_CUDA("GPU (CUDA)", true, false),
        DISTRIBUTED_SPARK("Distributed (Spark)", false, true),
        CPU_NATIVE_BLAS("CPU (Native BLAS)", false, false),
        CPU_PURE_JAVA("CPU (Pure Java)", false, false);

        public final String displayName;
        public final boolean requiresGPU;
        public final boolean requiresDistributed;

        BackendType(String displayName, boolean requiresGPU, boolean requiresDistributed) {
            this.displayName = displayName;
            this.requiresGPU = requiresGPU;
            this.requiresDistributed = requiresDistributed;
        }
    }

    private ComputeBackendManager() {
        detectAvailableBackends();
    }

    public static ComputeBackendManager getInstance() {
        return INSTANCE;
    }

    /**
     * Detect available backends at startup.
     */
    private void detectAvailableBackends() {
        // Check GPU (CUDA/JCuda)
        try {
            Class.forName("jcuda.Pointer");
            gpuAvailable = true;
            LOG.info("GPU (CUDA) backend detected");
        } catch (ClassNotFoundException e) {
            gpuAvailable = false;
            LOG.fine("GPU backend not available: JCuda not found");
        }

        // Check Spark
        try {
            Class.forName("org.apache.spark.SparkConf");
            sparkAvailable = true;
            LOG.info("Distributed (Spark) backend detected");
        } catch (ClassNotFoundException e) {
            sparkAvailable = false;
            LOG.fine("Distributed backend not available: Spark not found");
        }

        // Check Native BLAS
        try {
            Class.forName("org.netlib.blas.BLAS");
            nativeBlasAvailable = true;
            LOG.info("Native BLAS backend detected");
        } catch (ClassNotFoundException e) {
            nativeBlasAvailable = false;
            LOG.fine("Native BLAS not available");
        }
    }

    /**
     * Set preferred backend. Will fallback if unavailable.
     */
    public void setPreferredBackend(BackendType backend) {
        this.preferredBackend = backend;
        selectBestAvailableBackend();
    }

    /**
     * Select best available backend based on preference and availability.
     */
    private void selectBestAvailableBackend() {
        inFallbackMode = false;
        fallbackReason = null;

        // Try preferred backend first
        if (isBackendAvailable(preferredBackend)) {
            currentBackend = preferredBackend;
            LOG.info("Using preferred backend: " + currentBackend.displayName);
            return;
        }

        // Fallback logic
        inFallbackMode = true;
        fallbackReason = preferredBackend.displayName + " not available";

        // Fallback chain: GPU → Distributed → Native BLAS → Pure Java
        if (gpuAvailable && preferredBackend.requiresGPU) {
            currentBackend = BackendType.GPU_CUDA;
        } else if (sparkAvailable && preferredBackend.requiresDistributed) {
            currentBackend = BackendType.DISTRIBUTED_SPARK;
        } else if (nativeBlasAvailable) {
            currentBackend = BackendType.CPU_NATIVE_BLAS;
        } else {
            currentBackend = BackendType.CPU_PURE_JAVA;
        }

        LOG.warning("Fallback to " + currentBackend.displayName + ": " + fallbackReason);
    }

    /**
     * Check if backend is available.
     */
    private boolean isBackendAvailable(BackendType backend) {
        switch (backend) {
            case GPU_CUDA:
                return gpuAvailable;
            case DISTRIBUTED_SPARK:
                return sparkAvailable;
            case CPU_NATIVE_BLAS:
                return nativeBlasAvailable;
            case CPU_PURE_JAVA:
                return true; // Always available
            default:
                return false;
        }
    }

    // Getters for status queries

    public BackendType getPreferredBackend() {
        return preferredBackend;
    }

    public BackendType getCurrentBackend() {
        return currentBackend;
    }

    public boolean isInFallbackMode() {
        return inFallbackMode;
    }

    public String getFallbackReason() {
        return fallbackReason;
    }

    public boolean isGpuAvailable() {
        return gpuAvailable;
    }

    public boolean isSparkAvailable() {
        return sparkAvailable;
    }

    public boolean isNativeBlasAvailable() {
        return nativeBlasAvailable;
    }

    /**
     * Get user-friendly status string.
     */
    public String getStatusString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Compute Backend Status:\n");
        sb.append("  Preferred: ").append(preferredBackend.displayName).append("\n");
        sb.append("  Current:   ").append(currentBackend.displayName);
        if (inFallbackMode) {
            sb.append(" [FALLBACK: ").append(fallbackReason).append("]");
        }
        sb.append("\n");
        sb.append("  Available Backends:\n");
        sb.append("    GPU (CUDA):          ").append(gpuAvailable ? "✓" : "✗").append("\n");
        sb.append("    Distributed (Spark): ").append(sparkAvailable ? "✓" : "✗").append("\n");
        sb.append("    Native BLAS:         ").append(nativeBlasAvailable ? "✓" : "✗").append("\n");
        sb.append("    Pure Java:           ✓\n");
        return sb.toString();
    }

    /**
     * Print status to console.
     */
    public void printStatus() {
        System.out.println(getStatusString());
    }
}


