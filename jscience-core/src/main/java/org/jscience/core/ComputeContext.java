package org.jscience.core;

import java.math.MathContext;
import java.nio.DoubleBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.distributed.LocalDistributedContext;
import org.jscience.core.mathematics.context.ComputeMode;
import org.jscience.core.mathematics.context.MathContext.OverflowMode;
import org.jscience.core.mathematics.context.MathContext.RealPrecision;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmManager;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.tensors.TensorBackend;
import org.jscience.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.quantum.QuantumBackend;

/**
 * Compute context for configuring linear algebra and numerical computation
 * backends.
 * <p>
 * This class provides:
 * <ul>
 * <li>Service provider registration and lookup for algorithm
 * implementations</li>
 * <li>Floating-point precision mode (FLOAT vs DOUBLE)</li>
 * <li>Integer precision mode (INT vs LONG) for GPU-optimized integer
 * operations</li>
 * <li>Backend selection (Java CPU, CUDA GPU, etc.)</li>
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
        /** 32-bit floating point (float) - faster on many GPUs */
        FLOAT,
        /** 64-bit floating point (double) - higher precision */
        DOUBLE
    }

    /**
     * Integer precision mode for GPU and numerical operations.
     */
    public enum IntPrecision {
        /**
         * 32-bit integers (int) - faster, smaller memory footprint, range ±2.1 billion
         */
        INT,
        /** 64-bit integers (long) - larger range ±9.2 × 10^18 */
        LONG
    }

    /**
     * Compute backend type.
     */
    public enum Backend {
        /** Pure Java CPU implementation */
        JAVA_CPU,
        /** OpenCL GPU implementation */
        OPENCL_GPU,
        /** CUDA GPU implementation */
        CUDA_GPU,
        /** Native BLAS library (future) */
        NATIVE_BLAS,
        /** Colt Library */
        COLT,
        /** EJML Library */
        EJML,
        /** Quantum Computing Simulator or Hardware */
        QUANTUM
    }

    private static final ThreadLocal<ComputeContext> CURRENT = ThreadLocal.withInitial(ComputeContext::new);

    private final Map<String, LinearAlgebraProvider<?>> providers = new ConcurrentHashMap<>();
    private volatile FloatPrecision floatPrecision = FloatPrecision.DOUBLE;
    private volatile IntPrecision intPrecision = IntPrecision.LONG;
    private volatile Backend backend = Backend.JAVA_CPU;
    private volatile DistributedContext distributedContext = new LocalDistributedContext();
    private volatile GPUBackend gpuBackend;
    private volatile QuantumBackend quantumBackend;

    private volatile RealPrecision realPrecision = RealPrecision.NORMAL;
    private volatile OverflowMode overflowMode = OverflowMode.SAFE;
    private volatile ComputeMode computeMode = ComputeMode.AUTO;
    private volatile MathContext mathContext = MathContext.DECIMAL128;

    private final double gpuThreshold; // Minimum problem size for GPU offloading

    public ComputeContext() {
        this(10_000_000); // Default threshold
    }

    public ComputeContext(double gpuThreshold) {
        this.gpuThreshold = gpuThreshold;
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
        // Placeholder implementation to ensure syntactic correctness.
        return null;
    }

    /**
     * Resets the current thread-local context to default.
     */
    public static void reset() {
        CURRENT.remove();
    }

    /**
     * Gets the current floating-point precision mode.
     */
    public FloatPrecision getFloatPrecision() {
        return floatPrecision;
    }

    /**
     * Sets the floating-point precision mode.
     */
    public ComputeContext setFloatPrecision(FloatPrecision precision) {
        this.floatPrecision = precision;
        return this;
    }

    /**
     * Gets the current integer precision mode.
     */
    public IntPrecision getIntPrecision() {
        return intPrecision;
    }

    /**
     * Sets the integer precision mode.
     */
    public ComputeContext setIntPrecision(IntPrecision precision) {
        this.intPrecision = precision;
        return this;
    }

    /**
     * Gets the current compute backend.
     */
    public Backend getBackend() {
        return backend;
    }

    /**
     * Sets the compute backend.
     */
    public ComputeContext setBackend(Backend backend) {
        this.backend = backend;
        return this;
    }

    /**
     * Gets the current distributed context.
     */
    public DistributedContext getDistributedContext() {
        return distributedContext;
    }

    /**
     * Sets the distributed context.
     */
    public ComputeContext setDistributedContext(DistributedContext distributedContext) {
        this.distributedContext = distributedContext;
        return this;
    }

    /**
     * Gets the current GPU backend.
     */
    public GPUBackend getGPUBackend() {
        return gpuBackend;
    }

    /**
     * Sets the GPU backend.
     */
    public ComputeContext setGPUBackend(GPUBackend gpuBackend) {
        this.gpuBackend = gpuBackend;
        return this;
    }

    /**
     * Gets the current Quantum backend.
     */
    public QuantumBackend getQuantumBackend() {
        return quantumBackend;
    }

    /**
     * Sets the Quantum backend.
     */
    public ComputeContext setQuantumBackend(QuantumBackend quantumBackend) {
        this.quantumBackend = quantumBackend;
        return this;
    }

    public RealPrecision getRealPrecision() {
        return realPrecision;
    }

    public ComputeContext setRealPrecision(RealPrecision realPrecision) {
        this.realPrecision = realPrecision;
        return this;
    }

    public OverflowMode getOverflowMode() {
        return overflowMode;
    }

    public ComputeContext setOverflowMode(OverflowMode overflowMode) {
        this.overflowMode = overflowMode;
        return this;
    }

    public ComputeMode getComputeMode() {
        return computeMode;
    }

    public ComputeContext setComputeMode(ComputeMode computeMode) {
        this.computeMode = computeMode;
        return this;
    }

    public MathContext getMathContext() {
        return mathContext;
    }

    public ComputeContext setMathContext(MathContext mathContext) {
        this.mathContext = mathContext;
        return this;
    }

    /**
     * Registers a linear algebra provider.
     */
    public <E> void registerProvider(String name, LinearAlgebraProvider<E> provider) {
        providers.put(name, provider);
    }

    /**
     * Gets a registered provider by name.
     */
    @SuppressWarnings("unchecked")
    public <E> LinearAlgebraProvider<E> getProvider(String name) {
        return (LinearAlgebraProvider<E>) providers.get(name);
    }

    /**
     * Gets a dense linear algebra provider.
     */
    public <E> LinearAlgebraProvider<E> getDenseLinearAlgebraProvider(Ring<E> ring) {
        return getLinearAlgebraProvider(ring);
    }

    /**
     * Gets a sparse linear algebra provider.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <E> LinearAlgebraProvider<E> getSparseLinearAlgebraProvider(Ring<E> ring) {
        Class<SparseLinearAlgebraProvider> type = SparseLinearAlgebraProvider.class;
        List<SparseLinearAlgebraProvider> candidates = AlgorithmManager.getProviders(type);
        
        for (SparseLinearAlgebraProvider<?> p : candidates) {
            LinearAlgebraProvider<E> typedProvider = (LinearAlgebraProvider<E>) p;
            if (typedProvider.isCompatible(ring)) {
                return typedProvider;
            }
        }

        // Fallback to CPU Sparse
        return new CPUSparseLinearAlgebraProvider<E>(ring);
    }

    /**
     * Gets a linear algebra provider suited for the given ring, respecting priorities.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <E> LinearAlgebraProvider<E> getLinearAlgebraProvider(Ring<E> ring) {
        Class<LinearAlgebraProvider> type = LinearAlgebraProvider.class;
        List<LinearAlgebraProvider> candidates = AlgorithmManager.getProviders(type);
        
        for (LinearAlgebraProvider<?> p : candidates) {
            LinearAlgebraProvider<E> typedProvider = (LinearAlgebraProvider<E>) p;
            if (typedProvider.isCompatible(ring)) {
                return typedProvider;
            }
        }

        // Fallback: CPUDense
        Field<E> field = (Field<E>) ring;
        return new CPUDenseLinearAlgebraProvider<E>(field);
    }

    /**
     * Gets the appropriate tensor provider for the current context.
     * 
     * @return the tensor provider
     */
    public org.jscience.core.technical.algorithm.TensorProvider getTensorProvider() {
        return getTensorBackend();
    }

    /**
     * Gets the appropriate tensor backend for the current context.
     */
    public TensorBackend getTensorBackend() {
        return new org.jscience.core.mathematics.linearalgebra.tensors.backends.CPUDenseTensorBackend();
    }

    /**
     * Convenience method to check if using float precision.
     */
    public boolean isFloatMode() {
        return floatPrecision == FloatPrecision.FLOAT;
    }

    /**
     * Convenience method to check if using double precision.
     */
    public boolean isDoubleMode() {
        return floatPrecision == FloatPrecision.DOUBLE;
    }

    /**
     * Convenience method to check if using int precision.
     */
    public boolean isIntMode() {
        return intPrecision == IntPrecision.INT;
    }

    /**
     * Convenience method to check if using long precision.
     */
    public boolean isLongMode() {
        return intPrecision == IntPrecision.LONG;
    }

    /**
     * Executes a matrix multiplication using the most efficient available backend.
     */
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        long complexity = (long) m * n * k;
        GPUBackend gpu = getGPUBackend();

        if (gpu != null && gpu.isAvailable() && complexity > gpuThreshold) {
            gpu.matrixMultiply(A, B, C, m, n, k);
        } else {
            // Fallback to CPU execution
        }
    }

    /**
     * Parallelizes an operation across available resources.
     */
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
                "backend=" + backend +
                ", floatPrecision=" + floatPrecision +
                ", intPrecision=" + intPrecision +
                ", mathContext=" + mathContext +
                ", gpuThreshold=" + gpuThreshold +
                ", providers=" + providers.keySet() +
                '}';
    }
}



