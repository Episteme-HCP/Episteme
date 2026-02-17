/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.nativ;

/**
 * Mixin interface for backends that load native libraries via JNI, Panama FFM,
 * or other native interop mechanisms.
 * <p>
 * This is a <b>capability interface</b>, not a position in the
 * {@link org.jscience.core.technical.backend.ComputeBackend} hierarchy.
 * Any backend type (CPU, GPU, SIMD, etc.) can additionally implement
 * {@code NativeBackend} to indicate it relies on native code.
 * </p>
 * <p>
 * Examples:
 * <ul>
 *   <li>{@code NativeBLASBackend implements ComputeBackend, NativeBackend}
 *       — CPU BLAS via Panama FFM</li>
 *   <li>{@code CUDABackend implements GPUBackend, NativeBackend}
 *       — CUDA via Panama FFM</li>
 *   <li>{@code OpenCLBackend implements GPUBackend, NativeBackend}
 *       — OpenCL via Panama FFM</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public interface LibraryBackend {

    /**
     * Checks if the native library is loaded and available.
     *
     * @return true if the native library has been successfully loaded
     */
    boolean isLoaded();

    /**
     * Returns the name of the native library this backend depends on.
     *
     * @return library name (e.g., "openblas", "cuda", "fftw3")
     */
    default String getNativeLibraryName() {
        return "unknown";
    }
}
