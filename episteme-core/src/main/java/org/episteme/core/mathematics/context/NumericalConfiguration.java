package org.episteme.core.mathematics.context;

import java.math.MathContext;
import org.episteme.core.mathematics.context.MathContext.OverflowMode;
import org.episteme.core.mathematics.context.MathContext.RealPrecision;
import org.episteme.core.Episteme;


/**
 * Configuration for numerical computation, including precision, backend preferences, and thresholds.
 * Standardized as NumericalConfiguration in the mathematics.context package.
 */
public class NumericalConfiguration {

    /**
     * Floating-point precision mode for GPU and numerical operations.
     */
    public enum FloatPrecision {
        FLOAT, DOUBLE
    }

    /**
     * Integer precision mode for GPU and numerical operations.
     */
    public enum IntPrecision {
        INT, LONG
    }

    private volatile FloatPrecision floatPrecision = FloatPrecision.DOUBLE;
    private volatile IntPrecision intPrecision = IntPrecision.LONG;
    private volatile String backendId = "java-cpu";

    private volatile RealPrecision realPrecision = RealPrecision.NORMAL;
    private volatile OverflowMode overflowMode = OverflowMode.SAFE;
    private volatile ComputeMode computeMode = ComputeMode.AUTO;
    private volatile MathContext mathContext = MathContext.DECIMAL128;
    private volatile org.episteme.core.technical.backend.distributed.DistributedContext distributedContext;


    private double gpuThreshold = Double.parseDouble(Episteme.getProperty("compute.gpu.threshold", "10000000"));
    private int parallelThreshold = Integer.parseInt(Episteme.getProperty("compute.parallel.threshold", "50000"));
    private int maxThreads = Integer.parseInt(Episteme.getProperty("compute.parallel.max_threads", String.valueOf(Runtime.getRuntime().availableProcessors())));

    // Linear Algebra Constants
    private double epsilonDouble = Double.parseDouble(Episteme.getProperty("math.linearalgebra.epsilon.double", "1e-12"));
    private float epsilonFloat = Float.parseFloat(Episteme.getProperty("math.linearalgebra.epsilon.float", "1e-7"));
    private double stabilityThreshold = Double.parseDouble(Episteme.getProperty("math.linearalgebra.stability.threshold", "1e-15"));
    private int maxIterations = Integer.parseInt(Episteme.getProperty("math.linearalgebra.max_iterations", "1000"));
    private int gmresRestart = Integer.parseInt(Episteme.getProperty("math.linearalgebra.gmres.restart", "30"));
    private int precisionBits = Integer.parseInt(Episteme.getProperty("math.linearalgebra.precision.bits", "256"));
    private double pivotThreshold = Double.parseDouble(Episteme.getProperty("math.linearalgebra.pivot.threshold", "1e-3"));

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

    public String getBackendId() {
        return backendId;
    }

    public NumericalConfiguration setBackendId(String backendId) {
        this.backendId = backendId;
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

    public double getEpsilonDouble() {
        return epsilonDouble;
    }

    public NumericalConfiguration setEpsilonDouble(double epsilonDouble) {
        this.epsilonDouble = epsilonDouble;
        return this;
    }

    public float getEpsilonFloat() {
        return epsilonFloat;
    }

    public NumericalConfiguration setEpsilonFloat(float epsilonFloat) {
        this.epsilonFloat = epsilonFloat;
        return this;
    }

    public double getStabilityThreshold() {
        return stabilityThreshold;
    }

    public NumericalConfiguration setStabilityThreshold(double stabilityThreshold) {
        this.stabilityThreshold = stabilityThreshold;
        return this;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public NumericalConfiguration setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

    public int getGmresRestart() {
        return gmresRestart;
    }

    public NumericalConfiguration setGmresRestart(int gmresRestart) {
        this.gmresRestart = gmresRestart;
        return this;
    }

    public int getPrecisionBits() {
        return precisionBits;
    }

    public NumericalConfiguration setPrecisionBits(int precisionBits) {
        this.precisionBits = precisionBits;
        return this;
    }

    public double getPivotThreshold() {
        return pivotThreshold;
    }

    public NumericalConfiguration setPivotThreshold(double pivotThreshold) {
        this.pivotThreshold = pivotThreshold;
        return this;
    }

    /**
     * Applies a computation mode, selecting the appropriate backend and adjusting precision if needed.
     */
    public void applyComputeMode(ComputeMode mode) {
        this.computeMode = mode;
        String targetBackendId = "java-cpu";

        switch (mode) {
            case OPENCL:
                targetBackendId = findBackendIdPart("linear-algebra", "opencl");
                break;
            case CUDA:
                targetBackendId = findBackendIdPart("linear-algebra", "cuda");
                break;
            case CPU:
                targetBackendId = "java-cpu";
                break;
            case AUTO:
                if (isGpuAvailable()) {
                    targetBackendId = org.episteme.core.technical.backend.BackendDiscovery.getInstance()
                            .getBestProvider("linear-algebra").map(p -> p.getId()).orElse("java-cpu");
                    setFloatPrecision(FloatPrecision.FLOAT);
                    setIntPrecision(IntPrecision.INT);
                } else {
                    targetBackendId = "java-cpu";
                    setRealPrecision(RealPrecision.NORMAL);
                    setFloatPrecision(FloatPrecision.DOUBLE);
                    setIntPrecision(IntPrecision.LONG);
                }
                break;
        }
        this.backendId = targetBackendId;
    }

    private String findBackendIdPart(String type, String idPart) {
        return org.episteme.core.technical.backend.BackendDiscovery.getInstance()
                .getAvailableProvidersByType(type).stream()
                .filter(p -> p.getId().toLowerCase().contains(idPart.toLowerCase()))
                .map(p -> p.getId())
                .findFirst().orElse("java-cpu");
    }

    public boolean isGpuAvailable() {
        return org.episteme.core.technical.backend.BackendDiscovery.getInstance().getProviders().stream()
                .anyMatch(p -> (p.getId().toLowerCase().contains("cuda") || p.getId().toLowerCase().contains("opencl")) && p.isAvailable());
    }

    public org.episteme.core.technical.backend.distributed.DistributedContext getDistributedContext() {
        return distributedContext;
    }

    public NumericalConfiguration setDistributedContext(org.episteme.core.technical.backend.distributed.DistributedContext distributedContext) {
        this.distributedContext = distributedContext;
        return this;
    }
}
