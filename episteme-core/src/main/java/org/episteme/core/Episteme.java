package org.episteme.core;

import org.episteme.core.mathematics.context.ComputeMode;
import org.episteme.core.mathematics.context.MathContext;


/**
 * The central entry point and configuration dashboard for the Episteme library.
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
 * Episteme.setComputeMode(ComputeMode.CUDA);
 * 
 * // Configure floating-point precision
 * Episteme.setFloatPrecision(); // 32-bit float - faster
 * Episteme.setDoublePrecision(); // 64-bit double - more precise
 * 
 * // Configure integer precision
 * Episteme.setIntPrecision(); // 32-bit int - faster on most GPUs
 * Episteme.setLongPrecision(); // 64-bit long - larger range
 * }</pre>
 * 
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Episteme {

    private Episteme() {
        // Prevent instantiation
    }

    private static final java.util.Properties properties = new java.util.Properties();

    static {
        loadConfiguration();
        loadPreferences();
        loadVersionInfo();
    }



    private static void loadConfiguration() {
        try (java.io.InputStream is = Episteme.class.getResourceAsStream("/episteme.properties")) {
            if (is != null) {
                properties.load(is);
            }
        } catch (Exception e) {
            // Ignore - defaults will be used or null returned
        }
    }

    /**
     * Gets a global configuration property from episteme.properties.
     * @param key the property key
     * @return the property value or null
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a global configuration property with default.
     * @param key the property key
     * @param defaultValue definition value
     * @return the property value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /** The version string (e.g., "5.0.0-SNAPSHOT") */
    public static String VERSION;

    /** The build date (e.g., "2025-12-29") */
    public static String BUILD_DATE;

    /** The authors of Episteme */
    public static final String[] AUTHORS = {
            "Silvere Martin-Michiellot",
            "Gemini AI (Google DeepMind)"
    };

    private static void loadVersionInfo() {
        String v = "1.0.0-SNAPSHOT";
        String d = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        try (java.io.InputStream is = Episteme.class.getResourceAsStream("/episteme.properties")) {
            if (is != null) {
                java.util.Properties p = new java.util.Properties();
                p.load(is);
                v = p.getProperty("episteme.version", v);
                d = p.getProperty("episteme.build.date", d);
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
    public static void savePreferences() {
        try {
            org.episteme.core.io.UserPreferences prefs = org.episteme.core.io.UserPreferences.getInstance();
            org.episteme.core.mathematics.context.NumericalConfiguration config = getNumericalConfiguration();
            
            prefs.set("compute.mode", config.getComputeMode().name());
            prefs.set("float.precision", config.getFloatPrecision().name());
            prefs.set("int.precision", config.getIntPrecision().name());
            
            java.math.MathContext mc = config.getMathContext();
            prefs.set("math.precision", String.valueOf(mc.getPrecision()));
            prefs.set("math.rounding", mc.getRoundingMode().name());

            // Save other preferred backends
            prefs.set("linear.algebra.backend", config.getBackendId());
            
            prefs.save();
        } catch (Exception e) {
            System.err.println("Failed to save preferences: " + e.getMessage());
        }
    }

    /**
     * Loads settings from user preferences.
     */
    public static void loadPreferences() {
        try {
            org.episteme.core.io.UserPreferences prefs = org.episteme.core.io.UserPreferences.getInstance();
            org.episteme.core.mathematics.context.NumericalConfiguration config = getNumericalConfiguration();

            String modeStr = prefs.get("compute.mode");
            if (modeStr != null) {
                config.applyComputeMode(org.episteme.core.mathematics.context.ComputeMode.valueOf(modeStr));
            }

            String floatStr = prefs.get("float.precision");
            if (floatStr != null) {
                config.setFloatPrecision(org.episteme.core.mathematics.context.NumericalConfiguration.FloatPrecision.valueOf(floatStr));
            }

            String intStr = prefs.get("int.precision");
            if (intStr != null) {
                config.setIntPrecision(org.episteme.core.mathematics.context.NumericalConfiguration.IntPrecision.valueOf(intStr));
            }

            try {
                String precStr = prefs.get("math.precision");
                int prec = (precStr != null) ? Integer.parseInt(precStr) : 34;
                String rmStr = prefs.get("math.rounding", "HALF_EVEN");
                java.math.RoundingMode rm = java.math.RoundingMode.valueOf(rmStr);
                config.setMathContext(new java.math.MathContext(prec, rm));
            } catch (Exception e) {
                // Ignore math context errors
            }
        } catch (Exception e) {
            // Ignore - use defaults
        }
    }

    private static final org.episteme.core.technical.algorithm.ProviderRegistry providerRegistry = new org.episteme.core.technical.algorithm.ProviderRegistry();
    
    /**
     * Gets the global provider registry.
     */
    public static org.episteme.core.technical.algorithm.ProviderRegistry getProviderRegistry() {
        return providerRegistry;
    }

    private static final java.util.concurrent.ForkJoinPool SHARED_POOL = new java.util.concurrent.ForkJoinPool(
            Runtime.getRuntime().availableProcessors(),
            java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            null, true);

    /**
     * Executes a computation potentially in parallel using a shared thread pool.
     */
    public static <T> T computeParallel(java.util.function.Supplier<T> task) {
        return SHARED_POOL.submit(() -> task.get()).join();
    }

    /**
     * Checks if the current computation context has been cancelled.
     * @throws RuntimeException if cancelled
     */
    public static void checkCancelled() {
        if (MathContext.isCancelled()) {
            throw new RuntimeException("Computation cancelled");
        }
    }
    
    /**
     * Synonym for checkCancelled to match legacy API.
     */
    public static void checkCurrentCancelled() {
        checkCancelled();
    }

    /**
     * Configures the library for maximum performance.
     */
    public static void configureForPerformance() {
        getNumericalConfiguration().applyComputeMode(ComputeMode.AUTO);
    }

    /**
     * Configures the library for maximum precision.
     */
    public static void configureForPrecision() {
        org.episteme.core.mathematics.context.NumericalConfiguration config = getNumericalConfiguration();
        config.applyComputeMode(ComputeMode.CPU);
        config.setRealPrecision(org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT);
    }

    /**
     * Returns true if a GPU is likely available.
     */
    public static boolean isGpuAvailable() {
        return org.episteme.core.mathematics.context.MathContext.getNumericalConfiguration().isGpuAvailable();
    }

    /**
     * Checks if ND4J is available in the classpath or as a backend.
     */
    public static boolean isND4JAvailable() {
        if (isBackendAvailable("linear-algebra", "nd4j") || isBackendAvailable("tensor", "nd4j")) return true;
        try {
            Class.forName("org.nd4j.linalg.factory.Nd4j");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Checks if Apache Spark is available in the classpath or as a backend.
     */
    public static boolean isSparkAvailable() {
        if (isBackendAvailable("distributed", "spark")) return true;
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
        java.util.List<String> bNames = new java.util.ArrayList<>(org.episteme.core.technical.backend.BackendManager.staticAvailableNames());
        return bNames;
    }

    /**
     * Returns the numerical configuration for the current thread.
     */
    public static org.episteme.core.mathematics.context.NumericalConfiguration getNumericalConfiguration() {
        // Need to expose STATE and config in MathContext
        return MathContext.getNumericalConfiguration();
    }


    /**
     * Returns a report of the current configuration and capabilities.
     * 
     * @return a string describing the current state
     */
    public static String getConfigurationReport() {
        // Force load preferences if not done
        loadPreferences();
        
        StringBuilder sb = new StringBuilder();
        String line = "================================================================================\n";
        
        // Use I18N for the main title as before
        sb.append(line);
        sb.append("   " + org.episteme.core.ui.i18n.I18N.getInstance().get("report.title", "Episteme Configuration Report") + "\n");
        sb.append(line);

        // 1. SYSTEM INFO
        sb.append("\n[SYSTEM INFORMATION]\n");
        sb.append("Episteme Version : ").append(VERSION).append("\n");
        sb.append("Build Date       : ").append(BUILD_DATE).append("\n");
        sb.append("Java Version     : ").append(System.getProperty("java.version")).append("\n");
        sb.append("Java Vendor      : ").append(System.getProperty("java.vendor")).append("\n");
        sb.append("OS Name          : ").append(System.getProperty("os.name")).append("\n");
        sb.append("OS Arch          : ").append(System.getProperty("os.arch")).append("\n");
        sb.append("Available Cores  : ").append(Runtime.getRuntime().availableProcessors()).append("\n");
        
        // 2. PREFERENCES (User settings)
        sb.append("\n[USER PREFERENCES & PARAMETERS]\n");
        org.episteme.core.io.UserPreferences prefs = org.episteme.core.io.UserPreferences.getInstance();
        java.util.Map<String, String> prefMap = prefs.getPreferencesMap();
        if (prefMap.isEmpty()) {
            sb.append("  (No user preferences saved)\n");
        } else {
            for (java.util.Map.Entry<String, String> entry : prefMap.entrySet()) {
                sb.append("  ").append(String.format("%-30s", entry.getKey()))
                  .append(" = ").append(entry.getValue()).append("\n");
            }
        }
        
        // 3. COMPUTING CONTEXT
        sb.append("\n[COMPUTING CONTEXT]\n");
        sb.append("Compute Mode     : ").append(getComputeMode()).append("\n");
        sb.append("Float Precision  : ").append(getFloatPrecisionMode()).append("\n");
        sb.append("Integer Precision: ").append(getIntPrecisionMode()).append("\n");
        sb.append("Math Precision   : ").append(getMathContext().getPrecision())
          .append(" digits (Rounding: ").append(getMathContext().getRoundingMode()).append(")\n");
        sb.append("GPU Available    : ").append(isGpuAvailable() ? "YES" : "NO").append("\n");
        
        // 4. BACKENDS (Detailed)
        sb.append("\n[REGISTERED BACKENDS]\n");
        // Use reflection or hardcoded list of types from BackendDiscovery to be dynamic
        String[] types = {
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_MATH,
            "linear-algebra",
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_TENSOR,
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_MOLECULAR,
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_PLOTTING,
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_QUANTUM,
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_MAP,
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_GRAPH,
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_AUDIO,
            org.episteme.core.technical.backend.BackendDiscovery.TYPE_DISTRIBUTED
        };
        
        for (String type : types) {
            String label = type.substring(0, 1).toUpperCase() + type.substring(1).replace("-", " ");
            appendBackends(sb, label, type, getBackendId(type));
        }

        // 5. LIBRARIES (Found in Classpath)
        sb.append("\n[LIBRARIES DETECTION]\n");
        checkLibrary(sb, "Kotlin Utils", "kotlin.Unit");
        checkLibrary(sb, "Groovy", "groovy.lang.GroovySystem");
        checkLibrary(sb, "Apache Spark", "org.apache.spark.api.java.JavaSparkContext");
        checkLibrary(sb, "ND4J / DL4J", "org.nd4j.linalg.factory.Nd4j");
        checkLibrary(sb, "OpenCL (JOCL)", "org.jocl.CL");
        checkLibrary(sb, "CUDA (JCuda)", "jcuda.Pointer");
        checkLibrary(sb, "Javalin", "io.javalin.Javalin");
        checkLibrary(sb, "Jackson", "com.fasterxml.jackson.databind.ObjectMapper");
        checkLibrary(sb, "gRPC", "io.grpc.ManagedChannel");
        checkLibrary(sb, "MPI (MPJ Express)", "mpi.MPI");
        checkLibrary(sb, "Indriya (Units)", "tech.units.indriya.format.SimpleUnitFormat");
        
        // 6. IO LOADERS (Readers/Writers)
        sb.append("\n[IO LOADERS]\n");
        appendServiceLoaders(sb, org.episteme.core.io.ResourceReader.class, "Reader");
        appendServiceLoaders(sb, org.episteme.core.io.ResourceWriter.class, "Writer");
        
        // 7. DEVICES
        sb.append("\n[DEVICES]\n");
        try {
            org.episteme.core.ui.MasterControlDiscovery discovery = org.episteme.core.ui.MasterControlDiscovery.getInstance();
            java.util.List<org.episteme.core.ui.MasterControlDiscovery.ClassInfo> devices = discovery.findClasses("Device");
            if (devices.isEmpty()) {
                sb.append("  (No 'Device' classes found in scan)\n");
            } else {
                for (org.episteme.core.ui.MasterControlDiscovery.ClassInfo info : devices) {
                    sb.append("  - ").append(info.simpleName).append(" (").append(info.fullName).append(")\n");
                }
            }
        } catch (Throwable t) {
            sb.append("  (Error scanning devices (UI module missing?): ").append(t.getMessage()).append(")\n");
        }
        
        sb.append("\n").append(line);
        return sb.toString();
    }

    /**
     * Returns the current computation mode.
     */
    public static ComputeMode getComputeMode() {
        return getNumericalConfiguration().getComputeMode();
    }

    /**
     * Sets the computation mode.
     */
    public static void setComputeMode(ComputeMode mode) {
        getNumericalConfiguration().applyComputeMode(mode);
    }

    /**
     * Sets 32-bit float precision.
     */
    public static void setFloatPrecision() {
        getNumericalConfiguration().setFloatPrecision(org.episteme.core.mathematics.context.NumericalConfiguration.FloatPrecision.FLOAT);
    }

    /**
     * Sets 64-bit double precision.
     */
    public static void setDoublePrecision() {
        getNumericalConfiguration().setFloatPrecision(org.episteme.core.mathematics.context.NumericalConfiguration.FloatPrecision.DOUBLE);
    }

    /**
     * Returns the float precision mode.
     */
    public static org.episteme.core.mathematics.context.NumericalConfiguration.FloatPrecision getFloatPrecisionMode() {
        return getNumericalConfiguration().getFloatPrecision();
    }

    /**
     * Sets 32-bit integer precision.
     */
    public static void setIntPrecision() {
        getNumericalConfiguration().setIntPrecision(org.episteme.core.mathematics.context.NumericalConfiguration.IntPrecision.INT);
    }

    /**
     * Sets 64-bit long precision.
     */
    public static void setLongPrecision() {
        getNumericalConfiguration().setIntPrecision(org.episteme.core.mathematics.context.NumericalConfiguration.IntPrecision.LONG);
    }

    /**
     * Returns the integer precision mode.
     */
    public static org.episteme.core.mathematics.context.NumericalConfiguration.IntPrecision getIntPrecisionMode() {
        return getNumericalConfiguration().getIntPrecision();
    }

    /**
     * Sets the default MathContext.
     */
    public static void setMathContext(java.math.MathContext context) {
        getNumericalConfiguration().setMathContext(context);
    }

    /**
     * Gets the current MathContext.
     */
    public static java.math.MathContext getMathContext() {
        return getNumericalConfiguration().getMathContext();
    }

    /**
     * Returns the generic current backend for a type.
     */
    public static String getBackendId(String type) {
        return org.episteme.core.io.UserPreferences.getInstance().getPreferredBackend(type);
    }

    /**
     * Sets the backend for the specified type.
     */
    public static void setBackendId(String type, String id) {
        org.episteme.core.io.UserPreferences.getInstance().setPreferredBackend(type, id);
    }

    public static String getMathBackendId() {
        return getBackendId(org.episteme.core.technical.backend.BackendDiscovery.TYPE_MATH);
    }

    public static void setMathBackendId(String id) {
        setBackendId(org.episteme.core.technical.backend.BackendDiscovery.TYPE_MATH, id);
    }

    public static String getTensorBackendId() {
        return getBackendId(org.episteme.core.technical.backend.BackendDiscovery.TYPE_TENSOR);
    }

    public static void setTensorBackendId(String id) {
        setBackendId(org.episteme.core.technical.backend.BackendDiscovery.TYPE_TENSOR, id);
    }

    public static String getMapBackendId() {
        return getBackendId(org.episteme.core.technical.backend.BackendDiscovery.TYPE_MAP);
    }

    public static void setMapBackendId(String id) {
        setBackendId(org.episteme.core.technical.backend.BackendDiscovery.TYPE_MAP, id);
    }

    public static org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackend getPlottingBackend2D() {
        return org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackendManager.getInstance().get2D();
    }

    public static void setPlottingBackend2D(org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackend backend) {
        org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackendManager.getInstance().set2D(backend != null ? backend.getId() : "auto");
    }

    public static org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackend getPlottingBackend3D() {
        return org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackendManager.getInstance().get3D();
    }

    public static void setPlottingBackend3D(org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackend backend) {
        org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackendManager.getInstance().set3D(backend != null ? backend.getId() : "auto");
    }

    /**
     * Checks if a backend is available by type or ID part.
     */
    public static boolean isBackendAvailable(String type, String idPart) {
        return org.episteme.core.technical.backend.BackendDiscovery.getInstance().getAvailableProvidersByType(type).stream()
                .anyMatch(p -> p.getId().toLowerCase().contains(idPart.toLowerCase()));
    }

    /**
     * Main entry point for CLI usage and configuration verification.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Parse CLI arguments
        for (String arg : args) {
            if ("-report".equalsIgnoreCase(arg)) {
                System.out.println(getConfigurationReport());
                System.exit(0);
            }
        }

        // Parse system properties
        String modeProp = System.getProperty("org.episteme.core.compute.mode");
        if (modeProp != null) {
            try {
                ComputeMode mode = ComputeMode.valueOf(modeProp.toUpperCase());
                setComputeMode(mode);
            } catch (IllegalArgumentException e) {
                System.err.println(org.episteme.core.ui.i18n.I18N.getInstance().get("cli.invalid_mode", "Invalid compute mode") + ": " + modeProp);
            }
        }

        String floatProp = System.getProperty("org.episteme.core.float.precision");
        if ("float".equalsIgnoreCase(floatProp)) {
            setFloatPrecision();
        } else if ("double".equalsIgnoreCase(floatProp)) {
            setDoublePrecision();
        }

        String intProp = System.getProperty("org.episteme.core.int.precision");
        if ("int".equalsIgnoreCase(intProp)) {
            setIntPrecision();
        } else if ("long".equalsIgnoreCase(intProp)) {
            setLongPrecision();
        }

        // Launch Master Control
        try {
            System.out.println(org.episteme.core.ui.i18n.I18N.getInstance().get("cli.launching", "Launching Episteme Master Control..."));
            javafx.application.Application.launch(org.episteme.core.ui.EpistemeMasterControl.class, args);
        } catch (Throwable e) {
            System.out.println(org.episteme.core.ui.i18n.I18N.getInstance().get("cli.launch_error", "Episteme Master Control not available or JavaFX missing. Showing CLI report.") + " " + (e.getMessage() != null ? e.getMessage() : ""));
            System.out.println(getConfigurationReport());
        }
    }

    // --- Report Helper Methods ---

    private static void appendBackends(StringBuilder sb, String label, String type, String currentId) {
        sb.append("  ").append(label).append(" (Current: ").append(currentId != null ? currentId : "AUTO").append("):\n");
        try {
            java.util.List<org.episteme.core.technical.backend.Backend> list = 
                 org.episteme.core.technical.backend.BackendManager.staticGetProvidersByType(type);
            if (list == null || list.isEmpty()) {
                sb.append("    (None registered via SPI)\n");
            } else {
                for (org.episteme.core.technical.backend.Backend p : list) {
                     String marker = (p.getId().equals(currentId)) ? "*" : " ";
                     sb.append("    ").append(marker).append(" [").append(p.getId()).append("] ")
                       .append(p.getName()).append(p.isAvailable() ? "" : " (N/A)").append("\n");
                }
            }
        } catch (Throwable t) {
             sb.append("    (Error checking backends: ").append(t.getMessage()).append(")\n");
        }
    }
    
    private static void checkLibrary(StringBuilder sb, String name, String className) {
        boolean avail = false;
        try { Class.forName(className); avail = true; } catch (Throwable t) {}
        sb.append("  ").append(String.format("%-25s", name)).append(": ").append(avail ? "INSTALLED" : "MISSING").append("\n");
    }
    
    private static <T> void appendServiceLoaders(StringBuilder sb, Class<T> clazz, String typeLabel) {
         try {
             java.util.ServiceLoader<T> loader = java.util.ServiceLoader.load(clazz);
             int count = 0;
             for (T item : loader) {
                 count++;
                 String name = item.getClass().getSimpleName();
                 // Try to check for name() method via reflection if it's not resourceIO
                 if (item instanceof org.episteme.core.io.ResourceIO) {
                      name = ((org.episteme.core.io.ResourceIO<?>) item).getName();
                 }
                 sb.append("  ").append(typeLabel).append(": ").append(name).append(" (").append(item.getClass().getName()).append(")\n");
             }
             if (count == 0) sb.append("  (No ").append(typeLabel).append("s found via ServiceLoader)\n");
         } catch (Throwable t) {
             sb.append("  (Error loading ").append(typeLabel).append("s: ").append(t.getMessage()).append(")\n");
         }
    }
}


