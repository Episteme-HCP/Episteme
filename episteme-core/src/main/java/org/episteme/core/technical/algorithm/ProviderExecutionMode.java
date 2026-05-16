/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.technical.algorithm;

/**
 * Thread-local execution mode flag for provider fallback control.
 * <p>
 * In {@link Mode#NORMAL} mode, providers may fall back to alternative
 * implementations (e.g., CPU fallback when CUDA fails). In
 * {@link Mode#BENCHMARK} or {@link Mode#AUTOTUNING} mode, fallbacks
 * are disabled and failures surface as exceptions, ensuring honest
 * performance measurements.
 * </p>
 *
 * <pre>
 * ProviderExecutionMode.set(Mode.BENCHMARK);
 * try {
 *     provider.multiply(a, b); // will throw if CUDA fails
 * } finally {
 *     ProviderExecutionMode.reset();
 * }
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class ProviderExecutionMode {

    /**
     * Execution modes that control fallback behavior.
     */
    public enum Mode {
        /** Normal application use — fallbacks allowed for graceful degradation. */
        NORMAL,
        /** Benchmarking — fallbacks disabled, failures are errors. */
        BENCHMARK,
        /** Auto-tuning — fallbacks disabled, failures are errors. */
        AUTOTUNING
    }

    private static final ThreadLocal<Mode> CURRENT = ThreadLocal.withInitial(() -> Mode.NORMAL);

    private ProviderExecutionMode() {
        // Utility class, no instantiation
    }

    /** Returns the current execution mode for this thread. */
    public static Mode get() {
        return CURRENT.get();
    }

    /** Sets the execution mode for this thread. */
    public static void set(Mode mode) {
        CURRENT.set(mode);
    }

    /**
     * Returns {@code true} if the current mode allows fallback to
     * alternative implementations. Only {@link Mode#NORMAL} permits fallbacks.
     */
    public static boolean isFallbackAllowed() {
        return CURRENT.get() == Mode.NORMAL;
    }

    /** Resets the execution mode to {@link Mode#NORMAL} and removes the thread-local. */
    public static void reset() {
        CURRENT.remove();
    }
}
