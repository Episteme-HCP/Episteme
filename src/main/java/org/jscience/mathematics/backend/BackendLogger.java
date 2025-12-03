package org.jscience.mathematics.backend;

import org.jscience.mathematics.context.MathContext;
import org.jscience.util.Logger;

/**
 * Specialized logger for mathematical backend operations.
 * <p>
 * Tracks context switches, compute mode changes, and performance metrics.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class BackendLogger {

    private static final Logger LOG = Logger.getLogger(BackendLogger.class);

    private BackendLogger() {
        // Utility class
    }

    /**
     * Logs a MathContext change.
     *
     * @param oldContext the previous context
     * @param newContext the new context
     */
    public static void logContextSwitch(MathContext oldContext, MathContext newContext) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(() -> String.format("MathContext switched: %s -> %s", oldContext, newContext));
        }
    }

    /**
     * Logs a compute mode change.
     *
     * @param oldMode the previous mode
     * @param newMode the new mode
     */
    public static void logComputeModeChange(Object oldMode, Object newMode) {
        if (LOG.isInfoEnabled()) {
            LOG.info(String.format("Compute mode changed: %s -> %s", oldMode, newMode));
        }
    }

    /**
     * Logs backend initialization.
     *
     * @param backendName the backend name (e.g., "CUDA", "CPU")
     * @param success     whether initialization succeeded
     */
    public static void logBackendInit(String backendName, boolean success) {
        if (success) {
            LOG.info(String.format("%s backend initialized successfully", backendName));
        } else {
            LOG.warn(String.format("%s backend initialization failed, falling back to CPU", backendName));
        }
    }

    /**
     * Logs a performance metric.
     *
     * @param operation  the operation name
     * @param durationMs the duration in milliseconds
     */
    public static void logPerformance(String operation, long durationMs) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(() -> String.format("%s completed in %d ms", operation, durationMs));
        }
    }

    /**
     * Logs a performance metric with data size.
     *
     * @param operation  the operation name
     * @param dataSize   the data size (e.g., matrix dimension)
     * @param durationMs the duration in milliseconds
     */
    public static void logPerformance(String operation, int dataSize, long durationMs) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(() -> String.format("%s (size=%d) completed in %d ms", operation, dataSize, durationMs));
        }
    }

    /**
     * Logs a GPU operation.
     *
     * @param operation        the operation name
     * @param matrixDimensions the matrix dimensions (e.g., "1000x1000")
     */
    public static void logGPUOperation(String operation, String matrixDimensions) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(() -> String.format("GPU %s: %s", operation, matrixDimensions));
        }
    }

    /**
     * Gets the underlying logger for advanced usage.
     *
     * @return the logger
     */
    public static Logger getLogger() {
        return LOG;
    }
}
