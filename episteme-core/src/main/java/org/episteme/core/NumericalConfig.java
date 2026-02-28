package org.episteme.core;

import java.math.MathContext;
import org.episteme.core.ComputeContext.Backend;
import org.episteme.core.ComputeContext.FloatPrecision;
import org.episteme.core.ComputeContext.IntPrecision;
import org.episteme.core.mathematics.context.ComputeMode;
import org.episteme.core.mathematics.context.MathContext.OverflowMode;
import org.episteme.core.mathematics.context.MathContext.RealPrecision;

/**
 * Configuration for numerical computation, including precision, backend preferences, and thresholds.
 * Extracted from {@link ComputeContext}.
 */
public class NumericalConfig {

    private volatile FloatPrecision floatPrecision = FloatPrecision.DOUBLE;
    private volatile IntPrecision intPrecision = IntPrecision.LONG;
    private volatile Backend backend = Backend.JAVA_CPU;

    private volatile RealPrecision realPrecision = RealPrecision.NORMAL;
    private volatile OverflowMode overflowMode = OverflowMode.SAFE;
    private volatile ComputeMode computeMode = ComputeMode.AUTO;
    private volatile MathContext mathContext = MathContext.DECIMAL128;

    private double gpuThreshold = 10_000_000; // Default threshold

    public NumericalConfig() {
    }

    public FloatPrecision getFloatPrecision() {
        return floatPrecision;
    }

    public NumericalConfig setFloatPrecision(FloatPrecision floatPrecision) {
        this.floatPrecision = floatPrecision;
        return this;
    }

    public IntPrecision getIntPrecision() {
        return intPrecision;
    }

    public NumericalConfig setIntPrecision(IntPrecision intPrecision) {
        this.intPrecision = intPrecision;
        return this;
    }

    public Backend getBackend() {
        return backend;
    }

    public NumericalConfig setBackend(Backend backend) {
        this.backend = backend;
        return this;
    }

    public RealPrecision getRealPrecision() {
        return realPrecision;
    }

    public NumericalConfig setRealPrecision(RealPrecision realPrecision) {
        this.realPrecision = realPrecision;
        return this;
    }

    public OverflowMode getOverflowMode() {
        return overflowMode;
    }

    public NumericalConfig setOverflowMode(OverflowMode overflowMode) {
        this.overflowMode = overflowMode;
        return this;
    }

    public ComputeMode getComputeMode() {
        return computeMode;
    }

    public NumericalConfig setComputeMode(ComputeMode computeMode) {
        this.computeMode = computeMode;
        return this;
    }

    public MathContext getMathContext() {
        return mathContext;
    }

    public NumericalConfig setMathContext(MathContext mathContext) {
        this.mathContext = mathContext;
        return this;
    }

    public double getGpuThreshold() {
        return gpuThreshold;
    }

    public NumericalConfig setGpuThreshold(double gpuThreshold) {
        this.gpuThreshold = gpuThreshold;
        return this;
    }
}
