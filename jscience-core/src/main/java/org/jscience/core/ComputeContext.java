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
import org.jscience.core.mathematics.linearalgebra.tensors.backends.TensorProvider;
import org.jscience.core.mathematics.linearalgebra.tensors.backends.CPUDenseTensorProvider;

import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CUDADenseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.CUDASparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.OpenCLDenseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.OpenCLSparseLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.ColtLinearAlgebraProvider;
import org.jscience.core.mathematics.linearalgebra.providers.EJMLLinearAlgebraProvider;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.distributed.LocalDistributedContext;
import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.physics.quantum.QuantumBackend;
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

    protected ComputeContext() {
        // Providers are registered lazily
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
    public <E> LinearAlgebraProvider<E> getSparseLinearAlgebraProvider(
            org.jscience.core.mathematics.structures.rings.Ring<E> ring) {

        boolean canUseGpu = (ring.zero() instanceof org.jscience.core.mathematics.numbers.real.Real);

        switch (backend) {
            case CUDA_GPU:
                if (canUseGpu) {
                    try {
                        return new CUDASparseLinearAlgebraProvider<>((Field<E>) ring);
                    } catch (Throwable t) {
                        return new CPUSparseLinearAlgebraProvider<>(ring);
                    }
                }
                // Fallthrough
            case OPENCL_GPU:
                if (canUseGpu) {
                    try {
                        return new OpenCLSparseLinearAlgebraProvider<>((Field<E>) ring);
                    } catch (Throwable t) {
                        // Fallback to CPU
                        return new CPUSparseLinearAlgebraProvider<>(ring);
                    }
                }
                // Fallthrough
            case JAVA_CPU:
            default:
                return new CPUSparseLinearAlgebraProvider<>(ring);
        }
    }

    /**
     * Gets a dense linear algebra provider.
     */
    public <E> LinearAlgebraProvider<E> getLinearAlgebraProvider(
            org.jscience.core.mathematics.structures.rings.Ring<E> ring) {
        // First check if a specific provider is registered for this type/name
        // (Implementation detail: we could add a map Key(backend, fieldType) ->
        // Provider)

        // For now, use the robust switching logic derived from DenseVector
        boolean canUseGpu = (ring.zero() instanceof org.jscience.core.mathematics.numbers.real.Real);

        switch (backend) {
            case CUDA_GPU:
                if (canUseGpu) {
                    try {
                        return new CUDADenseLinearAlgebraProvider<>((Field<E>) ring);
                    } catch (Throwable t) {
                        // Fallback
                        return new CPUDenseLinearAlgebraProvider<>(ring);
                    }
                }
                // Fallthrough
            case OPENCL_GPU:
                if (canUseGpu) {
                    try {
                        return new OpenCLDenseLinearAlgebraProvider<>((Field<E>) ring);
                    } catch (Throwable t) {
                        // Fallback to CPU
                        return new CPUDenseLinearAlgebraProvider<>(ring);
                    }
                }
                // Fallthrough

            case COLT:
                try {
                    return new ColtLinearAlgebraProvider<>((Field<E>) ring);
                } catch (Throwable t) {
                    return new CPUDenseLinearAlgebraProvider<>(ring);
                }

            case EJML:
                try {
                    return new EJMLLinearAlgebraProvider<>((Field<E>) ring);
                } catch (Throwable t) {
                    return new CPUDenseLinearAlgebraProvider<>(ring);
                }

            case JAVA_CPU:
            default:
                return new CPUDenseLinearAlgebraProvider<>(ring);
        }
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
    @Override
    public String toString() {
        return "ComputeContext{" +
                "backend=" + backend +
                ", floatPrecision=" + floatPrecision +
                ", intPrecision=" + intPrecision +
                ", mathContext=" + mathContext +
                ", providers=" + providers.keySet() +
                '}';
    }
}



