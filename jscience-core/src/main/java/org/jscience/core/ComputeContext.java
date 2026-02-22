package org.jscience.core;

import java.nio.DoubleBuffer;
import java.util.List;
import java.util.stream.IntStream;

import org.jscience.core.technical.backend.distributed.DistributedContext;
import org.jscience.core.distributed.LocalDistributedContext;
import java.math.MathContext;
import org.jscience.core.mathematics.context.ComputeMode;
import org.jscience.core.mathematics.context.MathContext.OverflowMode;
import org.jscience.core.mathematics.context.MathContext.RealPrecision;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorBackend;
import org.jscience.core.technical.algorithm.OperationContext;
import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorProvider;
import org.jscience.core.technical.algorithm.ProviderRegistry;

/**
 * Compute context for configuring linear algebra and numerical computation
 * backends.
 * <p>
 * This class provides a facade for:
 * <ul>
 * <li>Service provider registration and lookup (via {@link ProviderRegistry})</li>
 * <li>Numerical configuration (via {@link NumericalConfig})</li>
 * <li>Backend selection and execution</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ComputeContext {

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

    /**
     * Compute backend type.
     */
    public enum Backend {
        JAVA_CPU, OPENCL_GPU, CUDA_GPU, NATIVE_BLAS, COLT, EJML, QUANTUM
    }

    private static final ThreadLocal<ComputeContext> CURRENT = ThreadLocal.withInitial(ComputeContext::new);

    // DELEGATES
    private final NumericalConfig numericalConfig = new NumericalConfig();
    private final ProviderRegistry providerRegistry = new ProviderRegistry();

    // RESOURCES (Kept in Context)
    private volatile DistributedContext distributedContext = new LocalDistributedContext();
    private volatile GPUBackend gpuBackend;
    private volatile boolean cancelled = false;

    public ComputeContext() {
        this(10_000_000); // Default threshold passed to config
    }

    public ComputeContext(double gpuThreshold) {
        numericalConfig.setGpuThreshold(gpuThreshold);
    }

    /**
     * Returns the current thread-local compute context.
     * If not set, returns a default instance (isolated per thread).
     */
    public static ComputeContext current() {
        return CURRENT.get();
    }

    /**
     * Sets the current thread-local compute context.
     */
    public static void setCurrent(ComputeContext context) {
        CURRENT.set(context);
    }

    /**
     * Creates a dense vector storage from a list of data.
     * This method is a placeholder and needs proper implementation.
     */
    public static <E> VectorStorage<E> createDenseStorage(List<E> data, Ring<E> ring) {
        return null;
    }

    /**
     * Resets the current thread-local context to default.
     */
    public static void reset() {
        CURRENT.remove();
    }

    // ==========================================================
    // DELEGATED CONFIGURATION METHODS
    // ==========================================================

    public FloatPrecision getFloatPrecision() {
        return numericalConfig.getFloatPrecision();
    }

    public ComputeContext setFloatPrecision(FloatPrecision precision) {
        numericalConfig.setFloatPrecision(precision);
        return this;
    }

    public IntPrecision getIntPrecision() {
        return numericalConfig.getIntPrecision();
    }

    public ComputeContext setIntPrecision(IntPrecision precision) {
        numericalConfig.setIntPrecision(precision);
        return this;
    }

    public Backend getBackend() {
        return numericalConfig.getBackend();
    }

    public ComputeContext setBackend(Backend backend) {
        numericalConfig.setBackend(backend);
        return this;
    }

    public RealPrecision getRealPrecision() {
        return numericalConfig.getRealPrecision();
    }

    public ComputeContext setRealPrecision(RealPrecision realPrecision) {
        numericalConfig.setRealPrecision(realPrecision);
        return this;
    }

    public OverflowMode getOverflowMode() {
        return numericalConfig.getOverflowMode();
    }

    public ComputeContext setOverflowMode(OverflowMode overflowMode) {
        numericalConfig.setOverflowMode(overflowMode);
        return this;
    }

    public ComputeMode getComputeMode() {
        return numericalConfig.getComputeMode();
    }

    public ComputeContext setComputeMode(ComputeMode computeMode) {
        numericalConfig.setComputeMode(computeMode);
        return this;
    }

    public MathContext getMathContext() {
        return numericalConfig.getMathContext();
    }

    public ComputeContext setMathContext(MathContext mathContext) {
        numericalConfig.setMathContext(mathContext);
        return this;
    }

    // ==========================================================
    // RESOURCE MANAGEMENT
    // ==========================================================

    public DistributedContext getDistributedContext() {
        return distributedContext;
    }

    public ComputeContext setDistributedContext(DistributedContext distributedContext) {
        this.distributedContext = distributedContext;
        return this;
    }

    public GPUBackend getGPUBackend() {
        return gpuBackend;
    }

    public ComputeContext setGPUBackend(GPUBackend gpuBackend) {
        this.gpuBackend = gpuBackend;
        return this;
    }

    // ==========================================================
    // CANCELLATION SUPPORT
    // ==========================================================

    /** Returns true if the current context has been cancelled. */
    public boolean isCancelled() { return cancelled; }

    /** Sets the cancellation state for this context. */
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    /** 
     * Convenience method to check for cancellation and throw an exception.
     * Often used in deep loops or recursive algorithms.
     * @throws org.jscience.core.technical.algorithm.OperationCancelledException if cancelled
     */
    public void checkCancelled() {
        if (cancelled) {
            throw new org.jscience.core.technical.algorithm.OperationCancelledException();
        }
    }
    
    /** Static helper to check cancellation on the current thread-local context. */
    public static void checkCurrentCancelled() {
        current().checkCancelled();
    }

    // ==========================================================
    // PROVIDER REGISTRY DELEGATION
    // ==========================================================

    public <E> void registerProvider(String name, LinearAlgebraProvider<E> provider) {
        providerRegistry.register(name, provider);
    }

    public <E> LinearAlgebraProvider<E> getProvider(String name) {
        return providerRegistry.get(name);
    }

    public <E> LinearAlgebraProvider<E> getDenseLinearAlgebraProvider(Ring<E> ring) {
        return getLinearAlgebraProvider(ring);
    }

    public <E> LinearAlgebraProvider<E> getSparseLinearAlgebraProvider(Ring<E> ring) {
        return providerRegistry.getSparseLinearAlgebraProvider(ring);
    }

    public <E> LinearAlgebraProvider<E> getLinearAlgebraProvider(OperationContext ctx, Ring<E> ring) {
        return providerRegistry.selectLinearAlgebraProvider(ctx, ring);
    }

    /**
     * Gets a linear algebra provider suited for the given ring, respecting priorities.
     */
    public <E> LinearAlgebraProvider<E> getLinearAlgebraProvider(Ring<E> ring) {
        // Build OperationContext based on NumericalConfig
        OperationContext.Builder builder = new OperationContext.Builder();
        
        FloatPrecision fp = numericalConfig.getFloatPrecision();
        if (fp == FloatPrecision.FLOAT) {
            builder.addHint(OperationContext.Hint.FLOAT32_OK);
        }
        
        Backend b = numericalConfig.getBackend();
        if (b == Backend.CUDA_GPU || b == Backend.OPENCL_GPU) {
            builder.addHint(OperationContext.Hint.GPU_RESIDENT);
        }
        
        // Add data size hint? (Not available without data, but could use threshold?)
        // The original method didn't have data size.
        
        return getLinearAlgebraProvider(builder.build(), ring);
    }

    public TensorProvider getTensorProvider() {
        return getTensorBackend(); // Delegate
    }

    public TensorBackend getTensorBackend() {
        return new org.jscience.core.mathematics.linearalgebra.tensors.backends.CPUDenseTensorBackend();
    }

    // ==========================================================
    // CONVENIENCE METHODS
    // ==========================================================

    public boolean isFloatMode() {
        return numericalConfig.getFloatPrecision() == FloatPrecision.FLOAT;
    }

    public boolean isDoubleMode() {
        return numericalConfig.getFloatPrecision() == FloatPrecision.DOUBLE;
    }

    public boolean isIntMode() {
        return numericalConfig.getIntPrecision() == IntPrecision.INT;
    }

    public boolean isLongMode() {
        return numericalConfig.getIntPrecision() == IntPrecision.LONG;
    }

    // ==========================================================
    // EXECUTION HELPERS
    // ==========================================================

    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        long complexity = (long) m * n * k;
        GPUBackend gpu = getGPUBackend();

        if (gpu != null && gpu.isAvailable() && complexity > numericalConfig.getGpuThreshold()) {
            gpu.matrixMultiply(A, B, C, m, n, k);
        } else {
            // Fallback to CPU execution
        }
    }

    public void parallelStencil(DoubleBuffer data, int width, int height) {
        int size = width * height;
        IntStream.range(0, size).parallel().forEach(i -> {
            synchronized (data) { 
               // Placeholder logic
            }
        });
    }

    @Override
    public String toString() {
        return "ComputeContext{" +
                "numericalConfig=" + numericalConfig + // Need toString() in Config?
                ", distributedContext=" + distributedContext +
                '}';
    }
}



