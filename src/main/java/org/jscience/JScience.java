package org.jscience;

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

import org.jscience.mathematics.context.ComputeMode;
import org.jscience.mathematics.context.MathContext;

/**
 * The central entry point and configuration dashboard for the JScience library.
 * <p>
 * This class provides static methods to configure global settings, such as
 * computation modes (CPU/GPU), precision settings, and other library-wide
 * preferences.
 * </p>
 * 
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * // Configure for high-performance GPU computing
 * JScience.setComputeMode(ComputeMode.GPU);
 * 
 * // Configure floating-point precision
 * JScience.setFloatPrecision(); // 32-bit float - faster
 * JScience.setDoublePrecision(); // 64-bit double - more precise
 * 
 * // Configure integer precision
 * JScience.setIntPrecision(); // 32-bit int - faster on most GPUs
 * JScience.setLongPrecision(); // 64-bit long - larger range
 * }</pre>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class JScience {

    private JScience() {
        // Prevent instantiation
    }

    // ================= COMPUTE MODE =================

    /**
     * Sets the global computation mode for linear algebra operations.
     * 
     * @param mode the compute mode (CPU, GPU, AUTO)
     */
    public static void setComputeMode(ComputeMode mode) {
        MathContext.setCurrent(MathContext.getCurrent().withComputeMode(mode));
        // Also update ComputeContext backend
        switch (mode) {
            case GPU:
                ComputeContext.current().setBackend(ComputeContext.Backend.OPENCL_GPU);
                break;
            case CPU:
                ComputeContext.current().setBackend(ComputeContext.Backend.JAVA_CPU);
                break;
            case AUTO:
                if (isGpuAvailable()) {
                    ComputeContext.current().setBackend(ComputeContext.Backend.OPENCL_GPU);
                } else {
                    ComputeContext.current().setBackend(ComputeContext.Backend.JAVA_CPU);
                }
                break;
        }
    }

    /**
     * Returns the current computation mode.
     * 
     * @return the current compute mode
     */
    public static ComputeMode getComputeMode() {
        return MathContext.getCurrent().getComputeMode();
    }

    // ================= FLOATING-POINT PRECISION =================

    /**
     * Sets the library to use exact arithmetic (BigDecimal) where applicable.
     */
    public static void setExactPrecision() {
        MathContext.setCurrent(MathContext.getCurrent().withRealPrecision(MathContext.RealPrecision.EXACT));
    }

    /**
     * Sets the library to use standard floating-point arithmetic (double).
     */
    public static void setStandardPrecision() {
        MathContext.setCurrent(MathContext.getCurrent().withRealPrecision(MathContext.RealPrecision.NORMAL));
        ComputeContext.current().setFloatPrecision(ComputeContext.FloatPrecision.DOUBLE);
    }

    /**
     * Sets the library to use fast floating-point arithmetic (float).
     */
    public static void setFastPrecision() {
        MathContext.setCurrent(MathContext.getCurrent().withRealPrecision(MathContext.RealPrecision.FAST));
        ComputeContext.current().setFloatPrecision(ComputeContext.FloatPrecision.FLOAT);
    }

    /**
     * Sets 32-bit float precision for GPU/numerical operations.
     * <p>
     * Faster on most GPUs but lower precision (~7 decimal digits).
     * </p>
     */
    public static void setFloatPrecision() {
        ComputeContext.current().setFloatPrecision(ComputeContext.FloatPrecision.FLOAT);
    }

    /**
     * Sets 64-bit double precision for GPU/numerical operations.
     * <p>
     * Higher precision (~15 decimal digits) but slower on many GPUs.
     * </p>
     */
    public static void setDoublePrecision() {
        ComputeContext.current().setFloatPrecision(ComputeContext.FloatPrecision.DOUBLE);
    }

    /**
     * Returns the current floating-point precision mode.
     */
    public static ComputeContext.FloatPrecision getFloatPrecisionMode() {
        return ComputeContext.current().getFloatPrecision();
    }

    // ================= INTEGER PRECISION =================

    /**
     * Sets 32-bit int precision for GPU/numerical integer operations.
     * <p>
     * Faster on most GPUs (especially consumer GPUs), smaller memory footprint,
     * but limited to range ±2.1 billion.
     * </p>
     */
    public static void setIntPrecision() {
        ComputeContext.current().setIntPrecision(ComputeContext.IntPrecision.INT);
    }

    /**
     * Sets 64-bit long precision for GPU/numerical integer operations.
     * <p>
     * Larger range (±9.2 × 10^18) but slower on some GPUs,
     * as many consumer GPUs have weak int64 support.
     * </p>
     */
    public static void setLongPrecision() {
        ComputeContext.current().setIntPrecision(ComputeContext.IntPrecision.LONG);
    }

    /**
     * Returns the current integer precision mode.
     */
    public static ComputeContext.IntPrecision getIntPrecisionMode() {
        return ComputeContext.current().getIntPrecision();
    }

    // ================= CONVENIENCE CONFIGURATIONS =================

    /**
     * Configures the library for maximum performance.
     * <p>
     * Sets ComputeMode to AUTO (GPU if available), float precision to FLOAT,
     * and integer precision to INT.
     * </p>
     */
    public static void configureForPerformance() {
        setComputeMode(ComputeMode.AUTO);
        setFloatPrecision();
        setIntPrecision();
    }

    /**
     * Configures the library for maximum precision.
     * <p>
     * Sets ComputeMode to CPU (usually required for arbitrary precision),
     * precision to EXACT, and integer precision to LONG.
     * </p>
     */
    public static void configureForPrecision() {
        setComputeMode(ComputeMode.CPU);
        setExactPrecision();
        setLongPrecision();
    }

    /**
     * Configures the library for balanced performance/precision.
     * <p>
     * Uses GPU if available with double precision for floats and long for integers.
     * </p>
     */
    public static void configureBalanced() {
        setComputeMode(ComputeMode.AUTO);
        setDoublePrecision();
        setLongPrecision();
    }

    // ================= INTROSPECTION & REPORTING =================

    /**
     * Checks if GPU acceleration is available on the current system.
     * 
     * @return true if GPU is available, false otherwise
     */
    public static boolean isGpuAvailable() {
        try {
            // Check both CUDA and OpenCL backends
            boolean cuda = new org.jscience.technical.backend.cuda.CudaBackend().isAvailable();
            boolean opencl = new org.jscience.technical.backend.opencl.OpenCLBackend().isAvailable();
            return cuda || opencl;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Returns a collection of available compute backend names.
     * 
     * @return collection of backend names (e.g., "Java CPU", "CUDA-JCublas")
     */
    public static java.util.Collection<String> getAvailableBackends() {
        return org.jscience.technical.backend.BackendManager.getAvailableBackendNames();
    }

    /**
     * Returns the global compute context for advanced configuration.
     * <p>
     * Use this to register custom providers, check available backends, etc.
     * </p>
     */
    public static ComputeContext getComputeContext() {
        return ComputeContext.current();
    }

    /**
     * Returns a report of the current configuration and capabilities.
     * 
     * @return a string describing the current state
     */
    public static String getConfigurationReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("JScience Configuration Report\n");
        sb.append("=============================\n");
        sb.append("Compute Mode: ").append(getComputeMode()).append("\n");
        sb.append("Backend: ").append(ComputeContext.current().getBackend()).append("\n");
        sb.append("Float Precision: ").append(getFloatPrecisionMode()).append("\n");
        sb.append("Integer Precision: ").append(getIntPrecisionMode()).append("\n");
        sb.append("Real Precision: ").append(MathContext.getCurrent().getRealPrecision()).append("\n");
        sb.append("GPU Available: ").append(isGpuAvailable() ? "Yes" : "No (or not loaded)").append("\n");
        sb.append("Available Backends: ").append(getAvailableBackends()).append("\n");
        sb.append("Registered Providers: ").append(ComputeContext.current().toString()).append("\n");
        return sb.toString();
    }

    /**
     * Main entry point for CLI usage and configuration verification.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Parse system properties
        String modeProp = System.getProperty("org.jscience.compute.mode");
        if (modeProp != null) {
            try {
                ComputeMode mode = ComputeMode.valueOf(modeProp.toUpperCase());
                setComputeMode(mode);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid compute mode: " + modeProp);
            }
        }

        String floatProp = System.getProperty("org.jscience.float.precision");
        if ("float".equalsIgnoreCase(floatProp)) {
            setFloatPrecision();
        } else if ("double".equalsIgnoreCase(floatProp)) {
            setDoublePrecision();
        }

        String intProp = System.getProperty("org.jscience.int.precision");
        if ("int".equalsIgnoreCase(intProp)) {
            setIntPrecision();
        } else if ("long".equalsIgnoreCase(intProp)) {
            setLongPrecision();
        }

        System.out.println(getConfigurationReport());
    }
}
