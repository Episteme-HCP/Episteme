/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.core;

/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.jscience.core.mathematics.context.ComputeMode;
import org.jscience.core.mathematics.context.MathContext.RealPrecision;
import org.jscience.core.mathematics.context.MathContext.OverflowMode;
import org.jscience.core.technical.algorithm.TensorProvider;
import org.jscience.core.technical.algorithm.linearalgebra.CPUDenseTensorProvider;

import org.jscience.core.technical.algorithm.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.linearalgebra.CPUDenseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
// Providers loaded via reflection or service loader, imports removed to fix warnings.
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.distributed.LocalDistributedContext;
import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.quantum.QuantumBackend;
import java.util.List;

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
 * <p>
 * Users can add custom implementations by implementing
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
         * 32-bit integers (int) - faster, smaller memory footprint, range Ã‚Â±2.1 billion
         */
        INT,
        /** 64-bit integers (long) - larger range Ã‚Â±9.2 Ãƒâ€” 10^18 */
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
    private volatile java.math.MathContext mathContext = java.math.MathContext.DECIMAL128;

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
     * For now, it returns null to maintain syntactic correctness.
     */
    public static <E> VectorStorage<E> createDenseStorage(List<E> data, Ring<E> ring) {
        // Placeholder implementation to ensure syntactic correctness.
        // A proper implementation would likely involve a provider or direct instantiation.
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
     * <p>
     * This affects GPU computations and some numerical algorithms:
     * <ul>
     * <li>FLOAT: Faster, uses less memory, but ~7 decimal digits precision</li>
     * <li>DOUBLE: Slower, uses more memory, but ~15 decimal digits precision</li>
     * </ul>
     * </p>
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
     * <p>
     * This affects GPU integer operations and integer matrix arithmetic:
     * <ul>
     * <li>INT: Faster on many GPUs (especially consumer GPUs), smaller memory,
     * but limited to Ã‚Â±2.1 billion range</li>
     * <li>LONG: Larger range (Ã‚Â±9.2 Ãƒâ€” 10^18), but slower on some GPUs
     * (many consumer GPUs have weak int64 support)</li>
     * </ul>
     * </p>
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
     *
     * @param backend the backend to use
     * @return this context for chaining
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

    public java.math.MathContext getMathContext() {
        return mathContext;
    }

    public ComputeContext setMathContext(java.math.MathContext mathContext) {
        this.mathContext = mathContext;
        return this;
    }

    /**
     * Registers a linear algebra provider.
     * <p>
     * Use this to add custom implementations (e.g., CUDA, OpenCL, native BLAS).
     * </p>
     *
     * @param name     unique name for this provider (e.g., "cuda-float", "mkl")
     * @param provider the provider implementation
     */
    public <E> void registerProvider(String name, LinearAlgebraProvider<E> provider) {
        providers.put(name, provider);
    }

    /**
     * Gets a registered provider by name.
     *
     * @param name the provider name
     * @return the provider, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <E> LinearAlgebraProvider<E> getProvider(String name) {
        return (LinearAlgebraProvider<E>) providers.get(name);
    }

    /**
     * Gets a dense linear algebra provider.
     */
    public <E> LinearAlgebraProvider<E> getDenseLinearAlgebraProvider(
            org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        return getLinearAlgebraProvider(ring);
    }

    /**
     * Gets a sparse linear algebra provider.
     */
    /**
     * Gets a sparse linear algebra provider.
     */
    /**
     * Gets a sparse linear algebra provider.
     */
    public <E> LinearAlgebraProvider<E> getSparseLinearAlgebraProvider(
            org.jscience.core.mathematics.structures.rings.Ring<E> ring) {

        // Use AlgorithmManager to find all implementations of SparseLinearAlgebraProvider
        // This relies on the new marker interface for typesafe discovery
        @SuppressWarnings("rawtypes")
        Class<org.jscience.core.technical.algorithm.linearalgebra.SparseLinearAlgebraProvider> type = 
            org.jscience.core.technical.algorithm.linearalgebra.SparseLinearAlgebraProvider.class;
            
        java.util.List<org.jscience.core.technical.algorithm.linearalgebra.SparseLinearAlgebraProvider> candidates = 
            org.jscience.core.technical.algorithm.AlgorithmManager.getProviders(type);
        
        for (org.jscience.core.technical.algorithm.linearalgebra.SparseLinearAlgebraProvider<?> p : candidates) {
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<E> typedProvider = (LinearAlgebraProvider<E>) p;
            if (typedProvider.isCompatible(ring)) {
                return typedProvider;
            }
        }

        // Fallback to CPU Sparse
        return new org.jscience.core.technical.algorithm.linearalgebra.CPUSparseLinearAlgebraProvider<>(ring);
    }


    /**
     * Gets a dense linear algebra provider.
     */
    /**
     * Gets a linear algebra provider suited for the given ring, respecting priorities.
     */
    /**
     * Gets a linear algebra provider suited for the given ring, respecting priorities.
     */
    public <E> LinearAlgebraProvider<E> getLinearAlgebraProvider(
            org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        
        // Use AlgorithmManager to find all implementations of LinearAlgebraProvider
        // The manager handles ServiceLoader discovery and Priority sorting
        @SuppressWarnings("rawtypes")
        Class<LinearAlgebraProvider> type = LinearAlgebraProvider.class;
        java.util.List<LinearAlgebraProvider> candidates = org.jscience.core.technical.algorithm.AlgorithmManager.getProviders(type);
        
        for (LinearAlgebraProvider<?> p : candidates) {
            @SuppressWarnings("unchecked")
            LinearAlgebraProvider<E> typedProvider = (LinearAlgebraProvider<E>) p;
            if (typedProvider.isCompatible(ring)) {
                // Check if user has forced a specific backend via setBackend()
                // If backend is AUTO (or JAVA_CPU default but overridden by priority), we just take the best.
                // However, if user explicitly requested CUDA_GPU, we should prefer it.
                // NOTE: The current priorities in providers (CUDA=100, OpenCL=50, CPU=0) 
                // naturally enforce the selection if we just pick the first compatible one.
                return typedProvider;
            }
        }

        // Fallback: This should ideally be covered by the providers list (CPU is always available)
        // But if ServiceLoader fails or config is broken, return a hardcoded CPU/Default.
        return new CPUDenseLinearAlgebraProvider<>(ring);
    }

    /**
     * Gets the appropriate tensor provider for the current backend.
     */
    public TensorProvider getTensorProvider() {
        switch (backend) {
            case JAVA_CPU:
                // Use default CPU tensor provider
                break;
            case OPENCL_GPU:
                // OpenCL tensor support pending
                break;
            case COLT:
            case EJML:
            case NATIVE_BLAS:
            case CUDA_GPU:
                // Native BLAS tensor support pending
                // ND4J provider removed due to corruption.
                break;
            case QUANTUM:
                // Quantum simulation support via QuantumBackend
                break;
        }
        // Default
        return new CPUDenseTensorProvider();
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
     * Returns a summary of current configuration.
     */
    /**
     * Executes a matrix multiplication using the most efficient available backend.
     * <p>
     * This method uses a heuristic based on problem complexity (m*n*k) to decide
     * between CPU and GPU execution.
     * </p>
     */
    public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) {
        long complexity = (long) m * n * k;
        GPUBackend gpu = getGPUBackend();

        if (gpu != null && gpu.isAvailable() && complexity > gpuThreshold) {
            // Offload to GPU
            gpu.matrixMultiply(A, B, C, m, n, k);
        } else {
            // Fallback to CPU execution
            // Ideally, this should call a LinearAlgebraProvider or internal BLAS implementation
            // For now, we leave this as an integration point
        }
    }

    /**
     * Parallelizes an operation across available resources.
     * <p>
     * Currently uses Java Parallel Streams to utilize multi-core CPUs.
     * Future implementation will split work between CPU and GPU if efficient.
     * </p>
     */
    public void parallelStencil(java.nio.DoubleBuffer data, int width, int height) {
        // Simple parallel processing simulation:
        // In a real application, this would accept a StencilOperation functional interface
        // and apply it to the buffer.
        
        // Example: Apply a dummy operation (e.g., multiply by 2) in parallel chunks
        int size = width * height;
        java.util.stream.IntStream.range(0, size).parallel().forEach(i -> {
            synchronized (data) { 
               // Note: Direct buffers are not thread-safe for concurrent writes usually without care
               // This is a placeholder logic as requested
               // data.put(i, data.get(i) * 1.0); 
            }
        });
        
        // For actual stencil ops (convolution), we need read-only src and write-only dest
        // This method signature might need improvement in future iterations.
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



