/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.context;

/**
 * Computation context for mathematical operations.
 * <p>
 * Controls precision preferences, overflow checking, and other computational
 * settings. Can be configured globally, per-thread, or per-operation.
 * </p>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>{@code
 * // Thread-local context
 * MathContext.setCurrent(MathContext.fast());
 * Real r = Real.of(3.14); // Uses float
 * 
 * // Computation block
 * Real result = MathContext.exact().compute(() -> {
 *     Real a = Real.of("1.5");
 *     Real b = Real.of("2.7");
 *     return a.add(b);
 * });
 * }</pre>
 * 
 * * */
public final class MathContext {

    private static final String PROP_EXACT_PRECISION = "org.episteme.core.math.precision.exact";
    private static final int DEFAULT_EXACT_PRECISION = Integer.parseInt(
            System.getProperty(PROP_EXACT_PRECISION, 
            org.episteme.core.Episteme.getProperty(PROP_EXACT_PRECISION, "1000")));


    /**
     *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum RealPrecision {
        /** Fast mode - uses float (7 digits, GPU-friendly) */
        FAST,

        /** Normal mode - uses double (15 digits, default) */
        NORMAL,

        /** Exact mode - uses BigDecimal (arbitrary precision) */
        EXACT
    }

    /** Overflow checking strategy */
    public enum OverflowMode {
        /** Safe mode - check before every operation (default) */
        SAFE,

        /** Unsafe mode - no checking (fastest, may wrap) */
        UNSAFE,

        /** Lazy mode - check only when accessing result */
        LAZY
    }

    private static final ThreadLocal<MathState> STATE = InheritableThreadLocal.withInitial(MathState::new);

    private static class MathState {
        final NumericalConfiguration config = new NumericalConfiguration();
        volatile boolean cancelled = false;
    }

    private final RealPrecision realPrecision;
    private final OverflowMode overflowMode;
    private final ComputeMode computeMode;
    private final java.math.MathContext javaMathContext;

    private MathContext(RealPrecision realPrecision, OverflowMode overflowMode, ComputeMode computeMode, java.math.MathContext javaMathContext) {
        this.realPrecision = realPrecision;
        this.overflowMode = overflowMode;
        this.computeMode = computeMode;
        this.javaMathContext = javaMathContext;
    }

    /**
     * Returns the current thread-local context.
     */
    public static MathContext getCurrent() {
        MathState state = STATE.get();
        NumericalConfiguration config = state.config;
        return new MathContext(
                config.getRealPrecision(),
                config.getOverflowMode(),
                config.getComputeMode(),
                config.getMathContext());
    }

    /**
     * Sets the current thread-local context.
     */
    public static void setCurrent(MathContext context) {
        MathState state = STATE.get();
        NumericalConfiguration config = state.config;
        config.setRealPrecision(context.getRealPrecision());
        config.setOverflowMode(context.getOverflowMode());
        config.setComputeMode(context.getComputeMode());
        config.setMathContext(context.getJavaMathContext());
    }

    /**
     * Resets the current thread-local context to defaults.
     */
    public static void reset() {
        STATE.remove();
    }

    /**
     * Returns true if the current context has been cancelled.
     */
    public static boolean isCancelled() {
        return STATE.get().cancelled;
    }

    /**
     * Sets the cancellation state for the current context.
     */
    public static void setCancelled(boolean cancelled) {
        STATE.get().cancelled = cancelled;
    }

    /**
     * Checks if the current thread's context is cancelled.
     * @throws RuntimeException if cancelled
     */
    public static void checkCurrentCancelled() {
        if (isCancelled()) {
            throw new RuntimeException("Computation cancelled");
        }
    }

    /**
     * Synonym for checkCurrentCancelled to match legacy and provider APIs.
     */
    public static void checkCancelled() {
        checkCurrentCancelled();
    }

    /**
     * Returns the numerical configuration for the current thread.
     */
    public static NumericalConfiguration getNumericalConfiguration() {
        return STATE.get().config;
    }


    /**
     * Creates a fast computation context (float precision).
     */
    public static MathContext fast() {
        return new MathContext(RealPrecision.FAST, OverflowMode.SAFE, ComputeMode.AUTO,
                java.math.MathContext.DECIMAL32);
    }

    /**
     * Creates a normal computation context (double precision).
     */
    public static MathContext normal() {
        return new MathContext(RealPrecision.NORMAL, OverflowMode.SAFE, ComputeMode.AUTO,
                java.math.MathContext.DECIMAL64);
    }

    /**
     * Creates an exact computation context (BigDecimal).
     */
    public static MathContext exact() {
        return new MathContext(RealPrecision.EXACT, OverflowMode.SAFE, ComputeMode.AUTO,
                new java.math.MathContext(DEFAULT_EXACT_PRECISION, java.math.RoundingMode.HALF_UP));
    }

    /**
     * Creates an unsafe context (no overflow checking).
     */
    public static MathContext unsafe() {
        return new MathContext(RealPrecision.NORMAL, OverflowMode.UNSAFE, ComputeMode.AUTO,
                java.math.MathContext.DECIMAL64);
    }

    /**
     * Executes a computation with this context.
     */
    public <T> T compute(java.util.function.Supplier<T> computation) {
        MathContext previous = getCurrent();
        try {
            setCurrent(this);
            return computation.get();
        } finally {
            setCurrent(previous);
        }
    }

    /**
     * Gets the real number precision mode.
     */
    public RealPrecision getRealPrecision() {
        return realPrecision;
    }

    /**
     * Returns true if the precision is EXACT.
     */
    public boolean isHighPrecision() {
        return realPrecision == RealPrecision.EXACT;
    }

    /**
     * Returns true if the precision is NORMAL or EXACT.
     */
    public boolean isDoubleOrHigherPrecision() {
        return realPrecision == RealPrecision.NORMAL || realPrecision == RealPrecision.EXACT;
    }

    /**
     * Returns true if the precision is FAST.
     */
    public boolean isFastPrecision() {
        return realPrecision == RealPrecision.FAST;
    }

    /**
     * Gets the overflow checking mode.
     */
    public OverflowMode getOverflowMode() {
        return overflowMode;
    }

    /**
     * Checks if overflow checking is enabled.
     */
    public boolean isOverflowCheckingEnabled() {
        return overflowMode == OverflowMode.SAFE;
    }

    /**
     * Gets the compute mode (CPU/GPU/AUTO).
     */
    public ComputeMode getComputeMode() {
        return computeMode;
    }

    /**
     * Gets the java.math.MathContext.
     */
    public java.math.MathContext getJavaMathContext() {
        return javaMathContext;
    }

    /**
     * Returns the precision in bits (binary digits).
     * <p>
     * Calculated as ceil(digits * log2(10)).
     * </p>
     * 
     * @return the number of bits required for the current decimal precision
     */
    public long getPrecisionBits() {
        if (realPrecision == RealPrecision.FAST) return 24; // float
        if (realPrecision == RealPrecision.NORMAL) return 53; // double
        
        int digits = javaMathContext.getPrecision();
        if (digits == 0) return 3322; // Default for 'unlimited' if we must choose
        return (long) Math.ceil(digits * 3.32192809489);
    }

    /**
     * Returns a new context with the specified real precision.
     */
    public MathContext withRealPrecision(RealPrecision realPrecision) {
        return new MathContext(realPrecision, this.overflowMode, this.computeMode, this.javaMathContext);
    }

    /**
     * Returns a new context with the specified overflow mode.
     */
    public MathContext withOverflowMode(OverflowMode overflowMode) {
        return new MathContext(this.realPrecision, overflowMode, this.computeMode, this.javaMathContext);
    }

    /**
     * Returns a new context with the specified compute mode.
     */
    public MathContext withComputeMode(ComputeMode computeMode) {
        return new MathContext(this.realPrecision, this.overflowMode, computeMode, this.javaMathContext);
    }

    /**
     * Returns a new context with the specified java.math.MathContext.
     */
    public MathContext withJavaMathContext(java.math.MathContext javaMathContext) {
        return new MathContext(this.realPrecision, this.overflowMode, this.computeMode, javaMathContext);
    }

    /**
     * Returns a new context with the specified precision (EXACT mode).
     * 
     * @param precision the number of digits
     * @return the new context
     */
    public static MathContext withPrecision(int precision) {
        return MathContext.exact().withJavaMathContext(new java.math.MathContext(precision, java.math.RoundingMode.HALF_UP));
    }

    @Override
    public String toString() {
        return "MathContext{real=" + realPrecision + ", overflow=" + overflowMode + ", compute=" + computeMode
                + ", javaMC=" + javaMathContext + "}";
    }
}


