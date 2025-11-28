package org.jscience;

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
 * // Configure for exact arithmetic
 * JScience.setExactPrecision();
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

    /**
     * Sets the global computation mode for linear algebra operations.
     * 
     * @param mode the compute mode (CPU, GPU, AUTO)
     */
    /**
     * Sets the global computation mode for linear algebra operations.
     * 
     * @param mode the compute mode (CPU, GPU, AUTO)
     */
    public static void setComputeMode(ComputeMode mode) {
        MathContext.setCurrent(MathContext.getCurrent().withComputeMode(mode));
    }

    /**
     * Returns the current computation mode.
     * 
     * @return the current compute mode
     */
    public static ComputeMode getComputeMode() {
        return MathContext.getCurrent().getComputeMode();
    }

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
    }

    /**
     * Sets the library to use fast floating-point arithmetic (float).
     */
    public static void setFastPrecision() {
        MathContext.setCurrent(MathContext.getCurrent().withRealPrecision(MathContext.RealPrecision.FAST));
    }

    /**
     * Configures the library for maximum performance.
     * Sets ComputeMode to AUTO (or GPU if available) and precision to FAST or
     * NORMAL.
     */
    public static void configureForPerformance() {
        setComputeMode(ComputeMode.AUTO);
        setStandardPrecision();
    }

    /**
     * Configures the library for maximum precision.
     * Sets ComputeMode to CPU (usually required for arbitrary precision) and
     * precision to EXACT.
     */
    public static void configureForPrecision() {
        setComputeMode(ComputeMode.CPU);
        setExactPrecision();
    }

    // --- Introspection & Logging ---

    /**
     * Checks if GPU acceleration is available on the current system.
     * 
     * @return true if GPU is available, false otherwise
     */
    public static boolean isGpuAvailable() {
        try {
            return org.jscience.mathematics.provider.CudaLinearAlgebraProvider.isAvailable();
        } catch (Throwable t) {
            return false;
        }
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
        sb.append("Real Precision: ").append(MathContext.getCurrent().getRealPrecision()).append("\n");
        sb.append("GPU Available: ").append(isGpuAvailable() ? "Yes" : "No (or not loaded)").append("\n");
        // Add more details as needed
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

        System.out.println(getConfigurationReport());
    }
}
