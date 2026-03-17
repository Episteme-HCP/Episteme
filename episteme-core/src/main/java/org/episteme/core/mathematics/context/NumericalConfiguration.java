package org.episteme.core.mathematics.context;

import java.math.MathContext;
import org.episteme.core.ComputeContext.Backend;
import org.episteme.core.ComputeContext.FloatPrecision;
import org.episteme.core.ComputeContext.IntPrecision;
import org.episteme.core.mathematics.context.MathContext.OverflowMode;
import org.episteme.core.mathematics.context.MathContext.RealPrecision;
import org.episteme.core.Episteme;

/**
 * Configuration for numerical computation, including precision, backend preferences, and thresholds.
 * Standardized as NumericalConfiguration in the mathematics.context package.
 */
public class NumericalConfiguration {

    private volatile FloatPrecision floatPrecision = FloatPrecision.DOUBLE;
    private volatile IntPrecision intPrecision = IntPrecision.LONG;
    private volatile Backend backend = Backend.JAVA_CPU;

    private volatile RealPrecision realPrecision = RealPrecision.NORMAL;
    private volatile OverflowMode overflowMode = OverflowMode.SAFE;
    private volatile ComputeMode computeMode = ComputeMode.AUTO;
    private volatile MathContext mathContext = MathContext.DECIMAL128;

    private double gpuThreshold = Double.parseDouble(Episteme.getProperty("compute.gpu.threshold", "10000000"));
    private int parallelThreshold = Integer.parseInt(Episteme.getProperty("compute.parallel.threshold", "50000"));
    private int maxThreads = Integer.parseInt(Episteme.getProperty("compute.parallel.max_threads", String.valueOf(Runtime.getRuntime().availableProcessors())));

    public NumericalConfiguration() {
    }

    public FloatPrecision getFloatPrecision() {
        return floatPrecision;
    }

    public NumericalConfiguration setFloatPrecision(FloatPrecision floatPrecision) {
        this.floatPrecision = floatPrecision;
        return this;
    }

    public IntPrecision getIntPrecision() {
        return intPrecision;
    }

    public NumericalConfiguration setIntPrecision(IntPrecision intPrecision) {
        this.intPrecision = intPrecision;
        return this;
    }

    public Backend getBackend() {
        return backend;
    }

    public NumericalConfiguration setBackend(Backend backend) {
        this.backend = backend;
        return this;
    }

    public RealPrecision getRealPrecision() {
        return realPrecision;
    }

    public NumericalConfiguration setRealPrecision(RealPrecision realPrecision) {
        this.realPrecision = realPrecision;
        return this;
    }

    public OverflowMode getOverflowMode() {
        return overflowMode;
    }

    public NumericalConfiguration setOverflowMode(OverflowMode overflowMode) {
        this.overflowMode = overflowMode;
        return this;
    }

    public ComputeMode getComputeMode() {
        return computeMode;
    }

    public NumericalConfiguration setComputeMode(ComputeMode computeMode) {
        this.computeMode = computeMode;
        return this;
    }

    public MathContext getMathContext() {
        return mathContext;
    }

    public NumericalConfiguration setMathContext(MathContext mathContext) {
        this.mathContext = mathContext;
        return this;
    }

    public double getGpuThreshold() {
        return gpuThreshold;
    }

    public NumericalConfiguration setGpuThreshold(double gpuThreshold) {
        this.gpuThreshold = gpuThreshold;
        return this;
    }

    public int getParallelThreshold() {
        return parallelThreshold;
    }

    public NumericalConfiguration setParallelThreshold(int parallelThreshold) {
        this.parallelThreshold = parallelThreshold;
        return this;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public NumericalConfiguration setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }
}
