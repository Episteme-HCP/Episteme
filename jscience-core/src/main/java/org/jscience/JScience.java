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
 * JScience.setComputeMode(ComputeMode.CUDA);
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
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class JScience {

    private JScience() {
        // Prevent instantiation
    }

    static {
        loadPreferences();
        loadVersionInfo();
    }

    /** The version string (e.g., "5.0.0-SNAPSHOT") */
    public static String VERSION;

    /** The build date (e.g., "2025-12-29") */
    public static String BUILD_DATE;

    /** The authors of JScience */
    public static final String[] AUTHORS = {
            "Silvere Martin-Michiellot",
            "Gemini AI (Google DeepMind)"
    };

    private static void loadVersionInfo() {
        String v = "5.0.0-SNAPSHOT";
        String d = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        try (java.io.InputStream is = JScience.class.getResourceAsStream("/jscience.properties")) {
            if (is != null) {
                java.util.Properties p = new java.util.Properties();
                p.load(is);
                v = p.getProperty("jscience.version", v);
                d = p.getProperty("jscience.build.date", d);
            }
        } catch (Exception e) {
            // Ignore - use defaults
        }
        VERSION = v;
        BUILD_DATE = d;
    }

    /**
     * Saves current settings to user preferences.
     */
    /**
     * Saves current settings to user preferences.
     */
    public static void savePreferences() {
        try {
            org.jscience.io.UserPreferences prefs = org.jscience.io.UserPreferences.getInstance();
            prefs.set("compute.mode", getComputeMode().name());
            prefs.set("float.precision", getFloatPrecisionMode().name());
            prefs.set("int.precision", getIntPrecisionMode().name());
            java.math.MathContext mc = getMathContext();
            prefs.set("math.precision", String.valueOf(mc.getPrecision()));
            prefs.set("math.rounding", mc.getRoundingMode().name());

            // Backends are now persisted immediately on setXXX(), so mostly no need to save here
            // except for legacy plotting/linear algebra fields kept in this class
            if (plottingBackend2D != null) prefs.set("plotting.backend.2d", plottingBackend2D.name());
            if (plottingBackend3D != null) prefs.set("plotting.backend.3d", plottingBackend3D.name());
            if (getLinearAlgebraProviderId() != null) prefs.set("linear.algebra.backend", getLinearAlgebraProviderId());

            prefs.save();
        } catch (Exception e) {
            System.err.println("Failed to save preferences: " + e.getMessage());
        }
    }

    /**
     * Loads settings from user preferences.
     */
    /**
     * Loads settings from user preferences.
     */
    public static void loadPreferences() {
        try {
            org.jscience.io.UserPreferences prefs = org.jscience.io.UserPreferences.getInstance();

            String modeStr = prefs.get("compute.mode");
            if (modeStr != null) {
                setComputeMode(ComputeMode.valueOf(modeStr));
            }

            String floatStr = prefs.get("float.precision");
            if (floatStr != null) {
                ComputeContext.current().setFloatPrecision(ComputeContext.FloatPrecision.valueOf(floatStr));
            }

            String intStr = prefs.get("int.precision");
            if (intStr != null) {
                ComputeContext.current().setIntPrecision(ComputeContext.IntPrecision.valueOf(intStr));
            }

            try {
                String precStr = prefs.get("math.precision");
                int prec = (precStr != null) ? Integer.parseInt(precStr) : 34;
                String rmStr = prefs.get("math.rounding", "HALF_EVEN");
                java.math.RoundingMode rm = java.math.RoundingMode.valueOf(rmStr);
                setMathContext(new java.math.MathContext(prec, rm));
            } catch (Exception e) {
                // Ignore math context errors
            }

            // Load Backends (Using setters to populate runtime if needed, though mostly direct access is preferred)
            try {
                String pb2d = prefs.get("plotting.backend.2d");
                if (pb2d != null) plottingBackend2D = org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend.valueOf(pb2d);
                
                String pb3d = prefs.get("plotting.backend.3d");
                if (pb3d != null) plottingBackend3D = org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend.valueOf(pb3d);
                
                // No need to load other backends into local fields as they are now fetching directly from UserPreferences
                // The getters (getMolecularBackendId, etc.) read from UserPreferences directly.
                
            } catch (Exception e) {
                // Ignore backend loading errors
            }
        } catch (Exception e) {
            // Ignore - use defaults
        }
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
            case OPENCL:
                ComputeContext.current().setBackend(ComputeContext.Backend.OPENCL_GPU);
                break;
            case CUDA:
                ComputeContext.current().setBackend(ComputeContext.Backend.CUDA_GPU);
                break;
            case CPU:
                ComputeContext.current().setBackend(ComputeContext.Backend.JAVA_CPU);
                break;
            case AUTO:
                if (isGpuAvailable()) {
                    ComputeContext.current().setBackend(ComputeContext.Backend.OPENCL_GPU);
                    // AUTO logic: On GPU, prioritize Fast performance
                    setFloatPrecision();
                    setIntPrecision();
                } else {
                    ComputeContext.current().setBackend(ComputeContext.Backend.JAVA_CPU);
                    // AUTO logic: On CPU, prioritize Standard precision
                    setStandardPrecision();
                    setLongPrecision();
                }
                break;
        }
    }

    /**
     * Re-applies AUTO selection logic based on current environment.
     */
    public static void refreshAutoSettings() {
        if (getComputeMode() == ComputeMode.AUTO) {
            setComputeMode(ComputeMode.AUTO);
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
     * but limited to range Ã‚Â±2.1 billion.
     * </p>
     */
    public static void setIntPrecision() {
        ComputeContext.current().setIntPrecision(ComputeContext.IntPrecision.INT);
    }

    /**
     * Sets 64-bit long precision for GPU/numerical integer operations.
     * <p>
     * Larger range (Ã‚Â±9.2 Ãƒâ€” 10^18) but slower on some GPUs,
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

    // ================= MATH CONTEXT =================

    /**
     * Sets the global java.math.MathContext for arbitrary precision operations.
     */
    public static void setMathContext(java.math.MathContext mathContext) {
        ComputeContext.current().setMathContext(mathContext);
    }

    /**
     * Returns the current java.math.MathContext.
     */
    public static java.math.MathContext getMathContext() {
        return ComputeContext.current().getMathContext();
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
        setDoublePrecision();
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

    /**
     * Checks if GPU acceleration is available on the current system.
     * 
     * @return true if GPU is available, false otherwise
     */
    public static boolean isGpuAvailable() {
        return isCudaAvailable() || isOpenCLAvailable();
    }

    /**
     * Checks if CUDA is available.
     */
    public static boolean isCudaAvailable() {
        try {
            return new org.jscience.technical.backend.cuda.CUDABackend().isAvailable();
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Checks if OpenCL is available.
     */
    public static boolean isOpenCLAvailable() {
        try {
            return new org.jscience.technical.backend.opencl.OpenCLBackend().isAvailable();
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Checks if ND4J is available in the classpath.
     */
    public static boolean isND4JAvailable() {
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Checks if Apache Spark is available in the classpath.
     */
    public static boolean isSparkAvailable() {
        try {
            Class.forName("org.apache.spark.api.java.JavaSparkContext");
            return true;
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
    // ================= PLOTTING BACKEND =================

    private static org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend plottingBackend2D = org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend.XCHART;
    private static org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend plottingBackend3D = org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend.JZY3D;

    /**
     * Gets the current 2D plotting backend.
     */
    public static org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend getPlottingBackend2D() {
        return plottingBackend2D;
    }

    /**
     * Sets the current 2D plotting backend.
     */
    public static void setPlottingBackend2D(org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend backend) {
        plottingBackend2D = backend;
    }

    /**
     * Gets the current 3D plotting backend.
     */
    public static org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend getPlottingBackend3D() {
        return plottingBackend3D;
    }

    /**
     * Sets the current 3D plotting backend.
     */
    /**
     * Sets the current 3D plotting backend.
     */
    public static void setPlottingBackend3D(org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend backend) {
        plottingBackend3D = backend;
    }
    
    // ================= QUANTUM BACKEND =================
    
    /**
     * Gets the ID of the current Quantum Backend.
     */
    public static String getQuantumBackendId() {
        return org.jscience.io.UserPreferences.getInstance().getPreferredBackend("quantum");
    }

    /**
     * Sets the Quantum Backend by ID.
     */
    public static void setQuantumBackendId(String id) {
        org.jscience.io.UserPreferences.getInstance().setPreferredBackend("quantum", id);
    }

    // ================= LINEAR ALGEBRA BACKEND =================

    /**
     * Gets the ID of the current Linear Algebra Provider.
     */
    public static String getLinearAlgebraProviderId() {
        String val = org.jscience.io.UserPreferences.getInstance().get("linear.algebra.backend");
        return (val != null) ? val : "cpu-dense";
    }

    /**
     * Sets the Linear Algebra Provider by ID.
     */
    public static void setLinearAlgebraProviderId(String id) {
        org.jscience.io.UserPreferences.getInstance().set("linear.algebra.backend", id);
        org.jscience.io.UserPreferences.getInstance().save(); // Force save as this might be critical
    }

    // ================= MATH BACKEND =================

    public static String getMathBackendId() {
        return org.jscience.io.UserPreferences.getInstance().getPreferredBackend("math");
    }

    public static void setMathBackendId(String id) {
        org.jscience.io.UserPreferences.getInstance().setPreferredBackend("math", id);
    }

    // ================= TENSOR BACKEND =================

    public static String getTensorBackendId() {
        return org.jscience.io.UserPreferences.getInstance().getPreferredBackend("tensor");
    }

    public static void setTensorBackendId(String id) {
        org.jscience.io.UserPreferences.getInstance().setPreferredBackend("tensor", id);
    }

    // ================= MOLECULAR BACKEND =================

    /**
     * Gets the ID of the current Molecular Backend.
     */
    public static String getMolecularBackendId() {
        return org.jscience.io.UserPreferences.getInstance().getPreferredBackend("molecular");
    }

    /**
     * Sets the Molecular Backend by ID.
     */
    public static void setMolecularBackendId(String id) {
        org.jscience.io.UserPreferences.getInstance().setPreferredBackend("molecular", id);
    }

    // ================= MAP BACKEND =================

    /**
     * Gets the ID of the current Map Backend.
     */
    public static String getMapBackendId() {
        return org.jscience.io.UserPreferences.getInstance().getPreferredBackend("map");
    }

    /**
     * Sets the Map Backend by ID.
     */
    public static void setMapBackendId(String id) {
        org.jscience.io.UserPreferences.getInstance().setPreferredBackend("map", id);
    }

    // ================= NETWORK BACKEND =================

    /**
     * Gets the ID of the current Network Backend.
     */
    public static String getNetworkBackendId() {
        return org.jscience.io.UserPreferences.getInstance().getPreferredBackend("network");
    }

    /**
     * Sets the Network Backend by ID.
     */
    public static void setNetworkBackendId(String id) {
        org.jscience.io.UserPreferences.getInstance().setPreferredBackend("network", id);
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

        // Launch Master Control
        try {
            System.out.println("Launching JScience Master Control...");
            javafx.application.Application.launch(org.jscience.ui.JScienceMasterControl.class, args);
            return;
        } catch (Throwable e) {
            System.out.println("JScience Master Control not available or JavaFX missing. Showing CLI report. " + e.getMessage());
        }

        System.out.println(getConfigurationReport());
    }
}
