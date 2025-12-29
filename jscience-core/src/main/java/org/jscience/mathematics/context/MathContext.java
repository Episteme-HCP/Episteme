/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.context;

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
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class MathContext {

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

    // Thread-local storage is now handled by ComputeContext
    // private static final ThreadLocal<MathContext> CURRENT ...

    // Default context logic is now implicit or handled by ComputeContext defaults
    // But we keep a static default for API compatibility if needed
    private static MathContext DEFAULT = new MathContext(
            RealPrecision.NORMAL,
            OverflowMode.SAFE);

    private final RealPrecision realPrecision;
    private final OverflowMode overflowMode;
    private final ComputeMode computeMode;

    /**
     * Creates a new computation context.
     */
    public MathContext(RealPrecision realPrecision, OverflowMode overflowMode, ComputeMode computeMode) {
        this.realPrecision = realPrecision;
        this.overflowMode = overflowMode;
        this.computeMode = computeMode;
    }

    /**
     * Creates a new computation context (default AUTO compute mode).
     */
    public MathContext(RealPrecision realPrecision, OverflowMode overflowMode) {
        this(realPrecision, overflowMode, ComputeMode.AUTO);
    }

    /**
     * Returns the default context.
     */
    public static MathContext getDefault() {
        return DEFAULT;
    }

    /**
     * Sets the global default context.
     */
    public static void setDefault(MathContext context) {
        DEFAULT = context;
    }

    /**
     * Returns the current thread-local context (view of ComputeContext).
     */
    public static MathContext getCurrent() {
        org.jscience.ComputeContext cc = org.jscience.ComputeContext
                .current();
        return new MathContext(cc.getRealPrecision(), cc.getOverflowMode(), cc.getComputeMode());
    }

    /**
     * Sets the current thread-local context (updates ComputeContext).
     */
    public static void setCurrent(MathContext context) {
        org.jscience.ComputeContext cc = org.jscience.ComputeContext
                .current();
        cc.setRealPrecision(context.getRealPrecision());
        cc.setOverflowMode(context.getOverflowMode());
        cc.setComputeMode(context.getComputeMode());
    }

    /**
     * Resets thread-local context to default.
     */
    public static void reset() {
        org.jscience.ComputeContext.reset();
    }

    /**
     * Creates a fast computation context (float precision).
     */
    public static MathContext fast() {
        return new MathContext(RealPrecision.FAST, OverflowMode.SAFE);
    }

    /**
     * Creates a normal computation context (double precision).
     */
    public static MathContext normal() {
        return new MathContext(RealPrecision.NORMAL, OverflowMode.SAFE);
    }

    /**
     * Creates an exact computation context (BigDecimal).
     */
    public static MathContext exact() {
        return new MathContext(RealPrecision.EXACT, OverflowMode.SAFE);
    }

    /**
     * Creates an unsafe context (no overflow checking).
     */
    public static MathContext unsafe() {
        return new MathContext(RealPrecision.NORMAL, OverflowMode.UNSAFE);
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
     * Returns a new context with the specified real precision.
     */
    public MathContext withRealPrecision(RealPrecision realPrecision) {
        return new MathContext(realPrecision, this.overflowMode, this.computeMode);
    }

    /**
     * Returns a new context with the specified overflow mode.
     */
    public MathContext withOverflowMode(OverflowMode overflowMode) {
        return new MathContext(this.realPrecision, overflowMode, this.computeMode);
    }

    /**
     * Returns a new context with the specified compute mode.
     */
    public MathContext withComputeMode(ComputeMode computeMode) {
        return new MathContext(this.realPrecision, this.overflowMode, computeMode);
    }

    @Override
    public String toString() {
        return "MathContext{real=" + realPrecision + ", overflow=" + overflowMode + ", compute=" + computeMode + "}";
    }
}